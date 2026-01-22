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

import static com.lsadf.core.infra.web.JsonAttributes.*;

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
public class InventoryItemCreateWebSocketEventHandler implements WebSocketEventHandler {

  private final InventoryService inventoryService;
  private final ObjectMapper objectMapper;
  private final GameSessionQueryService gameSessionQueryService;

  @Override
  public void handleEvent(WebSocketSession session, WebSocketEvent event) throws Exception {
    JsonNode jsonNode = event.getData();
    var gameSession = gameSessionQueryService.findGameSessionById(event.getSessionId());
    var gameSaveId = gameSession.getGameSaveId();
    var itemClientId = jsonNode.get(CLIENT_ID_CAMEL_CASE).asString();
    var type = jsonNode.get(TYPE).asString();
    var blueprintId = jsonNode.get(BLUEPRINT_ID_CAMEL_CASE).asString();
    var rarity = jsonNode.get(RARITY).asString();
    var isEquipped = jsonNode.get(IS_EQUIPPED_CAMEL_CASE).asBoolean();
    var level = jsonNode.get(LEVEL).asInt();
    var mainStatistic = jsonNode.get(MAIN_STAT_CAMEL_CASE).get(STATISTIC);
    var mainBaseValue = jsonNode.get(MAIN_STAT_CAMEL_CASE).get(BASE_VALUE_CAMEL_CASE).asFloat();
    List<ItemStatDto> additionalStatList = new ArrayList<>();
    jsonNode
        .get(ADDITIONAL_STATS_CAMEL_CASE)
        .forEach(
            node -> {
              var statistic = node.get(STATISTIC).asString();
              var statisticEnum = ItemStatistic.fromString(statistic);
              var baseValue = node.get(BASE_VALUE_CAMEL_CASE).asFloat();
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

    inventoryService.createItemInInventory(gameSaveId, itemCommand);

    sendAck(session, event);
  }

  @Override
  public EventType getEventType() {
    return WebSocketEventType.INVENTORY_ITEM_CREATE;
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
