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

package com.lsadf.core.infra.valkey.cache.game.save.characteristics;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.valkey.RedisConstants;
import com.lsadf.core.infra.valkey.cache.ValkeyHistoCacheAdapter;
import org.springframework.data.redis.core.RedisTemplate;

public class CharacteristicsCacheAdapter extends ValkeyHistoCacheAdapter<Characteristics>
    implements CharacteristicsCachePort {
  private static final String HISTO_KEY_TYPE = RedisConstants.CHARACTERISTICS_HISTO;

  public CharacteristicsCacheAdapter(
      RedisTemplate<String, Characteristics> redisTemplate, String keyType, int expirationSeconds) {
    super(redisTemplate, keyType, HISTO_KEY_TYPE, expirationSeconds);
  }
}
