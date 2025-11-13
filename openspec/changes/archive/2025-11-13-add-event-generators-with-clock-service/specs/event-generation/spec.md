# Event Generation Capability

## ADDED Requirements

### Requirement: Event factories must use ClockService for timestamps

**Priority**: High  
**Category**: Core Functionality

Event factory beans must inject and use `ClockService` to obtain timestamps when creating events, rather than calling
`System.currentTimeMillis()` directly. This ensures testability and consistency with the application's time management
approach.

#### Scenario: GameMailEventFactory creates events with clock-based timestamps

**Given** a `GameMailEventFactory` bean is configured with a `ClockService`  
**And** the `ClockService` is set to a fixed time (e.g., "2024-01-15T10:00:00Z")  
**When** the factory creates a `GameMailReadEvent` with gameMailId "uuid-123" and userId "user@example.com"  
**Then** the event must have a timestamp of 1705314000000 (epoch millis for 2024-01-15T10:00:00Z)  
**And** the event must have the gameMailId "uuid-123"  
**And** the event must have the userId "user@example.com"

#### Scenario: Event timestamp changes when clock is advanced

**Given** a `GameMailEventFactory` bean is configured with a `ClockService`  
**And** the `ClockService` is set to "2024-01-15T10:00:00Z"  
**When** the factory creates a first `GameMailAttachmentsClaimedEvent`  
**And** the clock is advanced by 5 seconds  
**And** the factory creates a second `GameMailAttachmentsClaimedEvent`  
**Then** the second event timestamp must be exactly 5000 milliseconds greater than the first

### Requirement: Event factories must be available as Spring beans

**Priority**: High  
**Category**: Configuration

Event factory implementations must be registered as Spring beans (using `@Component` or explicit configuration) to
enable dependency injection and integration with the application context.

#### Scenario: GameMailEventFactory is autowirable

**Given** the Spring application context is loaded  
**When** a component requests `GameMailEventFactory` via constructor injection  
**Then** a `GameMailEventFactoryImpl` instance must be provided  
**And** the instance must have `ClockService` injected

### Requirement: Event factories must provide domain-specific factory methods

**Priority**: High  
**Category**: API Design

Event factory interfaces must provide factory methods that match domain operations and hide timestamp injection details
from callers.

#### Scenario: GameMailEventFactory provides factory methods

**Given** a `GameMailEventFactory` interface  
**Then** it must provide a method `createGameMailReadEvent(UUID gameMailId, String userId)` that returns
`GameMailReadEvent`  
**And** it must provide a method `createGameMailAttachmentsClaimedEvent(UUID gameMailId, String userId)` that returns
`GameMailAttachmentsClaimedEvent`  
**And** neither method must require the caller to provide a timestamp

## ADDED Requirements - Supporting Changes

### Requirement: AEvent must accept timestamp as constructor parameter

**Priority**: High  
**Category**: Core Functionality

The `AEvent` base class must be refactored to accept timestamp as a constructor parameter to enable controlled timestamp
injection while maintaining backward compatibility.

#### Scenario: AEvent accepts explicit timestamp

**Given** a concrete event class extending `AEvent`  
**When** the event is constructed with eventType and timestamp parameters  
**Then** the event must use the provided timestamp  
**And** the event must not call `System.currentTimeMillis()`

#### Scenario: AEvent maintains backward compatibility

**Given** a concrete event class extending `AEvent`  
**When** the event is constructed with only eventType parameter (legacy constructor)  
**Then** the event must still have a valid timestamp  
**And** existing code must compile without changes

### Requirement: EventPublisherPort implementations must use event factories

**Priority**: High  
**Category**: Integration

EventPublisherPort implementations must inject and use event factories instead of directly instantiating events to
ensure consistent timestamp handling.

#### Scenario: GameMailEventPublisher uses factory

**Given** a `GameMailEventPublisher` (EventPublisherPort implementation) configured with a `GameMailEventFactory`  
**When** the publisher's `publishGameMailReadEvent` method is called with userId and gameMailId  
**Then** the publisher must call `eventFactory.createGameMailReadEvent(gameMailId, userId)`  
**And** the publisher must publish the generated event  
**And** the publisher must not call `new GameMailReadEvent(...)` directly

## Cross-References

- Related to: Clock Management (uses `ClockService`)
- Related to: Event Publishing (modifies event publisher implementations)
- Related to: Testing Infrastructure (enables time-controlled testing)

