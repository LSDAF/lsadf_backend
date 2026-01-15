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
package com.lsadf.core.domain.game.mail.event;

import static com.lsadf.core.domain.game.mail.event.GameMailEventType.GAME_MAIL_READ;

import com.lsadf.core.shared.event.AEvent;
import com.lsadf.core.shared.event.Event;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class GameMailReadEvent extends AEvent implements Event {
  private final UUID gameMailId;
  private final String userId;

  /**
   * Constructor with timestamp parameter.
   *
   * @param gameMailId the game mail ID
   * @param userId the user ID
   * @param timestamp the timestamp in epoch milliseconds
   */
  public GameMailReadEvent(UUID gameMailId, String userId, Long timestamp) {
    super(GAME_MAIL_READ, timestamp);
    this.gameMailId = gameMailId;
    this.userId = userId;
  }

  /**
   * Constructor for backward compatibility using current system time.
   *
   * @param gameMailId the game mail ID
   * @param userId the user ID
   */
  public GameMailReadEvent(UUID gameMailId, String userId) {
    this(gameMailId, userId, System.currentTimeMillis());
  }
}
