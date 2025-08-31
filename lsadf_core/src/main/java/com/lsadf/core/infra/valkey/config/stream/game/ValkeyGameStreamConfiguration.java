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

package com.lsadf.core.infra.valkey.config.stream.game;

import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.scheduler.FlushScheduler;
import com.lsadf.core.infra.valkey.cache.flush.scheduler.impl.FlushSchedulerImpl;
import com.lsadf.core.infra.valkey.config.properties.ValkeyGameStreamPersistenceProperties;
import com.lsadf.core.infra.valkey.config.properties.ValkeyGameStreamProperties;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandlerRegistry;
import com.lsadf.core.infra.valkey.stream.consumer.impl.GameStreamConsumer;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.serializer.EventSerializer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Configuration for debounced persistence system components. This creates the necessary beans for
 * stream-driven, debounced cache flushing that uses Redis ZSET for resilient, deterministic
 * persistence.
 */
@Configuration
@ConditionalOnProperty(prefix = "valkey.config", value = "enabled", havingValue = "true")
public class ValkeyGameStreamConfiguration {

  /**
   * Creates a debounced persistence stream consumer that updates ZSET timestamps when game save
   * events are received.
   */
  @Bean
  public GameStreamConsumer debouncedPersistenceConsumer(
      RedisTemplate<String, String> redisTemplate,
      EventSerializer<GameSaveEvent> gameEventSerializer,
      EventHandlerRegistry eventHandlerRegistry,
      ValkeyGameStreamProperties valkeyGameStreamProperties,
      ValkeyGameStreamPersistenceProperties valkeyGameStreamPersistenceProperties) {
    return new GameStreamConsumer(
        "game-stream-consumer",
        valkeyGameStreamProperties.getStreamKey(),
        valkeyGameStreamProperties.getConsumerGroup(),
        redisTemplate,
        gameEventSerializer,
        eventHandlerRegistry,
        valkeyGameStreamPersistenceProperties.getDebounceWindowMs());
  }

  @Bean
  public FlushScheduler flushScheduler(
      RedisTemplate<String, String> redisTemplate,
      CacheFlushService cacheFlushService,
      ScheduledExecutorService flushSchedulerExecutorService,
      ValkeyGameStreamPersistenceProperties valkeyGameStreamPersistenceProperties) {
    return new FlushSchedulerImpl(
        redisTemplate,
        cacheFlushService,
        flushSchedulerExecutorService,
        valkeyGameStreamPersistenceProperties.getCheckIntervalSeconds());
  }

  /**
   * Creates a scheduled executor service for the flush scheduler. Uses a single thread as we only
   * need one scheduler instance.
   */
  @Bean
  public ScheduledExecutorService flushSchedulerExecutorService() {
    return Executors.newSingleThreadScheduledExecutor(
        r -> {
          Thread thread = new Thread(r, "flush-scheduler");
          thread.setDaemon(true);
          return thread;
        });
  }
}
