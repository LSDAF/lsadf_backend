# Tasks: Add Event Factories with ClockService

## Implementation Tasks

- [ ] **Refactor AEvent base class**
    - [ ] Add constructor parameter for timestamp
    - [ ] Keep existing constructor for backward compatibility
    - [ ] Update Lombok annotations if needed
    - [ ] Verify no compilation errors

- [ ] **Update GameMailReadEvent**
    - [ ] Add constructor accepting timestamp parameter
    - [ ] Keep backward-compatible constructor
    - [ ] Ensure proper call to super with timestamp

- [ ] **Update GameMailAttachmentsClaimedEvent**
    - [ ] Add constructor accepting timestamp parameter
    - [ ] Keep backward-compatible constructor
    - [ ] Ensure proper call to super with timestamp

- [ ] **Create GameMailEventFactory interface**
    - [ ] Define interface in `com.lsadf.core.application.game.mail` package
    - [ ] Add method `createGameMailReadEvent(UUID gameMailId, String userId)`
    - [ ] Add method `createGameMailAttachmentsClaimedEvent(UUID gameMailId, String userId)`
    - [ ] Add proper JavaDoc

- [ ] **Implement GameMailEventFactoryImpl**
    - [ ] Create implementation in `com.lsadf.core.infra.event.factory.game.mail` package
    - [ ] Annotate with `@Component`
    - [ ] Inject `ClockService` via constructor
    - [ ] Implement both event creation methods using clock service
    - [ ] Add proper JavaDoc

- [ ] **Update GameMailEventPublisher**
    - [ ] Inject `GameMailEventFactory` via constructor
    - [ ] Replace direct event construction with factory calls
    - [ ] Remove unused imports

- [ ] **Update EventPublisherConfiguration**
    - [ ] Add `GameMailEventFactory` parameter to bean factory method
    - [ ] Pass factory to `GameMailEventPublisher` constructor

## Testing Tasks

- [ ] **Unit test for GameMailEventFactoryImpl**
    - [ ] Create test class in `lsadf_core/src/test/java`
    - [ ] Mock `ClockService` to return fixed time
    - [ ] Verify `GameMailReadEvent` has correct timestamp
    - [ ] Verify `GameMailAttachmentsClaimedEvent` has correct timestamp
    - [ ] Verify event fields are correctly set

- [ ] **Integration test for event publisher**
    - [ ] Verify events published via `GameMailEventPublisher` use clock service
    - [ ] Test with fixed clock to assert deterministic timestamps

- [ ] **Backward compatibility verification**
    - [ ] Run all existing tests without modification
    - [ ] Verify events created without explicit timestamp still work

## Validation Tasks

- [ ] **Code quality checks**
    - [ ] Run `make lint-check` to verify formatting
    - [ ] Run `make lint` to auto-format if needed
    - [ ] Verify license headers are present

- [ ] **Build verification**
    - [ ] Run `make clean install` to verify compilation
    - [ ] Ensure no compilation errors in any module

- [ ] **Test execution**
    - [ ] Run `make test-unit` to verify unit tests pass
    - [ ] Run `make test-bdd` to verify BDD tests pass
    - [ ] Fix any failing tests

- [ ] **Documentation**
    - [ ] Ensure JavaDoc is complete for new interfaces and classes
    - [ ] Update any relevant inline comments

## Completion Checklist

- [ ] All implementation tasks completed
- [ ] All tests passing
- [ ] Code formatted and linted
- [ ] No compilation errors
- [ ] License headers verified
- [ ] Ready for code review

