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

import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.producer.StreamProducer;
import com.lsadf.core.infra.valkey.stream.producer.impl.ValkeyStreamProducer;
import com.lsadf.core.infra.valkey.stream.serializer.EventSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ValkeyStreamProducerConfiguration {
  @Bean
  public StreamProducer<GameSaveEvent> gameSaveEventProducer(
      RedisTemplate<String, String> redisTemplate, EventSerializer<GameSaveEvent> eventSerializer) {
    return new ValkeyStreamProducer<>(redisTemplate, eventSerializer);
  }
}
