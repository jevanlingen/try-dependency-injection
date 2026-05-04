# Try Dependency Injection

Welcome to this GitHub repository dedicated to learning a simple dependency injection.
Here, you'll find a collection of Java classes demonstrating how you could create your own dependency injection.

Note that this is a very basic implementation just for demonstration purposes. It now also includes a simple, **asynchronous event-based system**:
- **`@EventListener`**: Annotate methods to automatically register them as event listeners.
- **`EventBus`**: A centralized, type-aware bus that dispatches events to the correct listeners on separate threads.
- **Decoupled Communication**: Services can publish events without knowing who is listening, ensuring a non-blocking flow.

This implementation is still simplified and doesn't handle more advanced scenarios like circular dependencies or handling different scopes for dependencies.
It's important to keep in mind that building a robust and production-ready dependency injection framework requires a lot more consideration and features.
