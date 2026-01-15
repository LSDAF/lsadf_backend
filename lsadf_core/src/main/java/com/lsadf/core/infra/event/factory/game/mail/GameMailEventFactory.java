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
package com.lsadf.core.infra.event.factory.game.mail;

import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.domain.game.mail.event.GameMailAttachmentsClaimedEvent;
import com.lsadf.core.domain.game.mail.event.GameMailReadEvent;
import com.lsadf.core.shared.event.EventFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory for creating game mail related events. Implements the {@link GameMailEventFactory}
 * interface. This factory uses the {@link ClockService} to obtain the current timestamp for event
 * creation. All events created by this factory include the game mail ID, user ID, and the timestamp
 * of the event.
 *
 * @see GameMailReadEvent
 * @see GameMailAttachmentsClaimedEvent
 * @see ClockService
 * @see EventFactory
 */
@Component
@RequiredArgsConstructor
public class GameMailEventFactory implements EventFactory {
  private final ClockService clockService;

  public GameMailReadEvent createGameMailReadEvent(UUID gameMailId, String userId) {
    long timestamp = clockService.nowInstant().toEpochMilli();
    return new GameMailReadEvent(gameMailId, userId, timestamp);
  }

  public GameMailAttachmentsClaimedEvent createGameMailAttachmentsClaimedEvent(
      UUID gameMailId, String userId) {
    long timestamp = clockService.nowInstant().toEpochMilli();
    return new GameMailAttachmentsClaimedEvent(gameMailId, userId, timestamp);
  }
}
