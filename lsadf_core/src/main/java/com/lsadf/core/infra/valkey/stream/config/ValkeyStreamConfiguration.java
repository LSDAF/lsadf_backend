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
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.serializer.EventSerializer;
import com.lsadf.core.infra.valkey.stream.serializer.impl.GameEventSerializer;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@Import({
  ValkeyStreamProducerConfiguration.class,
  ValkeyStreamConsumerConfiguration.class,
  ValkeyStreamAdapterConfiguration.class
})
public class ValkeyStreamConfiguration {

  @Bean
  public EventSerializer<GameSaveEvent> eventSerializer(ObjectMapper objectMapper) {
    return new GameEventSerializer(objectMapper);
  }

  @Bean(name = "streamListenerExecutor")
  public TaskExecutor streamListenerExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("streamListenerExecutor-");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30);
    executor.initialize();
    return executor;
  }

  @Bean
  public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<
          String, MapRecord<String, String, String>>
      streamMessageListenerContainerOptions(TaskExecutor streamListenerExecutor) {
    return StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
        .pollTimeout(Duration.ofSeconds(1))
        .executor(streamListenerExecutor)
        .build();
  }

  @Bean
  public StreamMessageListenerContainer<String, MapRecord<String, String, String>>
      streamMessageListenerContainer(
          RedisConnectionFactory connectionFactory,
          StreamMessageListenerContainer.StreamMessageListenerContainerOptions<
                  String, MapRecord<String, String, String>>
              options) {

    return StreamMessageListenerContainer.create(connectionFactory, options);
  }
}
