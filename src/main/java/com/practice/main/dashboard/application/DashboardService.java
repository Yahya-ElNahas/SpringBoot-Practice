package com.practice.main.dashboard.application;

import com.practice.main.cart.application.CartService;
import com.practice.main.cart.application.dto.response.CartItemResponse;
import com.practice.main.dashboard.application.dto.internal.TaskFuture;
import com.practice.main.dashboard.application.dto.request.TaskRequest;
import com.practice.main.dashboard.application.dto.response.DashboardResponse;
import com.practice.main.dashboard.application.dto.response.TaskResponse;
import com.practice.main.dashboard.application.exception.TaskExecutionException;
import com.practice.main.dashboard.application.util.TaskExecutionUtil;
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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;

    private final ApplicationContext applicationContext;

    private final TaskExecutionUtil taskExecutor;

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

            List<TaskFuture> taskFutures = requestBody.stream().map(
                    taskRequest -> new TaskFuture(
                            executor.submit(() -> taskExecutor.executeTask(userPrincipal, taskRequest)),
                            taskRequest
                    )
            ).toList();

            return taskFutures.stream().map(
                    taskFuture -> {
                        try {
                            return taskFuture.future().get(2, TimeUnit.SECONDS);
                        }  catch (ExecutionException e) {
                            String message = e.getCause().getMessage();

                            if(e.getCause() instanceof TaskExecutionException taskEx) {
                                message = taskEx.getArgs()[0].toString();
                            }

                            return new TaskResponse(
                                    taskFuture.taskRequest().feature(),
                                    taskFuture.taskRequest().method(),
                                    message,
                                    null
                            );
                        } catch (TimeoutException e) {
                            taskFuture.future().cancel(true);
                            return new TaskResponse(
                                    taskFuture.taskRequest().feature(),
                                    taskFuture.taskRequest().method(),
                                    e.getMessage(),
                                    null
                            );
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).toList();
        }
    }

    public Map<String, List<String>> getAllServices() {
        String[] serviceNames = {"user", "product", "cart", "order", "receipt"};

        Map<String, List<String>> services = new HashMap<>();
        for(String serviceName : serviceNames) {
            Object bean = applicationContext.getBean(serviceName + "Service");
            Class<?> targetClass = AopUtils.getTargetClass(bean);

            List<String> methods = Arrays.stream(targetClass.getDeclaredMethods())
                    .map(method -> {
                        List<String> parameters = Arrays.stream(method.getParameters()).map(
                                parameter -> parameter.getType().getSimpleName() + ' ' + parameter.getName()
                        ).toList();
                        return method.getName() + '(' + parameters + ')';
                    })
                    .filter(name -> !name.startsWith("lambda"))
                    .toList();

            services.put(serviceName, methods);
        }

        return services;
    }
}