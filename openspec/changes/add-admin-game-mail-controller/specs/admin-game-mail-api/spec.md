# Admin Game Mail API

## ADDED Requirements

### Requirement: Send Game Mail to All Game Saves

The system SHALL provide an endpoint that allows admin users to send game mail to all game saves using a specified email
template, so that admins can distribute rewards or announcements to all players.

#### Scenario: Admin sends mail to all game saves with valid template

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**And** there exist multiple game saves in the system  
**And** there exists a valid game mail template with ID "be7f65c8-c2eb-4dba-b351-746c293751d3"  
**When** I POST to `/api/v1/admin/game_mail/send` with body:

```json
{
  "emailTemplateId": "be7f65c8-c2eb-4dba-b351-746c293751d3"
}
```

**Then** the response status code should be 200  
**And** the response body should contain an empty ApiResponse with no error message  
**And** all game saves MUST receive a game mail with the specified template

#### Scenario: Admin attempts to send mail with non-existent template

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**And** there exists no template with ID "00000000-0000-0000-0000-000000000000"  
**When** I POST to `/api/v1/admin/game_mail/send` with body:

```json
{
  "emailTemplateId": "00000000-0000-0000-0000-000000000000"
}
```

**Then** the response status code MUST be 404  
**And** the response MUST contain an error message indicating the template was not found

#### Scenario: Non-admin user attempts to send mail to all game saves

**Given** I am authenticated as a regular user without ROLE_ADMIN  
**When** I POST to `/api/v1/admin/game_mail/send` with any valid payload  
**Then** the response status code MUST be 403  
**And** the response MUST contain a forbidden error message

#### Scenario: Unauthenticated user attempts to send mail to all game saves

**Given** I am not authenticated (no JWT token)  
**When** I POST to `/api/v1/admin/game_mail/send` with any payload  
**Then** the response status code MUST be 401  
**And** the response MUST contain an unauthorized error message

---

### Requirement: Send Game Mail to Specific Game Save

The system SHALL provide an endpoint that allows admin users to send game mail to a specific game save identified by its
ID, so that admins can send targeted rewards or messages to individual players.

#### Scenario: Admin sends mail to specific game save with valid data

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**And** there exists a game save with ID "0530e1fe-3428-4edd-bb32-cb563419d0bd"  
**And** there exists a valid game mail template with ID "be7f65c8-c2eb-4dba-b351-746c293751d3"  
**When** I POST to `/api/v1/admin/game_mail/send/0530e1fe-3428-4edd-bb32-cb563419d0bd` with body:

```json
{
  "gameSaveId": "0530e1fe-3428-4edd-bb32-cb563419d0bd",
  "emailTemplateId": "be7f65c8-c2eb-4dba-b351-746c293751d3"
}
```

**Then** the response status code MUST be 200  
**And** the response body MUST contain an empty ApiResponse with no error message  
**And** the specified game save MUST receive a game mail with the template

#### Scenario: Admin attempts to send mail to non-existent game save

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**And** there exists no game save with ID "00000000-0000-0000-0000-000000000000"  
**And** there exists a valid game mail template with ID "be7f65c8-c2eb-4dba-b351-746c293751d3"  
**When** I POST to `/api/v1/admin/game_mail/send/00000000-0000-0000-0000-000000000000` with body:

```json
{
  "gameSaveId": "00000000-0000-0000-0000-000000000000",
  "emailTemplateId": "be7f65c8-c2eb-4dba-b351-746c293751d3"
}
```

**Then** the response status code MUST be 404  
**And** the response MUST contain an error message indicating the game save was not found

#### Scenario: Admin sends mail to specific game save with non-existent template

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**And** there exists a game save with ID "0530e1fe-3428-4edd-bb32-cb563419d0bd"  
**And** there exists no template with ID "00000000-0000-0000-0000-000000000000"  
**When** I POST to `/api/v1/admin/game_mail/send/0530e1fe-3428-4edd-bb32-cb563419d0bd` with body:

