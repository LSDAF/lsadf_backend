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

package com.lsadf.core.infra.websocket.handler.game.inventory;

import static com.lsadf.core.infra.web.JsonAttributes.*;
import static com.lsadf.core.infra.websocket.event.WebSocketEventType.INVENTORY_ITEM_DELETE;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.event.system.AckWebSocketEvent;
import com.lsadf.core.infra.websocket.event.system.ErrorWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.EventType;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class InventoryItemDeleteWebSocketEventHandler implements WebSocketEventHandler {

  private final InventoryService inventoryService;
  private final ObjectMapper objectMapper;
  private final WebSocketEventFactory eventFactory;

  @Override
  public void handleEvent(WebSocketSession session, WebSocketEvent event) throws Exception {
    Map<String, Object> attributes = session.getAttributes();
    UUID gameSaveId = (UUID) attributes.get(GAME_SAVE_ID);

    try {
      JsonNode data = event.getData();
      if (data == null || data.isEmpty()) {
        throw new IllegalArgumentException("Missing required clientId field");
      }

      // Extract clientId from the payload
      JsonNode clientIdNode = data.get(CLIENT_ID);
      if (clientIdNode == null || clientIdNode.asString().isEmpty()) {
        throw new IllegalArgumentException("Missing required clientId field");
      }
      String clientId = clientIdNode.asString();

      log.info("Handling inventory item delete for clientId: {}", clientId);
      inventoryService.deleteItemFromInventory(gameSaveId, clientId);
      log.info("Item deleted successfully with clientId: {}", clientId);

      sendAck(session, event);
    } catch (Exception e) {
      log.error("Error processing inventory item delete", e);
      ErrorWebSocketEvent errorEvent =
          eventFactory.createErrorEvent(
              event, "Error while deleting inventory item: " + e.getMessage());
      TextMessage textMessage = new TextMessage(objectMapper.writeValueAsBytes(errorEvent));
      session.sendMessage(textMessage);
      throw e;
    }
  }

  @Override
  public EventType getEventType() {
    return INVENTORY_ITEM_DELETE;
  }

  private void sendAck(WebSocketSession session, WebSocketEvent event) throws Exception {
    AckWebSocketEvent ackEvent = eventFactory.createAckEvent(event);
    String ackPayload = objectMapper.writeValueAsString(ackEvent);
    session.sendMessage(new TextMessage(ackPayload));

    log.info(
        "Sent ACK for eventType: {} with eventId: {}", event.getEventType(), event.getMessageId());
  }
}
