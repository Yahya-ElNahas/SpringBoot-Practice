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
                    .orElseThrow(() -> new NoSuchMethodException("Method not found: " + task.method()));

            Object[] args = method.getParameterCount() > 0 ? formMethodArgs(
                        userPrincipal,
                        task.args().toArray(),
                        method.getParameterTypes()
                    ) : null;

            Class<?> returnType = method.getReturnType();

            Object result = returnType.cast(method.invoke(bean, args));

            return new TaskResponse(
                    task.feature(),
                    task.method(),
                    result
            );

        } catch (InvocationTargetException e) {
            throw new TaskExecutionException(task.feature(), task.method(), (DomainException) e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new TaskExecutionException(task.feature(), task.method(), e);
        }
    }

    private Object[] formMethodArgs(UserPrincipal userPrincipal, Object[] args, Class<?>[] types) throws Exception {
        List<Object> result = new ArrayList<>();

        int argIndex = 0;
        for(Class<?> type : types) {
            if(type.equals(UserPrincipal.class)) {
                result.add(userPrincipal);
                continue;
            }
            result.add(typCast(args[argIndex++], type));
        }

        return result.toArray();
    }

    private Object typCast(Object arg, Class<?> type) throws Exception {
        if(arg == null) {
            return null;
        }

        if(type.equals(String.class)) {
            return arg.toString();
        }

        if(type.equals(UUID.class)) {
            return UUID.fromString(arg.toString());
        }

        if(type.equals(int.class)) {
            return Integer.parseInt(arg.toString());
        }

        if(type.equals(double.class)) {
            return Double.parseDouble(arg.toString());
        }

        if(arg instanceof Map<?,?> map) {
            Constructor<?> constructor = Arrays.stream(type.getConstructors())
                    .findFirst()
                    .orElseThrow();
            Parameter[] parameters = constructor.getParameters();

            Object[] values = new Object[parameters.length];
            for(int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Object value = map.get(parameter.getName());

                values[i] = typCast(value, parameter.getType());
            }

            return constructor.newInstance(values);
        }

        return type.cast(arg);
    }
}