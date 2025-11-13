# Proposal: Add Event Factories with ClockService

## Summary

Replace direct `System.currentTimeMillis()` usage in events with `ClockService` to improve testability and consistency
across the event system. Introduce event factory beans that encapsulate event creation logic and use `ClockService` for
timestamp generation.

## Motivation

Currently, the `AEvent` base class directly calls `System.currentTimeMillis()` to generate timestamps, which:

1. **Reduces testability**: Tests cannot control or mock time-based behavior
2. **Inconsistent with project patterns**: Other parts of the application use `ClockService` for time operations
3. **Violates dependency injection principles**: Hard-coded time sources make events difficult to test in isolation
4. **Complicates time-sensitive testing**: BDD tests and unit tests that need to verify time-based logic cannot control
   the clock

## Proposed Solution

1. **Refactor AEvent**: Remove direct `System.currentTimeMillis()` call and accept timestamp as a constructor parameter
2. **Create Event Factory Interfaces**: Define factory interfaces for each event type domain (e.g.,
   `GameMailEventFactory`)
3. **Implement Event Factories as Spring Beans**: Implement factories that inject `ClockService` and create events with
   controlled timestamps
4. **Update Event Publishers**: Refactor EventPublisherPort implementations to inject and use event factories instead of
   direct event construction
5. **Maintain Backward Compatibility**: Ensure existing event creation patterns continue to work during migration

## Scope

### In Scope

- Refactor `AEvent` constructor to accept timestamp parameter
- Create `GameMailEventFactory` interface and implementation
- Update `GameMailEventPublisher` (EventPublisherPort implementation) to inject and use the event factory
- Add configuration bean for event factories
- Update relevant tests to verify clock-based behavior

### Out of Scope

- Migration of other event types (inventory, Valkey events) - can be done incrementally
- Refactoring other usages of `System.currentTimeMillis()` outside the event system
- Changes to event serialization/deserialization logic

## Success Criteria

1. `AEvent` no longer directly calls `System.currentTimeMillis()`
2. Event factories are available as Spring beans
3. `GameMailEventPublisher` uses event factories
4. All existing tests pass without modification (backward compatibility)
5. New tests demonstrate clock control in event generation
6. Code passes Spotless formatting checks

## Risks and Mitigations

| Risk                                       | Impact | Mitigation                                                                                    |
|--------------------------------------------|--------|-----------------------------------------------------------------------------------------------|
| Breaking existing event creation           | High   | Maintain backward compatibility by keeping timestamp initialization in AEvent if not provided |
| Performance overhead from additional beans | Low    | Event factories are lightweight singleton beans with negligible overhead                      |
| Increased complexity                       | Low    | Clear interfaces and single responsibility design keep complexity manageable                  |

## Dependencies

- Existing `ClockService` and `ClockConfiguration` (already implemented)
- Spring Framework dependency injection
- No external library changes required

