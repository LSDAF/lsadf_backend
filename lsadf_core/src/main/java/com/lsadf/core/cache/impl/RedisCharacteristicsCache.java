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
package com.lsadf.core.cache.impl;

import static com.lsadf.core.constants.RedisConstants.CHARACTERISTICS;
import static com.lsadf.core.constants.RedisConstants.CHARACTERISTICS_HISTO;
import static com.lsadf.core.utils.CacheUtils.clearCache;
import static com.lsadf.core.utils.CacheUtils.getAllEntries;

import com.lsadf.core.cache.HistoCache;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.properties.RedisProperties;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisCharacteristicsCache extends RedisCache<Characteristics>
    implements HistoCache<Characteristics> {

  private static final String HISTO_KEY_TYPE = CHARACTERISTICS_HISTO;

  public RedisCharacteristicsCache(
      RedisTemplate<String, Characteristics> redisTemplate,
      int expirationSeconds,
      RedisProperties redisProperties) {
    super(redisTemplate, CHARACTERISTICS, expirationSeconds, redisProperties);
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
