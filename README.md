# REST Calls - BE Home Assignment

**Vishal Pandey**

## 🚀 Project Overview

This project implements a Spring Boot service that:

* Exposes an endpoint to update the status of an event.
* Periodically polls a mocked external API for "live" events.
* Publishes event scores to a Kafka topic.
* Uses scheduled tasks and includes structured logging, validation, and error handling.
* Includes unit and integration tests.

---

## 🔧 Setup & Run Instructions

### ✅ Prerequisites

* Java 17+
* Maven or Gradle
* Kafka (local or remote)
* Docker (optional, for Kafka + Mock API)

### 🛠️ Running the Application

#### Using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

#### Using Gradle:

```bash
./gradlew bootRun
```

### 🔁 Kafka Setup (Quick Start via Docker)

```bash
docker compose up -d
```
### 🔁 check events in kafka
```bash
docker exec -it sports-tracker-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic live-sports-events --from-beginning
```

Ensure your application connects to Kafka as defined in `application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```

---

## 🧪 How to Run Tests

Run all unit and integration tests:

### Maven

```bash
mvn test
```

### Gradle

```bash
./gradlew test
```

### Check formatting with Spotless

```bash
mvn spotless:check      # or ./gradlew spotlessCheck
mvn spotless:apply      # or ./gradlew spotlessApply
```

---

## 🧩 Design Decisions

### ✅ Event-Driven Scheduler

* Introduced `LiveEventScheduler` with `startLiveEvent()` and `stopLiveEvent()` to dynamically manage polling per event.
* Replaced the fixed `@Scheduled` polling with event-driven control based on `eventId` and `status`.

### ✅ Error Handling

* External REST failures and Kafka publishing failures are logged and retried.
* Exception handling is centralized in controller layer for meaningful error responses.

### ✅ In-Memory State

* Live event tracking uses `ConcurrentHashMap` to manage scheduled polling jobs.
* This is suitable for prototype/single-instance environments.

### ✅ Tests

* Unit tests for:

    * Status endpoint validation
    * Live scheduler logic
    * Error and success paths
* MockMvc tests for REST endpoints

---

## 🤖 AI Assistance

This project used **ChatGPT (OpenAI)** for the following:

| Area                   | AI Contribution                                                      | Human Review                          |
| ---------------------- | -------------------------------------------------------------------- | ------------------------------------- |
| Scheduler Design       | Generated a clean pattern for per-event polling with `TaskScheduler` | ✅ Reviewed & integrated manually      |
| Spotless Config        | Suggested plugin setup for Maven/Gradle                              | ✅ Verified                            |
| Error Logging Patterns | Generated structured logging templates                               | ✅ Edited messages & parameters        |
| README Template        | Base structure & sections                                            | ✅ Customized project-specific content |

All AI-generated parts were **manually tested**, improved, and aligned with the assignment requirements.

---

## 📂 Folder Structure

```
src/
├── main/
│   ├── java/com/sporty/sportstracker/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── service/
│   │   ├── config/
│   │   └── constants/
│   └── resources/
│       └── application.yml
├── test/
│   ├── java/...
```

---

## 📬 Endpoint

### POST `/events/status`

```json
{
  "eventId": "match-123",
  "status": true
}
```

* Starts polling if `status = true`
* Stops polling if `status = false`
* Returns `400` if validation fails.

---

## ✨ Future Enhancements

* Persist event status to a database
* Dynamic polling interval per event
* Metrics and observability (Micrometer, Prometheus)

---
