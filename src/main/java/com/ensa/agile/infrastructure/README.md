# Infrastructure Layer

## Concept

The **Infrastructure Layer** (also called "Secondary Adapters") contains:

- Technical implementations of domain interfaces
- Database persistence (JPA)
- Security implementations (JWT, Spring Security)
- External services integration
- Framework-specific code

```
┌─────────────────────────────────────────┐
│      Infrastructure Layer               │
│                                         │
│  ┌──────────────────────────────────┐  │
│  │         Persistence              │  │
│  │                                  │  │
│  │  JPA Entities ──► Adapters ──►  │  │
│  │       ▲              │          │  │
│  │       │              │          │  │
│  │       └──────────────┘          │  │
│  │           implements            │  │
│  │              │                  │  │
│  │              ▼                  │  │
│  │    Domain Repository Interfaces │  │
│  └──────────────────────────────────┘  │
│                                         │
│  ┌──────────────────────────────────┐  │
│  │          Security                │  │
│  │                                  │  │
│  │  JWT Service  ◄──  Interface   │  │
│  │  Filters      ◄──  defined in  │  │
│  │  Config       ◄──  Application │  │
│  └──────────────────────────────────┘  │
│                                         │
└─────────────────────────────────────────┘
              ▲
              │ depends on
┌─────────────┴───────────────────────────┐
│           Domain Layer                  │
│    (repository interfaces = ports)      │
└─────────────────────────────────────────┘
```

## Why This Matters

Infrastructure layer depends on domain, NOT the other way around:

- **Dependency Inversion**: Domain defines interfaces (ports), infrastructure implements them (adapters)
- **Replaceable**: Can swap JPA for MongoDB, JWT for OAuth2, etc.
- **Testable**: Domain logic tested without real database
- **Technology Agnostic**: Domain doesn't know about Spring, JPA, or JWT

## Structure

```
infrastructure/
├── persistence/
│   ├── config/
│   │   ├── JpaConfig.java              # JPA configuration
│   │   ├── PersistenceBeans.java       # Repository bean definitions
│   │   └── ApplicationAuditorAware.java # Audit configuration
│   ├── jpa/
│   │   ├── user/
│   │   │   ├── UserJpaEntity.java      # JPA entity with @Entity
│   │   │   ├── UserJpaMapper.java      # Maps JPA <-> Domain
│   │   │   ├── JpaUserRepository.java  # Spring Data interface
│   │   │   └── UserRepositoryAdapter.java # Implements domain interface
│   │   ├── epic/
│   │   │   ├── EpicJpaEntity.java
│   │   │   ├── EpicJpaMapper.java
│   │   │   ├── JpaEpicRepository.java
│   │   │   └── EpicRepositoryAdapter.java
│   │   ├── story/
│   │   ├── sprint/
│   │   ├── task/
│   │   ├── product/
│   │   └── global/
│   │       ├── BaseJpaEntity.java
│   │       ├── BaseJpaMapper.java
│   │       ├── TransactionalWrapper.java
│   │       └── join/
│   │           └── FetchJoinSpec.java
│   └── service/
│       ├── FetchService.java
│       ├── FetchPlan.java
│       ├── BackLogRepo.java
│       └── BackLogViews.java
└── security/
    ├── config/
    │   ├── SecurityConfig.java         # Spring Security configuration
    │   ├── SecurityBeans.java          # Security-related beans
    │   └── DevSecurityBeans.java       # Dev profile beans
    ├── filter/
    │   ├── JwtFilter.java              # JWT validation filter
    │   └── FilterChainExceptionHandler.java
    ├── auth/
    │   ├── MyAuthenticationProvider.java
    │   └── MyAuthenticationEntryPoint.java
    ├── service/
    │   ├── JwtServiceImpl.java         # Implements JwtService interface
    │   ├── AbacService.java            # Attribute-based access control
    │   └── AuthenticationService.java
    ├── utils/
    │   └── PasswordEncoderImpl.java
    └── exception/
        ├── JwtValidationException.java
        └── JwtCreationException.java
```

## Repository Pattern (Adapter Pattern)

### Step 1: Domain Interface (Port)

```java
// In domain layer
public interface UserRepository {
    Optional<User> findById(UserId id);
    User save(User user);
    void delete(UserId id);
}
```

### Step 2: JPA Entity

```java
// In infrastructure layer
@Entity
@Table(name = "users")
public class UserJpaEntity {
    @Id
    private UUID id;
    
    @Column(unique = true)
    private String email;
    
    private String password;
    private String username;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### Step 3: Spring Data Repository

```java
// Spring Data interface
public interface JpaUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### Step 4: Mapper

