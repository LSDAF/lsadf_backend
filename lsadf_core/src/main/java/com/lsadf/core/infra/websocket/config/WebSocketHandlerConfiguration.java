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

package com.lsadf.core.infra.websocket.config;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.*;
import com.lsadf.core.infra.websocket.handler.impl.WebSocketEventHandlerRegistry;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class WebSocketHandlerConfiguration {
  @Bean
  public GameWebSocketHandler gameWebSocketHandler(
      WebSocketEventHandlerRegistry webSocketEventHandlerRegistry, ObjectMapper objectMapper) {
    return new GameWebSocketHandler(webSocketEventHandlerRegistry, objectMapper);
  }

  @Bean
  public WebSocketEventHandlerRegistry webSocketEventHandlerRegistry(
      List<WebSocketEventHandler> webSocketEventHandlers) {
    return new WebSocketEventHandlerRegistry(webSocketEventHandlers);
  }

  @Bean
  public CharacteristicsWebSocketEventHandler characteristicsWebSocketEventHandler(
      CharacteristicsCommandService characteristicsCommandService, ObjectMapper objectMapper) {
    return new CharacteristicsWebSocketEventHandler(characteristicsCommandService, objectMapper);
  }

  @Bean
  public CurrencyWebSocketEventHandler currencyWebSocketEventHandler(
      CurrencyCommandService currencyCommandService, ObjectMapper objectMapper) {
    return new CurrencyWebSocketEventHandler(currencyCommandService, objectMapper);
  }

  @Bean
  public GameSaveWebSocketEventHandler gameSaveWebSocketEventHandler(
      GameSaveService gameSaveService, ObjectMapper objectMapper) {
    return new GameSaveWebSocketEventHandler(gameSaveService, objectMapper);
  }

  @Bean
  public InventoryItemCreateWebSocketEventHandler inventoryItemCreateWebSocketEventHandler(
      InventoryService inventoryService, ObjectMapper objectMapper) {
    return new InventoryItemCreateWebSocketEventHandler(inventoryService, objectMapper);
  }

  @Bean
  public StageWebSocketEventHandler stageWebSocketEventHandler(
      StageCommandService stageCommandService, ObjectMapper objectMapper) {
    return new StageWebSocketEventHandler(stageCommandService, objectMapper);
  }
}
