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

package com.lsadf.core.unit.infra.websocket.handler.inventory;

import static com.lsadf.core.infra.web.JsonAttributes.*;
import static com.lsadf.core.infra.websocket.event.WebSocketEventType.INVENTORY_ITEM_DELETE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.handler.game.inventory.InventoryItemDeleteWebSocketEventHandler;
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
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
class InventoryItemDeleteWebSocketEventHandlerTests {

  @Mock private InventoryService inventoryService;

  private final ObjectMapper objectMapper =
      JsonMapper.builder()
          .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy())
          .build();

  @Mock private WebSocketSession session;

  @Spy private WebSocketEventFactory eventFactory;

  private Map<String, Object> sessionAttributes;

  private InventoryItemDeleteWebSocketEventHandler eventHandler;

  @BeforeEach
  void setUp() {
    eventHandler =
        new InventoryItemDeleteWebSocketEventHandler(inventoryService, objectMapper, eventFactory);
  }

  @Test
  void shouldReturnCorrectEventType() {
    var eventType = eventHandler.getEventType();
    assertThat(eventType).isEqualTo(INVENTORY_ITEM_DELETE);
  }

  @Test
  void shouldHandleInventoryItemDeleteEvent() throws Exception {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    String testClientId = "test-client-id-123";
    String jsonPayload = String.format("{\"client_id\":\"%s\"}", testClientId);
    JsonNode node = objectMapper.readTree(jsonPayload);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_DELETE, UUID.randomUUID(), node);

    // when
    eventHandler.handleEvent(session, event);

    // then
    verify(inventoryService).deleteItemFromInventory(gameSaveId, testClientId);
    verify(session).sendMessage(any(TextMessage.class));
  }

  @Test
  void shouldSendAckAfterSuccessfulDeletion() throws Exception {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    String testClientId = "test-client-id-456";
    String jsonPayload = String.format("{\"client_id\":\"%s\"}", testClientId);
    JsonNode node = objectMapper.readTree(jsonPayload);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_DELETE, UUID.randomUUID(), node);

    // when
    eventHandler.handleEvent(session, event);

    // then
    ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
    verify(session).sendMessage(messageCaptor.capture());

    TextMessage sentMessage = messageCaptor.getValue();
    JsonNode sentNode = objectMapper.readTree(sentMessage.getPayload());
    assertEquals("ACK", sentNode.get(EVENT_TYPE).asString());
  }

  @Test
  void shouldThrowExceptionForInvalidRequest() {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    String jsonPayload = "{\"client_id\":\"\"}"; // Empty clientId
    JsonNode node = objectMapper.readTree(jsonPayload);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_DELETE, UUID.randomUUID(), node);

    // when / then
    Exception exception =
        assertThrows(Exception.class, () -> eventHandler.handleEvent(session, event));
    assertTrue(exception.getMessage().contains("Missing required clientId field"));
  }

  @Test
  void shouldThrowErrorWhenEmptyRequest() {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    JsonNode node = objectMapper.readTree("{}");
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_DELETE, UUID.randomUUID(), node);

    // when / then
    Exception exception =
        assertThrows(Exception.class, () -> eventHandler.handleEvent(session, event));
    assertTrue(exception.getMessage().contains("Missing required clientId field"));
  }
}