```java
@Component
public class UserJpaMapper {
    
    public User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        
        return User.builder()
            .id(new UserId(entity.getId()))
            .email(new Email(entity.getEmail()))
            .username(entity.getUsername())
            .passwordHash(entity.getPassword())
            .role(entity.getRole())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
    
    public UserJpaEntity toJpa(User domain) {
        if (domain == null) return null;
        
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(domain.getId().getValue());
        entity.setEmail(domain.getEmail().getValue());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPasswordHash());
        entity.setRole(domain.getRole());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
```

### Step 5: Adapter (Implementation)

```java
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    
    private final JpaUserRepository jpaRepository;
    private final UserJpaMapper mapper;
    
    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomain);
    }
    
    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public void delete(UserId id) {
        jpaRepository.deleteById(id.getValue());
    }
}
```

## Why Two Entities?

**Domain Entity** (`domain/user/entity/User.java`):
- Pure Java, no framework dependencies
- Contains business logic
- Used throughout business layer
- Can be tested without database

**JPA Entity** (`infrastructure/persistence/jpa/user/UserJpaEntity.java`):
- Framework-specific (JPA annotations)
- Database mapping only
- No business logic
- Infrastructure concern

This separation allows:
- Changing database technology without touching domain
- Testing domain logic without database
- Evolving domain model independently of database schema

## Security Implementation

### JWT Service

Implements `JwtService` interface from application layer:

```java
@Service
public class JwtServiceImpl implements JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long expiration;
    
    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getEmail().getValue())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    @Override
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return username.equals(user.getEmail().getValue()) && !isTokenExpired(token);
    }
    
    // Helper methods...
}
```

### JWT Filter

Validates JWT on each request:

```java
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### Security Configuration

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

## Transaction Management

Infrastructure handles transactions using Spring's `@Transactional`:

```java
@Component
@RequiredArgsConstructor
public class TransactionalWrapper {
    
    @Transactional
    public <T> T execute(Supplier<T> operation) {
        return operation.get();
    }
    
    @Transactional(readOnly = true)
    public <T> T executeReadOnly(Supplier<T> operation) {
        return operation.get();
    }
}
```

Use cases in application layer use this wrapper for transactions.

## Fetch Service

Optimized data fetching to avoid N+1 queries:

```java
@Service
@RequiredArgsConstructor
public class FetchService {
    
    private final EntityManager entityManager;
    
    public <T> T fetchWithPlan(T root, FetchPlan<T> plan) {
        // Implementation to eagerly load specified associations
        // Reduces N+1 query problems
    }
}
```

## Database Configuration

### application.yaml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/agile_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

## Learning Points

1. **Adapter Pattern**: Infrastructure adapts domain interfaces to real implementations
2. **Two Entities**: Separate domain and JPA entities for flexibility
3. **Mappers**: Convert between domain and JPA representations
4. **Dependency Direction**: Infrastructure depends on domain
5. **Technology Replaceable**: Can swap JPA, JWT, or database without touching domain
6. **No Business Logic**: Infrastructure handles technical concerns only
7. **Configuration**: Externalized config (YAML/properties)

## Testing Infrastructure

Repository adapters tested with `@DataJpaTest`:

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryAdapterTest {
    
    @Autowired
    private JpaUserRepository jpaRepository;
    
    private UserRepositoryAdapter adapter;
    
    @BeforeEach
    void setUp() {
        adapter = new UserRepositoryAdapter(jpaRepository, new UserJpaMapper());
    }
    
    @Test
    void shouldSaveAndRetrieveUser() {
        User user = User.builder()
            .id(new UserId(UUID.randomUUID()))
            .email(new Email("test@example.com"))
            .username("testuser")
            .passwordHash("hashedpassword")
            .role(Role.USER)
            .build();
        
        User saved = adapter.save(user);
        Optional<User> found = adapter.findById(saved.getId());
        
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail().getValue());
    }
}
```

## Files to Review

- `persistence/jpa/*/RepositoryAdapter.java` - Adapter implementations
- `persistence/jpa/*/*Mapper.java` - Entity mappers
- `security/service/JwtServiceImpl.java` - JWT implementation
- `security/filter/JwtFilter.java` - JWT validation
- `security/config/SecurityConfig.java` - Security setup
- `persistence/config/PersistenceBeans.java` - Bean definitions

## Common Patterns

### Soft Delete

```java
@Entity
public class UserJpaEntity {
    // ... other fields
    
    private boolean deleted = false;
    private LocalDateTime deletedAt;
}
```

### Auditing

```java
@EntityListeners(AuditingEntityListener.class)
public class BaseJpaEntity {
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String updatedBy;
}
```

### Composite Keys

```java
@Entity
@IdClass(SprintMemberId.class)
public class SprintMemberJpaEntity {
    
    @Id
    private UUID sprintId;
    
    @Id
    private UUID userId;
    
    private SprintRole role;
}
```

## Benefits Summary

- **Testability**: Domain tested without database
- **Flexibility**: Swap technologies easily
- **Clean Dependencies**: No circular dependencies
- **Maintainability**: Clear separation of concerns
- **Evolution**: Domain evolves independently of infrastructure
