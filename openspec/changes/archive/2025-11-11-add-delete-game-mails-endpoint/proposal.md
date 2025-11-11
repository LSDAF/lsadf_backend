# Proposal: add-delete-game-mails-endpoint

## Metadata

- **Status**: Proposed
- **Created**: 2025-11-11
- **Author**: AI Assistant
- **Related Specs**: admin-game-mail-api

## Summary

Add a new admin endpoint to manually trigger deletion of game mails based on expiration timestamp or specific mail IDs.
This provides admins with the ability to clean up expired game mails on-demand rather than relying solely on automated
scheduled tasks.

## Motivation

Currently, game mail deletion is only available through automated scheduled tasks. This proposal adds manual control for
administrators to:

- Delete expired game mails immediately on-demand
- Clean up specific game mails by ID for troubleshooting or data management
- Test game mail deletion functionality during development and QA

## Proposed Changes

### Capabilities Affected

- admin-game-mail-api: New DELETE endpoint for game mail deletion

### High-Level Approach

1. Add new DELETE endpoint `/api/v1/admin/game_mail` with optional `expired` query parameter
2. Support two deletion modes:
    - Delete all expired mails before a given timestamp
    - Delete specific mails by IDs (in request body)
3. Reuse existing `GameMailCommandService.deleteExpiredGameMails()` and `deleteGameMail()` methods
4. Add comprehensive unit tests for the service methods
5. Add comprehensive unit tests for the controller
6. Add BDD tests for the endpoint

## Impact Assessment

### Breaking Changes

- None. This is a new endpoint that doesn't modify existing functionality.

### Dependencies

- None. Uses existing service layer methods.

### Security Considerations

- Endpoint requires admin role (ROLE_ADMIN) via existing security configuration
- Uses existing JWT authentication and authorization mechanisms

## Acceptance Criteria

- [ ] DELETE endpoint is accessible at `/api/v1/admin/game_mail`
- [ ] Endpoint accepts optional `expired` query parameter (timestamp in epoch seconds)
- [ ] Endpoint accepts optional request body with list of mail IDs
- [ ] Endpoint returns 200 OK on successful deletion
- [ ] Endpoint returns 401 for unauthenticated users
- [ ] Endpoint returns 403 for non-admin users
- [ ] Unit tests added for GameMailCommandService methods
- [ ] Unit tests added for AdminGameMailController
- [ ] BDD test passes for expired mail deletion scenario
- [ ] `make test` passes successfully

