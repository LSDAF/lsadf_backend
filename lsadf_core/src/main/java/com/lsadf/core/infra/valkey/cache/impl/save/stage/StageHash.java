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

package com.lsadf.core.infra.valkey.cache.impl.save.stage;

import static com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHash.StageHashAttributes.*;

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
@RedisHash(value = STAGE_HASH_KEY)
public class StageHash implements Hash<UUID> {

  @Id
  @Column(value = STAGE_GAME_SAVE_ID)
  private UUID gameSaveId;

  @Column(value = STAGE_CURRENT_STAGE)
  private Long currentStage;

  @Column(value = STAGE_MAX_STAGE)
  private Long maxStage;

  @TimeToLive private Long expiration;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class StageHashAttributes {
    public static final String STAGE_HASH_KEY = "stage";
    public static final String STAGE_GAME_SAVE_ID = "gameSaveId";
    public static final String STAGE_CURRENT_STAGE = "currentStage";
    public static final String STAGE_MAX_STAGE = "maxStage";
  }

  @Override
  public UUID getId() {
    return this.gameSaveId;
  }
}
