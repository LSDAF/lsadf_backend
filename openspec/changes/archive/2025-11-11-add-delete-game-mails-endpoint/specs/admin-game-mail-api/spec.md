# admin-game-mail-api Specification Delta

This document contains the specification changes for the admin-game-mail-api capability.

## ADDED Requirements

### Requirement: Delete Game Mails

The system SHALL provide an endpoint that allows admin users to manually delete game mails, either by expiration
timestamp or by specific mail IDs, so that admins can clean up mail data on-demand.

#### Scenario: Admin deletes expired game mails with valid timestamp

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**And** there exist game mails in the system  
**And** some mails have expired before timestamp "2025-11-10T00:00:00Z" (epoch: 1731196800)  
**When** I DELETE to `/api/v1/admin/game_mail?expired=1731196800`  
**Then** the response status code MUST be 200  
**And** the response body MUST contain an empty ApiResponse with no error message  
**And** all game mails with expiration timestamp before the provided timestamp MUST be deleted from the database

#### Scenario: Admin deletes specific game mails by IDs

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**And** there exist game mails with IDs "be7f65c8-c2eb-4dba-b351-746c293751d3" and "
38e9b063-724b-4794-ba7e-157d1e25c465"  
**When** I DELETE to `/api/v1/admin/game_mail` with body:

```json
{
  "mailIds": [
    "be7f65c8-c2eb-4dba-b351-746c293751d3",
    "38e9b063-724b-4794-ba7e-157d1e25c465"
  ]
}
```

**Then** the response status code MUST be 200  
**And** the response body MUST contain an ApiResponse indicating the number of deleted mails  
**And** the specified game mails MUST be deleted from the database

#### Scenario: Admin attempts to delete with invalid timestamp format

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**When** I DELETE to `/api/v1/admin/game_mail?expired=invalid-timestamp`  
**Then** the response status code MUST be 400  
**And** the response MUST contain an error message indicating invalid timestamp format

#### Scenario: Non-admin user attempts to delete game mails

**Given** I am authenticated as a regular user without ROLE_ADMIN  
**When** I DELETE to `/api/v1/admin/game_mail` with any parameters  
**Then** the response status code MUST be 403  
**And** the response MUST contain a forbidden error message

#### Scenario: Unauthenticated user attempts to delete game mails

**Given** I am not authenticated (no JWT token)  
**When** I DELETE to `/api/v1/admin/game_mail` with any parameters  
**Then** the response status code MUST be 401  
**And** the response MUST contain an unauthorized error message

#### Scenario: Admin deletes game mails with no parameters

**Given** I am authenticated as an admin user with ROLE_ADMIN  
**When** I DELETE to `/api/v1/admin/game_mail` with no query parameters and no request body  
**Then** the response status code MUST be 400  
**And** the response MUST contain an error message indicating that either expired parameter or mailIds must be provided

---

## MODIFIED Requirements

### Requirement: Swagger API Documentation

The system SHALL provide comprehensive Swagger/OpenAPI documentation for the admin game mail endpoints, so that
developers and API consumers can understand how to use the API correctly.

#### Scenario: Swagger UI displays admin game mail endpoints

**Given** the application is running  
**When** I navigate to the Swagger UI page  
**Then** I MUST see a section titled "Admin Game Mail Controller"  
**And** it MUST list the endpoint "POST /api/v1/admin/game_mail/send"  
**And** it MUST list the endpoint "POST /api/v1/admin/game_mail/send/{gameSaveId}"  
**And** it MUST list the endpoint "DELETE /api/v1/admin/game_mail"  
**And** all endpoints MUST document the required authentication (Bearer/OAuth2)  
**And** all endpoints MUST show example request and response schemas

