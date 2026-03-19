# Functionality Documentation

> Current state as of 2026-03-19. Branch: `feature/first`.

---

## Overview

A **Points Counter** web application built with Quarkus 3.32.4. It exposes a small REST API backed by an in-memory atomic counter and serves a single-page HTML dashboard at the root path.

---

## Project Structure

Classes are organised into **feature packages** — all code for a given domain lives together rather than being split by layer.

```
experiment/
├── src/
│   ├── main/
│   │   ├── java/org/acme/
│   │   │   ├── counter/
│   │   │   │   ├── CounterResource.java    # REST endpoints for the counter
│   │   │   │   └── CounterService.java     # Counter business logic (singleton)
│   │   │   └── greeting/
│   │   │       └── GreetingResource.java   # Hello World endpoint
│   │   └── resources/
│   │       ├── application.properties      # Quarkus config (defaults only)
│   │       └── META-INF/resources/
│   │           └── index.html              # Web UI dashboard
│   └── test/
│       └── java/org/acme/
│           ├── counter/
│           │   ├── CounterServiceTest.java # Unit tests for CounterService
│           │   └── CounterResourceTest.java# Integration tests for /api/counter
│           └── greeting/
│               ├── GreetingResourceTest.java # Tests for /hello
│               └── GreetingResourceIT.java   # IT for packaged mode
├── .github/workflows/ci.yml               # GitHub Actions CI pipeline
├── pom.xml                                # Maven build config
└── README.md
```

---

## REST API Endpoints

| Method | Path                        | Response        | Description                          |
|--------|-----------------------------|-----------------|--------------------------------------|
| GET    | `/hello`                    | `text/plain`    | Returns "Hello from Quarkus REST"    |
| GET    | `/api/counter`              | `text/plain`    | Returns current counter value        |
| POST   | `/api/counter/increment`    | `text/plain`    | Increments counter, returns new value|
| POST   | `/api/counter/decrement`    | `text/plain`    | Decrements counter, returns new value|
| POST   | `/api/counter/reset`        | `text/plain`    | Resets counter to 0, returns 0       |
| GET    | `/`                         | `text/html`     | Serves the web UI dashboard          |

---

## Backend

### `CounterService` (`@ApplicationScoped`)

Singleton CDI bean. Manages an `AtomicInteger` counter, providing thread-safe operations.

| Method        | Returns | Behaviour                          |
|---------------|---------|------------------------------------|
| `get()`       | `int`   | Returns current value              |
| `increment()` | `int`   | Atomically increments, returns new |
| `decrement()` | `int`   | Atomically decrements, returns new |
| `reset()`     | `int`   | Resets to 0, returns 0             |

Initial value: `0`. State is in-memory only — restarting the application resets the counter.

### `CounterResource`

JAX-RS resource at base path `/api/counter`. Injects `CounterService` and maps each HTTP call to the corresponding service method. All responses are `text/plain` integer strings.

### `GreetingResource`

JAX-RS resource at `/hello`. Returns the static string `"Hello from Quarkus REST"`. Serves as a Hello World / health-check endpoint.

---

## Frontend

`src/main/resources/META-INF/resources/index.html` is served automatically by Quarkus at `http://localhost:8080/`.

- **Score display** — large centred number showing the current counter value.
- **Three buttons** — `+ Point` (green), `- Point` (red), `Reset` (grey).
- **JavaScript** — a shared `callApi(path, method)` async helper calls the REST API and updates the score display. Errors are shown in red below the buttons.
- On page load, `GET /api/counter` is called to populate the initial value.

---

## Build & Run

| Action                | Command                              |
|-----------------------|--------------------------------------|
| Dev mode (hot reload) | `mvn quarkus:dev`                    |
| Run unit tests        | `mvn test`                           |
| Run all tests         | `mvn verify`                         |
| Package JAR           | `mvn package`                        |
| Run packaged JAR      | `java -jar target/quarkus-app/quarkus-run.jar` |
| Native build          | `mvn package -Pnative`               |

Default port: **8080**.

---

## Tech Stack

| Layer       | Technology                              |
|-------------|-----------------------------------------|
| Framework   | Quarkus 3.32.4                          |
| REST        | JAX-RS (quarkus-rest)                   |
| DI          | CDI / Arc                               |
| Java        | Java 21                                 |
| Build       | Maven 3.9+ (Maven Wrapper included)     |
| Testing     | JUnit 5 + REST Assured                  |
| CI          | GitHub Actions (`mvn verify` on push)   |
| Containers  | Dockerfiles for JVM and native (GraalVM)|

---

## Tests

### Unit Tests (`mvn test`)

**`CounterServiceTest`** — pure JUnit 5, no Quarkus bootstrap, instantiates `CounterService` directly:
- Initial value is 0
- Two successive increments yield 1, then 2
- Decrement from 0 yields -1
- Increment twice then reset returns 0

**`GreetingResourceTest`** — `@QuarkusTest`, asserts `GET /hello` returns HTTP 200 with body `"Hello from Quarkus REST"`.

### Integration Tests (`mvn verify`)

**`CounterResourceTest`** — `@QuarkusTest`, resets counter before each test:
- `GET /api/counter` returns `"0"`
- `POST /api/counter/increment` returns `"1"`
- `POST /api/counter/decrement` returns `"-1"`
- Increment twice then reset returns `"0"`

**`GreetingResourceIT`** — extends `GreetingResourceTest` with `@QuarkusIntegrationTest` for packaged/native mode.

---

## CI/CD

`.github/workflows/ci.yml` triggers on:
- Push to `main` or `feature/**` branches
- Pull requests targeting `main`

Steps: checkout → Java 21 (Temurin) setup with Maven cache → `mvn verify`.
