import com.di.annotations.*;
import com.di.architecture.EventBus;
import com.di.model.ExampleEvent;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

void main() throws InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {
    final var initializedBeans = initializeClasses();
    initializedEventBus(initializedBeans);

    // Stay alive
    while (true) {
        Thread.sleep(1000);
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

private static void initializedEventBus(Map<Class<?>, Object> initializedBeans) {
    EventBus eventBus = (EventBus) initializedBeans.get(EventBus.class);
    if (eventBus != null) {
        for (Object bean : initializedBeans.values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventListener.class)) {
                    eventBus.register(bean, method);
                }
            }
        }

        eventBus.publish(new ExampleEvent("Start the event flow..."));
    }
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
