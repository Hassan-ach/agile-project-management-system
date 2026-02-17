# Presentation Layer

## Concept

The **Presentation Layer** (also called the "Interface Adapters" layer) is responsible for:

- Handling HTTP requests and responses
- REST API controllers
- Input validation (format, not business rules)
- Exception handling and translation
- Authentication/authorization entry points
- Converting HTTP concerns to application use cases

```
┌─────────────────────────────────────────┐
│       Presentation Layer                │
│                                         │
│  ┌──────────────────────────────────┐  │
│  │         Controllers              │  │
│  │                                  │  │
│  │  @RestController                 │  │
│  │  public class UserController {   │  │
│  │      @PostMapping("/login")      │  │
│  │      public ResponseEntity<>     │  │
│  │             login(...) {}        │  │
│  │  }                               │  │
│  └──────────────────────────────────┘  │
│                                         │
│  ┌──────────────────────────────────┐  │
│  │    GlobalExceptionHandler        │  │
│  │                                  │  │
│  │  Catches exceptions from         │  │
│  │  all layers and returns          │  │
│  │  appropriate HTTP responses      │  │
│  └──────────────────────────────────┘  │
│                                         │
└─────────────────────────────────────────┘
              │
              │ calls
              ▼
┌─────────────────────────────────────────┐
│        Application Layer                │
│        (use cases, DTOs)                │
└─────────────────────────────────────────┘
```

## Why This Matters

The presentation layer is a **primary adapter** in Hexagonal Architecture:

- It adapts external HTTP requests to internal use cases
- It knows nothing about domain entities (uses DTOs only)
- It can be replaced (e.g., add GraphQL or CLI) without changing business logic
- HTTP concerns (status codes, headers, JSON) are isolated here

## Structure

```
presentation/
├── controller/
│   ├── AuthController.java              # Authentication endpoints
│   ├── EpicController.java              # Epic CRUD
│   ├── ProductBackLogController.java    # Product management
│   ├── SprintController.java            # Sprint operations
│   ├── TaskController.java              # Task management
│   └── UserStoryController.java         # Story management
└── advice/
    └── GlobalExceptionHandler.java      # Centralized error handling
```

## Controllers

Controllers are thin - they delegate all business logic to use cases:

```java
@RestController
@RequestMapping("/api/epics")
@RequiredArgsConstructor
public class EpicController {
    
    private final CreateEpicUseCase createEpicUseCase;
    private final GetEpicUseCase getEpicUseCase;
    private final ListEpicsUseCase listEpicsUseCase;
    private final UpdateEpicUseCase updateEpicUseCase;
    
    @PostMapping
    public ResponseEntity<EpicResponse> createEpic(
            @Valid @RequestBody CreateEpicRequest request) {
        EpicResponse response = createEpicUseCase.execute(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EpicResponse> getEpic(@PathVariable UUID id) {
        EpicResponse response = getEpicUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<EpicResponse>> listEpics(
            @RequestParam UUID productId) {
        List<EpicResponse> responses = listEpicsUseCase.execute(productId);
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EpicResponse> updateEpic(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEpicRequest request) {
        EpicResponse response = updateEpicUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }
}
```

## API Endpoints

### Authentication Controller (`/api/auth`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/login` | Authenticate user, return JWT |
| POST | `/register` | Create new user account |
| POST | `/refresh` | Refresh access token |
| POST | `/logout` | Invalidate token (client-side) |

**Example Request:**
```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Example Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

### Product Backlog Controller (`/api/products`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List all products (paginated) |
| POST | `/` | Create new product |
| GET | `/{id}` | Get product details |
| PUT | `/{id}` | Update product |
| DELETE | `/{id}` | Delete product |
| POST | `/{id}/members` | Add member to product |
| DELETE | `/{id}/members/{userId}` | Remove member |

### Epic Controller (`/api/epics`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List epics (filter by product) |
| POST | `/` | Create epic |
| GET | `/{id}` | Get epic details |
| PUT | `/{id}` | Update epic |
| DELETE | `/{id}` | Delete epic |
| GET | `/{id}/stories` | Get stories in epic |

### User Story Controller (`/api/stories`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List stories (filter by epic/sprint) |
| POST | `/` | Create story |
| GET | `/{id}` | Get story details |
| PUT | `/{id}` | Update story |
| DELETE | `/{id}` | Delete story |
| POST | `/{id}/assign-to-sprint` | Assign to sprint |
| POST | `/{id}/remove-from-sprint` | Remove from sprint |

### Sprint Controller (`/api/sprints`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List sprints |
| POST | `/` | Create sprint |
| GET | `/{id}` | Get sprint details |
| PUT | `/{id}` | Update sprint |
| POST | `/{id}/start` | Start sprint |
| POST | `/{id}/complete` | Complete sprint |
| GET | `/{id}/stories` | Get stories in sprint |
| POST | `/{id}/members` | Add member to sprint |

### Task Controller (`/api/tasks`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List tasks (filter by story/user) |
| POST | `/` | Create task |
| GET | `/{id}` | Get task details |
| PUT | `/{id}` | Update task |
| DELETE | `/{id}` | Delete task |
| POST | `/{id}/assign` | Assign to user |
| POST | `/{id}/log-hours` | Log hours spent |

## Request Validation

Controllers validate request format using Bean Validation:

```java
public class CreateEpicRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    
    @NotNull(message = "Product ID is required")
    private UUID productId;
}
```

Validation errors return 400 Bad Request:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "title",
      "message": "Title is required"
    }
  ]
}
```

