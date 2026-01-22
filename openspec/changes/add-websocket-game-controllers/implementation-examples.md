# WebSocket Implementation Examples

This document provides concrete implementation examples for the proposed WebSocket game controllers.

## 1. Core Event Types

### WebSocketEventType.java

```java
package com.lsadf.core.infra.websocket.event;

import com.lsadf.core.shared.event.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketEventType implements EventType {
    // Stage events
    STAGE_UPDATE("websocket.stage.update"),

    // Characteristics events
    CHARACTERISTICS_UPDATE("websocket.characteristics.update"),

    // Inventory events
    INVENTORY_ITEM_CREATE("websocket.inventory.item.create"),
    INVENTORY_ITEM_UPDATE("websocket.inventory.item.update"),
    INVENTORY_ITEM_DELETE("websocket.inventory.item.delete"),

    // Game save events
    GAME_SAVE_UPDATE_NICKNAME("websocket.game_save.update.nickname"),

    // System events
    ERROR("websocket.error"),
    ACK("websocket.ack");

    private final String value;
}
```

### AWebSocketEvent.java

```java
package com.lsadf.core.infra.websocket.event;

import com.lsadf.core.shared.event.AEvent;
import com.lsadf.core.shared.event.EventType;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public abstract class AWebSocketEvent extends AEvent {
    protected final UUID sessionId;
    protected final UUID messageId;
    protected final UUID userId;

    protected AWebSocketEvent(EventType eventType, UUID sessionId, UUID messageId, UUID userId) {
        super(eventType);
        this.sessionId = sessionId;
        this.messageId = messageId;
        this.userId = userId;
    }
}
```

### StageUpdateWebSocketEvent.java

```java
package com.lsadf.core.infra.websocket.event.game;

import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.infra.websocket.event.AWebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class StageUpdateWebSocketEvent extends AWebSocketEvent {
    private final UUID gameSaveId;
    private final StageRequest payload;

    public StageUpdateWebSocketEvent(UUID sessionId, UUID messageId, UUID userId, 
                                     UUID gameSaveId, StageRequest payload) {
        super(WebSocketEventType.STAGE_UPDATE, sessionId, messageId, userId);
        this.gameSaveId = gameSaveId;
        this.payload = payload;
    }
}
```

## 2. WebSocket Handler Infrastructure

### WebSocketEventHandler.java

```java
package com.lsadf.core.infra.websocket.handler;

import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketEventHandler {
    /**
     * Handles the WebSocket event
     * @param session the WebSocket session
     * @param event the event to handle
     * @throws Exception if handling fails
     */
    void handleEvent(WebSocketSession session, Event event) throws Exception;

    /**
     * Gets the event type this handler supports
     * @return the event type
     */
    EventType getEventType();
}
```

### WebSocketEventHandlerRegistry.java

```java
package com.lsadf.core.infra.websocket.handler;

import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WebSocketEventHandlerRegistry {
    private final Map<EventType, WebSocketEventHandler> handlers = new HashMap<>();

    public WebSocketEventHandlerRegistry(List<WebSocketEventHandler> handlerList) {
        for (WebSocketEventHandler handler : handlerList) {
            handlers.put(handler.getEventType(), handler);
            log.info("Registered WebSocket event handler for type: {}", handler.getEventType());
        }
    }

    public void handleEvent(WebSocketSession session, Event event) throws Exception {
        EventType eventType = event.getEventType();
        WebSocketEventHandler handler = handlers.get(eventType);
        
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for event type: " + eventType);
        }
        
        log.debug("Handling event type {} with handler {}", eventType, handler.getClass().getSimpleName());
        handler.handleEvent(session, event);
    }

    public boolean hasHandler(EventType eventType) {
        return handlers.containsKey(eventType);
    }
}
```

### GameWebSocketHandler.java

```java
package com.lsadf.core.infra.websocket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.infra.websocket.event.AWebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.system.ErrorWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandlerRegistry;
import com.lsadf.core.shared.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler implements WebsocketHandler {
    
    private final WebSocketEventHandlerRegistry eventHandlerRegistry;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Jwt jwt = (Jwt) session.getAttributes().get("jwt");
        String userId = jwt.getSubject();
        log.info("WebSocket connection established for user: {} session: {}", userId, session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            log.debug("Received WebSocket message: {}", payload);
            
            // Deserialize event
            Event event = objectMapper.readValue(payload, Event.class);
            
            // Validate session
            validateSession(session, event);
            
            // Handle event
            eventHandlerRegistry.handleEvent(session, event);
            
        } catch (Exception e) {
            log.error("Error handling WebSocket message", e);
            sendErrorEvent(session, e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed: {} status: {}", session.getId(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error for session: {}", session.getId(), exception);
        sendErrorEvent(session, exception);
    }

    private void validateSession(WebSocketSession session, Event event) {
        if (!(event instanceof AWebSocketEvent)) {
            throw new IllegalArgumentException("Event must be a WebSocket event");
        }
        
        AWebSocketEvent wsEvent = (AWebSocketEvent) event;
        Jwt jwt = (Jwt) session.getAttributes().get("jwt");
        String userId = jwt.getSubject();
        
        if (!userId.equals(wsEvent.getUserId().toString())) {
            throw new SecurityException("User ID mismatch");
        }
    }

    private void sendErrorEvent(WebSocketSession session, Throwable error) {
        try {
            ErrorWebSocketEvent errorEvent = new ErrorWebSocketEvent(
                UUID.randomUUID(), // sessionId
                UUID.randomUUID(), // messageId
                UUID.fromString("00000000-0000-0000-0000-000000000000"), // system userId
                "ERROR",
                error.getMessage(),
                null // originalMessageId
            );
            
            String json = objectMapper.writeValueAsString(errorEvent);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            log.error("Failed to send error event", e);
        }
    }
}
```

