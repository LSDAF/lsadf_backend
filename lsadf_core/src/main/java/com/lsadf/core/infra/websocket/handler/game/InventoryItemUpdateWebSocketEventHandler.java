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
import com.lsadf.core.application.game.inventory.ItemCommand;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.domain.game.inventory.ItemStatistic;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.system.AckWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.EventType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class InventoryItemUpdateWebSocketEventHandler implements WebSocketEventHandler {

  private final InventoryService inventoryService;
  private final ObjectMapper objectMapper;
  private final GameSessionQueryService gameSessionQueryService;

  @Override
  public void handleEvent(WebSocketSession session, WebSocketEvent event) throws Exception {
    JsonNode jsonNode = event.getData();
    var gameSession = gameSessionQueryService.findGameSessionById(event.getSessionId());
    var gameSaveId = gameSession.getGameSaveId();
    var itemClientId = jsonNode.get("clientId").asString();
    var type = jsonNode.get("type").asString();
    var blueprintId = jsonNode.get("blueprintId").asString();
    var rarity = jsonNode.get("rarity").asString();
    var isEquipped = jsonNode.get("isEquipped").asBoolean();
    var level = jsonNode.get("level").asInt();
    var mainStatistic = jsonNode.get("mainStat").get("statistic");
    var mainBaseValue = jsonNode.get("mainStat").get("baseValue").asFloat();
    List<ItemStatDto> additionalStatList = new ArrayList<>();
    jsonNode
        .get("additionalStats")
        .forEach(
            node -> {
              var statistic = node.get("statistic").asString();
              var statisticEnum = ItemStatistic.fromString(statistic);
              var baseValue = node.get("baseValue").asFloat();
              additionalStatList.add(new ItemStatDto(statisticEnum, baseValue));
            });

    ItemCommand itemCommand =
        new ItemRequest(
            itemClientId,
            type,
            blueprintId,
            rarity,
            isEquipped,
            level,
            new ItemStatDto(ItemStatistic.fromString(mainStatistic.asString()), mainBaseValue),
            additionalStatList);

    log.info("Updating inventory item for gameSaveId: {}", gameSaveId);

    inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemCommand);

    sendAck(session, event);
  }

  @Override
  public EventType getEventType() {
    return WebSocketEventType.INVENTORY_ITEM_UPDATE;
  }

  private void sendAck(WebSocketSession session, WebSocketEvent event) throws Exception {
    AckWebSocketEvent ack =
        new AckWebSocketEvent(
            event.getSessionId(),
            UUID.randomUUID(),
            event.getUserId(),
            event.getMessageId(),
            objectMapper.valueToTree(event));

    String json = objectMapper.writeValueAsString(ack);
    session.sendMessage(new TextMessage(json));
  }
}