## Exception Handling

The `GlobalExceptionHandler` translates exceptions to HTTP responses:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Access denied",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

## Security

Controllers use Spring Security annotations:

```java
@RestController
@RequestMapping("/api/sprints")
@RequiredArgsConstructor
public class SprintController {
    
    @PostMapping
    @PreAuthorize("hasRole('SCRUM_MASTER') or hasRole('ADMIN')")
    public ResponseEntity<SprintResponse> createSprint(
            @Valid @RequestBody CreateSprintRequest request) {
        // Only Scrum Masters and Admins can create sprints
    }
    
    @PostMapping("/{id}/start")
    @PreAuthorize("hasRole('SCRUM_MASTER')")
    public ResponseEntity<SprintResponse> startSprint(@PathVariable UUID id) {
        // Only Scrum Masters can start sprints
    }
}
```

## HTTP Status Codes

| Code | Usage |
|------|-------|
| 200 OK | Successful GET, PUT |
| 201 Created | Successful POST (new resource) |
| 204 No Content | Successful DELETE |
| 400 Bad Request | Validation error |
| 401 Unauthorized | Missing/invalid JWT |
| 403 Forbidden | Valid JWT but insufficient permissions |
| 404 Not Found | Resource doesn't exist |
| 409 Conflict | Business rule violation |
| 500 Internal Server Error | Unexpected error |

## Swagger/OpenAPI Documentation

Controllers use SpringDoc annotations for API docs:

```java
@RestController
@RequestMapping("/api/epics")
@Tag(name = "Epics", description = "Epic management APIs")
public class EpicController {
    
    @Operation(
        summary = "Create a new epic",
        description = "Creates an epic within a product backlog"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Epic created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping
    public ResponseEntity<EpicResponse> createEpic(
            @Valid @RequestBody CreateEpicRequest request) {
        // ...
    }
}
```

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Learning Points

1. **Thin Controllers**: Controllers only handle HTTP concerns, delegate to use cases
2. **No Business Logic**: Business rules are in domain layer, not controllers
3. **DTOs Only**: Controllers never see domain entities
4. **Consistent Responses**: Standard response format across all endpoints
5. **Centralized Error Handling**: One place to handle all exceptions
6. **RESTful Design**: Proper use of HTTP methods and status codes
7. **Documentation**: Self-documenting API with OpenAPI

## Testing Controllers

Controllers tested with MockMvc:

```java
@WebMvcTest(EpicController.class)
class EpicControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CreateEpicUseCase createEpicUseCase;
    
    @Test
    void shouldCreateEpic() throws Exception {
        // Given
        CreateEpicRequest request = new CreateEpicRequest("Title", "Desc", UUID.randomUUID());
        when(createEpicUseCase.execute(any())).thenReturn(new EpicResponse());
        
        // When/Then
        mockMvc.perform(post("/api/epics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }
    
    @Test
    void shouldReturn400WhenTitleBlank() throws Exception {
        CreateEpicRequest request = new CreateEpicRequest("", "Desc", UUID.randomUUID());
        
        mockMvc.perform(post("/api/epics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
```

## Files to Review

- `controller/AuthController.java` - Authentication endpoints
- `controller/EpicController.java` - CRUD example
- `controller/SprintController.java` - Business operations
- `advice/GlobalExceptionHandler.java` - Error handling

## Common Patterns

### Returning Created Resource Location

```java
@PostMapping
public ResponseEntity<EpicResponse> create(@RequestBody CreateEpicRequest request) {
    EpicResponse response = createEpicUseCase.execute(request);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.getId())
        .toUri();
    return ResponseEntity.created(location).body(response);
}
```

### Pagination

```java
@GetMapping
public ResponseEntity<Page<EpicResponse>> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam UUID productId) {
    Page<EpicResponse> responses = listEpicsUseCase.execute(productId, PageRequest.of(page, size));
    return ResponseEntity.ok(responses);
}
```
