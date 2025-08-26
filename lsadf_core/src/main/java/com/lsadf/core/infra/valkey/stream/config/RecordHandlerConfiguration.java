/*
 * Copyright © 2024-2025 LSDAF
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

package com.lsadf.core.infra.valkey.stream.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandlerRegistry;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.CharacteristicsUpdateEventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.EventHandlerRegistryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecordHandlerConfiguration {
  @Bean
  public EventHandlerRegistry recordHandlerRegistry() {
    return new EventHandlerRegistryImpl();
  }

  @Bean
  public EventHandler characteristicsUpdateEventHandler(
      ObjectMapper objectMapper,
      CharacteristicsService characteristicsService,
      EventHandlerRegistry eventHandlerRegistry) {
    EventHandler characteristicsUpdateEventHandler =
        new CharacteristicsUpdateEventHandler(characteristicsService, objectMapper);
    eventHandlerRegistry.registerHandler(
        characteristicsUpdateEventHandler.getEventType(), characteristicsUpdateEventHandler);
    return characteristicsUpdateEventHandler;
  }
}
