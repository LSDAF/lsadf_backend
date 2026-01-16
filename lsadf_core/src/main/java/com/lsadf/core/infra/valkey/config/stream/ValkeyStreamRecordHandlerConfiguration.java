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
package com.lsadf.core.infra.valkey.config.stream;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandlerRegistry;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.CharacteristicsUpdateEventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.CurrencyUpdateEventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.EventHandlerRegistryImpl;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.StageUpdateEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class ValkeyStreamRecordHandlerConfiguration {
  @Bean
  public EventHandlerRegistry recordHandlerRegistry() {
    return new EventHandlerRegistryImpl();
  }

  @Bean
  public EventHandler characteristicsUpdateEventHandler(
      ObjectMapper objectMapper,
      CharacteristicsCommandService characteristicsService,
      EventHandlerRegistry eventHandlerRegistry) {
    EventHandler characteristicsUpdateEventHandler =
        new CharacteristicsUpdateEventHandler(characteristicsService, objectMapper);
    eventHandlerRegistry.registerHandler(
        characteristicsUpdateEventHandler.getEventType(), characteristicsUpdateEventHandler);
    return characteristicsUpdateEventHandler;
  }

  @Bean
  public EventHandler stageUpdateEventHandler(
      ObjectMapper objectMapper,
      StageCommandService stageService,
      EventHandlerRegistry eventHandlerRegistry) {
    EventHandler stageUpdateEventHandler = new StageUpdateEventHandler(stageService, objectMapper);
    eventHandlerRegistry.registerHandler(
        stageUpdateEventHandler.getEventType(), stageUpdateEventHandler);
    return stageUpdateEventHandler;
  }

  @Bean
  public EventHandler currencyUpdateEventHandler(
      ObjectMapper objectMapper,
      CurrencyCommandService currencyService,
      EventHandlerRegistry eventHandlerRegistry) {
    EventHandler currencyUpdateEventHandler =
        new CurrencyUpdateEventHandler(currencyService, objectMapper);
    eventHandlerRegistry.registerHandler(
        currencyUpdateEventHandler.getEventType(), currencyUpdateEventHandler);
    return currencyUpdateEventHandler;
  }
}
