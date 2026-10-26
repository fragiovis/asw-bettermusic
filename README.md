# 🎧 BetterMusic — Event-Driven Microservices (first iteration)

*Software Architecture course (Architettura dei Sistemi Software) · Università degli Studi Roma Tre (A.Y. 2025/26)*

> This is my first iteration of the project. The team version, with events for every service, is in **[fragiovis/asw-bettermusic-1](https://github.com/fragiovis/asw-bettermusic-1)**.

BetterMusic is a small social network for music album reviews, built as Spring Boot microservices (`album`, `recensioni`, `connessioni`, `recensioni-seguite`) behind a Spring Cloud Gateway, with Consul for service discovery. The course provides a version in which services call each other synchronously over REST. The goal is to move it to an event-driven architecture.

## What this iteration adds
- **Database per service**: every service moved from in-memory HSQLDB to its own **PostgreSQL** instance, defined in `docker-compose.yml`.
- **First Kafka event flow**: `album` publishes an `AlbumCreatedEvent` on the `album.created` topic. `recensioni` consumes it with a `@KafkaListener` and stores a local album replica (JPA). Creating a review therefore no longer makes a remote call to the `album` service, which improves availability.
- **End-to-end test** through the API gateway: `test_kafka_album_recensioni.sh`.

Step-by-step change log (Italian): [`log_modifiche.md`](log_modifiche.md) · Original assignment and service API (Italian): [`ASSIGNMENT.md`](ASSIGNMENT.md)

## Running it
Requirements: Java 21, Gradle, Docker.

```bash
docker compose up -d consul kafka album-db recensioni-db connessioni-db recensioni-seguite-db
gradle build
./start-bettermusic.sh
./test_kafka_album_recensioni.sh
```

## Tech stack
Java 21 · Spring Boot 3 · Spring Cloud (Gateway, Consul) · Spring Data JPA · Spring Kafka · Apache Kafka · PostgreSQL · Docker Compose
