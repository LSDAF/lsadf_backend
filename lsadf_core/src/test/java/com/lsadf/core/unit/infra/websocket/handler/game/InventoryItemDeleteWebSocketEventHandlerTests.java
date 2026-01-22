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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.domain.game.inventory.ItemStatistic;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.handler.game.InventoryItemDeleteWebSocketEventHandler;
import java.util.List;
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
class InventoryItemDeleteWebSocketEventHandlerTests {
  @Mock private InventoryService inventoryService;
  private final ObjectMapper objectMapper = new ObjectMapper();
  @Mock private WebSocketSession session;
  @Mock private GameSessionQueryService gameSessionQueryService;

  private InventoryItemDeleteWebSocketEventHandler handler;
  private UUID sessionId;
  private UUID messageId;
  private UUID userId;
  private UUID gameSaveId;

  private static final ItemRequest itemRequest =
      ItemRequest.builder()
          .clientId("test-client-id-123")
          .type("boots")
          .blueprintId("blueprint_boots_001")
          .rarity("LEGENDARY")
          .isEquipped(true)
          .level(20)
          .mainStat(new ItemStatDto(ItemStatistic.ATTACK_ADD, 100.0f))
          .additionalStats(
              List.of(
                  new ItemStatDto(ItemStatistic.ATTACK_ADD, 50.0f),
                  new ItemStatDto(ItemStatistic.ATTACK_MULT, 25.0f)))
          .build();

  @BeforeEach
  void setUp() {
    handler =
        new InventoryItemDeleteWebSocketEventHandler(
            inventoryService, objectMapper, gameSessionQueryService);
    sessionId = UUID.randomUUID();
    messageId = UUID.randomUUID();
    userId = UUID.randomUUID();
    gameSaveId = UUID.randomUUID();
  }

  @Test
  void shouldReturnCorrectEventType() {
    assertEquals(WebSocketEventType.INVENTORY_ITEM_DELETE, handler.getEventType());
  }

  @Test
  void shouldHandleInventoryItemDeletion() throws Exception {
    JsonNode dataNode = objectMapper.valueToTree(itemRequest);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.INVENTORY_ITEM_DELETE, sessionId, messageId, userId, dataNode);

    GameSession gameSession = mock(GameSession.class);
    when(gameSession.getGameSaveId()).thenReturn(gameSaveId);
    when(gameSessionQueryService.findGameSessionById(any())).thenReturn(gameSession);
    handler.handleEvent(session, event);

    verify(inventoryService).deleteItemFromInventory(gameSaveId, itemRequest.getClientId());
    verify(session).sendMessage(any(TextMessage.class));
  }

  @Test
  void shouldSendAckAfterSuccessfulDelete() throws Exception {
    JsonNode dataNode = objectMapper.valueToTree(itemRequest);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.INVENTORY_ITEM_DELETE, sessionId, messageId, userId, dataNode);

    GameSession gameSession = mock(GameSession.class);
    when(gameSession.getGameSaveId()).thenReturn(gameSaveId);
    when(gameSessionQueryService.findGameSessionById(any())).thenReturn(gameSession);

    handler.handleEvent(session, event);

    ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
    verify(session).sendMessage(messageCaptor.capture());

    TextMessage sentMessage = messageCaptor.getValue();
    JsonNode sentJson = objectMapper.readTree(sentMessage.getPayload());
    assertEquals("ACK", sentJson.get("eventType").asString());
  }

  @Test
  void shouldPropagateExceptionWhenServiceFails() throws Exception {
    JsonNode dataNode = objectMapper.valueToTree(itemRequest);
    WebSocketEvent event =
        new WebSocketEvent(
            WebSocketEventType.INVENTORY_ITEM_DELETE, sessionId, messageId, userId, dataNode);

    GameSession gameSession = mock(GameSession.class);
    when(gameSession.getGameSaveId()).thenReturn(gameSaveId);
    when(gameSessionQueryService.findGameSessionById(any())).thenReturn(gameSession);

    doThrow(new RuntimeException("Service error"))
        .when(inventoryService)
        .deleteItemFromInventory(any(), any());

    assertThrows(RuntimeException.class, () -> handler.handleEvent(session, event));

    verify(session, never()).sendMessage(any());
  }
}
