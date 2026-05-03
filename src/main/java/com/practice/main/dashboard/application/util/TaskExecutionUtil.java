package com.practice.main.dashboard.application.util;

import com.practice.main.common.exception.DomainException;
import com.practice.main.dashboard.application.dto.request.TaskRequest;
import com.practice.main.dashboard.application.dto.response.TaskResponse;
import com.practice.main.dashboard.application.exception.TaskExecutionException;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TaskExecutionUtil {

    private final ApplicationContext applicationContext;

    public TaskResponse executeTask(UserPrincipal userPrincipal, TaskRequest task) throws Exception {
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

        } catch (InvocationTargetException e) {
            throw new TaskExecutionException(task.feature(), task.method(), (DomainException) e.getTargetException());
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
