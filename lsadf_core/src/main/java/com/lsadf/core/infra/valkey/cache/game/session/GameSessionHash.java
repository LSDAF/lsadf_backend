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

package com.lsadf.core.infra.valkey.cache.game.session;

import static com.lsadf.core.infra.valkey.cache.game.session.GameSessionHash.GameSessionHashAttributes.*;
import static com.lsadf.core.infra.valkey.cache.game.session.GameSessionHash.GameSessionHashAttributes.GAME_SESSION_GAME_SAVE_ID;

import com.lsadf.core.infra.valkey.cache.Hash;
import java.time.Instant;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.relational.core.mapping.Column;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(GAME_SESSION_HASH_KEY)
@Getter
public class GameSessionHash implements Hash<UUID> {
  @Id
  @Column(value = GAME_SESSION_ID)
  private UUID id;

  @Column(value = GAME_SESSION_GAME_SAVE_ID)
  private UUID gameSaveId;

  @Column(value = GAME_SESSION_USER_EMAIL)
  private String userEmail;

  @Column(value = GAME_SESSION_END_TIME)
  private Instant endTime;

  @Column(value = GAME_SESSION_VERSION)
  private int version;

  @TimeToLive private Long expiration;

  @Override
  public UUID getId() {
    return id;
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSessionHashAttributes {
    public static final String GAME_SESSION_ID = "id";
    public static final String GAME_SESSION_GAME_SAVE_ID = "gameSaveId";
    public static final String GAME_SESSION_USER_EMAIL = "userEmail";
    public static final String GAME_SESSION_END_TIME = "endTime";
    public static final String GAME_SESSION_HASH_KEY = "game_session";
    public static final String GAME_SESSION_VERSION = "version";
  }
}
