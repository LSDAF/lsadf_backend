# Design: Event Factories with ClockService

## Architecture Overview

The event generation system follows a factory pattern with dependency injection to provide testable, time-aware event
creation.

```
┌─────────────────────────────────────────────────────────────┐
│                    Event Publisher Layer                     │
│  (Uses factories to publish domain events)                   │
└──────────────────────────┬──────────────────────────────────┘
                           │ uses
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   Event Factory Layer                        │
│  (Factory beans that create events with timestamps)          │
│  - GameMailEventFactory                                      │
│  - [Future: InventoryEventFactory, etc.]                     │
└──────────────────────────┬──────────────────────────────────┘
                           │ injects
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      ClockService                            │
│  (Provides controlled time source)                           │
└─────────────────────────────────────────────────────────────┘
                           │ used by
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                        AEvent                                │
│  (Base event class with timestamp parameter)                 │
└─────────────────────────────────────────────────────────────┘
```

## Component Design

### 1. AEvent Refactoring

**Current Implementation:**

```java
public abstract class AEvent implements Event {
    protected final EventType eventType;
    protected final Long timestamp = System.currentTimeMillis(); // Direct call
}
```

**New Implementation:**

```java
public abstract class AEvent implements Event {
    protected final EventType eventType;
    protected final Long timestamp;

    // Constructor accepting timestamp
    protected AEvent(EventType eventType, Long timestamp) {
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    // Backward compatibility constructor (uses current time)
    protected AEvent(EventType eventType) {
        this(eventType, System.currentTimeMillis());
    }
}
```

**Rationale:**

- Maintains backward compatibility with existing event constructors
- Allows explicit timestamp injection for testability
- Eventually, the no-arg timestamp constructor can be deprecated

### 2. Event Factory Interface

```java
public interface GameMailEventFactory {
    GameMailReadEvent createGameMailReadEvent(UUID gameMailId, String userId);

    GameMailAttachmentsClaimedEvent createGameMailAttachmentsClaimedEvent(UUID gameMailId, String userId);
}
```

**Design Decisions:**

- One factory per domain aggregate (Game Mail, Inventory, etc.)
- Methods mirror the domain operations that produce events
- Interface enables mocking in tests
- Clear method names that express intent

### 3. Event Factory Implementation

```java

@Component
public class GameMailEventFactoryImpl implements GameMailEventFactory {
    private final ClockService clockService;

    public GameMailEventFactoryImpl(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public GameMailReadEvent createGameMailReadEvent(UUID gameMailId, String userId) {
        long timestamp = clockService.nowInstant().toEpochMilli();
        return new GameMailReadEvent(gameMailId, userId, timestamp);
    }

    @Override
    public GameMailAttachmentsClaimedEvent createGameMailAttachmentsClaimedEvent(
            UUID gameMailId, String userId) {
        long timestamp = clockService.nowInstant().toEpochMilli();
        return new GameMailAttachmentsClaimedEvent(gameMailId, userId, timestamp);
    }
}
```

**Design Decisions:**

- Use `@Component` for auto-discovery (consistent with Spring conventions in the project)
- Constructor injection for `ClockService` (required dependency)
- Convert `Instant` to `epochMilli` for consistency with existing timestamp format
- Each method encapsulates the creation logic and timestamp injection

### 4. Configuration

No additional configuration class needed - event factories will be auto-discovered through component scanning in the
existing `com.lsadf.core.infra.event.config` package structure.

## Event Constructor Changes

Each concrete event class needs an additional constructor accepting timestamp:

```java
// Before
public GameMailReadEvent(UUID gameMailId, String userId) {
    super(GAME_MAIL_READ);
    this.gameMailId = gameMailId;
    this.userId = userId;
}

// After - add timestamp constructor
public GameMailReadEvent(UUID gameMailId, String userId, Long timestamp) {
    super(GAME_MAIL_READ, timestamp);
    this.gameMailId = gameMailId;
    this.userId = userId;
}

// Keep backward compatibility
public GameMailReadEvent(UUID gameMailId, String userId) {
    this(gameMailId, userId, System.currentTimeMillis());
}
```

## Migration Strategy

### Phase 1: Foundation (This Change)

1. Refactor `AEvent` with timestamp parameter
2. Create `GameMailEventFactory`
3. Update `GameMailReadEvent` and `GameMailAttachmentsClaimedEvent` constructors
4. Update `GameMailEventPublisher` (EventPublisherPort implementation) to inject and use factory

### Phase 2: Incremental Migration (Future)

- Inventory events
- Valkey stream events
- Other domain events

### Phase 3: Cleanup (Future)

- Remove backward-compatibility constructors
- Mark direct event construction as deprecated

## Testing Strategy

1. **Unit Tests**: Mock `ClockService` to control time in event generation tests
2. **Integration Tests**: Use `ClockService.setClock()` to control time in BDD scenarios
3. **Backward Compatibility Tests**: Verify events created without timestamp still work

## Trade-offs

| Approach                           | Pros                                  | Cons                                  | Decision                                      |
|------------------------------------|---------------------------------------|---------------------------------------|-----------------------------------------------|
| **Constructor injection** (chosen) | Immutable, clear dependency, testable | Requires constructor changes          | ✅ Chosen - aligns with immutability principle |
| Setter injection                   | No constructor changes                | Mutable state, unclear initialization | ❌ Not chosen                                  |
| Static factory methods             | Flexible, hides constructor           | Less idiomatic in Spring              | ❌ Not chosen                                  |
| Builder pattern                    | Flexible, readable                    | Overkill for simple events            | ❌ Not chosen                                  |

## Consistency with Project Patterns

- **Follows existing `ClockService` usage**: Controllers and services already use `ClockService`
- **Spring Bean conventions**: Uses `@Component` and constructor injection like other beans
- **Package structure**: Follows `com.lsadf.core.infra.event.*` conventions
- **Code style**: Will pass Google Java Style checks via Spotless
- **Lombok usage**: Maintains existing Lombok patterns (`@RequiredArgsConstructor`, etc.)

