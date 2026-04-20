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
