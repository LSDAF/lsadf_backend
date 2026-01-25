/*
 * Copyright © 2024-2026 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lsadf.core.infra.websocket.config;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.system.ErrorWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandlerRegistry;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler implements WebsocketHandler {

  private final WebSocketEventHandlerRegistry eventHandlerRegistry;
  private final ObjectMapper objectMapper;
  private final WebSocketEventFactory eventFactory;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    var attributes = session.getAttributes();
    String userEmail = attributes.get(USER_EMAIL).toString();
    UUID gameSessionId = (UUID) attributes.get(GAME_SESSION_ID);
    UUID gameSaveId = (UUID) attributes.get(GAME_SESSION_ID);
    log.info(
        "WebSocket connection established for user: {} session: {}, game save id: {}",
        userEmail,
        gameSessionId,
        gameSaveId);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    WebSocketEvent event = null;
    try {
      String buffer = message.getPayload();
      log.debug("Received WebSocket message: {}", buffer);

      tools.jackson.databind.JsonNode jsonNode = objectMapper.readTree(buffer);
      WebSocketEventType eventType =
          WebSocketEventType.valueOf(jsonNode.get(EVENT_TYPE).asString());
      UUID messageId = UUID.fromString(jsonNode.get(MESSAGE_ID).asString());
      Long timestamp = jsonNode.get(TIMESTAMP).asLong();
      JsonNode data = jsonNode.get(DATA);
      event = new WebSocketEvent(eventType, messageId, timestamp, data);

      eventHandlerRegistry.handleEvent(session, event);
    } catch (JacksonException e) {
      log.error("JSON parsing error", e);
    } catch (Exception e) {
      log.error("Error handling WebSocket message", e);
      sendErrorEvent(session, e, event);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("WebSocket connection closed: {} status: {}", session.getId(), status);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    log.error("WebSocket transport error for session: {}", session.getId(), exception);
    sendErrorEvent(session, exception, null);
  }

  private void sendErrorEvent(
      WebSocketSession session, Throwable error, @Nullable WebSocketEvent event) {
    try {
      ErrorWebSocketEvent errorEvent =
          (event != null)
              ? eventFactory.createErrorEvent(event, error.getMessage())
              : eventFactory.createErrorEvent(error.getMessage());

      byte[] json = objectMapper.writeValueAsBytes(errorEvent);
      session.sendMessage(new BinaryMessage(json));
    } catch (Exception e) {
      log.error("Failed to send error event", e);
    }
  }
}
