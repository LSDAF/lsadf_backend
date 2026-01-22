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

import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.game.GameSaveUpdateNicknameWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.game.GameSaveWebSocketEventHandler;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class GameSaveWebSocketEventHandlerTests {

  @Mock private GameSaveService gameSaveService;

  @Mock private ObjectMapper objectMapper;

  @Mock private WebSocketSession session;

  private GameSaveWebSocketEventHandler handler;

  private UUID sessionId;
  private UUID messageId;
  private UUID userId;
  private UUID gameSaveId;

  @BeforeEach
  void setUp() {
    handler = new GameSaveWebSocketEventHandler(gameSaveService, objectMapper);
    sessionId = UUID.randomUUID();
    messageId = UUID.randomUUID();
    userId = UUID.randomUUID();
    gameSaveId = UUID.randomUUID();
  }

  @Test
  void shouldReturnCorrectEventType() {
    assertEquals(WebSocketEventType.GAME_SAVE_UPDATE_NICKNAME, handler.getEventType());
  }

  @Test
  void shouldHandleGameSaveNicknameUpdate() throws Exception {
    GameSaveNicknameUpdateRequest request =
        GameSaveNicknameUpdateRequest.builder().getNickname("NewNickname").build();
    GameSaveUpdateNicknameWebSocketEvent event =
        new GameSaveUpdateNicknameWebSocketEvent(sessionId, messageId, userId, gameSaveId, request);

    when(objectMapper.writeValueAsString(any())).thenReturn("{}");

    handler.handleEvent(session, event);

    verify(gameSaveService).updateGameSave(eq(gameSaveId), any());
    verify(session).sendMessage(any(TextMessage.class));
  }

  @Test
  void shouldSendAckAfterSuccessfulUpdate() throws Exception {
    GameSaveNicknameUpdateRequest request =
        GameSaveNicknameUpdateRequest.builder().getNickname("NewNickname").build();
    GameSaveUpdateNicknameWebSocketEvent event =
        new GameSaveUpdateNicknameWebSocketEvent(sessionId, messageId, userId, gameSaveId, request);

    when(objectMapper.writeValueAsString(any())).thenReturn("{\"type\":\"ACK\"}");

    handler.handleEvent(session, event);

    ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
    verify(session).sendMessage(messageCaptor.capture());

    TextMessage sentMessage = messageCaptor.getValue();
    assertEquals("{\"type\":\"ACK\"}", sentMessage.getPayload());
  }

  @Test
  void shouldPropagateExceptionWhenServiceFails() throws Exception {
    GameSaveNicknameUpdateRequest request =
        GameSaveNicknameUpdateRequest.builder().getNickname("NewNickname").build();
    GameSaveUpdateNicknameWebSocketEvent event =
        new GameSaveUpdateNicknameWebSocketEvent(sessionId, messageId, userId, gameSaveId, request);

    doThrow(new RuntimeException("Service error"))
        .when(gameSaveService)
        .updateGameSave(any(), any());

    assertThrows(RuntimeException.class, () -> handler.handleEvent(session, event));

    verify(session, never()).sendMessage(any());
  }
}
