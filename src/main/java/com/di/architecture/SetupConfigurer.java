package com.di.architecture;

import com.di.annotations.*;
import com.di.annotations.EventListener;
import com.di.annotations.http.GET;
import com.di.annotations.http.POST;
import com.di.model.DataEvent;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SetupConfigurer {
    public static void configure(boolean enableEvents, boolean enableEventExampleFlow, boolean enableServer) {
        try {
            final var initializedBeans = initializeClasses();

            if (enableEvents) {
                initializeEventBus(initializedBeans, enableEventExampleFlow, enableServer);
            }
            if (enableServer) {
                initializeAndRunServer(initializedBeans);
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Class<?>, Object> initializeClasses() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final var suitableClasses = new ArrayList<>(getSuitableClasses());
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

    private static void initializeEventBus(Map<Class<?>, Object> initializedBeans, boolean enableEventExampleFlow, boolean enableServer) throws InterruptedException {
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

            if (enableEventExampleFlow) {
                eventBus.publish(new DataEvent("Start the event flow..."));

                if (!enableServer) {
                    // Stay alive
                    while (true) {
                        Thread.sleep(1000);
                    }
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

    private static List<Class<?>> getSuitableClasses() {
        return new Reflections("com.di", new SubTypesScanner(false))
                .getSubTypesOf(Object.class).stream()
                .filter(it -> it.isAnnotationPresent(Bean.class) ||
                              it.isAnnotationPresent(Configuration.class) ||
                              it.isAnnotationPresent(Repository.class) ||
                              it.isAnnotationPresent(RestController.class) ||
                              it.isAnnotationPresent(Service.class))
                .toList();
    }

}
