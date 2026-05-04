# Try Dependency Injection & Events

Welcome to this educational repository designed to demystify the core mechanics of Dependency Injection (DI) and Event-Driven Architecture in Java.
This project demonstrates how modern frameworks (like Spring or Micronaut) use annotations and reflection to automate object lifecycle management and service communication.

## Key Features

### 1. Annotation-Based DI
The project implements a custom DI container that automatically instantiates and wires beans using:
- **Stereotype Annotations**: `@Service`, `@Repository`, `@RestController`, `@Bean`, and `@Configuration`.
- **Automatic Constructor Injection**: Dependencies are resolved and injected into constructors automatically during startup.
- **Service Discovery**: Uses the `reflections` library to scan the classpath for annotated classes.

### 2. Asynchronous Event System
A fully decoupled communication layer that allows services to interact without direct dependencies:
- **`@EventListener`**: A custom annotation to mark methods that handle specific events.
- **Type-Aware EventBus**: A centralized bus that ensures events are only delivered to listeners with matching method signatures.
- **Non-Blocking Execution**: Built on a `CachedThreadPool`, ensuring that event publishing and processing happen on separate threads.

## How it Works
1. **Scanning**: On startup, the `Startup` class scans the `com.di` package for suitable classes.
2. **Dependency Resolution**: A custom initialization loop resolves the dependency graph, ensuring beans are created in the correct order.
3. **Event Registration**: Once beans are initialized, the container scans for `@EventListener` methods and registers them with the `EventBus`.
4. **Lifecycle**: The application remains active, allowing the asynchronous event flow to continue indefinitely.

---

*Note: This is a simplified implementation for learning purposes. It focuses on clarity rather than handling advanced scenarios like circular dependencies, scoped beans (Prototype/Request), or complex proxying.*
