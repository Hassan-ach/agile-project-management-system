# Agile Project Management System

A university project demonstrating **Clean Architecture** and **Domain-Driven Design** principles using Spring Boot. This agile project management system implements Scrum methodology with proper separation of concerns.

## Learning Objectives

This project was built to learn and demonstrate:

- **Clean Architecture**: Dependency inversion, layers with clear boundaries
- **Domain-Driven Design (DDD)**: Entities, value objects, repositories
- **Hexagonal Architecture**: Ports and adapters pattern
- **Spring Boot 4**: Latest Spring ecosystem features
- **JWT Security**: Stateless authentication and authorization
- **JPA/Hibernate**: Object-relational mapping
- **ArchUnit**: Architecture testing to enforce layer dependencies

## Architecture Overview

```
┌─────────────────────────────────────┐
│       Presentation Layer            │
│  ┌──────────────┐  ┌──────────┐     │
│  │ Controllers  │  │ Exception│     │
│  └──────────────┘  │ Handler  │     │
│                    └──────────┘     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Application Layer            │
│  ┌──────────────┐  ┌──────────┐    │
│  │  Use Cases   │  │   DTOs   │    │
│  └──────────────┘  │ Mappers  │    │
│                    └──────────┘    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Domain Layer               │
│  ┌──────────────┐  ┌──────────┐    │
│  │  Entities    │  │Repository│    │
│  └──────────────┘  │Interfaces│    │
│  ┌──────────────┐  └──────────┘    │
│  │  Exceptions  │                  │
│  └──────────────┘                  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Infrastructure Layer           │
│  ┌──────────────┐  ┌──────────┐    │
│  │JPA Entities  │  │Adapters  │    │
│  └──────────────┘  └──────────┘    │
│  ┌──────────────┐                  │
│  │Security/JWT  │                  │
│  └──────────────┘                  │
└─────────────────────────────────────┘
```

**Key Principle**: Dependencies point inward. Domain layer knows nothing about Spring, JPA, or HTTP.

## Tech Stack

| Technology | Purpose |
|------------|---------|
| **Spring Boot 4.0.0** | Application framework |
| **Java 21** | Language with modern features |
| **PostgreSQL** | Relational database |
| **Spring Data JPA** | Data persistence |
| **Spring Security + JWT** | Authentication/Authorization |
| **Lombok** | Boilerplate reduction |
| **SpringDoc OpenAPI** | API documentation (Swagger) |
| **ArchUnit** | Architecture testing |
| **Docker Compose** | Database containerization |

## Project Structure

```
src/main/java/com/ensa/agile/
├── domain/          # Business logic, entities, repository interfaces
├── application/     # Use cases, DTOs, mappers
├── presentation/    # REST controllers
└── infrastructure/  # JPA, Security implementations
```

Each layer has its own README with detailed documentation.

## Quick Start

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Setup

1. **Start PostgreSQL database:**
   ```bash
   docker-compose up -d
   ```

2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   # or
   mvn spring-boot:run
   ```

3. **Access API documentation:**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI JSON: http://localhost:8080/v3/api-docs

### Default Users

The system creates default users on startup:
- **Admin**: admin@agile.com / admin123
- **User**: user@agile.com / user123

## Architecture Layers

| Layer | Responsibility | Read More |
|-------|----------------|-----------|
| **Domain** | Core business logic, framework-agnostic | [domain/README.md](src/main/java/com/ensa/agile/domain/README.md) |
| **Application** | Use cases, orchestration | [application/README.md](src/main/java/com/ensa/agile/application/README.md) |
| **Presentation** | HTTP handling, REST API | [presentation/README.md](src/main/java/com/ensa/agile/presentation/README.md) |
| **Infrastructure** | Technical implementations | [infrastructure/README.md](src/main/java/com/ensa/agile/infrastructure/README.md) |

## Domain Model

```
ProductBackLog
    │
    ├── Epic
    │     │
    │     └── UserStory
    │           │
    │           ├── SprintBackLog (optional)
    │           │
    │           └── Task
    │                 │
    │                 └── User (assignee)
    │
    └── ProjectMember
          │
          └── User
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - Authenticate and get JWT token
- `POST /api/auth/register` - Register new user

### Product Backlog
- `GET /api/products` - List all products
- `POST /api/products` - Create product
- `GET /api/products/{id}` - Get product details

### Epics
- `GET /api/epics` - List all epics
- `POST /api/epics` - Create epic
- `PUT /api/epics/{id}` - Update epic

### User Stories
- `GET /api/stories` - List all stories
- `POST /api/stories` - Create story
- `PUT /api/stories/{id}` - Update story
- `POST /api/stories/{id}/assign-to-sprint` - Assign to sprint

### Sprints
- `GET /api/sprints` - List all sprints
- `POST /api/sprints` - Create sprint
- `PUT /api/sprints/{id}/start` - Start sprint
- `PUT /api/sprints/{id}/complete` - Complete sprint

### Tasks
- `GET /api/tasks` - List all tasks
- `POST /api/tasks` - Create task
- `PUT /api/tasks/{id}` - Update task
- `PUT /api/tasks/{id}/assign` - Assign to user

## Testing

Run architecture tests to verify layer dependencies:

```bash
./mvnw test -Dtest=ArchitectureTest
```

Run all tests:

```bash
./mvnw test
```
