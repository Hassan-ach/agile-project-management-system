# Application Layer

## Concept

The **Application Layer** contains application-specific business rules and orchestrates use cases. It:

- Defines use cases (application services)
- Coordinates domain objects to perform tasks
- Defines DTOs for input/output
- Maps between DTOs and domain entities
- Handles transactions
- Knows about domain layer, but not about infrastructure

```
┌─────────────────────────────────────────┐
│        Application Layer                │
│                                         │
│  ┌──────────────┐  ┌──────────────┐    │
│  │   Use Cases  │  │     DTOs     │    │
│  │              │  │              │    │
│  │ - LoginUC    │  │ - Request    │    │
│  │ - RegisterUC │  │ - Response   │    │
│  │ - CreateEpic │  │              │    │
│  │ - StartSprnt │  │  Immutable   │    │
│  └──────────────┘  └──────────────┘    │
│                                         │
│  ┌──────────────┐  ┌──────────────┐    │
│  │   Mappers    │  │  Exceptions  │    │
│  │              │  │              │    │
│  │ Entity->DTO  │  │ AppException │    │
│  │ DTO->Entity  │  │ Unauthorized │    │
│  └──────────────┘  └──────────────┘    │
│                                         │
│  ┌──────────────┐  ┌──────────────┐    │
│  │  Security    │  │ Transaction  │    │
│  │ Interfaces   │  │   Wrapper    │    │
│  │              │  │              │    │
│  │ - JwtService │  │ @Transactional│   │
│  └──────────────┘  └──────────────┘    │
│                                         │
└─────────────────────────────────────────┘
              │
              │ uses
              ▼
┌─────────────────────────────────────────┐
│           Domain Layer                  │
│        (entities, repositories)         │
└─────────────────────────────────────────┘
```

## Why This Matters

The application layer acts as a **mediator** between the presentation layer and the domain layer:

- **Presentation** doesn't directly manipulate domain entities
- **Use cases** represent specific business operations
- **DTOs** prevent leaking domain details to the outside world

## Structure

```
application/
├── user/
│   ├── usecase/
│   │   ├── LoginUseCase.java
│   │   ├── RegisterUseCase.java
│   │   ├── GetUserInfoUseCase.java
│   │   └── UpdateUserUseCase.java
│   ├── request/
│   │   ├── AuthenticationRequest.java
│   │   └── RegisterRequest.java
│   ├── response/
│   │   ├── AuthenticationResponse.java
│   │   └── UserInfoResponse.java
│   ├── mapper/
│   │   └── UserMapper.java
│   └── exception/
│       ├── AuthenticationException.java
│       └── UserAlreadyExistsException.java
├── epic/
│   ├── usecase/
│   │   ├── CreateEpicUseCase.java
│   │   ├── UpdateEpicUseCase.java
│   │   ├── GetEpicUseCase.java
│   │   └── ListEpicsUseCase.java
│   ├── request/
│   │   └── EpicRequest.java
│   ├── response/
│   │   └── EpicResponse.java
│   └── mapper/
│       └── EpicMapper.java
├── story/
│   ├── usecase/
│   │   ├── CreateStoryUseCase.java
│   │   ├── UpdateStoryUseCase.java
│   │   ├── AssignToSprintUseCase.java
│   │   └── ListStoriesUseCase.java
│   ├── request/
│   │   └── StoryRequest.java
│   ├── response/
│   │   └── StoryResponse.java
│   └── mapper/
│       └── StoryMapper.java
├── sprint/
│   ├── usecase/
│   │   ├── CreateSprintUseCase.java
│   │   ├── StartSprintUseCase.java
│   │   ├── CompleteSprintUseCase.java
│   │   └── GetSprintDetailsUseCase.java
│   ├── request/
│   │   └── SprintRequest.java
│   ├── response/
│   │   └── SprintResponse.java
│   └── mapper/
│       └── SprintMapper.java
├── task/
│   ├── usecase/
│   │   ├── CreateTaskUseCase.java
│   │   ├── UpdateTaskUseCase.java
│   │   ├── AssignTaskUseCase.java
│   │   └── LogHoursUseCase.java
│   ├── request/
│   │   └── TaskRequest.java
│   ├── response/
│   │   └── TaskResponse.java
│   └── mapper/
│       └── TaskMapper.java
├── product/
│   ├── usecase/
│   ├── request/
│   ├── response/
│   └── mapper/
└── global/
    ├── usecase/
    │   ├── BaseUseCase.java      # Abstract template
    │   └── IBaseUseCase.java     # Interface
    ├── service/
    │   └── ApplicationService.java
    └── transaction/
        └── TransactionalWrapper.java
```

## Use Case Pattern

Use cases follow a consistent pattern:

