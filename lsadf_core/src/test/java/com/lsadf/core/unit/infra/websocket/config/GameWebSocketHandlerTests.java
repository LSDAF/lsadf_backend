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

import static com.lsadf.core.infra.web.JsonAttributes.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.websocket.config.GameWebSocketHandler;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandlerRegistry;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
class GameWebSocketHandlerTests {

  @Mock private WebSocketEventHandlerRegistry eventHandlerRegistry;

  private final ObjectMapper objectMapper =
      JsonMapper.builder()
          .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy())
          .build();

  @Mock private WebSocketSession session;

  @Mock private Jwt jwt;

  @Spy private WebSocketEventFactory eventFactory;

  private GameWebSocketHandler handler;
  private Map<String, Object> sessionAttributes;

  @BeforeEach
  void setUp() {
    handler = new GameWebSocketHandler(eventHandlerRegistry, objectMapper, eventFactory);
    sessionAttributes = new HashMap<>();
    sessionAttributes.put("jwt", jwt);
    sessionAttributes.put(USER_EMAIL, "toto@test.com");
    sessionAttributes.put(GAME_SESSION_ID, UUID.randomUUID());
    sessionAttributes.put(GAME_SAVE_ID, UUID.randomUUID());

    lenient().when(session.getAttributes()).thenReturn(sessionAttributes);
  }

  @Test
  void shouldEstablishConnectionSuccessfully() throws Exception {
    handler.afterConnectionEstablished(session);

    verify(session).getAttributes();
  }

  @Test
  void shouldHandleTextMessageSuccessfully() throws Exception {
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(10L, 20L, 30L, 40L, 50L);
    JsonNode node = objectMapper.valueToTree(characteristicsRequest);
    var event =
        new WebSocketEvent(WebSocketEventType.CHARACTERISTICS_UPDATE, UUID.randomUUID(), node);

    String payload = objectMapper.writeValueAsString(event);
    TextMessage message = new TextMessage(payload);

    handler.handleTextMessage(session, message);

    verify(eventHandlerRegistry).handleEvent(eq(session), any());
  }

  @Test
  void shouldSendErrorEventWhenExceptionOccurs() throws Exception {
    String payload = "{\"invalid\":\"json\"}";
    TextMessage message = new TextMessage(payload.getBytes(StandardCharsets.UTF_8));

    handler.handleTextMessage(session, message);

    ArgumentCaptor<BinaryMessage> messageCaptor = ArgumentCaptor.forClass(BinaryMessage.class);
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

    handler.handleTransportError(session, exception);

    verify(session).sendMessage(any(BinaryMessage.class));
  }
}
