# Tasks: Add Event Factories with ClockService

## Implementation Tasks

- [x] **Refactor AEvent base class**
    - [x] Add constructor parameter for timestamp
    - [x] Keep existing constructor for backward compatibility
    - [x] Update Lombok annotations if needed
    - [x] Verify no compilation errors

- [x] **Update GameMailReadEvent**
    - [x] Add constructor accepting timestamp parameter
    - [x] Keep backward-compatible constructor
    - [x] Ensure proper call to super with timestamp

- [x] **Update GameMailAttachmentsClaimedEvent**
    - [x] Add constructor accepting timestamp parameter
    - [x] Keep backward-compatible constructor
    - [x] Ensure proper call to super with timestamp

- [x] **Create GameMailEventFactory interface**
    - [x] Define interface in `com.lsadf.core.application.game.mail` package
    - [x] Add method `createGameMailReadEvent(UUID gameMailId, String userId)`
    - [x] Add method `createGameMailAttachmentsClaimedEvent(UUID gameMailId, String userId)`
    - [x] Add proper JavaDoc

- [x] **Implement GameMailEventFactoryImpl**
    - [x] Create implementation in `com.lsadf.core.infra.event.factory.game.mail` package
    - [x] Annotate with `@Component`
    - [x] Inject `ClockService` via constructor
    - [x] Implement both event creation methods using clock service
    - [x] Add proper JavaDoc

- [x] **Update GameMailEventPublisher**
    - [x] Inject `GameMailEventFactory` via constructor
    - [x] Replace direct event construction with factory calls
    - [x] Remove unused imports

- [x] **Update EventPublisherConfiguration**
    - [x] Add `GameMailEventFactory` parameter to bean factory method
    - [x] Pass factory to `GameMailEventPublisher` constructor

## Testing Tasks

- [x] **Unit test for GameMailEventFactoryImpl**
    - [x] Create test class in `lsadf_core/src/test/java`
    - [x] Mock `ClockService` to return fixed time
    - [x] Verify `GameMailReadEvent` has correct timestamp
    - [x] Verify `GameMailAttachmentsClaimedEvent` has correct timestamp
    - [x] Verify event fields are correctly set

- [x] **Integration test for event publisher**
    - [x] Verify events published via `GameMailEventPublisher` use clock service
    - [x] Test with fixed clock to assert deterministic timestamps

- [x] **Backward compatibility verification**
    - [x] Run all existing tests without modification
    - [x] Verify events created without explicit timestamp still work

## Validation Tasks

- [x] **Code quality checks**
    - [x] Run `make lint-check` to verify formatting
    - [x] Run `make lint` to auto-format if needed
    - [x] Verify license headers are present

- [x] **Build verification**
    - [x] Run `make clean install` to verify compilation
    - [x] Ensure no compilation errors in any module

- [x] **Test execution**
    - [x] Run `make test-unit` to verify unit tests pass
    - [x] Run `make test-bdd` to verify BDD tests pass
    - [x] Fix any failing tests

- [x] **Documentation**
    - [x] Ensure JavaDoc is complete for new interfaces and classes
    - [x] Update any relevant inline comments

## Completion Checklist

- [x] All implementation tasks completed
- [x] All tests passing
- [x] Code formatted and linted
- [x] No compilation errors
- [x] License headers verified
- [x] Ready for code review

