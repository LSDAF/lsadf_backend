# WebSocket Game Controllers Design Document

## Context

The LSADF backend currently handles all game operations through REST controllers. Game state updates (stage progression,
characteristics changes, inventory management) require multiple round-trip HTTP requests. The existing event
infrastructure (`Event`, `EventHandler`, `EventHandlerRegistry`) is used internally for Valkey stream processing but not
exposed to clients.

This design proposes migrating game mutation operations to WebSocket while:

1. Reusing the existing `Event` abstraction
2. Maintaining REST for session management and initial state retrieval
3. Enabling real-time bi-directional communication

## Goals / Non-Goals

### Goals

- Enable real-time game state updates via WebSocket
- Reuse existing `Event` interface and patterns for WebSocket messages
- Maintain consistent event handling pattern (similar to `EventHandler` infrastructure)
- Support authenticated WebSocket connections with JWT
- Provide backward compatibility during migration period
- Keep session management and authentication in REST layer

### Non-Goals

- Complete replacement of REST API (session, auth remain REST)
- Real-time multiplayer synchronization (future enhancement)
- WebSocket for non-game operations (user, admin endpoints)
- Custom WebSocket protocol (use standard WebSocket/STOMP)

## Decisions

### 1. Reuse Event Infrastructure

**Decision**: Extend existing `Event` interface for WebSocket messages

**Rationale**:

- `Event` interface already provides `EventType` and `timestamp` abstraction
- `EventHandler` pattern proven in Valkey stream processing
- Consistent event handling across WebSocket and internal streams
- Serializable nature (`Event extends Serializable`) compatible with WebSocket frames

**Implementation**:

```java
// Extend EventType for WebSocket
public enum WebSocketEventType implements EventType {
    STAGE_UPDATE("stage.update"),
    CHARACTERISTICS_UPDATE("characteristics.update"),
    INVENTORY_ADD("inventory.add"),
    INVENTORY_UPDATE("inventory.update"),
    INVENTORY_DELETE("inventory.delete"),
    GAME_SAVE_UPDATE("game_save.update");

    private final String value;
    // ...
}

// Create WebSocket-specific event base
public abstract class AWebSocketEvent extends AEvent {
    protected final UUID sessionId;
    protected final UUID messageId;

    protected AWebSocketEvent(EventType eventType, UUID sessionId, UUID messageId) {
        super(eventType);
        this.sessionId = sessionId;
        this.messageId = messageId;
    }
}

// Example: Stage update event
public class StageUpdateWebSocketEvent extends AWebSocketEvent {
    private final UUID gameSaveId;
    private final StageRequest payload;
    // ...
}
```

### 2. WebSocket Handler Architecture

**Decision**: Implement handler pattern mirroring `EventHandler` infrastructure

**Rationale**:

- Proven pattern in existing codebase (see `EventHandlerRegistry`)
- Clear separation of concerns per game entity
- Easy to extend with new event types
- Testable individual handlers

**Implementation**:

```java
// Mirror existing EventHandler interface
public interface WebSocketEventHandler {
    void handleEvent(WebSocketSession session, Event event) throws Exception;

    EventType getEventType();
}

// Registry for routing
public class WebSocketEventHandlerRegistry {
    private final Map<EventType, WebSocketEventHandler> handlers;

    public void handleEvent(WebSocketSession session, Event event) {
        WebSocketEventHandler handler = handlers.get(event.getEventType());
        if (handler == null) {
            throw new IllegalArgumentException("No handler for: " + event.getEventType());
        }
        handler.handleEvent(session, event);
    }
}

// Main WebSocket handler
public class GameWebSocketHandler extends TextWebSocketHandler {
    private final WebSocketEventHandlerRegistry registry;
    private final ObjectMapper objectMapper;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        Event event = objectMapper.readValue(message.getPayload(), Event.class);
        registry.handleEvent(session, event);
    }
}
```

### 3. Authentication Strategy

**Decision**: Use JWT authentication via query parameter or subprotocol

**Rationale**:

- Consistent with existing REST authentication
- WebSocket doesn't support custom headers in browser environments
- Query parameter is standard WebSocket auth pattern
- Spring Security integration available

**Implementation**:

```java
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private final JwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        String token = extractToken(request);
        if (token == null) {
            return false;
        }

        Jwt jwt = jwtDecoder.decode(token);
        attributes.put("jwt", jwt);
        attributes.put("userId", jwt.getSubject());
        return true;
    }
}
```

### 4. Message Protocol

**Decision**: Use JSON-encoded Event objects with type discriminator

**Rationale**:

- Consistent with REST API (JSON)
- Existing Jackson configuration reusable
- Type discriminator enables polymorphic deserialization
- Standard WebSocket text frames

