# Proposal: Add Admin Game Mail Controller

## Summary

Create an `AdminGameMailController` that exposes two endpoints for sending game mails to game saves. The controller will
use the existing `GameMailSenderService` methods to send emails to all game saves or to a specific game save by ID.

## Why

The system needs an admin interface to trigger game mail distribution. Currently, the `GameMailSenderService` exists
with two methods (`sendGameMailToAllGameSaves()` and `sendGameMailToGameSaveById(SendEmailCommand)`), but there is no
controller exposing these capabilities through REST endpoints. This prevents administrators from manually triggering
email distributions for events, rewards, or announcements.

## Motivation

The system needs an admin interface to trigger game mail distribution. Currently, the `GameMailSenderService` exists
with two methods (`sendGameMailToAllGameSaves()` and `sendGameMailToGameSaveById(SendEmailCommand)`), but there is no
controller exposing these capabilities through REST endpoints.

## Goals

- Create an `AdminGameMailController` interface and implementation following the existing admin controller patterns
- Expose two POST endpoints:
    - `/api/v1/admin/game_mail/send` - Send game mail to all game saves
    - `/api/v1/admin/game_mail/send/{gameSaveId}` - Send game mail to a specific game save
- Create a DTO (`SendEmailRequestDto`) to wrap the `SendEmailCommand` data
- Implement proper authorization with `ROLE_ADMIN` requirement
- Create comprehensive BDD tests in Gherkin format covering both endpoints and error scenarios
- Follow existing patterns from other admin controllers (e.g., `AdminCacheController`)

## Non-Goals

- Modifying the existing `GameMailSenderService` interface or implementation
- Creating new game mail template management endpoints (already exists in `AdminGameMailTemplateController`)
- Implementing asynchronous processing (can be added later if needed)

## Proposed Solution

### Components to Create

1. **AdminGameMailController** (interface)
    - Located at: `lsadf_admin/src/main/java/com/lsadf/admin/application/game/mail/AdminGameMailController.java`
    - Defines two POST endpoints with Swagger annotations
    - Requires ROLE_ADMIN via JWT authentication
    - Returns `ApiResponse<Void>` with HTTP 200 on success

2. **AdminGameMailControllerImpl** (implementation)
    - Located at: `lsadf_admin/src/main/java/com/lsadf/admin/application/game/mail/AdminGameMailControllerImpl.java`
    - Extends `BaseController` and implements `AdminGameMailController`
    - Injects and delegates to `GameMailSenderService`
    - Handles exceptions and returns appropriate error responses

3. **SendEmailRequestDto** (DTO)
    - Located at: `lsadf_admin/src/main/java/com/lsadf/admin/application/game/mail/dto/SendEmailRequestDto.java`
    - Maps to `SendEmailCommand` with fields: `gameSaveId`, `emailTemplateId`
    - Includes validation annotations

4. **BDD Feature Tests**
    - Update existing: `lsadf_admin/src/test/resources/features/controllers/admin_game_mail_controller.feature`
    - Scenarios:
        - Admin user successfully sends game mail to all game saves
        - Admin user successfully sends game mail to specific game save
        - Admin user attempts to send mail with non-existing template (error)
        - Admin user attempts to send mail to non-existing game save (error)

### API Endpoints

#### POST /api/v1/admin/game_mail/send

- **Description**: Sends game mail to all game saves using a specified template
- **Request Body**: `SendEmailRequestDto` with `emailTemplateId` only
- **Response**: `ApiResponse<Void>` with HTTP 200 on success
- **Authorization**: Requires ROLE_ADMIN

#### POST /api/v1/admin/game_mail/send/{gameSaveId}

- **Description**: Sends game mail to a specific game save
- **Path Parameter**: `gameSaveId` (UUID)
- **Request Body**: `SendEmailRequestDto` with `gameSaveId` and `emailTemplateId`
- **Response**: `ApiResponse<Void>` with HTTP 200 on success
- **Authorization**: Requires ROLE_ADMIN

### Error Handling

Following the existing pattern in other admin controllers:

- **401 Unauthorized**: User not authenticated
- **403 Forbidden**: User lacks ROLE_ADMIN
- **404 Not Found**: Game save or email template not found
- **400 Bad Request**: Invalid request data
- **500 Internal Server Error**: Unexpected errors

## Dependencies

- Existing `GameMailSenderService` interface and implementation
- Existing `SendEmailCommand` record
- Spring Security for authentication/authorization
- Existing admin controller infrastructure (BaseController, ApiResponse utilities)

## Testing Strategy

1. **Unit Tests**: Controller unit tests following pattern from `AdminCacheControllerTests`
2. **BDD Tests**: Gherkin scenarios covering happy paths and error cases
3. **Integration**: Tests will verify proper delegation to `GameMailSenderService`

## Open Questions

None - the implementation is straightforward and follows established patterns.

## Alternatives Considered

1. **Single endpoint with optional parameter**: Decided against to maintain clear separation of concerns
2. **Asynchronous processing**: Deferred to future work - current synchronous approach is simpler

## Timeline

- Implementation: ~2-4 hours
- Testing: ~1-2 hours
- Total: ~3-6 hours

