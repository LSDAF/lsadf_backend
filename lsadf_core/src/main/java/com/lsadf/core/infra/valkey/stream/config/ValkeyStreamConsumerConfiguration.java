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

import com.lsadf.core.infra.valkey.stream.config.properties.ValkeyStreamProperties;
import com.lsadf.core.infra.valkey.stream.consumer.GameStreamConsumer;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandlerRegistry;
import com.lsadf.core.infra.valkey.stream.consumer.impl.ValkeyGameStreamConsumer;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.serializer.EventSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

@Configuration
@Slf4j
@Import(RecordHandlerConfiguration.class)
public class ValkeyStreamConsumerConfiguration {

  @Bean
  public GameStreamConsumer gameConsumer(
      EventHandlerRegistry registry, EventSerializer<GameSaveEvent> eventSerializer) {
    return new ValkeyGameStreamConsumer(
        "game_stream_consumer-1", "game_stream", "game_consumer_group", registry, eventSerializer);
  }

  @Bean
  public Subscription gameConsumerSubscription(
      ValkeyStreamProperties valkeyStreamProperties,
      StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer,
      GameStreamConsumer dataConsumer,
      RedisTemplate<String, String> redisTemplate) {

    String streamKey = valkeyStreamProperties.getGameStreamKey();
    String groupName = valkeyStreamProperties.getGameStreamConsumerGroup();
    String consumerName = dataConsumer.getId();

    try {
      redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.from("$"), groupName);
      log.info("Consumer group '{}' created for stream '{}'", groupName, streamKey);
    } catch (RedisSystemException e) {
      if (e.getRootCause() != null
          && e.getRootCause().getMessage() != null
          && e.getRootCause().getMessage().contains("BUSYGROUP")) {
        log.warn("Consumer group '{}' already exists for stream '{}'", groupName, streamKey);
      } else {
        log.error(
            "Error creating consumer group '{}' for stream '{}': {}",
            groupName,
            streamKey,
            e.getMessage());
      }
    } catch (Exception e) {
      log.error(
          "Unexpected error creating consumer group '{}' for stream '{}': {}",
          groupName,
          streamKey,
          e.getMessage(),
          e);
    }

    Subscription subscription =
        listenerContainer.receiveAutoAck(
            Consumer.from(groupName, consumerName),
            StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
            dataConsumer::handleEvent);

    log.info(
        "Subscription created for consumer '{}' on group '{}', stream '{}'",
        consumerName,
        groupName,
        streamKey);

    listenerContainer.start();
    log.info("StreamMessageListenerContainer started.");

    return subscription;
  }
}