**Message Format**:

```json
{
  "@type": "StageUpdateWebSocketEvent",
  "eventType": "stage.update",
  "timestamp": 1674567890123,
  "sessionId": "uuid",
  "messageId": "uuid",
  "gameSaveId": "uuid",
  "payload": {
    "currentStage": 5,
    "maxStage": 10
  }
}
```

### 5. Error Handling

**Decision**: Send error events back through WebSocket, don't close connection

**Rationale**:

- Preserve connection for subsequent operations
- Client can handle errors and retry
- Consistent with event-driven architecture

**Error Event**:

```java
public class ErrorWebSocketEvent extends AWebSocketEvent {
    private final String errorCode;
    private final String errorMessage;
    private final UUID originalMessageId;
}
```

### 6. Spring Configuration Choice

**Decision**: Use Spring WebSocket (not STOMP) for initial implementation

**Rationale**:

- Simpler protocol, lower overhead
- Direct control over message handling
- STOMP can be added later if pub/sub needed
- Sufficient for current use case (client-server updates)

**Future Enhancement**: Add STOMP if real-time broadcast to multiple clients needed

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                        Client                                │
│  ┌────────────┐         ┌──────────────────────────────┐   │
│  │ REST API   │ ◄─────► │  GET /stage, /inventory      │   │
│  │ (Initial)  │         │  (Initial state retrieval)   │   │
│  └────────────┘         └──────────────────────────────┘   │
│                                                              │
│  ┌────────────┐         ┌──────────────────────────────┐   │
│  │ WebSocket  │ ◄─────► │  /ws/game                     │   │
│  │ (Updates)  │         │  (Real-time game updates)    │   │
│  └────────────┘         └──────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────┐
│                   GameWebSocketHandler                       │
│                                                              │
│  1. Authenticate (JWT)                                       │
│  2. Deserialize Event                                        │
│  3. Route to Handler                                         │
└─────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    ▼               ▼               ▼
         ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
         │StageWSHandler│  │CharacterWS   │  │InventoryWS   │
         │              │  │Handler       │  │Handler       │
         └──────────────┘  └──────────────┘  └──────────────┘
                    │               │               │
                    └───────────────┼───────────────┘
                                    ▼
                    ┌─────────────────────────────┐
                    │   Domain Services           │
                    │   (StageCommandService,     │
                    │    InventoryService, etc.)  │
                    └─────────────────────────────┘
```

## Risks / Trade-offs

### Risk: WebSocket Connection Management

**Mitigation**:

- Implement heartbeat/ping-pong mechanism
- Add reconnection logic on client
- Store session state in Redis for recovery

### Risk: Authentication Token Expiry

**Mitigation**:

- Send refresh event before token expiry
- Allow re-authentication without closing connection
- Implement token refresh via WebSocket message

### Risk: Breaking Changes for Clients

**Mitigation**:

- Phased rollout (WebSocket alongside REST)
- Deprecation period (3 months) before removing REST endpoints
- Comprehensive migration documentation

### Trade-off: Increased Server Complexity

**Benefit**: Real-time updates, better user experience
**Cost**: More complex connection/session management

### Trade-off: Stateful Connections

**Benefit**: Lower latency, reduced overhead
**Cost**: Harder to scale horizontally (need sticky sessions or Redis-backed session store)

## Migration Plan

### Phase 1: Implementation (Weeks 1-3)

1. Implement WebSocket infrastructure (handlers, events, configuration)
2. Add WebSocket endpoints alongside existing REST
3. Internal testing and validation

### Phase 2: Deprecation (Weeks 4-6)

1. Mark REST mutation endpoints as `@Deprecated`
2. Update API documentation
3. Notify clients of deprecation
4. Provide WebSocket client examples

### Phase 3: Migration Period (Months 1-3)

1. Support both REST and WebSocket
2. Monitor WebSocket adoption metrics
3. Assist clients with migration
4. Fix issues discovered in production

### Phase 4: Cleanup (Month 4)

1. Remove deprecated REST endpoints
2. Clean up unused code
3. Performance optimization
4. Final documentation update

## Open Questions

1. **Should we use STOMP for better pub/sub support?**
    - Decision: Start with plain WebSocket, add STOMP if needed

2. **How to handle concurrent updates to same game save?**
    - Decision: Use optimistic locking with version field, send conflict events

3. **Should WebSocket handle read operations or only mutations?**
    - Decision: Only mutations via WebSocket, reads remain REST for simplicity

4. **What's the reconnection strategy for clients?**
    - Decision: Client-side exponential backoff, server sends full state on reconnect

5. **How to test WebSocket endpoints in BDD tests?**
    - Decision: Use Spring WebSocket test framework, add WebSocket client in test utilities
