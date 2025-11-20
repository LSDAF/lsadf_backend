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

package com.lsadf.core.infra.valkey.config.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.infra.valkey.config.properties.ValkeyGameStreamProperties;
import com.lsadf.core.infra.valkey.stream.consumer.StreamConsumer;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.infra.valkey.stream.serializer.ValkeyEventSerializer;
import com.lsadf.core.infra.valkey.stream.serializer.impl.GameValkeyEventSerializer;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

@Slf4j
@Configuration
@Import({
  ValkeyStreamProducerConfiguration.class,
  ValkeyStreamAdapterConfiguration.class,
  ValkeyStreamRecordHandlerConfiguration.class,
})
@ConditionalOnProperty(prefix = "valkey.config", name = "enabled", havingValue = "true")
public class ValkeyStreamConfiguration {

  @Bean
  public ValkeyEventSerializer<ValkeyGameSaveUpdatedEvent> eventSerializer(
      ObjectMapper objectMapper) {
    return new GameValkeyEventSerializer(objectMapper);
  }

  @Bean
  public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<
          String, MapRecord<String, String, String>>
      streamMessageListenerContainerOptions(TaskExecutor streamListenerExecutor) {
    return StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
        .pollTimeout(Duration.ofMillis(100))
        .executor(streamListenerExecutor)
        .build();
  }

  @Bean(destroyMethod = "stop")
  public StreamMessageListenerContainer<String, MapRecord<String, String, String>>
      streamMessageListenerContainer(
          RedisConnectionFactory connectionFactory,
          StreamMessageListenerContainer.StreamMessageListenerContainerOptions<
                  String, MapRecord<String, String, String>>
              options) {
    return StreamMessageListenerContainer.create(connectionFactory, options);
  }

  @Bean
  public Subscription gameConsumerSubscription(
      ValkeyGameStreamProperties valkeyGameStreamProperties,
      StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer,
      StreamConsumer dataConsumer,
      RedisTemplate<String, String> redisTemplate) {

    String streamKey = valkeyGameStreamProperties.getStreamKey();
    String groupName = valkeyGameStreamProperties.getConsumerGroup();
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
    return subscription;
  }
}
