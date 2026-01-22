# WebSocket Game Controllers Implementation Proposal

## Why

The current architecture uses REST controllers for all game-related operations (stage, characteristics, inventory, game
save), which leads to:

- High latency for real-time game state updates
- Inefficient polling or repeated requests for game state changes
- Lack of bi-directional communication for real-time game events
- Existing event infrastructure (Event, EventHandler) is only used for internal Valkey streams, not exposed to clients

Moving game update operations to WebSockets will enable real-time, efficient bi-directional communication while keeping
session management in REST controllers.

## What Changes

- Implement a `GameWebSocketHandler` extending Spring's `WebSocketHandler` interface
- Create WebSocket-specific event types and DTOs for game operations (stage, characteristics, inventory, game save
  updates)
- Implement `WebSocketEventHandler` interface hierarchy to handle incoming WebSocket events
- Integrate existing `Event` interface for WebSocket messages (reusing `com.lsadf.core.shared.event.Event`)
- Create WebSocket configuration for game endpoints (`/ws/game`)
- Add WebSocket session management with authentication/authorization
- **BREAKING**: Deprecate POST/PUT/DELETE operations in game controllers (StageController, CharacteristicsController,
  InventoryController) - only GET operations remain for initial state
- Keep SessionController as pure REST

## Impact

### Affected Code

- **New modules**:
    - `lsadf_core/infra/websocket/handler/` - WebSocket handler implementations
    - `lsadf_core/infra/websocket/event/` - WebSocket event types
    - `lsadf_core/infra/web/dto/websocket/` - WebSocket DTOs for all game entities
    - `lsadf_api/websocket/` - WebSocket endpoint configuration

- **Modified modules**:
    - `lsadf_core/shared/event/` - Extend Event interface for WebSocket compatibility
    - Game controllers (Stage, Characteristics, Inventory, GameSave) - deprecate mutation endpoints

### Breaking Changes

- **BREAKING**: Game mutation operations move from REST to WebSocket
- **BREAKING**: Clients must establish WebSocket connection for game updates
- **BREAKING**: Response format changes from REST ApiResponse to WebSocket event-based messages

### Migration Path

1. Phase 1: Implement WebSocket handlers alongside existing REST endpoints
2. Phase 2: Mark REST mutation endpoints as deprecated
3. Phase 3: Remove deprecated REST endpoints after client migration period
