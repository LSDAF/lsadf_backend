# Tasks: add-delete-game-mails-endpoint

This document outlines the implementation tasks for adding the delete game mails endpoint.

## Task List

### 1. Update AdminGameMailController Interface

- Add DELETE endpoint method signature
- Add proper Swagger annotations
- Define path constant for deletion endpoint

**Validation**: Endpoint appears in controller interface with proper annotations

### 2. Implement DELETE Endpoint in AdminGameMailControllerImpl

- Add implementation for deleteGameMails method
- Handle both deletion modes (expired timestamp vs specific IDs)
- Extract timestamp from query parameter
- Extract mail IDs from request body when provided
- Call appropriate GameMailCommandService methods
- Return proper HTTP response

**Validation**: Implementation compiles without errors

### 3. Add Unit Tests for GameMailCommandService

- Test deleteExpiredGameMails with valid timestamp
- Test deleteExpiredGameMails delegates to repository correctly
- Test deleteGameMail with valid command
- Test deleteGameMail returns correct count
- Test deleteGameMail with empty list

**Validation**: All GameMailCommandService tests pass

### 4. Add Unit Tests for AdminGameMailController

- Test DELETE endpoint returns 401 when not authenticated
- Test DELETE endpoint returns 403 when user not admin
- Test DELETE endpoint returns 200 when successful (expired mails)
- Test DELETE endpoint returns 200 when successful (specific IDs)
- Test DELETE endpoint returns 400 with invalid timestamp format
- Test DELETE endpoint validates parameters correctly

**Validation**: All AdminGameMailController tests pass

### 5. Update BDD Step Definitions

- Ensure step definition for "delete expired game mails" exists and works correctly
- Verify the step correctly constructs the DELETE request with timestamp

**Validation**: BDD step definition compiles and executes

### 6. Add Request DTO (if needed)

- Create DeleteGameMailsRequest DTO if bulk deletion by IDs is needed
- Add validation annotations

**Validation**: DTO compiles and validates correctly

### 7. Run Full Test Suite

- Execute `make test` to ensure all tests pass
- Verify BDD scenario for game mail deletion passes
- Fix any failing tests

**Validation**: `make test` succeeds with all tests passing

## Dependencies

- Task 3 and 4 can be done in parallel
- Task 5 depends on Task 2
- Task 7 depends on all previous tasks

## Verification Strategy

Each task includes inline validation criteria. Final verification via `make test` ensures integration correctness.

