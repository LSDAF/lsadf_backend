/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.application.game.characteristics.out;

import static com.lsadf.core.infra.cache.CacheUtils.clearCache;
import static com.lsadf.core.infra.cache.CacheUtils.getAllEntries;
import static com.lsadf.core.infra.cache.RedisConstants.CHARACTERISTICS;
import static com.lsadf.core.infra.cache.RedisConstants.CHARACTERISTICS_HISTO;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.configuration.ValkeyProperties;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.lsadf.core.infra.cache.impl.ValkeyCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class ValkeyCharacteristicsCache extends ValkeyCache<Characteristics>
    implements HistoCache<Characteristics> {

  private static final String HISTO_KEY_TYPE = CHARACTERISTICS_HISTO;

  public ValkeyCharacteristicsCache(
      RedisTemplate<String, Characteristics> redisTemplate,
      int expirationSeconds,
      ValkeyProperties valkeyProperties) {
    super(redisTemplate, CHARACTERISTICS, expirationSeconds, valkeyProperties);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public void set(String key, Characteristics value) {
    set(key, value, this.expirationSeconds);
  }

  /** {@inheritDoc} */
  @Override
  public void set(String gameSaveId, Characteristics characteristics, int expirationSeconds) {
    try {
      Characteristics toUpdateCharacteristics = characteristics;
      Optional<Characteristics> optionalCharacteristics = get(gameSaveId);
      if (optionalCharacteristics.isPresent()) {
        toUpdateCharacteristics =
            mergeCharacteristics(optionalCharacteristics.get(), characteristics);
      }
      if (expirationSeconds > 0) {
        redisTemplate
            .opsForValue()
            .set(
                keyType + gameSaveId, toUpdateCharacteristics, expirationSeconds, TimeUnit.SECONDS);
      } else {
        redisTemplate.opsForValue().set(keyType + gameSaveId, toUpdateCharacteristics);
      }
      redisTemplate.opsForValue().set(CHARACTERISTICS_HISTO + gameSaveId, characteristics);
    } catch (DataAccessException e) {
      log.warn("Error while setting characteristics in redis cache", e);
    }
  }

  private static Characteristics mergeCharacteristics(
      Characteristics toUpdate, Characteristics newCharacteristics) {
    if (newCharacteristics.getAttack() != null) {
      toUpdate.setAttack(newCharacteristics.getAttack());
    }
    if (newCharacteristics.getCritChance() != null) {
      toUpdate.setCritChance(newCharacteristics.getCritChance());
    }
    if (newCharacteristics.getCritDamage() != null) {
      toUpdate.setCritDamage(newCharacteristics.getCritDamage());
    }
    if (newCharacteristics.getHealth() != null) {
      toUpdate.setHealth(newCharacteristics.getHealth());
    }
    if (newCharacteristics.getResistance() != null) {
      toUpdate.setResistance(newCharacteristics.getResistance());
    }

    return toUpdate;
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, Characteristics> getAllHisto() {
    return getAllEntries(redisTemplate, HISTO_KEY_TYPE);
  }

  /** {@inheritDoc} */
  @Override
  public void clear() {
    super.clear();
    clearCache(redisTemplate, HISTO_KEY_TYPE);
  }
}
