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
package com.lsadf.application.bdd.step_definition.when;

import static com.lsadf.core.infra.web.JsonAttributes.CLIENT_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.bdd.config.BddFieldConstants;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

/** Step definitions for the when steps in Inventory WebSocket BDD scenarios */
@Slf4j(topic = "[INVENTORY WEBSOCKET WHEN STEP DEFINITIONS]")
public class BddInventoryWebSocketWhenStepDefinitions extends BddLoader {

  @When("^the user sends an inventory item create event through WebSocket$")
  public void whenUserSendsInventoryItemCreateEvent(DataTable dataTable) {
    try {
      var data = dataTable.asMaps(String.class, String.class);
      assertThat(data).hasSize(1);

      var row = data.getFirst();
      ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

      @SuppressWarnings("resource")
      WebSocketSession session = webSocketSessionStack.peek();

      JsonNode node = objectMapper.valueToTree(itemRequest);
      WebSocketEvent event = new WebSocketEvent(WebSocketEventType.INVENTORY_ITEM_CREATE, node);

      String json = objectMapper.writeValueAsString(event);
      session.sendMessage(new TextMessage(json));

      log.info("Sent inventory item create event: {}", json);
    } catch (Exception e) {
      log.error("Failed to send inventory item create event", e);
      exceptionStack.push(e);
    }
  }

  @When("^the user sends an inventory item update event through WebSocket$")
  public void whenUserSendsInventoryItemUpdateEvent(DataTable dataTable) {
    try {
      var data = dataTable.asMaps(String.class, String.class);
      assertThat(data).hasSize(1);

      var row = data.getFirst();
      ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

      @SuppressWarnings("resource")
      WebSocketSession session = webSocketSessionStack.peek();

      JsonNode node = objectMapper.valueToTree(itemRequest);
      WebSocketEvent event = new WebSocketEvent(WebSocketEventType.INVENTORY_ITEM_UPDATE, node);

      String json = objectMapper.writeValueAsString(event);
      session.sendMessage(new TextMessage(json));

      log.info("Sent inventory item update event: {}", json);
    } catch (Exception e) {
      log.error("Failed to send inventory item update event", e);
      exceptionStack.push(e);
    }
  }

  @When("^the user sends an inventory item delete event through WebSocket$")
  public void whenUserSendsInventoryItemDeleteEvent(DataTable dataTable) {
    try {
      var data = dataTable.asMaps(String.class, String.class);
      assertThat(data).hasSize(1);

      var row = data.getFirst();
      String clientId = row.get(BddFieldConstants.Item.CLIENT_ID);

      @SuppressWarnings("resource")
      WebSocketSession session = webSocketSessionStack.peek();

      // Create a simple JSON object with just the clientId
      ObjectNode deletePayload = objectMapper.createObjectNode();
      deletePayload.put(CLIENT_ID, clientId);

      WebSocketEvent event =
          new WebSocketEvent(WebSocketEventType.INVENTORY_ITEM_DELETE, deletePayload);

      String json = objectMapper.writeValueAsString(event);
      session.sendMessage(new TextMessage(json));

      log.info("Sent inventory item delete event: {}", json);
    } catch (Exception e) {
      log.error("Failed to send inventory item delete event", e);
      exceptionStack.push(e);
    }
  }

  @When("^the user sends an inventory item create event through WebSocket with missing fields$")
  public void whenUserSendsInventoryItemCreateEventWithMissingFields(DataTable dataTable) {
    try {
      var data = dataTable.asMaps(String.class, String.class);
      assertThat(data).hasSize(1);

      var row = data.getFirst();

      WebSocketSession session = webSocketSessionStack.peek();

      // Create incomplete payload (missing required fields like clientId, level, etc.)
      Map<String, Object> incompletePayload = new HashMap<>();
      incompletePayload.putAll(row);

      JsonNode node = objectMapper.valueToTree(incompletePayload);
      WebSocketEvent event = new WebSocketEvent(WebSocketEventType.INVENTORY_ITEM_CREATE, node);

      String json = objectMapper.writeValueAsString(event);
      session.sendMessage(new TextMessage(json));

      log.info("Sent inventory item create event with missing fields: {}", json);
    } catch (Exception e) {
      log.error("Failed to send inventory item create event with missing fields", e);
      exceptionStack.push(e);
    }
  }
}
