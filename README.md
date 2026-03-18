# experiment

A Hello World REST API built with [Quarkus](https://quarkus.io/) 3.32.4.

Exposes a single endpoint: `GET /hello` → `Hello from Quarkus REST`

## Requirements

- Java 21+
- Maven 3.9+

## Build

```bash
mvn package
```

## Run

**Dev mode** (live reload enabled):
```bash
mvn quarkus:dev
```

**From packaged JAR:**
```bash
mvn package
java -jar target/quarkus-app/quarkus-run.jar
```

The app starts on http://localhost:8080

## Test

```bash
# Unit tests
mvn test

# Unit + integration tests (requires native build)
mvn verify -Pnative
```

## Endpoints

| Method | Path | Response |
|--------|------|----------|
| GET | `/hello` | `Hello from Quarkus REST` |
