# Tasks: Add Admin Game Mail Controller

## Implementation Tasks

- [ ] **Create SendEmailRequestDto**
    - [ ] Create `lsadf_core/src/main/java/com/lsadf/core/infra/web/dto/request/game/mail/SendGameMailRequest`
    - [ ] Add fields: `UUID gameSaveId` (nullable), `UUID emailTemplateId` (required)
    - [ ] Add validation annotations (`@NotNull` for emailTemplateId)
    - [ ] Add license header and Lombok annotations

- [ ] **Create AdminGameMailController interface**
    - [ ] Create `lsadf_admin/src/main/java/com/lsadf/admin/application/game/mail/AdminGameMailController.java`
    - [ ] Add `@RequestMapping` with `AdminApiPathConstants.ADMIN_GAME_MAIL` path
    - [ ] Add Swagger annotations (`@Tag`, `@SecurityRequirement`)
    - [ ] Define
      `sendGameMailToAllGameSaves(@RequestBody SendEmailRequestDto request, @AuthenticationPrincipal Jwt jwt)` with POST
      mapping to `/send`
    - [ ] Define
      `sendGameMailToGameSaveById(@PathVariable UUID gameSaveId, @RequestBody SendGameMailRequest request, @AuthenticationPrincipal Jwt jwt)`
      with POST mapping to `/send/{gameSaveId}`
    - [ ] Add comprehensive Swagger documentation for both endpoints
    - [ ] Add nested `Constants.ApiPaths` class with path constants

- [ ] **Create AdminGameMailControllerImpl implementation**
    - [ ] Create `lsadf_admin/src/main/java/com/lsadf/admin/application/game/mail/AdminGameMailControllerImpl.java`
    - [ ] Extend `BaseController` and implement `AdminGameMailController`
    - [ ] Add `@RestController` and `@Slf4j` annotations
    - [ ] Inject `GameMailSenderService` via constructor
    - [ ] Implement `sendGameMailToAllGameSaves()` - validate JWT, call service method, return 200 response
    - [ ] Implement `sendGameMailToGameSaveById()` - validate JWT, build `SendEmailCommand`, call service method, return
      200 response
    - [ ] Add proper error handling for all exceptions
    - [ ] Override `getLogger()` method

- [ ] **Update BDD feature file**
    - [ ] Update `lsadf_admin/src/test/resources/features/controllers/admin_game_mail_controller.feature`
    - [ ] Complete scenario: "An admin user sends a game mail to a specific game save"
    - [ ] Complete scenario: "An admin user sends a game mail to all game saves"
    - [ ] Complete scenario: "An admin user sends a game mail to a specific game save with a non-existing template"
    - [ ] Complete scenario: "An admin user sends a game mail to all game saves with a non-existing template"
    - [ ] Add proper Given/When/Then steps for each scenario
    - [ ] Verify response status codes (200 for success, 404 for not found)

- [ ] **Create BDD step definitions**
    - [ ] Create or update step definitions class for admin game mail controller tests
    - [ ] Implement step: "the user requests the admin endpoint to send game mail to all game saves with template
      {uuid}"
    - [ ] Implement step: "the user requests the admin endpoint to send game mail to game save {uuid} with template
      {uuid}"
    - [ ] Implement assertions for success and error responses

- [ ] **Create unit tests**
    - [ ] Create
      `lsadf_admin/src/test/java/com/lsadf/admin/application/unit/controller/AdminGameMailControllerTests.java`
    - [ ] Mock `GameMailSenderService`
    - [ ] Test `sendGameMailToAllGameSaves()` with valid request
    - [ ] Test `sendGameMailToGameSaveById()` with valid request
    - [ ] Test error handling for invalid JWT
    - [ ] Test error handling for service exceptions
    - [ ] Verify proper delegation to service layer

## Validation Tasks

- [ ] Run `make lint` to ensure code style compliance
- [ ] Run `make test-unit` to verify unit tests pass
- [ ] Run `make test-bdd` to verify BDD tests pass
- [ ] Run `openspec validate add-admin-game-mail-controller --strict`
- [ ] Verify Swagger UI displays both endpoints correctly
- [ ] Manually test both endpoints using Swagger UI or Postman

## Documentation Tasks

- [ ] Verify all classes have proper license headers
- [ ] Verify all public methods have Javadoc comments
- [ ] Verify Swagger annotations provide clear API documentation

