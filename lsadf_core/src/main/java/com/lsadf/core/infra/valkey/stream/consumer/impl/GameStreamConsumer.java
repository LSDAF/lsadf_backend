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

import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import com.lsadf.core.infra.valkey.stream.consumer.StreamConsumer;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandlerRegistry;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.infra.valkey.stream.exception.EventHandlingException;
import com.lsadf.core.infra.valkey.stream.serializer.ValkeyEventSerializer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import tools.jackson.core.JacksonException;

/**
 * Stream consumer that handles debounced persistence by managing ZSET timestamps for game save
 * entries that need to be flushed to the database.
 */
@Slf4j
public class GameStreamConsumer extends ValkeyStreamConsumer implements StreamConsumer {

  private final RedisTemplate<String, String> redisTemplate;
  private final ValkeyEventSerializer<ValkeyGameSaveUpdatedEvent> gameValkeyEventSerializer;
  private final long debounceWindowMs;
  private final EventHandlerRegistry handlerRegistry;

  public GameStreamConsumer(
      String id,
      String streamKey,
      String consumerGroup,
      RedisTemplate<String, String> redisTemplate,
      ValkeyEventSerializer<ValkeyGameSaveUpdatedEvent> gameValkeyEventSerializer,
      EventHandlerRegistry handlerRegistry,
      long debounceWindowMs) {
    super(id, streamKey, consumerGroup);
    this.redisTemplate = redisTemplate;
    this.handlerRegistry = handlerRegistry;
    this.gameValkeyEventSerializer = gameValkeyEventSerializer;
    this.debounceWindowMs = debounceWindowMs;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void handleEvent(MapRecord<String, String, String> message) {
    log.debug("Debounced persistence consumer received message: {}", message.getId());

    Map<String, String> eventData = message.getValue();

    ValkeyGameSaveUpdatedEvent event;
    try {
      event = gameValkeyEventSerializer.deserialize(eventData);
    } catch (JacksonException e) {
      log.error("Error deserializing game save event for debounced persistence", e);
      throw new EventHandlingException("Failed to deserialize game save event", e);
    }

    var optionalHandler = handlerRegistry.getHandler(event.getEventType());
    if (optionalHandler.isEmpty()) {
      log.error("No handler found for event type: {}", event.getEventType());
      throw new IllegalArgumentException(
          "No handler found for event type: " + event.getEventType());
    }
    EventHandler handler = optionalHandler.get();
    try {
      handler.handleEvent(event);
    } catch (JacksonException e) {
      log.error("Error handling event: {}", event, e);
      throw new EventHandlingException(e);
    }

    // Add/update the game save in ZSET with new flush timestamp
    // This resets the debounce window every time there's an update
    resetDebounceWindow(event);
  }

  private void resetDebounceWindow(ValkeyGameSaveUpdatedEvent event) {
    String gameSaveId = event.getGameSaveId().toString();
    long currentTimestamp = System.currentTimeMillis();
    long flushTimestamp = currentTimestamp + debounceWindowMs;
    redisTemplate.opsForZSet().add(FlushStatus.PENDING.getKey(), gameSaveId, flushTimestamp);
    log.debug(
        "Updated flush timestamp for game save {} to {} (debounce window: {}ms)",
        gameSaveId,
        flushTimestamp,
        debounceWindowMs);
  }
}
