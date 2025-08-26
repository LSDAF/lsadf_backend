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
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.application.game.save.currency.CurrencyEventPublisherPort;
import com.lsadf.core.application.game.save.stage.StageEventPublisherPort;
import com.lsadf.core.infra.valkey.stream.adapter.CharacteristicsEventPublisherAdapter;
import com.lsadf.core.infra.valkey.stream.adapter.CurrencyEventPublisherAdapter;
import com.lsadf.core.infra.valkey.stream.adapter.StageEventPublisherAdapter;
import com.lsadf.core.infra.valkey.stream.config.properties.ValkeyStreamProperties;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.producer.StreamProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValkeyStreamAdapterConfiguration {
  @Bean
  public CharacteristicsEventPublisherPort characteristicsEventPublisherPort(
      StreamProducer<GameSaveEvent> streamProducer,
      ValkeyStreamProperties valkeyStreamProperties,
      ObjectMapper objectMapper) {
    return new CharacteristicsEventPublisherAdapter(
        streamProducer, valkeyStreamProperties.getGameStreamKey(), objectMapper);
  }

  @Bean
  public StageEventPublisherPort stageEventPublisherPort(
      StreamProducer<GameSaveEvent> streamProducer,
      ValkeyStreamProperties valkeyStreamProperties,
      ObjectMapper objectMapper) {
    return new StageEventPublisherAdapter(
        valkeyStreamProperties.getGameStreamKey(), streamProducer, objectMapper);
  }

  @Bean
  public CurrencyEventPublisherPort currencyEventPublisherPort(
      StreamProducer<GameSaveEvent> streamProducer,
      ValkeyStreamProperties valkeyStreamProperties,
      ObjectMapper objectMapper) {
    return new CurrencyEventPublisherAdapter(
        streamProducer, valkeyStreamProperties.getGameStreamKey(), objectMapper);
  }
}