## 3. Specific Event Handlers

### StageWebSocketEventHandler.java

```java
package com.lsadf.core.infra.websocket.handler.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.command.UpdateStageCommand;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.game.StageUpdateWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class StageWebSocketEventHandler implements WebSocketEventHandler {

    private final StageCommandService stageCommandService;
    private final ObjectMapper objectMapper;

    @Override
    public void handleEvent(WebSocketSession session, Event event) throws Exception {
        StageUpdateWebSocketEvent stageEvent = (StageUpdateWebSocketEvent) event;

        log.info("Handling stage update for gameSaveId: {}", stageEvent.getGameSaveId());

        // Convert to command
        UpdateStageCommand command = UpdateStageCommand.builder()
                .gameSaveId(stageEvent.getGameSaveId())
                .userId(stageEvent.getUserId())
                .currentStage(stageEvent.getPayload().getCurrentStage())
                .maxStage(stageEvent.getPayload().getMaxStage())
                .build();

        // Execute command
        stageCommandService.updateStage(command);

        // Send acknowledgment
        sendAck(session, stageEvent.getMessageId());
    }

    @Override
    public EventType getEventType() {
        return WebSocketEventType.STAGE_UPDATE;
    }

    private void sendAck(WebSocketSession session, UUID messageId) throws Exception {
        // Send acknowledgment message
        String ackMessage = objectMapper.writeValueAsString(
                Map.of(
                        "type", "ACK",
                        "messageId", messageId.toString(),
                        "timestamp", System.currentTimeMillis()
                )
        );
        session.sendMessage(new TextMessage(ackMessage));
    }
}
```

## 4. WebSocket Configuration

### WebSocketConfiguration.java

```java
package com.lsadf.core.infra.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final GameWebSocketHandler gameWebSocketHandler;
    private final WebSocketAuthInterceptor authInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler, "/ws/game")
                .addInterceptors(authInterceptor)
                .setAllowedOrigins("*"); // Configure CORS appropriately
    }
}
```

### WebSocketAuthInterceptor.java

```java
package com.lsadf.core.infra.websocket.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        try {
            String token = extractToken(request);
            if (token == null) {
                log.warn("No token found in WebSocket handshake");
                return false;
            }

            Jwt jwt = jwtDecoder.decode(token);
            attributes.put("jwt", jwt);
            attributes.put("userId", jwt.getSubject());

            log.info("WebSocket authentication successful for user: {}", jwt.getSubject());
            return true;

        } catch (Exception e) {
            log.error("WebSocket authentication failed", e);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // No-op
    }

    private String extractToken(ServerHttpRequest request) {
        // Extract from query parameter: ws://host/ws/game?token=xxx
        String query = request.getURI().getQuery();
        if (query != null && query.contains("token=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    return param.substring(6);
                }
            }
        }
        return null;
    }
}
```

## 5. Event Merging with Existing Event Infrastructure

The existing `Event` interface can be reused for WebSocket events with minimal changes:

```java
// Existing Event interface (no changes needed)
public interface Event extends Serializable {
    EventType getEventType();

    Long getTimestamp();
}

// WebSocket events implement the same interface
public class StageUpdateWebSocketEvent extends AWebSocketEvent implements Event {
    // Implementation already extends AEvent which implements Event
}

// This allows EventHandler and WebSocketEventHandler to work with same Event abstraction
```

The key insight is that `WebSocketEventHandler` and the existing `EventHandler` (used for Valkey streams) can both work
with the `Event` interface, but have different signatures:

- **EventHandler** (Valkey): `void handleEvent(Event event)`
- **WebSocketEventHandler**: `void handleEvent(WebSocketSession session, Event event)`

This difference is intentional - WebSocket handlers need the session to send responses back to clients.

## Summary

This implementation:

1. ✅ Reuses existing `Event` interface and patterns
2. ✅ Provides WebSocket-specific event types and handlers
3. ✅ Implements authentication via JWT in query parameters
4. ✅ Uses Jackson for JSON serialization (consistent with REST)
5. ✅ Follows Spring WebSocket patterns
6. ✅ Mirrors existing `EventHandler` architecture for consistency
7. ✅ Includes error handling and acknowledgments
8. ✅ Validates user sessions for security

The proposal is ready for review and implementation according to the tasks checklist.
