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
package com.lsadf.core.unit.infra.websocket.config;

import static org.mockito.Mockito.*;

import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.websocket.config.GameWebSocketHandler;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.system.ErrorWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.impl.WebSocketEventHandlerRegistry;
import com.lsadf.core.shared.event.Event;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class GameWebSocketHandlerTests {

  @Mock private WebSocketEventHandlerRegistry eventHandlerRegistry;

  @Mock private ObjectMapper objectMapper;

  @Mock private WebSocketSession session;

  @Mock private Jwt jwt;

  private GameWebSocketHandler handler;
  private Map<String, Object> sessionAttributes;

  private static final String USER_ID = "user123";
  private static final String SESSION_ID = "session123";

  @BeforeEach
  void setUp() {
    handler = new GameWebSocketHandler(eventHandlerRegistry, objectMapper);
    sessionAttributes = new HashMap<>();
    sessionAttributes.put("jwt", jwt);

    lenient().when(session.getAttributes()).thenReturn(sessionAttributes);
    lenient().when(session.getId()).thenReturn(SESSION_ID);
    lenient().when(jwt.getSubject()).thenReturn(USER_ID);
  }

  @Test
  void shouldEstablishConnectionSuccessfully() throws Exception {
    handler.afterConnectionEstablished(session);

    verify(session).getAttributes();
    verify(jwt).getSubject();
  }

  @Test
  void shouldHandleTextMessageSuccessfully() throws Exception {
    UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000123");
    UUID sessionId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();

    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(10L, 20L, 30L, 40L, 50L);
    JsonNode dataNode = objectMapper.valueToTree(characteristicsRequest);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.CHARACTERISTICS_UPDATE, sessionId, messageId, userId, dataNode);

    String payload = "{\"type\":\"CHARACTERISTICS_UPDATE\"}";
    TextMessage message = new TextMessage(payload);

    when(jwt.getSubject()).thenReturn(userId.toString());
    when(objectMapper.readValue(payload, WebSocketEvent.class)).thenReturn(event);

    handler.handleTextMessage(session, message);

    verify(objectMapper).readValue(payload, WebSocketEvent.class);
    verify(eventHandlerRegistry).handleEvent(session, event);
  }

  @Test
  void shouldRejectMessageWhenUserIdMismatch() throws Exception {
    UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000123");
    UUID sessionId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();

    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(10L, 20L, 30L, 40L, 50L);
    JsonNode dataNode = objectMapper.valueToTree(characteristicsRequest);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.CHARACTERISTICS_UPDATE, sessionId, messageId, userId, dataNode);
    String payload = "{\"type\":\"CHARACTERISTICS_UPDATE\"}";
    TextMessage message = new TextMessage(payload);

    when(jwt.getSubject()).thenReturn(userId.toString());
    when(objectMapper.readValue(payload, Event.class)).thenReturn(event);
    when(objectMapper.writeValueAsString(any(ErrorWebSocketEvent.class))).thenReturn("{}");

    handler.handleTextMessage(session, message);

    verify(eventHandlerRegistry, never()).handleEvent(any(), any());
    ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
    verify(session).sendMessage(messageCaptor.capture());
  }

  @Test
  void shouldSendErrorEventWhenExceptionOccurs() throws Exception {
    String payload = "{\"invalid\":\"json\"}";
    TextMessage message = new TextMessage(payload);

    when(objectMapper.readValue(payload, Event.class))
        .thenThrow(new RuntimeException("Parse error"));
    when(objectMapper.writeValueAsString(any(ErrorWebSocketEvent.class))).thenReturn("{}");

    handler.handleTextMessage(session, message);

    ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
    verify(session).sendMessage(messageCaptor.capture());
    verify(eventHandlerRegistry, never()).handleEvent(any(), any());
  }

  @Test
  void shouldHandleConnectionClosed() throws Exception {
    CloseStatus status = CloseStatus.NORMAL;

    handler.afterConnectionClosed(session, status);

    verify(session).getId();
  }

  @Test
  void shouldHandleTransportError() throws Exception {
    Throwable exception = new RuntimeException("Transport error");
    when(objectMapper.writeValueAsString(any(ErrorWebSocketEvent.class))).thenReturn("{}");

    handler.handleTransportError(session, exception);

    verify(session).sendMessage(any(TextMessage.class));
  }
}
