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

### 3. Lightweight HTTP Server
A simple server implementation that demonstrates infrastructure bootstrapping:
- **Low-Level Socket Handling**: Shows how a basic HTTP server can be built using standard Java `ServerSocket`.
- **DI Integration**: The server is managed as a bean within the DI container, showcasing how lifecycle management extends to infrastructure.

## How it Works
The startup process is divided into two distinct phases:

### Phase 1: The DI Container
1. **Scanning**: The `SetupConfigurer` scans the `com.di` package to identify all classes marked with stereotype annotations.
2. **Dependency Resolution**: A custom initialization loop resolves the dependency graph, instantiating beans and injecting their dependencies via constructors.

### Phase 2: Feature Bootstrapping
Depending on the flags in `Startup.java`, the following steps are performed:
1. **Event Registration**: If `ENABLE_EVENTS` is active, the container scans the initialized beans for `@EventListener` methods and wires them to the `EventBus`.
2. **Infrastructure Startup**: If `ENABLE_SERVER` is active, the `Server` bean is launched on port 8080.
3. **Continuous Execution**: The application stays alive, allowing the asynchronous event system and HTTP server to handle incoming traffic and internal communication.

## Configuration
The application behavior can be toggled in the `Startup.java` class using the following flags:

- **`ENABLE_EVENTS`**: Enables the initialization of the `EventBus` and registration of `@EventListener` methods.
- **`ENABLE_EVENT_EXAMPLE_FLOW`**: If events are enabled, this triggers an initial `DataEvent` to demonstrate the asynchronous flow.
- **`ENABLE_SERVER`**: Starts the internal HTTP server on port 8080.

---

*Note: This is a simplified implementation for learning purposes. It focuses on clarity rather than handling advanced scenarios like circular dependencies, scoped beans (Prototype/Request), or complex proxying.*
