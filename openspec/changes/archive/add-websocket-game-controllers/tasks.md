# Implementation Tasks

## 1. Core Event Infrastructure

- [ ] 1.1 Extend `Event` interface with WebSocket-specific metadata (sessionId, messageId)
- [ ] 1.2 Create `WebSocketEventType` enum extending `EventType`
- [ ] 1.3 Create abstract `AWebSocketEvent` base class extending `AEvent`
- [ ] 1.4 Create `WebSocketEventFactory` for creating WebSocket events
- [ ] 1.5 Define WebSocket event types for all game operations (STAGE_UPDATE, CHARACTERISTICS_UPDATE, INVENTORY_ADD,
  etc.)

## 2. WebSocket Handler Infrastructure

- [ ] 2.1 Implement `GameWebSocketHandler` extending Spring's `WebSocketHandler`
- [ ] 2.2 Create `WebSocketEventHandler` interface (similar to existing `EventHandler`)
- [ ] 2.3 Create `WebSocketEventHandlerRegistry` for routing events to handlers
- [ ] 2.4 Implement authentication/authorization interceptor for WebSocket connections
- [ ] 2.5 Add WebSocket session management (store authenticated sessions)

## 3. Game-Specific Event Handlers

- [ ] 3.1 Implement `StageWebSocketEventHandler` (handle stage updates)
- [ ] 3.2 Implement `CharacteristicsWebSocketEventHandler` (handle characteristics updates)
- [ ] 3.3 Implement `InventoryWebSocketEventHandler` (handle inventory CRUD operations)
- [ ] 3.4 Implement `GameSaveWebSocketEventHandler` (handle game save operations)
- [ ] 3.5 Add error handling and validation for all handlers

## 4. WebSocket DTOs and Messages

- [ ] 4.1 Create `WebSocketMessage<T>` wrapper for all WebSocket communications
- [ ] 4.2 Create WebSocket request DTOs (reuse existing request DTOs where applicable)
- [ ] 4.3 Create WebSocket response DTOs (reuse existing response DTOs where applicable)
- [ ] 4.4 Add message serialization/deserialization with Jackson

## 5. WebSocket Configuration

- [ ] 5.1 Create `WebSocketConfiguration` class with endpoint mappings
- [ ] 5.2 Configure WebSocket endpoint: `/ws/game`
- [ ] 5.3 Add STOMP/SockJS support (optional, decide based on requirements)
- [ ] 5.4 Configure message broker for broadcasting (if needed)
- [ ] 5.5 Add WebSocket security configuration

## 6. Controller Migration

- [ ] 6.1 Mark POST/PUT/DELETE endpoints as `@Deprecated` in StageController
- [ ] 6.2 Mark POST/PUT/DELETE endpoints as `@Deprecated` in CharacteristicsController
- [ ] 6.3 Mark POST/PUT/DELETE endpoints as `@Deprecated` in InventoryController
- [ ] 6.4 Add deprecation notices in Swagger documentation
- [ ] 6.5 Keep GET endpoints for initial state retrieval

## 7. Testing

- [ ] 7.1 Add unit tests for WebSocket event handlers
- [ ] 7.2 Add integration tests for WebSocket connections
- [ ] 7.3 Add BDD tests for game operations via WebSocket
- [ ] 7.4 Test authentication/authorization on WebSocket connections
- [ ] 7.5 Test concurrent WebSocket sessions
- [ ] 7.6 Add performance tests for WebSocket vs REST

## 8. Documentation

- [ ] 8.1 Document WebSocket event protocol
- [ ] 8.2 Create WebSocket client examples
- [ ] 8.3 Update API documentation with WebSocket endpoints
- [ ] 8.4 Add migration guide for REST to WebSocket transition
- [ ] 8.5 Document error handling and retry strategies
