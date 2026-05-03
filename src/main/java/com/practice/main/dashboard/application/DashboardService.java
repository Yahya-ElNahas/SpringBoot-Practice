package com.practice.main.dashboard.application;

import com.practice.main.cart.application.CartService;
import com.practice.main.cart.application.dto.response.CartItemResponse;
import com.practice.main.common.response.ApiResponse;
import com.practice.main.dashboard.application.dto.request.TaskRequest;
import com.practice.main.dashboard.application.dto.response.DashboardResponse;
import com.practice.main.dashboard.application.dto.response.TaskResponse;
import com.practice.main.order.application.OrderService;
import com.practice.main.order.application.dto.response.OrderResponse;
import com.practice.main.product.application.ProductService;
import com.practice.main.product.application.dto.response.ProductResponse;
import com.practice.main.security.principal.UserPrincipal;
import com.practice.main.user.application.UserService;
import com.practice.main.user.application.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;

    private final ApplicationContext applicationContext;

    public DashboardResponse getUserDashboard(UserPrincipal principal) {
        try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<UserResponse> userFuture = executor.submit(() ->
                    userService.getUser(principal.userId())
            );
            Future<List<CartItemResponse>> cartFuture = executor.submit(() ->
                    cartService.getUserCart(principal.userId())
            );
            Future<List<OrderResponse>> ordersFuture = executor.submit(() ->
                    orderService.getUserOrders(principal.userId())
            );
            Future<List<ProductResponse>> productsFuture = executor.submit(() ->
                    productService.getLatestProducts(3)
            );

            return new DashboardResponse(
                    userFuture.get(),
                    cartFuture.get(),
                    ordersFuture.get(),
                    productsFuture.get()
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

//        CompletableFuture<UserResponse> getUserTask = userService.getUser(principal.userId());
//        CompletableFuture<List<ProductResponse>> getProductsTask = productService.getLatestProducts(3);
//        CompletableFuture<List<CartItemResponse>> getCartTask = cartService.getUserCart(principal.userId());
//        CompletableFuture<List<OrderResponse>> getOrdersTask = orderService.getUserOrders(principal.userId());
//
//        CompletableFuture.allOf(
//                getUserTask,
//                getProductsTask,
//                getCartTask,
//                getOrdersTask
//        ).join();
//
//        return new DashboardResponse(
//                getUserTask.join(),
//                getCartTask.join(),
//                getOrdersTask.join(),
//                getProductsTask.join()
//        );
    }

    public List<TaskResponse> executeAllTasks(UserPrincipal userPrincipal, List<TaskRequest> requestBody) {
        try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<TaskResponse>> taskFutures = requestBody.stream().map(
                    taskRequest -> executor.submit(() -> executeTask(userPrincipal, taskRequest))
            ).toList();

            return taskFutures.stream().map(
                    future -> {
                        try {
                            return future.get();
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TaskResponse executeTask(UserPrincipal userPrincipal, TaskRequest task) {
        try {
            Object bean = applicationContext.getBean(task.feature().toLowerCase().concat("Service"));
            Class<?> targetClass = AopUtils.getTargetClass(bean);

            Method method = Arrays.stream(targetClass.getMethods())
                    .filter(m -> m.getName().equals(task.method()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchMethodException(task.method()));

            List<Object> args = typeCast(
                    task.args().toArray(),
                    method.getParameterTypes()
            );

            if(Arrays.asList(method.getParameterTypes()).contains(UserPrincipal.class)) {
                args.addFirst(userPrincipal);
            }

            Class<?> returnType = method.getReturnType();

            Object result = returnType.cast(method.invoke(bean, args.toArray()));

            new TaskResponse(
                    task.feature(),
                    task.method(),
                    result
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private List<Object> typeCast(Object[] args, Class<?>[] types) throws Exception {
        List<Object> result = new ArrayList<>();

        for(int i = 0, j = 0; j < types.length; i++, j++) {
            Object arg = args[i];
            Class<?> type = types[j];

            if(type.equals(UserPrincipal.class)) {
                i--;
                continue;
            }

            if(arg instanceof Map<?,?>) {
                Constructor<?> constructor = Arrays.stream(type.getConstructors()).findFirst().orElseThrow();

                Parameter[] params = constructor.getParameters();

                Object[] values = new Object[params.length];

                int index = 0;
                for(Parameter param : params) {
                    Object value = ((Map<?, ?>)arg).get(param.getName());

                    if(param.getType().equals(double.class)) {
                        values[index++] = ((Number) value).doubleValue();

                        continue;
                    }
                    if(param.getType().equals(int.class)) {
                        values[index++] = ((Number) value).intValue();

                        continue;
                    }

                    values[index++] = param.getType().cast(value);
                }

                log.info("{}", Arrays.stream(values).toList());

                Object instance = constructor.newInstance(values);

                result.add(instance);

                continue;
            }

            if(type.equals(UUID.class)) {
                result.add(UUID.fromString(arg.toString()));

                continue;
            }

            result.add(type.cast(arg));
        }

        return  result;
    }
}