```java
public class CreateEpicUseCase {
    
    private final EpicRepository epicRepository;
    private final ProductRepository productRepository;
    
    public CreateEpicUseCase(EpicRepository epicRepository, 
                             ProductRepository productRepository) {
        this.epicRepository = epicRepository;
        this.productRepository = productRepository;
    }
    
    public EpicResponse execute(CreateEpicRequest request) {
        // 1. Validate input
        // 2. Load dependencies
        ProductBackLog product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        // 3. Create domain entity
        Epic epic = new Epic(request.getTitle(), request.getDescription(), product);
        
        // 4. Persist
        Epic saved = epicRepository.save(epic);
        
        // 5. Return DTO
        return EpicMapper.toResponse(saved);
    }
}
```

## DTOs (Data Transfer Objects)

### Request DTOs
Input from presentation layer:

```java
public class CreateEpicRequest {
    private final String title;
    private final String description;
    private final UUID productId;
    
    // Constructor, getters only (immutable)
}
```

### Response DTOs
Output to presentation layer:

```java
public class EpicResponse {
    private final UUID id;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    
    // Constructor, getters only
}
```

**Why DTOs?**
- Decouple API from domain model
- Control what data is exposed
- Flatten complex domain relationships
- Version the API independently

## Mappers

Convert between domain entities and DTOs:

```java
public class EpicMapper {
    
    public static EpicResponse toResponse(Epic epic) {
        return new EpicResponse(
            epic.getId(),
            epic.getTitle(),
            epic.getDescription(),
            epic.getCreatedAt()
        );
    }
    
    public static List<EpicResponse> toResponseList(List<Epic> epics) {
        return epics.stream()
            .map(EpicMapper::toResponse)
            .toList();
    }
}
```

## Security in Application Layer

Security interfaces are defined here, implemented in infrastructure:

```java
// Interface in application layer
public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, User user);
}

// Implementation in infrastructure layer
@Component
public class JwtServiceImpl implements JwtService {
    // JWT logic here
}
```

## Transaction Management

Use cases mark transactions at the application boundary:

```java
@Transactional
public class StartSprintUseCase {
    
    public SprintResponse execute(StartSprintRequest request) {
        // Multiple operations that must succeed or fail together
        SprintBackLog sprint = sprintRepository.findById(request.getSprintId())
            .orElseThrow(() -> new EntityNotFoundException("Sprint not found"));
        
        sprint.start();
        sprintRepository.save(sprint);
        
        // Update all stories in sprint
        for (UserStory story : sprint.getStories()) {
            story.markAsInProgress();
            storyRepository.save(story);
        }
        
        return SprintMapper.toResponse(sprint);
    }
}
```

## Common Use Cases

### User Management
- `RegisterUseCase` - Create new user account
- `LoginUseCase` - Authenticate and generate JWT
- `GetUserInfoUseCase` - Load user profile

### Epic Management
- `CreateEpicUseCase` - Create epic in product backlog
- `UpdateEpicUseCase` - Modify epic details
- `ListEpicsUseCase` - Get all epics for a product

### Story Management
- `CreateStoryUseCase` - Create user story in epic
- `AssignToSprintUseCase` - Move story to sprint
- `UpdateStoryStatusUseCase` - Change story status

### Sprint Management
- `CreateSprintUseCase` - Create new sprint
- `StartSprintUseCase` - Begin sprint (activates stories)
- `CompleteSprintUseCase` - Finish sprint

### Task Management
- `CreateTaskUseCase` - Add task to story
- `AssignTaskUseCase` - Assign to user
- `LogHoursUseCase` - Record time spent

## Application Exceptions

Use case-specific errors:

- `AuthenticationException` - Invalid credentials
- `UserAlreadyExistsException` - Duplicate email
- `UnauthorizedException` - Permission denied
- `InvalidOperationException` - Business rule violation

## Learning Points

1. **Use Cases as API**: Each use case represents one user action
2. **Single Responsibility**: One use case does one thing
3. **DTOs for Decoupling**: API contracts separate from domain
4. **Transaction Boundaries**: Use cases define transaction scope
5. **Dependency Injection**: Repositories injected via constructors
6. **No Business Logic**: Orchestration only, domain entities hold logic

## Testing

Use cases tested with mocked repositories:

```java
@ExtendWith(MockitoExtension.class)
class CreateEpicUseCaseTest {
    
    @Mock
    private EpicRepository epicRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private CreateEpicUseCase useCase;
    
    @Test
    void shouldCreateEpic() {
        // Given
        CreateEpicRequest request = new CreateEpicRequest("Title", "Desc", UUID.randomUUID());
        when(productRepository.findById(any())).thenReturn(Optional.of(new ProductBackLog()));
        when(epicRepository.save(any())).thenReturn(new Epic());
        
        // When
        EpicResponse response = useCase.execute(request);
        
        // Then
        assertNotNull(response);
        verify(epicRepository).save(any());
    }
}
```

## Files to Review

- `global/usecase/BaseUseCase.java` - Template pattern
- `user/usecase/LoginUseCase.java` - Authentication flow
- `sprint/usecase/StartSprintUseCase.java` - Transaction example
- Any mapper class - DTO conversion
- Any request/response DTO - API contracts
