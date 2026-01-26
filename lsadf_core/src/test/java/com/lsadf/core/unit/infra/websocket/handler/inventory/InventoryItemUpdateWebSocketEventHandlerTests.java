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
import static com.lsadf.core.infra.websocket.event.WebSocketEventType.INVENTORY_ITEM_UPDATE;
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
import com.lsadf.core.infra.websocket.handler.game.inventory.InventoryItemUpdateWebSocketEventHandler;
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
class InventoryItemUpdateWebSocketEventHandlerTests {

  @Mock private InventoryService inventoryService;

  private final ObjectMapper objectMapper =
      JsonMapper.builder()
          .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy())
          .build();

  @Mock private WebSocketSession session;

  @Spy private WebSocketEventFactory eventFactory;

  @Mock private EventRequestValidator requestValidator;

  private Map<String, Object> sessionAttributes;

  private InventoryItemUpdateWebSocketEventHandler eventHandler;

  @BeforeEach
  void setUp() {
    eventHandler =
        new InventoryItemUpdateWebSocketEventHandler(
            inventoryService, objectMapper, eventFactory, requestValidator);
  }

  @Test
  void shouldReturnCorrectEventType() {
    var eventType = eventHandler.getEventType();
    assertThat(eventType).isEqualTo(INVENTORY_ITEM_UPDATE);
  }

  @Test
  void shouldHandleInventoryItemUpdateEvent() throws Exception {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    ItemStatDto mainStat = new ItemStatDto(ItemStatistic.ATTACK_ADD, 150.0f);
    ItemRequest request =
        ItemRequest.builder()
            .clientId("test-client-id-update")
            .type(SWORD.getType())
            .blueprintId("blueprint-456")
            .rarity(ItemRarity.EPIC.getRarity())
            .isEquipped(true)
            .level(15)
            .mainStat(mainStat)
            .additionalStats(new ArrayList<>())
            .build();

    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_UPDATE, UUID.randomUUID(), node);

    Item mockItem =
        Item.builder()
            .id(UUID.randomUUID())
            .gameSaveId(gameSaveId)
            .clientId("test-client-id-update")
            .blueprintId("blueprint-456")
            .itemType(SWORD)
            .itemRarity(ItemRarity.EPIC)
            .isEquipped(true)
            .level(15)
            .mainStat(new ItemStat(ItemStatistic.ATTACK_ADD, 150.0f))
            .additionalStats(new ArrayList<>())
            .build();

    when(inventoryService.updateItemInInventory(
            eq(gameSaveId), eq("test-client-id-update"), any(ItemRequest.class)))
        .thenReturn(mockItem);

    // when
    eventHandler.handleEvent(session, event);

    // then
    ArgumentCaptor<ItemRequest> requestCaptor = ArgumentCaptor.forClass(ItemRequest.class);
    verify(inventoryService)
        .updateItemInInventory(
            eq(gameSaveId), eq("test-client-id-update"), requestCaptor.capture());
    verify(requestValidator).validate(requestCaptor.getValue());

    ItemRequest capturedRequest = requestCaptor.getValue();
    assertThat(capturedRequest.getClientId()).isEqualTo("test-client-id-update");
    assertThat(capturedRequest.getType()).isEqualTo(SWORD.getType());
    assertThat(capturedRequest.getBlueprintId()).isEqualTo("blueprint-456");
    assertThat(capturedRequest.getRarity()).isEqualTo(ItemRarity.EPIC.getRarity());
    assertThat(capturedRequest.getIsEquipped()).isTrue();
    assertThat(capturedRequest.getLevel()).isEqualTo(15);

    verify(session).sendMessage(any(TextMessage.class));
  }

  @Test
  void shouldSendAckAfterSuccessfulUpdate() throws Exception {
    // given
    UUID gameSaveId = UUID.randomUUID();
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SAVE_ID, gameSaveId);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    ItemStatDto mainStat = new ItemStatDto(ItemStatistic.HEALTH_ADD, 80.0f);
    ItemRequest request =
        ItemRequest.builder()
            .clientId("test-client-id-ack")
            .type(SWORD.getType())
            .blueprintId("blueprint-789")
            .rarity(ItemRarity.RARE.getRarity())
            .isEquipped(false)
            .level(20)
            .mainStat(mainStat)
            .additionalStats(new ArrayList<>())
            .build();

    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_UPDATE, UUID.randomUUID(), node);

    Item mockItem =
        Item.builder()
            .id(UUID.randomUUID())
            .gameSaveId(gameSaveId)
            .clientId("test-client-id-ack")
            .blueprintId("blueprint-789")
            .itemType(SWORD)
            .itemRarity(ItemRarity.RARE)
            .isEquipped(false)
            .level(20)
            .mainStat(new ItemStat(ItemStatistic.HEALTH_ADD, 80.0f))
            .additionalStats(new ArrayList<>())
            .build();

    when(inventoryService.updateItemInInventory(
            eq(gameSaveId), eq("test-client-id-ack"), any(ItemRequest.class)))
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

    ItemStatDto mainStat =
        new ItemStatDto(ItemStatistic.ATTACK_ADD, -100.0f); // Invalid negative value
    ItemRequest request =
        ItemRequest.builder()
            .clientId("test-client-id-invalid")
            .type(SWORD.getType())
            .blueprintId("blueprint-invalid")
            .rarity(ItemRarity.NORMAL.getRarity())
            .isEquipped(false)
            .level(5)
            .mainStat(mainStat)
            .additionalStats(new ArrayList<>())
            .build();

    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_UPDATE, UUID.randomUUID(), node);

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
    WebSocketEvent event = new WebSocketEvent(INVENTORY_ITEM_UPDATE, UUID.randomUUID(), node);

    // when / then
    Exception exception =
        assertThrows(Exception.class, () -> eventHandler.handleEvent(session, event));
    assertTrue(exception.getMessage().contains("Missing required item fields"));
  }
}
