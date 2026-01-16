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
package com.lsadf.core.unit.infra.valkey.stream.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandlerRegistry;
import com.lsadf.core.infra.valkey.stream.consumer.impl.GameStreamConsumer;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveEventType;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.infra.valkey.stream.exception.EventHandlingException;
import com.lsadf.core.infra.valkey.stream.serializer.ValkeyEventSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import tools.jackson.core.JacksonException;

@ExtendWith(MockitoExtension.class)
class GameStreamConsumerTests {

  private static final String CONSUMER_ID = "test-consumer";
  private static final String STREAM_KEY = "game:saves";
  private static final String CONSUMER_GROUP = "save-processors";
  private static final long DEBOUNCE_WINDOW_MS = 1000L;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private RedisTemplate<String, String> redisTemplate;

  @Mock private ValkeyEventSerializer<@NonNull ValkeyGameSaveUpdatedEvent> valkeyEventSerializer;

  @Mock private EventHandlerRegistry handlerRegistry;

  @Captor private ArgumentCaptor<String> keyCaptor;

  @Captor private ArgumentCaptor<String> valueCaptor;

  @Captor private ArgumentCaptor<Double> scoreCaptor;

  private GameStreamConsumer gameStreamConsumer;

  @BeforeEach
  void setUp() {
    gameStreamConsumer =
        new GameStreamConsumer(
            CONSUMER_ID,
            STREAM_KEY,
            CONSUMER_GROUP,
            redisTemplate,
            valkeyEventSerializer,
            handlerRegistry,
            DEBOUNCE_WINDOW_MS);
  }

  @Test
  void getId_shouldReturnCorrectId() {
    assertEquals(CONSUMER_ID, gameStreamConsumer.getId());
  }

  @Test
  void handleEvent_shouldProcessValidEventAndUpdateFlushTimestamp() throws JacksonException {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    String userId = "user123";
    ValkeyGameSaveEventType eventType = ValkeyGameSaveEventType.STAGE_UPDATED;
    Map<String, String> eventData = new HashMap<>();

    MapRecord<String, String, String> mockRecord = createMockRecord(eventData);

    ValkeyGameSaveUpdatedEvent event = createGameSaveEvent(gameSaveId, userId, eventType);
    EventHandler eventHandler = mock(EventHandler.class);

    when(valkeyEventSerializer.deserialize(eventData)).thenReturn(event);
    when(handlerRegistry.getHandler(eventType)).thenReturn(Optional.of(eventHandler));
    when(redisTemplate.opsForZSet().add(anyString(), anyString(), anyDouble())).thenReturn(true);

    // Act
    gameStreamConsumer.handleEvent(mockRecord);

    // Assert
    verify(valkeyEventSerializer).deserialize(eventData);
    verify(handlerRegistry).getHandler(eventType);
    verify(eventHandler).handleEvent(event);
    verify(redisTemplate.opsForZSet())
        .add(eq(FlushStatus.PENDING.getKey()), eq(gameSaveId.toString()), any(Double.class));
  }

  @Test
  void handleEvent_shouldThrowException_whenDeserializationFails() throws JacksonException {
    // Arrange
    Map<String, String> eventData = new HashMap<>();
    MapRecord<String, String, String> mockRecord = createMockRecord(eventData);

    when(valkeyEventSerializer.deserialize(eventData))
        .thenThrow(new JacksonException("Deserialization failed") {});

    // Act & Assert
    EventHandlingException exception =
        assertThrows(
            EventHandlingException.class, () -> gameStreamConsumer.handleEvent(mockRecord));
    assertEquals("Failed to deserialize game save event", exception.getMessage());
    verify(handlerRegistry, never()).getHandler(any());
    verify(redisTemplate.opsForZSet(), never()).add(anyString(), anyString(), anyDouble());
  }

  @Test
  void handleEvent_shouldThrowException_whenNoHandlerFound() throws JacksonException {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    String userId = "user123";
    ValkeyGameSaveEventType eventType = ValkeyGameSaveEventType.CURRENCY_UPDATED;
    Map<String, String> eventData = new HashMap<>();

    MapRecord<String, String, String> mockRecord = createMockRecord(eventData);
    ValkeyGameSaveUpdatedEvent event = createGameSaveEvent(gameSaveId, userId, eventType);

    when(valkeyEventSerializer.deserialize(eventData)).thenReturn(event);
    when(handlerRegistry.getHandler(eventType)).thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> gameStreamConsumer.handleEvent(mockRecord));
    assertEquals("No handler found for event type: " + eventType, exception.getMessage());
    verify(redisTemplate.opsForZSet(), never()).add(anyString(), anyString(), anyDouble());
  }

  @Test
  void handleEvent_shouldThrowException_whenHandlerFails() throws JacksonException {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    String userId = "user123";
    ValkeyGameSaveEventType eventType = ValkeyGameSaveEventType.CHARACTERISTICS_UPDATED;
    Map<String, String> eventData = new HashMap<>();

    MapRecord<String, String, String> mockRecord = createMockRecord(eventData);
    ValkeyGameSaveUpdatedEvent event = createGameSaveEvent(gameSaveId, userId, eventType);
    EventHandler eventHandler = mock(EventHandler.class);

    when(valkeyEventSerializer.deserialize(eventData)).thenReturn(event);
    when(handlerRegistry.getHandler(eventType)).thenReturn(Optional.of(eventHandler));
    doThrow(new JacksonException("Handler processing failed") {})
        .when(eventHandler)
        .handleEvent(event);

    // Act & Assert
    assertThrows(EventHandlingException.class, () -> gameStreamConsumer.handleEvent(mockRecord));
    verify(redisTemplate.opsForZSet(), never()).add(anyString(), anyString(), anyDouble());
  }

  @Test
  void resetDebounceWindow_shouldAddGameSaveToZSet() throws JacksonException {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    String userId = "user123";
    ValkeyGameSaveEventType eventType = ValkeyGameSaveEventType.STAGE_UPDATED;
    Map<String, String> eventData = new HashMap<>();

    MapRecord<String, String, String> mockRecord = createMockRecord(eventData);
    ValkeyGameSaveUpdatedEvent event = createGameSaveEvent(gameSaveId, userId, eventType);
    EventHandler eventHandler = mock(EventHandler.class);

    when(valkeyEventSerializer.deserialize(eventData)).thenReturn(event);
    when(handlerRegistry.getHandler(eventType)).thenReturn(Optional.of(eventHandler));

    // Act
    gameStreamConsumer.handleEvent(mockRecord);

    // Assert
    verify(redisTemplate.opsForZSet(), times(1))
        .add(keyCaptor.capture(), valueCaptor.capture(), scoreCaptor.capture());
    var status = keyCaptor.getValue();
    var id = valueCaptor.getValue();
    assertEquals(FlushStatus.PENDING.getKey(), status);
    assertEquals(gameSaveId.toString(), id);
  }

  private MapRecord<String, String, String> createMockRecord(Map<String, String> eventData) {
    @SuppressWarnings("unchecked")
    MapRecord<String, String, String> mockRecord = mock(MapRecord.class);
    when(mockRecord.getValue()).thenReturn(eventData);
    when(mockRecord.getId()).thenReturn(mock(RecordId.class));
    return mockRecord;
  }

  private ValkeyGameSaveUpdatedEvent createGameSaveEvent(
      UUID gameSaveId, String userId, ValkeyGameSaveEventType eventType) {
    return new ValkeyGameSaveUpdatedEvent(eventType, gameSaveId, userId, null, new HashMap<>());
  }
}
