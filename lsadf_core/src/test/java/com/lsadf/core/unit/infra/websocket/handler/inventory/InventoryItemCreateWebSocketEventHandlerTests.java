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

import static com.lsadf.core.domain.game.inventory.ItemType.SWORD;
import static com.lsadf.core.infra.web.JsonAttributes.*;
import static com.lsadf.core.infra.websocket.event.WebSocketEventType.INVENTORY_ITEM_CREATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.domain.game.inventory.*;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.websocket.event.EventRequestValidator;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.handler.game.inventory.InventoryItemCreateWebSocketEventHandler;
import java.util.ArrayList;
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
class InventoryItemCreateWebSocketEventHandlerTests {

  @Mock private InventoryService inventoryService;

  private final ObjectMapper objectMapper =
      JsonMapper.builder()
          .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy())
          .build();

  @Mock private WebSocketSession session;

  @Spy private WebSocketEventFactory eventFactory;

  @Mock private EventRequestValidator requestValidator;

  private Map<String, Object> sessionAttributes;

  private InventoryItemCreateWebSocketEventHandler eventHandler;

  @BeforeEach
  void setUp() {
    eventHandler =
        new InventoryItemCreateWebSocketEventHandler(
            inventoryService, objectMapper, eventFactory, requestValidator);
  }

  @Test
  void shouldReturnCorrectEventType() {
    var eventType = eventHandler.getEventType();
    assertThat(eventType).isEqualTo(INVENTORY_ITEM_CREATE);
  }

  @Test
  void shouldHandleInventoryItemCreateEvent() throws Exception {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    ItemStatDto mainStat = new ItemStatDto(ItemStatistic.ATTACK_ADD, 100.0f);
    ItemRequest request =
        ItemRequest.builder()
            .clientId("test-client-id")
            .type(SWORD.getType())
            .blueprintId("blueprint-123")
            .rarity(ItemRarity.LEGENDARY.getRarity())
            .isEquipped(false)
            .level(10)
            .mainStat(mainStat)
            .additionalStats(new ArrayList<>())
            .build();

    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_CREATE, UUID.randomUUID(), node);

    Item mockItem =
        Item.builder()
            .id(UUID.randomUUID())
            .gameSaveId(gameSaveId)
            .clientId("test-client-id")
            .blueprintId("blueprint-123")
            .itemType(SWORD)
            .itemRarity(ItemRarity.LEGENDARY)
            .isEquipped(false)
            .level(10)
            .mainStat(new ItemStat(ItemStatistic.ATTACK_ADD, 100.0f))
            .additionalStats(new ArrayList<>())
            .build();

    when(inventoryService.createItemInInventory(eq(gameSaveId), any(ItemRequest.class)))
        .thenReturn(mockItem);

    // when
    eventHandler.handleEvent(session, event);

    // then
    ArgumentCaptor<ItemRequest> requestCaptor = ArgumentCaptor.forClass(ItemRequest.class);
    verify(inventoryService).createItemInInventory(eq(gameSaveId), requestCaptor.capture());
    verify(requestValidator).validate(requestCaptor.capture());

    ItemRequest capturedRequest = requestCaptor.getValue();
    assertThat(capturedRequest.getClientId()).isEqualTo("test-client-id");
    assertThat(capturedRequest.getType()).isEqualTo(SWORD.getType());
    assertThat(capturedRequest.getBlueprintId()).isEqualTo("blueprint-123");
    assertThat(capturedRequest.getRarity()).isEqualTo(ItemRarity.LEGENDARY.getRarity());
    assertThat(capturedRequest.getIsEquipped()).isFalse();
    assertThat(capturedRequest.getLevel()).isEqualTo(10);

    verify(session).sendMessage(any(TextMessage.class));
  }

  @Test
  void shouldSendAckAfterSuccessfulCreation() throws Exception {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    ItemStatDto mainStat = new ItemStatDto(ItemStatistic.ATTACK_ADD, 100.0f);
    ItemRequest request =
        ItemRequest.builder()
            .clientId("test-client-id")
            .type(SWORD.getType())
            .blueprintId("blueprint-123")
            .rarity(ItemRarity.LEGENDARY.getRarity())
            .isEquipped(false)
            .level(10)
            .mainStat(mainStat)
            .additionalStats(new ArrayList<>())
            .build();

    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_CREATE, UUID.randomUUID(), node);

    Item mockItem =
        Item.builder()
            .id(UUID.randomUUID())
            .gameSaveId(gameSaveId)
            .clientId("test-client-id")
            .blueprintId("blueprint-123")
            .itemType(SWORD)
            .itemRarity(ItemRarity.LEGENDARY)
            .isEquipped(false)
            .level(10)
            .mainStat(new ItemStat(ItemStatistic.ATTACK_ADD, 100.0f))
            .additionalStats(new ArrayList<>())
            .build();

    when(inventoryService.createItemInInventory(eq(gameSaveId), any(ItemRequest.class)))
        .thenReturn(mockItem);

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

    ItemStatDto mainStat = new ItemStatDto(ItemStatistic.ATTACK_ADD, -100.0f);
    ItemRequest request =
        ItemRequest.builder()
            .clientId("test-client-id")
            .type(SWORD.getType())
            .blueprintId("blueprint-123")
            .rarity(ItemRarity.LEGENDARY.getRarity())
            .isEquipped(false)
            .level(10)
            .mainStat(mainStat)
            .additionalStats(new ArrayList<>())
            .build();

    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_CREATE, UUID.randomUUID(), node);

    doThrow(new IllegalArgumentException("Validation failed"))
        .when(requestValidator)
        .validate(request);

    // when / then
    Exception exception =
        assertThrows(Exception.class, () -> eventHandler.handleEvent(session, event));
    assertTrue(exception.getMessage().contains("Validation failed"));
  }

  @Test
  void shouldThrowErrorWhenEmptyRequest() {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    JsonNode node = objectMapper.readTree("{}");
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_CREATE, UUID.randomUUID(), node);

    // when / then
    Exception exception =
        assertThrows(Exception.class, () -> eventHandler.handleEvent(session, event));
    assertTrue(exception.getMessage().contains("Missing required item fields"));
  }
}
