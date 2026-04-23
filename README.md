# Smart Campus API

## Overview
This is a JAX-RS RESTful web service built to manage a "Smart Campus". It provides a robust backend for managing Rooms, Sensors, and 
Sensor Readings using an embedded server configuration and Jersey for JSON binding.

## Build and Run Instructions
1. Clone this repository to your local machine.
2. Open the project in Apache NetBeans.
3. Right-click the project and select **Clean and Build** to download Maven dependencies.
4. Open the `theshara.smartcampusapi.Main` class.
5. Right-click anywhere in the code and select **Run File**.
6. The server will start on `http://localhost:8080/api/v1/`.

## 5 Sample cURL Commands
1. **Discovery Endpoint:** `curl -X GET http://localhost:8080/api/v1/`
2. **Create Room:** `curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d '{"id":"R1","name":"Library",
"capacity":100}'`
3. **Get All Sensors:** `curl -X GET http://localhost:8080/api/v1/sensors`
4. **Filter Sensors:** `curl -X GET http://localhost:8080/api/v1/sensors?type=CO2`
5. **Add Reading:** `curl -X POST http://localhost:8080/api/v1/sensors/S1/readings -H "Content-Type: application/json" -d '{"value":400.5}'`

---

# Conceptual Report

## Part 1: Service Architecture & Setup
**Question**: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures $(maps/lists)$ to prevent data loss or race conditions.
**Answer**: By default, JAX-RS Resource classes are request-scoped, meaning the runtime instantiates a brand new object for every incoming HTTP request and destroys it immediately after the response is sent. Because these resource instances are ephemeral, storing data in standard instance variables will result in immediate data loss between requests. To maintain state, such as an in-memory database, developers must utilize static data structures or singleton beans. Furthermore, because multiple requests can instantiate multiple resource objects concurrently accessing this shared static data, synchronization mechanisms (like ConcurrentHashMap or synchronized blocks) must be implemented to prevent race conditions and ensure data integrity.

**Question**: Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?
**Answer**: Hypermedia as the Engine of Application State (HATEOAS) is considered a hallmark of advanced RESTful design because it transforms a static API into a discoverable, self-documenting state machine. Instead of requiring client developers to hardcode URLs and rigidly follow static documentation, the API response dynamically provides hypermedia links indicating what actions are currently permitted and where to find related resources. This decouples the client from the server's URL routing structure, allowing the backend to evolve or change routes without breaking client implementations, as the client simply follows the links provided at runtime.

## Part 2: Room Management
**Question**: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.
**Answer**: When returning a list of rooms, sending only IDs minimizes the payload size, significantly reducing network bandwidth consumption and lowering the immediate processing load on the server. However, this shifts the burden to the client, which may suffer from the "N+1 query problem" if it has to execute numerous subsequent HTTP requests to fetch the details for each individual ID to render a complete user interface. Conversely, returning full room objects increases the initial network transfer size and server memory usage but allows the client to immediately access and display all necessary data with a single request, improving perceived performance on the client side at the cost of heavier payload overhead.

**Question**: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.
**Answer**: Yes, the DELETE operation is idempotent because making multiple identical requests has the exact same effect on the server's overall state as making a single request. If a client mistakenly sends the exact same DELETE request for a room multiple times, the first request will successfully remove the room from the database and return a successful response (such as 204 No Content), fundamentally altering the server's state. Any subsequent duplicate requests will simply return a 404 Not Found error because the resource is already gone; although the HTTP response codes differ between the first and subsequent requests, the actual state of the database remains exactly the same (the room is absent) after the initial deletion, completely satisfying the requirement for idempotency.

## Part 3: Sensor Operations & Linking
**Question**: We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?
**Answer**: When a method is explicitly annotated with @Consumes(MediaType.APPLICATION_JSON), it enforces a strict contract that the API will only accept payloads formatted as JSON. If a client attempts to bypass this by sending data in an unsupported format, such as text/plain or application/xml, the JAX-RS runtime intercepts the request before it ever reaches the application logic. The framework automatically rejects the mismatched payload and returns an HTTP 415 Unsupported Media Type error to the client, ensuring the server does not waste resources attempting to parse incompatible data or crash due to unexpected format exceptions.

**Question**: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?
**Answer**: While path parameters (e.g., /api/v1/sensors/type/CO2) are intended to identify specific, unique resources or hierarchical relationships, query parameters (e.g., ?type=CO2) are fundamentally designed to filter, sort, or modify the view of an existing collection. The query parameter approach is considered superior for filtering because it is inherently optional and modular, allowing clients to easily combine multiple search criteria (like ?type=CO2&status=ACTIVE) without forcing the backend to define rigid, deeply nested URL routes for every possible combination. This maintains the semantic clarity that the client is still accessing the main /sensors collection, merely requesting a refined subset of those resources.

## Part 4: Deep Nesting with Sub-Resources
**Question**: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?
**Answer**: The Sub-Resource Locator pattern provides significant architectural benefits by delegating request routing from a parent resource class to a completely separate child resource class, preventing the parent from becoming a monolithic, unmaintainable "God class." In large APIs, defining every deeply nested path inside a single controller leads to bloated code that violates the Single Responsibility Principle and becomes incredibly difficult to read. By using sub-resources, the logic is decoupled into highly cohesive, focused classes, making the API significantly easier to test, debug, and scale, while allowing child resources to seamlessly inherit context (like parent IDs) from the routing hierarchy.

## Part 5: Advanced Error Handling
**Question**: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?
**Answer**: HTTP 422 Unprocessable Entity is semantically more accurate than a standard 404 Not Found when a valid JSON payload references a missing external entity (like a non-existent roomId), because a 404 specifically implies that the target URL endpoint itself does not exist. In this scenario, the target endpoint (e.g., /sensors) is perfectly valid, and the JSON syntax is perfectly formatted, meaning the server successfully received and understood the request format. The failure occurs in the semantic business logic because the referenced dependency is missing; thus, a 422 correctly communicates that the server understands the content type and syntax, but cannot process the contained semantic instructions.

**Question**: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?
**Answer**: Exposing internal Java stack traces to external API consumers presents a critical cybersecurity risk by providing attackers with a detailed blueprint of the server's internal architecture. A stack trace reveals highly sensitive intelligence, including the exact frameworks and libraries being used (e.g., Jersey, Hibernate, Spring), their specific version numbers, internal file paths, and the structure of the underlying database or custom classes. Attackers can leverage this precise intelligence to map out the application's attack surface and cross-reference the exposed framework versions against databases of known vulnerabilities (CVEs), significantly lowering the barrier to entry for launching targeted and devastating exploits.


