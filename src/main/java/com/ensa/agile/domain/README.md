# Domain Layer

## Concept

The **Domain Layer** is the heart of Clean Architecture. It contains:

- Core business logic
- Domain entities
- Repository interfaces (ports)
- Domain exceptions
- Business rules and validations

## Key Principle

**Zero external dependencies** - This layer has NO imports from Spring, JPA, HTTP, or any framework. It's pure Java business logic.

```
┌─────────────────────────────────────────┐
│           Domain Layer                  │
│                                         │
│  ┌──────────────┐  ┌──────────────┐    │
│  │   Entities   │  │  Repository  │    │
│  │              │  │  Interfaces  │    │
│  │  - User      │  │   (Ports)    │    │
│  │  - Epic      │  │              │    │
│  │  - Story     │  │  - save()    │    │
│  │  - Task      │  │  - findById()│    │
│  │  - Sprint    │  │  - delete()  │    │
│  └──────────────┘  └──────────────┘    │
│                                         │
│  ┌──────────────┐  ┌──────────────┐    │
│  │   Enums      │  │  Exceptions  │    │
│  │              │  │              │    │
│  │  - Status    │  │  - DomainEx  │    │
│  │  - Priority  │  │  - NotFound  │    │
│  └──────────────┘  └──────────────┘    │
│                                         │
└─────────────────────────────────────────┘
              ▲
              │ depends on
   ┌──────────┴──────────┐
   │   Outer Layers      │
   └─────────────────────┘
```

## Why This Matters

In traditional MVC, entities are often JPA entities with annotations like `@Entity`, `@Id`, etc. This couples your business logic to your database technology.

**In Clean Architecture:**
- Domain entities are plain Java objects (POJOs)
- They contain business methods, not just getters/setters
- They enforce business rules through encapsulation

## Structure

```
domain/
├── user/
│   ├── entity/
│   │   └── User.java           # Domain entity with business logic
│   └── repository/
│       └── UserRepository.java # Interface (port)
├── epic/
│   ├── entity/
│   │   └── Epic.java
│   └── repository/
│       └── EpicRepository.java
├── story/
│   ├── entity/
│   │   ├── UserStory.java
│   │   └── UserStoryHistory.java
│   ├── repository/
│   │   └── UserStoryRepository.java
│   └── enums/
│       └── StoryStatus.java
├── sprint/
│   ├── entity/
│   │   ├── SprintBackLog.java
│   │   ├── SprintHistory.java
│   │   └── SprintMember.java
│   ├── repository/
│   │   └── SprintRepository.java
│   └── enums/
│       └── SprintStatus.java
├── task/
│   ├── entity/
│   │   ├── Task.java
│   │   └── TaskHistory.java
│   ├── repository/
│   │   └── TaskRepository.java
│   └── enums/
│       └── TaskStatus.java
├── product/
│   ├── entity/
│   │   ├── ProductBackLog.java
│   │   └── ProjectMember.java
│   ├── repository/
│   │   └── ProductRepository.java
│   └── enums/
│       └── ProjectRole.java
└── global/
    ├── entity/
    │   └── BaseDomainEntity.java    # Abstract base with common fields
    ├── repository/
    │   └── BaseDomainRepository.java
    ├── exception/
    │   ├── DomainException.java
    │   ├── EntityNotFoundException.java
    │   └── ValidationException.java
    ├── annotation/
    │   └── DomainAnnotation.java
    └── utils/
        └── ValidationUtils.java
```

## Entities

### User
Core user entity with authentication logic:
- Validates email format
- Validates password strength
- Role-based access control

**Key methods:**
- `authenticate(password)` - Validates credentials
- `changePassword(old, new)` - Enforces password policy

### Epic
Large body of work that can be broken down into stories:
- Has many user stories
- Belongs to a product backlog

### UserStory
User-centric feature description:
- Status tracking (TODO, IN_PROGRESS, DONE)
- Story points estimation
- Can be assigned to a sprint
- Has multiple tasks

**Business Rules:**
- Story points must be positive
- Cannot change status if sprint is completed
- Must have at least one task to be marked done

### SprintBackLog
Time-boxed iteration:
- Start/end dates
- Status lifecycle (PLANNED → ACTIVE → COMPLETED)
- Contains multiple user stories

**Business Rules:**
- Cannot have overlapping dates with other active sprints
- Must have at least one story to start
- Cannot add stories after completion

### Task
Individual work item:
- Assignee (User)
- Estimated hours
- Actual hours spent
- Status tracking

### ProductBackLog
Container for all work:
- Multiple epics
- Project members

## Repository Interfaces (Ports)

Repositories define contracts for data access without specifying implementation:

```java
public interface UserRepository {
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    User save(User user);
    void delete(UserId id);
    List<User> findAll();
}
```

**Note:** These are interfaces in the domain layer. The actual implementation (JPA, MongoDB, etc.) lives in the infrastructure layer.

## Enums

Domain-specific enumerations:

- `StoryStatus`: TODO, IN_PROGRESS, TESTING, DONE
- `SprintStatus`: PLANNED, ACTIVE, COMPLETED, CANCELLED
- `TaskStatus`: TODO, IN_PROGRESS, BLOCKED, DONE
- `UserRole`: ADMIN, PRODUCT_OWNER, SCRUM_MASTER, DEVELOPER

## Domain Exceptions

Business rule violations:

- `DomainException` - Base exception
- `EntityNotFoundException` - When entity doesn't exist
- `ValidationException` - Invalid business data
- `BusinessRuleViolationException` - Rule broken

## Base Domain Entity

All domain entities extend `BaseDomainEntity` which provides:
- `id` - Unique identifier
- `createdAt` - Creation timestamp
- `updatedAt` - Last update timestamp
- `version` - Optimistic locking

## Testing

Since domain layer has no external dependencies, testing is straightforward:

```java
@Test
void shouldNotAllowNegativeStoryPoints() {
    assertThrows(ValidationException.class, () -> {
        new UserStory("Title", "Desc", -5);
    });
}

@Test
void shouldCalculateRemainingHours() {
    Task task = new Task("Title", 8);
    task.logHours(3);
    assertEquals(5, task.getRemainingHours());
}
```

## Learning Points

1. **Encapsulation**: Entities hide internal state and expose behavior
2. **Rich Domain Model**: Entities have methods, not just data
3. **Immutability**: Value objects are immutable
4. **Validation**: Business rules enforced in domain, not database
5. **No Framework Dependencies**: Pure Java = easy to test, framework-agnostic

## Files to Review

- `user/entity/User.java` - Authentication logic
- `story/entity/UserStory.java` - Status transitions
- `sprint/entity/SprintBackLog.java` - Date validation
- `global/entity/BaseDomainEntity.java` - Common fields
- All repository interfaces - Port definitions
