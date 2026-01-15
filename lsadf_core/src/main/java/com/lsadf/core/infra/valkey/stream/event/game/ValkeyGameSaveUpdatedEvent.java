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
package com.lsadf.core.infra.valkey.stream.event.game;

import com.lsadf.core.shared.event.AEvent;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import java.io.Serial;
import java.util.Map;
import java.util.UUID;
import lombok.*;
import org.jspecify.annotations.Nullable;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ValkeyGameSaveUpdatedEvent extends AEvent implements Event {
  @Serial private static final long serialVersionUID = -8858694336369130030L;
  private final UUID gameSaveId;
  private final String userId;
  @Nullable private final UUID gameSessionId;
  private final Map<String, String> payload;

  public ValkeyGameSaveUpdatedEvent(
      EventType eventType,
      UUID gameSaveId,
      String userId,
      @Nullable UUID gameSessionId,
      Map<String, String> payload) {
    super(eventType);
    this.gameSaveId = gameSaveId;
    this.userId = userId;
    this.gameSessionId = gameSessionId;
    this.payload = payload;
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameSaveEventAttributes {
    public static final String GAME_SAVE_ID = "gameSaveId";
    public static final String PAYLOAD = "payload";
    public static final String GAME_SESSION_ID = "gameSessionId";
    public static final String USER_ID = "userId";
  }
}
