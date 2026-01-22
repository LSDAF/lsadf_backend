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

import com.lsadf.core.infra.websocket.event.AWebSocketEvent;
import com.lsadf.core.infra.websocket.event.system.ErrorWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.impl.WebSocketEventHandlerRegistry;
import com.lsadf.core.shared.event.Event;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

@Slf4j
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
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    try {
      String payload = message.getPayload();
      log.debug("Received WebSocket message: {}", payload);

      Event event = objectMapper.readValue(payload, Event.class);

      validateSession(session, event);

      eventHandlerRegistry.handleEvent(session, event);

    } catch (Exception e) {
      log.error("Error handling WebSocket message", e);
      sendErrorEvent(session, e, null);
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

  private void sendErrorEvent(WebSocketSession session, Throwable error, UUID originalMessageId) {
    try {
      ErrorWebSocketEvent errorEvent =
          new ErrorWebSocketEvent(
              UUID.randomUUID(),
              UUID.randomUUID(),
              UUID.fromString("00000000-0000-0000-0000-000000000000"),
              "ERROR",
              error.getMessage(),
              originalMessageId);

      String json = objectMapper.writeValueAsString(errorEvent);
      session.sendMessage(new TextMessage(json));
    } catch (Exception e) {
      log.error("Failed to send error event", e);
    }
  }
}
