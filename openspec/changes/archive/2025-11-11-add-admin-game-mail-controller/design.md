# Design: Add Admin Game Mail Controller

## Architecture Overview

This change adds a new admin controller following the established layered architecture pattern used throughout the LSADF
Admin module. The controller acts as a thin REST API layer that delegates to the existing `GameMailSenderService`
business logic.

```
┌─────────────────────────────────────┐
│   AdminGameMailController (API)     │
│  - sendGameMailToAllGameSaves()     │
│  - sendGameMailToGameSaveById()     │
└────────────┬────────────────────────┘
             │ delegates to
             ▼
┌─────────────────────────────────────┐
│   GameMailSenderService (Service)   │
│  - sendGameMailToAllGameSaves()     │
│  - sendGameMailToGameSaveById()     │
└─────────────────────────────────────┘
```

## Component Design

### AdminGameMailController (Interface)

**Responsibilities:**

- Define REST API contract for admin game mail operations
- Specify Swagger/OpenAPI documentation
- Declare authentication and authorization requirements

**Key Design Decisions:**

1. **Two separate endpoints**: `/send` (all) and `/send/{gameSaveId}` (specific) for clarity
2. **POST method**: Email sending is a state-changing operation
3. **Void response**: No payload needed on success, HTTP 200 sufficient
4. **JWT authentication**: Consistent with other admin controllers

**Annotations:**

- `@RequestMapping`: Maps to `/api/v1/admin/game_mail`
- `@Tag`: Groups endpoints in Swagger UI
- `@SecurityRequirement`: Documents OAuth2/Bearer authentication

### AdminGameMailControllerImpl (Implementation)

**Responsibilities:**

- Validate authenticated user via JWT
- Transform HTTP request DTOs to service command objects
- Delegate business logic to `GameMailSenderService`
- Handle exceptions and map to appropriate HTTP responses

**Key Design Decisions:**

1. **Extends BaseController**: Reuses `validateUser()` utility
2. **Constructor injection**: Enables proper testing with mocks
3. **Exception handling**: Leverages Spring's `@ControllerAdvice` for consistent error responses
4. **Logging**: Uses SLF4J for debugging and audit trails

**Error Mapping:**

- `UnauthorizedException` → 401 Unauthorized
- `NotFoundException` → 404 Not Found
- `IllegalArgumentException` → 400 Bad Request
- Generic `Exception` → 500 Internal Server Error

### SendEmailRequestDto (DTO)

**Responsibilities:**

- Transfer data from HTTP request to application layer
- Validate request payload structure

**Key Design Decisions:**

1. **Record type**: Immutable, concise, auto-generated equals/hashCode
2. **Nullable gameSaveId**: Allows reuse for both "send to all" and "send to specific" endpoints
3. **Validation annotations**: Bean Validation ensures data integrity at API boundary

**Fields:**

- `UUID gameSaveId`: Optional, identifies target game save
- `UUID emailTemplateId`: Required, identifies email template to use

**Mapping to SendEmailCommand:**

```java
SendEmailCommand command = new SendEmailCommand(
        request.gameSaveId(),
        request.emailTemplateId()
);
```

## Security Design

### Authentication Strategy

**OAuth2/JWT Bearer Token:**

- User must authenticate via Keycloak
- JWT contains user claims including roles
- Spring Security validates JWT signature and expiration

### Authorization Strategy

**Role-Based Access Control (RBAC):**

- Only users with `ROLE_ADMIN` can access endpoints
- Enforced at API Gateway or via `@PreAuthorize` (if needed)
- JWT validation in `BaseController.validateUser()` ensures token presence

**Implementation:**

```java

@Override
public ResponseEntity<ApiResponse<Void>> sendGameMailToAllGameSaves(
        SendEmailRequestDto request,
        Jwt jwt
) {
    validateUser(jwt); // Throws 401 if JWT invalid
    // ... delegate to service
}
```

## Testing Strategy

### Unit Tests

**Scope:** Controller layer only, mocking `GameMailSenderService`

**Test Cases:**

1. Valid request to send to all game saves → HTTP 200
2. Valid request to send to specific game save → HTTP 200
3. Null JWT → HTTP 401
4. Service throws `NotFoundException` → HTTP 404
5. Service throws generic exception → HTTP 500

**Tools:** JUnit 5, Mockito, AssertJ

### BDD Tests

**Scope:** End-to-end integration testing with real database, Redis, and Keycloak

**Scenarios:**

1. **Happy path - send to all**: Admin logs in, sends mail to all game saves, verifies 200 response
2. **Happy path - send to specific**: Admin logs in, sends mail to specific game save, verifies 200 response
3. **Error - invalid template**: Admin attempts to send mail with non-existent template, verifies 404 response
4. **Error - invalid game save**: Admin attempts to send mail to non-existent game save, verifies 404 response

