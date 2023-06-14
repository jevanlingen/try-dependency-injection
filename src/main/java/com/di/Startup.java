package com.di;

import com.di.annotations.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Startup {

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

    private static void initializeClasses() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final var suitableClasses = new ArrayList<>(getSuitableClasses());
        final var initializedClasses = new HashMap<Class<?>, Object>();

        while (initializedClasses.size() != suitableClasses.size()) {
            for (final var clazz : suitableClasses.stream().filter(it -> !initializedClasses.containsKey(it)).toList()) {
                final var constructor = clazz.getDeclaredConstructors()[0]; // this demo uses the [0] shortcut because each class has at least and at most 1 public constructor
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

        System.out.println("All classes loaded");
    }

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        initializeClasses();
    }
}
