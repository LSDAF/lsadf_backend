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
package com.lsadf.core.infra.websocket.handler.game;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.game.InventoryItemCreateWebSocketEvent;
import com.lsadf.core.infra.websocket.event.game.InventoryItemDeleteWebSocketEvent;
import com.lsadf.core.infra.websocket.event.game.InventoryItemUpdateWebSocketEvent;
import com.lsadf.core.infra.websocket.event.system.AckWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class InventoryItemCreateWebSocketEventHandler implements WebSocketEventHandler {

  private final InventoryService inventoryService;
  private final ObjectMapper objectMapper;

  @Override
  public void handleEvent(WebSocketSession session, Event event) throws Exception {
    InventoryItemCreateWebSocketEvent invEvent = (InventoryItemCreateWebSocketEvent) event;

    log.info("Creating inventory item for gameSaveId: {}", invEvent.getGameSaveId());

    inventoryService.createItemInInventory(invEvent.getGameSaveId(), invEvent.getPayload());

    sendAck(session, invEvent);
  }

  @Override
  public EventType getEventType() {
    return WebSocketEventType.INVENTORY_ITEM_CREATE;
  }

  private void sendAck(WebSocketSession session, InventoryItemCreateWebSocketEvent event)
      throws Exception {
    AckWebSocketEvent ack =
        new AckWebSocketEvent(
            event.getSessionId(), UUID.randomUUID(), event.getUserId(), event.getMessageId());

    String json = objectMapper.writeValueAsString(ack);
    session.sendMessage(new TextMessage(json));
  }
}

@Slf4j
@RequiredArgsConstructor
class InventoryItemUpdateWebSocketEventHandler implements WebSocketEventHandler {

  private final InventoryService inventoryService;
  private final ObjectMapper objectMapper;

  @Override
  public void handleEvent(WebSocketSession session, Event event) throws Exception {
    InventoryItemUpdateWebSocketEvent invEvent = (InventoryItemUpdateWebSocketEvent) event;

    log.info("Updating inventory item for gameSaveId: {}", invEvent.getGameSaveId());

    inventoryService.updateItemInInventory(
        invEvent.getGameSaveId(), invEvent.getItemClientId(), invEvent.getPayload());

    sendAck(session, invEvent);
  }

  @Override
  public EventType getEventType() {
    return WebSocketEventType.INVENTORY_ITEM_UPDATE;
  }

  private void sendAck(WebSocketSession session, InventoryItemUpdateWebSocketEvent event)
      throws Exception {
    AckWebSocketEvent ack =
        new AckWebSocketEvent(
            event.getSessionId(), UUID.randomUUID(), event.getUserId(), event.getMessageId());

    String json = objectMapper.writeValueAsString(ack);
    session.sendMessage(new TextMessage(json));
  }
}

@Slf4j
@RequiredArgsConstructor
class InventoryItemDeleteWebSocketEventHandler implements WebSocketEventHandler {

  private final InventoryService inventoryService;
  private final ObjectMapper objectMapper;

  @Override
  public void handleEvent(WebSocketSession session, Event event) throws Exception {
    InventoryItemDeleteWebSocketEvent invEvent = (InventoryItemDeleteWebSocketEvent) event;

    log.info("Deleting inventory item for gameSaveId: {}", invEvent.getGameSaveId());

    inventoryService.deleteItemFromInventory(invEvent.getGameSaveId(), invEvent.getItemClientId());

    sendAck(session, invEvent);
  }

  @Override
  public EventType getEventType() {
    return WebSocketEventType.INVENTORY_ITEM_DELETE;
  }

  private void sendAck(WebSocketSession session, InventoryItemDeleteWebSocketEvent event)
      throws Exception {
    AckWebSocketEvent ack =
        new AckWebSocketEvent(
            event.getSessionId(), UUID.randomUUID(), event.getUserId(), event.getMessageId());

    String json = objectMapper.writeValueAsString(ack);
    session.sendMessage(new TextMessage(json));
  }
}