**Tools:** Cucumber, Testcontainers

## Data Flow

### Send to All Game Saves

```
1. HTTP POST /api/v1/admin/game_mail/send
   Body: { "emailTemplateId": "uuid" }
   Header: Authorization: Bearer <jwt>

2. AdminGameMailControllerImpl.sendGameMailToAllGameSaves()
   - Validate JWT
   - Extract emailTemplateId from DTO

3. GameMailSenderService.sendGameMailToAllGameSaves()
   - Fetch all game saves from database
   - For each game save:
     - Create game mail entity
     - Attach email template
     - Save to database

4. HTTP 200 OK
   Body: { "data": null, "message": null }
```

### Send to Specific Game Save

```
1. HTTP POST /api/v1/admin/game_mail/send/{gameSaveId}
   Body: { "gameSaveId": "uuid", "emailTemplateId": "uuid" }
   Header: Authorization: Bearer <jwt>

2. AdminGameMailControllerImpl.sendGameMailToGameSaveById()
   - Validate JWT
   - Build SendEmailCommand from DTO

3. GameMailSenderService.sendGameMailToGameSaveById(command)
   - Verify game save exists
   - Verify email template exists
   - Create game mail entity
   - Attach email template
   - Save to database

4. HTTP 200 OK
   Body: { "data": null, "message": null }
```

## API Design

### Endpoint 1: Send to All Game Saves

**Request:**

```http
POST /api/v1/admin/game_mail/send
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "emailTemplateId": "be7f65c8-c2eb-4dba-b351-746c293751d3"
}
```

**Response (Success):**

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": null,
  "message": null
}
```

**Response (Error - Template Not Found):**

```http
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "data": null,
  "message": "Email template not found: be7f65c8-c2eb-4dba-b351-746c293751d3"
}
```

### Endpoint 2: Send to Specific Game Save

**Request:**

```http
POST /api/v1/admin/game_mail/send/0530e1fe-3428-4edd-bb32-cb563419d0bd
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "gameSaveId": "0530e1fe-3428-4edd-bb32-cb563419d0bd",
  "emailTemplateId": "be7f65c8-c2eb-4dba-b351-746c293751d3"
}
```

**Response (Success):**

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "data": null,
  "message": null
}
```

**Response (Error - Game Save Not Found):**

```http
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "data": null,
  "message": "Game save not found: 0530e1fe-3428-4edd-bb32-cb563419d0bd"
}
```

## Trade-offs

### Decision: Two Endpoints vs. One

**Chosen:** Two separate endpoints

**Rationale:**

- **Clarity**: Distinct paths make intent explicit (`/send` vs `/send/{id}`)
- **REST principles**: Resource-oriented URLs are more intuitive
- **Swagger UI**: Easier to document and test separately

**Alternative:** Single endpoint with optional `gameSaveId` in body

- **Pros**: Fewer lines of code
- **Cons**: Less RESTful, harder to document, ambiguous behavior

### Decision: Synchronous vs. Asynchronous

**Chosen:** Synchronous processing

**Rationale:**

- **Simplicity**: No need for queue infrastructure or background workers
- **Immediate feedback**: Admin knows immediately if operation succeeded
- **Existing pattern**: Matches other admin controller implementations

**Alternative:** Asynchronous with job queue

- **Pros**: Better scalability for bulk operations
- **Cons**: Added complexity, harder to debug, requires job status tracking
- **Future consideration**: Can migrate later if performance demands it

### Decision: DTO Naming

**Chosen:** `SendEmailRequestDto`

**Rationale:**

- **Specificity**: Clearly indicates this is for sending emails (not receiving)
- **Consistency**: Matches `GameMailTemplateRequest` pattern in codebase
- **Separation**: Distinct from domain `SendEmailCommand`

## Constraints

1. **Google Java Style Guide**: All code must pass Spotless formatting
2. **Apache 2.0 License**: All files require license headers
3. **Spring Boot patterns**: Must use existing `BaseController`, `ApiResponse` utilities
4. **Security**: Must validate JWT and require ROLE_ADMIN
5. **Testing**: Must include both unit and BDD tests

## Future Enhancements

1. **Asynchronous processing**: Use Spring `@Async` or message queue for bulk sends
2. **Pagination**: If sending to all game saves becomes too slow
3. **Preview mode**: Allow admins to preview email content before sending
4. **Scheduled sends**: Support delayed/scheduled email dispatch
5. **Audit logging**: Track who sent what email to which game saves

