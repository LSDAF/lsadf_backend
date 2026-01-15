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
package com.lsadf.core.unit.infra.event.factory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.domain.game.inventory.event.InventoryItemDeletedEvent;
import com.lsadf.core.infra.event.factory.game.inventory.InventoryEventFactory;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryEventFactoryTests {

  @Mock private ClockService clockService;

  private InventoryEventFactory eventFactory;

  private static final long FIXED_TIMESTAMP_MILLIS = 1700000000000L;
  private static final Instant FIXED_INSTANT = Instant.ofEpochMilli(FIXED_TIMESTAMP_MILLIS);
  private static final UUID TEST_GAME_SAVE_ID = UUID.randomUUID();
  private static final String TEST_USER_ID = "test-user-id";
  private static final String TEST_ITEM_ID = "test-item-id";

  @BeforeEach
  void setUp() {
    eventFactory = new InventoryEventFactory(clockService);
  }

  @Test
  @DisplayName(
      "createInventoryItemDeletedEvent should create event with correct timestamp from clock")
  void createInventoryItemDeletedEvent_shouldUseClockServiceTimestamp() {
    // Given
    when(clockService.nowInstant()).thenReturn(FIXED_INSTANT);

    // When
    InventoryItemDeletedEvent event =
        eventFactory.createInventoryItemDeletedEvent(TEST_USER_ID, TEST_GAME_SAVE_ID, TEST_ITEM_ID);

    // Then
    assertNotNull(event);
    assertEquals(TEST_GAME_SAVE_ID, event.getGameSaveId());
    assertEquals(TEST_USER_ID, event.getUserId());
    assertEquals(TEST_ITEM_ID, event.getItemId());
    assertEquals(FIXED_TIMESTAMP_MILLIS, event.getTimestamp());
    verify(clockService, times(1)).nowInstant();
  }

  @Test
  @DisplayName("Multiple events should have different timestamps when clock advances")
  void multipleEvents_shouldHaveDifferentTimestamps_whenClockAdvances() {
    // Given
    long firstTimestamp = 1700000000000L;
    long secondTimestamp = 1700000001000L;
    when(clockService.nowInstant())
        .thenReturn(Instant.ofEpochMilli(firstTimestamp))
        .thenReturn(Instant.ofEpochMilli(secondTimestamp));

    // When
    InventoryItemDeletedEvent firstEvent =
        eventFactory.createInventoryItemDeletedEvent(TEST_USER_ID, TEST_GAME_SAVE_ID, TEST_ITEM_ID);
    InventoryItemDeletedEvent secondEvent =
        eventFactory.createInventoryItemDeletedEvent(
            TEST_USER_ID, TEST_GAME_SAVE_ID, "another-item-id");

    // Then
    assertEquals(firstTimestamp, firstEvent.getTimestamp());
    assertEquals(secondTimestamp, secondEvent.getTimestamp());
    assertNotEquals(firstEvent.getTimestamp(), secondEvent.getTimestamp());
  }

  @Test
  @DisplayName("Factory should create events with same timestamp when called at same instant")
  void factory_shouldCreateEventsWithSameTimestamp_whenCalledAtSameInstant() {
    // Given
    when(clockService.nowInstant()).thenReturn(FIXED_INSTANT);

    // When
    InventoryItemDeletedEvent firstEvent =
        eventFactory.createInventoryItemDeletedEvent(TEST_USER_ID, TEST_GAME_SAVE_ID, TEST_ITEM_ID);
    InventoryItemDeletedEvent secondEvent =
        eventFactory.createInventoryItemDeletedEvent(
            TEST_USER_ID, TEST_GAME_SAVE_ID, "another-item-id");

    // Then
    assertEquals(FIXED_TIMESTAMP_MILLIS, firstEvent.getTimestamp());
    assertEquals(FIXED_TIMESTAMP_MILLIS, secondEvent.getTimestamp());
    assertEquals(firstEvent.getTimestamp(), secondEvent.getTimestamp());
  }

  @Test
  @DisplayName("createInventoryItemDeletedEvent should create event with all parameters correctly")
  void createInventoryItemDeletedEvent_shouldSetAllParametersCorrectly() {
    // Given
    UUID specificGameSaveId = UUID.randomUUID();
    String specificUserId = "specific-user";
    String specificItemId = "specific-item";
    when(clockService.nowInstant()).thenReturn(FIXED_INSTANT);

    // When
    InventoryItemDeletedEvent event =
        eventFactory.createInventoryItemDeletedEvent(
            specificUserId, specificGameSaveId, specificItemId);

    // Then
    assertNotNull(event);
    assertEquals(specificGameSaveId, event.getGameSaveId());
    assertEquals(specificUserId, event.getUserId());
    assertEquals(specificItemId, event.getItemId());
    assertEquals(FIXED_TIMESTAMP_MILLIS, event.getTimestamp());
  }
}
