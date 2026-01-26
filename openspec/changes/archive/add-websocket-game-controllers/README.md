# WebSocket Game Controllers - Change Proposal

## Overview

This change proposal introduces WebSocket support for real-time game state updates in the LSADF backend. The goal is to
migrate game mutation operations (stage, characteristics, inventory, game save updates) from REST to WebSocket while
maintaining session management in REST controllers.

## Status

✅ **Proposal Created** - Ready for review
⏸️ **Implementation** - Awaiting approval

## Key Documents

1. **[proposal.md](./proposal.md)** - High-level overview, motivation, and impact analysis
2. **[design.md](./design.md)** - Detailed technical design decisions and architecture
3. **[tasks.md](./tasks.md)** - Implementation checklist (8 phases, 40+ tasks)
4. **[implementation-examples.md](./implementation-examples.md)** - Concrete code examples for all components
5. **[specs/websocket-game-api/spec.md](./specs/websocket-game-api/spec.md)** - Formal requirements and scenarios

## Quick Summary

### What's Being Added

- WebSocket endpoint at `/ws/game` for real-time game updates
- Event-based message protocol reusing existing `Event` interface
- Handler registry pattern (mirrors existing Valkey EventHandler architecture)
- JWT authentication for WebSocket connections
- Support for: Stage, Characteristics, Inventory, and GameSave operations

### Key Design Decisions

1. **Reuse Event Infrastructure**: Extend existing `Event`, `EventType`, and `AEvent` classes
2. **Handler Pattern**: Mirror `EventHandler` pattern with `WebSocketEventHandler`
3. **Authentication**: JWT via query parameter (`/ws/game?token=xxx`)
4. **Protocol**: JSON-encoded events with type discriminator
5. **Error Handling**: Send error events without closing connection
6. **Technology**: Spring WebSocket (not STOMP initially)

### Breaking Changes

- Game mutation endpoints (POST/PUT/DELETE) will be deprecated in REST controllers
- Clients must migrate to WebSocket for real-time updates
- GET endpoints remain for initial state retrieval

## Implementation Phases

1. **Core Event Infrastructure** (5 tasks)
2. **WebSocket Handler Infrastructure** (5 tasks)
3. **Game-Specific Event Handlers** (5 tasks)
4. **WebSocket DTOs and Messages** (4 tasks)
5. **WebSocket Configuration** (5 tasks)
6. **Controller Migration** (5 tasks)
7. **Testing** (6 tasks)
8. **Documentation** (5 tasks)

**Total: 40 implementation tasks**

## Code Examples

The `implementation-examples.md` file provides complete, ready-to-use code for:

- `WebSocketEventType` enum
- `AWebSocketEvent` base class
- `GameWebSocketHandler` main handler
- `WebSocketEventHandlerRegistry` for routing
- `StageWebSocketEventHandler` example
- `WebSocketConfiguration` and authentication interceptor

## Architecture Highlights

### Event Flow

```
Client (WebSocket) 
  → GameWebSocketHandler (auth + deserialize)
    → WebSocketEventHandlerRegistry (route)
      → StageWebSocketEventHandler (process)
        → StageCommandService (execute)
          → Response sent back via WebSocket
```

### Event Reusability

```java
// Existing infrastructure
interface Event extends Serializable { ... }
abstract class AEvent implements Event { ... }
interface EventHandler { void handleEvent(Event event); }

// New WebSocket infrastructure (reuses Event!)
abstract class AWebSocketEvent extends AEvent { ... }
interface WebSocketEventHandler { void handleEvent(WebSocketSession, Event); }
```

## Validation

✅ Proposal validated with `openspec validate add-websocket-game-controllers --strict`
✅ All 10 requirements have scenarios
✅ No validation errors

## Next Steps

1. **Review** - Team review of proposal, design, and tasks
2. **Approval** - Product owner/architect approval
3. **Implementation** - Follow tasks.md checklist sequentially
4. **Testing** - Execute test plan (unit, integration, BDD)
5. **Documentation** - Client migration guide and API docs
6. **Deployment** - Phased rollout with deprecation period

## Questions?

See **Open Questions** section in [design.md](./design.md) for discussion points.

## Related Files

- Existing Event infrastructure: `lsadf_core/src/main/java/com/lsadf/core/shared/event/`
- Existing EventHandler pattern: `lsadf_core/src/main/java/com/lsadf/core/infra/valkey/stream/consumer/handler/`
- Current REST controllers: `lsadf_api/src/main/java/com/lsadf/application/controller/game/`
- WebSocket config stub: `lsadf_core/src/main/java/com/lsadf/core/infra/websocket/config/WebsocketHandler.java`

---

**Change ID**: `add-websocket-game-controllers`  
**Created**: 2026-01-22  
**Author**: AI Assistant  
**Status**: Awaiting Approval
