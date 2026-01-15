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
import com.lsadf.core.domain.game.mail.event.GameMailAttachmentsClaimedEvent;
import com.lsadf.core.domain.game.mail.event.GameMailReadEvent;
import com.lsadf.core.infra.event.factory.game.mail.GameMailEventFactory;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameMailEventFactoryTests {

  @Mock private ClockService clockService;

  private GameMailEventFactory eventFactory;

  private static final long FIXED_TIMESTAMP_MILLIS = 1700000000000L;
  private static final Instant FIXED_INSTANT = Instant.ofEpochMilli(FIXED_TIMESTAMP_MILLIS);
  private static final UUID TEST_GAME_MAIL_ID = UUID.randomUUID();
  private static final String TEST_USER_ID = "test-user-id";

  @BeforeEach
  void setUp() {
    eventFactory = new GameMailEventFactory(clockService);
  }

  @Test
  @DisplayName("createGameMailReadEvent should create event with correct timestamp from clock")
  void createGameMailReadEvent_shouldUseClockServiceTimestamp() {
    // Given
    when(clockService.nowInstant()).thenReturn(FIXED_INSTANT);

    // When
    GameMailReadEvent event = eventFactory.createGameMailReadEvent(TEST_GAME_MAIL_ID, TEST_USER_ID);

    // Then
    assertNotNull(event);
    assertEquals(TEST_GAME_MAIL_ID, event.getGameMailId());
    assertEquals(TEST_USER_ID, event.getUserId());
    assertEquals(FIXED_TIMESTAMP_MILLIS, event.getTimestamp());
    verify(clockService, times(1)).nowInstant();
  }

  @Test
  @DisplayName(
      "createGameMailAttachmentsClaimedEvent should create event with correct timestamp from clock")
  void createGameMailAttachmentsClaimedEvent_shouldUseClockServiceTimestamp() {
    // Given
    when(clockService.nowInstant()).thenReturn(FIXED_INSTANT);

    // When
    GameMailAttachmentsClaimedEvent event =
        eventFactory.createGameMailAttachmentsClaimedEvent(TEST_GAME_MAIL_ID, TEST_USER_ID);

    // Then
    assertNotNull(event);
    assertEquals(TEST_GAME_MAIL_ID, event.getGameMailId());
    assertEquals(TEST_USER_ID, event.getUserId());
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
    GameMailReadEvent firstEvent =
        eventFactory.createGameMailReadEvent(TEST_GAME_MAIL_ID, TEST_USER_ID);
    GameMailReadEvent secondEvent =
        eventFactory.createGameMailReadEvent(TEST_GAME_MAIL_ID, TEST_USER_ID);

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
    GameMailReadEvent readEvent =
        eventFactory.createGameMailReadEvent(TEST_GAME_MAIL_ID, TEST_USER_ID);
    GameMailAttachmentsClaimedEvent claimedEvent =
        eventFactory.createGameMailAttachmentsClaimedEvent(TEST_GAME_MAIL_ID, TEST_USER_ID);

    // Then
    assertEquals(FIXED_TIMESTAMP_MILLIS, readEvent.getTimestamp());
    assertEquals(FIXED_TIMESTAMP_MILLIS, claimedEvent.getTimestamp());
    assertEquals(readEvent.getTimestamp(), claimedEvent.getTimestamp());
  }
}
