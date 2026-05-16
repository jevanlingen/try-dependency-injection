# MiniFramework: Educational DI & Web Framework

Welcome to **MiniFramework**, a lightweight, "from-scratch" Java framework designed to demystify the core mechanics of modern application development. This project evolved from a simple Dependency Injection exercise into a small, functional framework that demonstrates how annotations, reflection, and low-level networking can be combined to automate object lifecycle management, event-driven communication, and HTTP routing.

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
A simple server implementation that demonstrates infrastructure bootstrapping and request routing:
- **Low-Level Socket Handling**: Shows how a basic HTTP server can be built using standard Java `ServerSocket`.
- **Dynamic Routing**: Supports path parameters using `{}` placeholders (e.g., `@GET("/user/{id}")`).
- **Positional Parameter Mapping**: Automatically maps path segments to method arguments by position, with basic type conversion for `int` and `String`.
- **DI Integration**: The server is managed as a bean within the DI container, invoking methods on `@RestController` beans that have been injected with their required services.

## Example Application
The `com.di.exampleapp` package contains a vertical slice demonstrating the full stack:
- **`UserController`**: Handles HTTP requests and uses `@GET` and `@POST` annotations.
- **`UserService`**: Contains business logic and orchestrates data flow.
- **`UserRepository`**: Manages an in-memory list of `User` records.
- **`User` model**: A simple record used to demonstrate data transfer through the layers.

Example interaction:
- `GET /user` -> Returns a list of all users.
- `GET /user/1` -> Returns the user with ID 1.
- `POST /user` -> Creates a new user and returns it with a generated ID.

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
