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

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.CurrencyEventPublisherPort;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.StageEventPublisherPort;
import com.lsadf.core.infra.websocket.event.EventRequestValidator;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandlerRegistry;
import com.lsadf.core.infra.websocket.handler.game.inventory.InventoryItemCreateWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.inventory.InventoryItemDeleteWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.inventory.InventoryItemUpdateWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.save.CharacteristicsWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.save.CurrencyWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.save.StageWebSocketEventHandler;
import jakarta.validation.Validator;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class WebSocketHandlerConfiguration {

  @Bean
  public GameWebSocketHandler gameWebSocketHandler(
      WebSocketEventHandlerRegistry webSocketEventHandlerRegistry,
      ObjectMapper objectMapper,
      WebSocketEventFactory webSocketEventFactory) {
    return new GameWebSocketHandler(
        webSocketEventHandlerRegistry, objectMapper, webSocketEventFactory);
  }

  @Bean
  public WebSocketEventHandlerRegistry webSocketEventHandlerRegistry(
      List<WebSocketEventHandler> webSocketEventHandlers) {
    return new WebSocketEventHandlerRegistry(webSocketEventHandlers);
  }

  @Bean
  public EventRequestValidator eventRequestValidator(Validator validator) {
    return new EventRequestValidator(validator);
  }

  @Bean
  public CurrencyWebSocketEventHandler currencyWebSocketEventHandler(
      CurrencyCommandService currencyCommandService,
      ObjectMapper objectMapper,
      CacheManager cacheManager,
      WebSocketEventFactory webSocketEventFactory,
      EventRequestValidator requestValidator,
      CurrencyEventPublisherPort currencyEventPublisherPort) {
    return new CurrencyWebSocketEventHandler(
        currencyCommandService,
        objectMapper,
        webSocketEventFactory,
        cacheManager,
        currencyEventPublisherPort,
        requestValidator);
  }

  @Bean
  public CharacteristicsWebSocketEventHandler characteristicsWebSocketEventHandler(
      CharacteristicsCommandService characteristicsCommandService,
      ObjectMapper objectMapper,
      CacheManager cacheManager,
      WebSocketEventFactory webSocketEventFactory,
      EventRequestValidator requestValidator,
      CharacteristicsEventPublisherPort characteristicsEventPublisherPort) {
    return new CharacteristicsWebSocketEventHandler(
        characteristicsCommandService,
        objectMapper,
        webSocketEventFactory,
        cacheManager,
        characteristicsEventPublisherPort,
        requestValidator);
  }

  @Bean
  public StageWebSocketEventHandler stageWebSocketEventHandler(
      StageCommandService stageCommandService,
      ObjectMapper objectMapper,
      CacheManager cacheManager,
      WebSocketEventFactory webSocketEventFactory,
      EventRequestValidator requestValidator,
      StageEventPublisherPort stageEventPublisherPort) {
    return new StageWebSocketEventHandler(
        stageCommandService,
        objectMapper,
        webSocketEventFactory,
        cacheManager,
        stageEventPublisherPort,
        requestValidator);
  }

  @Bean
  public InventoryItemCreateWebSocketEventHandler inventoryItemCreateWebSocketEventHandler(
      InventoryService inventoryService,
      ObjectMapper objectMapper,
      WebSocketEventFactory eventFactory,
      EventRequestValidator requestValidator) {
    return new InventoryItemCreateWebSocketEventHandler(
        inventoryService, objectMapper, eventFactory, requestValidator);
  }

  @Bean
  public InventoryItemDeleteWebSocketEventHandler inventoryItemDeleteWebSocketEventHandler(
      InventoryService inventoryService,
      ObjectMapper objectMapper,
      WebSocketEventFactory eventFactory) {
    return new InventoryItemDeleteWebSocketEventHandler(
        inventoryService, objectMapper, eventFactory);
  }

  @Bean
  public InventoryItemUpdateWebSocketEventHandler inventoryItemUpdateWebSocketEventHandler(
      InventoryService inventoryService,
      ObjectMapper objectMapper,
      WebSocketEventFactory eventFactory,
      EventRequestValidator requestValidator) {
    return new InventoryItemUpdateWebSocketEventHandler(
        inventoryService, objectMapper, eventFactory, requestValidator);
  }

  @Bean
  public WebSocketEventFactory webSocketEventFactory() {
    return new WebSocketEventFactory();
  }
}
