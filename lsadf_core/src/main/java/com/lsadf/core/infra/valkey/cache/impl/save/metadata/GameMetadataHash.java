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

package com.lsadf.core.infra.valkey.cache.impl.save.metadata;

import static com.lsadf.core.infra.valkey.cache.impl.save.metadata.GameMetadataHash.GameMetadataHashAttributes.*;

import com.lsadf.core.infra.valkey.cache.Hash;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = GAME_METADATA_HASH_KEY)
public class GameMetadataHash implements Hash<UUID> {

  @Id
  @Column(value = GameMetadataHashAttributes.GAME_SAVE_ID)
  private UUID id;

  @Column(value = GAME_SAVE_USER_EMAIL)
  private String userEmail;

  @Column(value = GAME_SAVE_NICKNAME)
  private String nickname;

  @TimeToLive private Long expiration;

  public GameMetadataHash(UUID id, String userEmail, String nickname) {
    this.id = id;
    this.userEmail = userEmail;
    this.nickname = nickname;
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameMetadataHashAttributes {
    public static final String GAME_METADATA_HASH_KEY = "game_save_metadata";
    public static final String GAME_SAVE_ID = "id";
    public static final String GAME_SAVE_USER_EMAIL = "userEmail";
    public static final String GAME_SAVE_NICKNAME = "userNickname";
  }
}
