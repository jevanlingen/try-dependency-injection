package com.di.architecture;

import com.di.annotations.*;
import com.di.annotations.EventListener;
import com.di.annotations.http.GET;
import com.di.annotations.http.POST;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SetupConfigurer {
    public static void configure() {
        try {
            final var initializedBeans = initializeClasses();
            initializeEventBus(initializedBeans);
            initializeAndRunServer(initializedBeans);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Class<?>, Object> initializeClasses() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final var suitableClasses = getSuitableClasses();
        final var initializedClasses = new HashMap<Class<?>, Object>();

        while (initializedClasses.size() != suitableClasses.size()) {
            for (final var clazz : suitableClasses.stream().filter(it -> !initializedClasses.containsKey(it)).toList()) {
                final var constructor = clazz.getDeclaredConstructors()[0];
                final var dependencies = Arrays.stream(constructor.getParameters())
                        .map(it -> initializedClasses.get(it.getType()))
                        .filter(Objects::nonNull)
                        .toList();

                if (constructor.getParameterCount() == dependencies.size()) {
                    System.out.printf("Bean `%s` loaded. External dependencies: %s%n", clazz.getCanonicalName(), dependencies.stream().map(it -> it.getClass().getCanonicalName()).toList());
                    initializedClasses.put(clazz, constructor.newInstance(dependencies.toArray()));
                }
            }
        }

        IO.println("All classes loaded");
        return initializedClasses;
    }

    private static void initializeEventBus(Map<Class<?>, Object> initializedBeans) {
        final var eventBus = (EventBus) initializedBeans.get(EventBus.class);
        if (eventBus != null) {
            for (Object bean : initializedBeans.values()) {
                final var eventListeners = Arrays.stream(bean.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(EventListener.class))
                        .toArray(Method[]::new);

                if (eventListeners.length > 0) {
                    eventBus.register(bean, eventListeners);
                }
            }
        }
    }

    private static void initializeAndRunServer(Map<Class<?>, Object> initializedBeans) {
        var server = (Server) initializedBeans.get(Server.class);

        for (Object bean : initializedBeans.values()) {
            if (bean.getClass().isAnnotationPresent(RestController.class)) {
                final var methods = Arrays.stream(bean.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(GET.class) || method.isAnnotationPresent(POST.class))
                        .toArray(Method[]::new);
                server.registerRoute(bean, methods);
            }
        }

        server.run();
    }

    private static Set<Class<?>> getSuitableClasses() {
        final var reflections = new Reflections("com.di", Scanners.TypesAnnotated);

        final var classes = reflections.getTypesAnnotatedWith(Bean.class);
        classes.addAll(reflections.getTypesAnnotatedWith(Bean.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Configuration.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        classes.addAll(reflections.getTypesAnnotatedWith(RestController.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));

        return classes;
    }

}