```json
{
  "gameSaveId": "0530e1fe-3428-4edd-bb32-cb563419d0bd",
  "emailTemplateId": "00000000-0000-0000-0000-000000000000"
}
```

**Then** the response status code MUST be 404  
**And** the response MUST contain an error message indicating the template was not found

#### Scenario: Admin sends mail with invalid UUID format

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**When** I POST to `/api/v1/admin/game_mail/send/invalid-uuid-format` with any payload  
**Then** the response status code MUST be 400  
**And** the response MUST contain an error message indicating invalid UUID format

#### Scenario: Non-admin user attempts to send mail to specific game save

**Given** I am authenticated as a regular user without ROLE_ADMIN  
**And** there exists a game save with ID "0530e1fe-3428-4edd-bb32-cb563419d0bd"  
**When** I POST to `/api/v1/admin/game_mail/send/0530e1fe-3428-4edd-bb32-cb563419d0bd` with any valid payload  
**Then** the response status code MUST be 403  
**And** the response MUST contain a forbidden error message

#### Scenario: Unauthenticated user attempts to send mail to specific game save

**Given** I am not authenticated (no JWT token)  
**When** I POST to `/api/v1/admin/game_mail/send/0530e1fe-3428-4edd-bb32-cb563419d0bd` with any payload  
**Then** the response status code MUST be 401  
**And** the response MUST contain an unauthorized error message

---

### Requirement: Data Validation for Send Email Request

The system MUST validate the structure and content of send email requests to ensure data integrity and prevent invalid
operations.

#### Scenario: Request with missing required emailTemplateId

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**When** I POST to `/api/v1/admin/game_mail/send` with body:

```json
{
  "emailTemplateId": null
}
```

**Then** the response status code MUST be 400  
**And** the response MUST contain a validation error for the emailTemplateId field

#### Scenario: Request with malformed JSON

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**When** I POST to `/api/v1/admin/game_mail/send` with malformed JSON body  
**Then** the response status code MUST be 400  
**And** the response MUST contain an error message indicating invalid JSON format

---

### Requirement: Swagger API Documentation

The system SHALL provide comprehensive Swagger/OpenAPI documentation for the admin game mail endpoints, so that
developers and API consumers can understand how to use the API correctly.

#### Scenario: Swagger UI displays admin game mail endpoints

**Given** the application is running  
**When** I navigate to the Swagger UI page  
**Then** I MUST see a section titled "Admin Game Mail Controller"  
**And** it MUST list the endpoint "POST /api/v1/admin/game_mail/send"  
**And** it MUST list the endpoint "POST /api/v1/admin/game_mail/send/{gameSaveId}"  
**And** both endpoints MUST document the required authentication (Bearer/OAuth2)  
**And** both endpoints MUST show example request and response schemas

---

### Requirement: Security Integration

The system MUST ensure that only authenticated admin users can access game mail sending functionality to prevent
unauthorized distribution of rewards.

#### Scenario: JWT token validation is enforced

**Given** the admin game mail endpoints are deployed  
**When** any request is made without a valid JWT token  
**Then** the request MUST be rejected with HTTP 401 Unauthorized  
**And** the request MUST NOT reach the service layer

#### Scenario: Admin role requirement is enforced

**Given** a user is authenticated with a valid JWT token  
**And** the token contains only "ROLE_USER" role (no ROLE_ADMIN)  
**When** the user attempts to access any admin game mail endpoint  
**Then** the request MUST be rejected with HTTP 403 Forbidden  
**And** the request MUST NOT reach the service layer

---

### Requirement: Error Response Consistency

The system SHALL provide consistent error response formats across all admin game mail endpoints, so that API consumers
can handle errors uniformly in client applications.

#### Scenario: All errors return ApiResponse format

**Given** any error occurs in admin game mail endpoints (404, 401, 403, 400, 500)  
**Then** the error response MUST follow the ApiResponse structure:

```json
{
  "data": null,
  "message": "descriptive error message"
}
```

**And** the HTTP status code MUST match the error type  
**And** the message MUST provide actionable information about the error

