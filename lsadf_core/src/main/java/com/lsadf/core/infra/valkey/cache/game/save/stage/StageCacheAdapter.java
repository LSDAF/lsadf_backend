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

package com.lsadf.core.infra.valkey.cache.game.save.stage;

import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.ValkeyConstants;
import com.lsadf.core.infra.valkey.cache.impl.ValkeyHistoCacheAdapter;
import org.springframework.data.redis.core.RedisTemplate;

public class StageCacheAdapter extends ValkeyHistoCacheAdapter<Stage> implements StageCachePort {
  private static final String HISTO_KEY_TYPE = ValkeyConstants.STAGE_HISTO;

  public StageCacheAdapter(
      RedisTemplate<String, Stage> redisTemplate, String keyType, int expirationSeconds) {
    super(redisTemplate, keyType, HISTO_KEY_TYPE, expirationSeconds);
  }
}
