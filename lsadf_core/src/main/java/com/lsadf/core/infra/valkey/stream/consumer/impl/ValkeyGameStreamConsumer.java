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

package com.lsadf.core.infra.valkey.stream.consumer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.core.infra.valkey.stream.consumer.GameStreamConsumer;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandlerRegistry;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.exception.EventHandlingException;
import com.lsadf.core.infra.valkey.stream.serializer.EventSerializer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;

@Slf4j
public class ValkeyGameStreamConsumer extends ValkeyStreamConsumer implements GameStreamConsumer {

  private final EventHandlerRegistry eventHandlerRegistry;
  private final EventSerializer<GameSaveEvent> gameEventSerializer;

  public ValkeyGameStreamConsumer(
      String id,
      String streamKey,
      String consumerGroup,
      EventHandlerRegistry eventHandlerRegistry,
      EventSerializer<GameSaveEvent> gameEventSerializer) {
    super(id, streamKey, consumerGroup);
    this.eventHandlerRegistry = eventHandlerRegistry;
    this.gameEventSerializer = gameEventSerializer;
  }

  @Override
  public void handleEvent(MapRecord<String, String, String> message) {
    log.info("Message received from stream: {}", message);
    Map<String, String> eventData = message.getValue();

    GameSaveEvent event;
    try {
      event = gameEventSerializer.deserialize(eventData);
    } catch (JsonProcessingException e) {
      log.error("Error deserializing event for stream {}", streamKey, e);
      throw new EventHandlingException(e);
    }

    var optionalHandler = eventHandlerRegistry.getHandler(event.getEventType());
    if (optionalHandler.isEmpty()) {
      log.error("No handler found for event type: {}", event.getEventType());
      throw new IllegalArgumentException(
          "No handler found for event type: " + event.getEventType());
    }
    EventHandler handler = optionalHandler.get();
    try {
      handler.handleEvent(event);
    } catch (Exception e) {
      log.error("Error handling event: {}", event, e);
      throw new EventHandlingException(e);
    }
    log.info("Message id: {} processed", message.getId());
  }
}
