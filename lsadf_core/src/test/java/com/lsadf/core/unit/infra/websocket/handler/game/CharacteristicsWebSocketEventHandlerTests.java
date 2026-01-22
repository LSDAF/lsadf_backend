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
package com.lsadf.core.unit.infra.websocket.handler.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.handler.game.CharacteristicsWebSocketEventHandler;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CharacteristicsWebSocketEventHandlerTests {

  @Mock private CharacteristicsCommandService characteristicsCommandService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Mock private WebSocketSession session;

  @Mock private GameSessionQueryService gameSessionQueryService;

  private CharacteristicsWebSocketEventHandler handler;

  private UUID sessionId;
  private UUID messageId;
  private UUID userId;
  private UUID gameSaveId;

  @BeforeEach
  void setUp() {
    handler =
        new CharacteristicsWebSocketEventHandler(
            characteristicsCommandService, objectMapper, gameSessionQueryService);
    sessionId = UUID.randomUUID();
    messageId = UUID.randomUUID();
    userId = UUID.randomUUID();
    gameSaveId = UUID.randomUUID();
  }

  @Test
  void shouldReturnCorrectEventType() {
    assertEquals(WebSocketEventType.CHARACTERISTICS_UPDATE, handler.getEventType());
  }

  @Test
  void shouldHandleCharacteristicsUpdateEvent() throws Exception {
    CharacteristicsRequest request = new CharacteristicsRequest(10L, 20L, 30L, 40L, 50L);
    JsonNode dataNode = objectMapper.valueToTree(request);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.CHARACTERISTICS_UPDATE, sessionId, messageId, userId, dataNode);

    GameSession gameSession = mock(GameSession.class);
    when(gameSession.getGameSaveId()).thenReturn(gameSaveId);
    when(gameSessionQueryService.findGameSessionById(any())).thenReturn(gameSession);

    handler.handleEvent(session, event);

    ArgumentCaptor<UpdateCacheCharacteristicsCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateCacheCharacteristicsCommand.class);
    verify(characteristicsCommandService).updateCacheCharacteristics(commandCaptor.capture());

    UpdateCacheCharacteristicsCommand command = commandCaptor.getValue();
    assertEquals(gameSaveId, command.gameSaveId());
    assertEquals(10L, command.attack());
    assertEquals(20L, command.critChance());
    assertEquals(30L, command.critDamage());
    assertEquals(40L, command.health());
    assertEquals(50L, command.resistance());

    verify(session).sendMessage(any(TextMessage.class));
  }

  @Test
  void shouldSendAckAfterSuccessfulUpdate() throws Exception {
    CharacteristicsRequest request = new CharacteristicsRequest(10L, 20L, 30L, 40L, 50L);
    JsonNode dataNode = objectMapper.valueToTree(request);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.CHARACTERISTICS_UPDATE, sessionId, messageId, userId, dataNode);

    GameSession gameSession = mock(GameSession.class);
    when(gameSession.getGameSaveId()).thenReturn(gameSaveId);
    when(gameSessionQueryService.findGameSessionById(any())).thenReturn(gameSession);

    handler.handleEvent(session, event);

    ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
    verify(session).sendMessage(messageCaptor.capture());

    TextMessage sentMessage = messageCaptor.getValue();
    JsonNode json = objectMapper.readTree(sentMessage.getPayload());
    assertEquals("ACK", json.get("eventType").asString());
  }

  @Test
  void shouldPropagateExceptionWhenServiceFails() throws Exception {
    CharacteristicsRequest request = new CharacteristicsRequest(10L, 20L, 30L, 40L, 50L);
    JsonNode dataNode = objectMapper.valueToTree(request);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.CHARACTERISTICS_UPDATE, sessionId, messageId, userId, dataNode);

    GameSession gameSession = mock(GameSession.class);
    when(gameSession.getGameSaveId()).thenReturn(gameSaveId);
    when(gameSessionQueryService.findGameSessionById(any())).thenReturn(gameSession);

    doThrow(new RuntimeException("Service error"))
        .when(characteristicsCommandService)
        .updateCacheCharacteristics(any());

    assertThrows(RuntimeException.class, () -> handler.handleEvent(session, event));

    verify(session, never()).sendMessage(any());
  }
}
