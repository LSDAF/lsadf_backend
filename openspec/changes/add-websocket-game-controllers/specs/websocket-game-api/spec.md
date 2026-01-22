# WebSocket Game API Specification

## ADDED Requirements

### Requirement: WebSocket Game Connection

The system SHALL provide a WebSocket endpoint for real-time game state updates at `/ws/game`.

#### Scenario: Successful WebSocket connection with valid JWT

- **WHEN** a client connects to `/ws/game?token={valid_jwt}`
- **THEN** the connection is established
- **AND** the user is authenticated based on the JWT token

#### Scenario: WebSocket connection rejection with invalid JWT

- **WHEN** a client attempts to connect with an invalid or missing JWT token
- **THEN** the WebSocket handshake is rejected
- **AND** the connection is not established

### Requirement: Stage Update via WebSocket

The system SHALL handle stage update events sent via WebSocket for game saves.

#### Scenario: Successful stage update

- **WHEN** an authenticated user sends a `STAGE_UPDATE` event with valid stage data
- **THEN** the stage is updated in the database and cache
- **AND** an acknowledgment message is sent back to the client
- **AND** the event includes `gameSaveId`, `currentStage`, and `maxStage`

#### Scenario: Stage update with unauthorized game save

- **WHEN** a user sends a `STAGE_UPDATE` event for a game save they don't own
- **THEN** an error event is sent back to the client
- **AND** the stage is not updated

### Requirement: Characteristics Update via WebSocket

The system SHALL handle characteristics update events sent via WebSocket for game saves.

#### Scenario: Successful characteristics update

- **WHEN** an authenticated user sends a `CHARACTERISTICS_UPDATE` event with valid characteristics data
- **THEN** the characteristics are updated in the database
- **AND** an acknowledgment message is sent back to the client

#### Scenario: Characteristics validation failure

- **WHEN** a user sends a `CHARACTERISTICS_UPDATE` event with invalid data
- **THEN** an error event is sent back describing the validation failure
- **AND** the characteristics are not updated

### Requirement: Inventory Operations via WebSocket

The system SHALL handle inventory create, update, and delete operations via WebSocket.

#### Scenario: Create inventory item

- **WHEN** an authenticated user sends an `INVENTORY_ITEM_CREATE` event
- **THEN** the item is created in the inventory
- **AND** the created item data is sent back to the client

#### Scenario: Update inventory item

- **WHEN** an authenticated user sends an `INVENTORY_ITEM_UPDATE` event for an existing item
- **THEN** the item is updated in the inventory
- **AND** the updated item data is sent back to the client

#### Scenario: Delete inventory item

- **WHEN** an authenticated user sends an `INVENTORY_ITEM_DELETE` event
- **THEN** the item is removed from the inventory
- **AND** an acknowledgment is sent back to the client

#### Scenario: Inventory operation on non-existent item

- **WHEN** a user sends an update or delete event for a non-existent item
- **THEN** an error event is sent back to the client
- **AND** no changes are made to the inventory

### Requirement: WebSocket Event Format

The system SHALL use JSON-encoded Event objects with type discriminator for all WebSocket messages.

#### Scenario: Event deserialization

- **WHEN** the server receives a WebSocket message
- **THEN** it deserializes the message as an Event object using the `@type` field
- **AND** routes the event to the appropriate handler based on `eventType`

#### Scenario: Event serialization for responses

- **WHEN** the server sends a response to the client
- **THEN** it serializes the Event object as JSON with `@type`, `eventType`, `timestamp`, and payload fields

### Requirement: WebSocket Error Handling

The system SHALL send error events back to clients when event processing fails, without closing the WebSocket
connection.

#### Scenario: Event processing error

- **WHEN** an exception occurs during event processing
- **THEN** an `ERROR` event is sent back to the client with error details
- **AND** the WebSocket connection remains open
- **AND** the error event includes the original `messageId` if available

#### Scenario: Unknown event type

- **WHEN** a client sends an event with an unknown `eventType`
- **THEN** an error event is sent indicating the event type is not supported
- **AND** the connection remains open

### Requirement: WebSocket Session Validation

The system SHALL validate that users can only perform operations on their own game saves via WebSocket.

#### Scenario: User ID validation

- **WHEN** a WebSocket event is received
- **THEN** the system validates that the `userId` in the event matches the authenticated JWT subject
- **AND** rejects the event if there is a mismatch

#### Scenario: Game save ownership validation

- **WHEN** a game-related event is processed
- **THEN** the system verifies the user owns the specified game save
- **AND** returns an error if the user does not own the game save

### Requirement: WebSocket Event Registry

The system SHALL maintain a registry of event handlers that routes incoming events to the appropriate handler.

#### Scenario: Handler registration

- **WHEN** the application starts
- **THEN** all WebSocketEventHandler implementations are registered in the registry
- **AND** each handler is mapped to its supported EventType

#### Scenario: Event routing

- **WHEN** an event is received via WebSocket
- **THEN** the registry routes it to the handler for that event type
- **AND** throws an exception if no handler is registered

### Requirement: WebSocket Connection Lifecycle

The system SHALL manage WebSocket connection establishment, maintenance, and closure.

#### Scenario: Connection established

- **WHEN** a WebSocket connection is successfully established
- **THEN** the connection is logged with user ID and session ID
- **AND** the session is ready to receive events

#### Scenario: Connection closed by client

- **WHEN** a client closes the WebSocket connection
- **THEN** the server logs the closure
- **AND** cleans up session resources

#### Scenario: Transport error

- **WHEN** a WebSocket transport error occurs
- **THEN** the error is logged
- **AND** an error event is sent to the client if possible
- **AND** the connection is closed if recovery is not possible

### Requirement: Event Reusability

The system SHALL reuse the existing `Event` interface and patterns for WebSocket messages, maintaining consistency with
internal event handling.

#### Scenario: Event interface compatibility

- **WHEN** a WebSocket event is created
- **THEN** it implements the existing `com.lsadf.core.shared.event.Event` interface
- **AND** includes `eventType` and `timestamp` fields

#### Scenario: WebSocket event extension

- **WHEN** a WebSocket-specific event is created
- **THEN** it extends `AWebSocketEvent` which extends `AEvent`
- **AND** includes additional fields: `sessionId`, `messageId`, and `userId`
