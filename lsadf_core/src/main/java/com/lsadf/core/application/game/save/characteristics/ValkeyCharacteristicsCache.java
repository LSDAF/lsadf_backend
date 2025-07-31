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
package com.lsadf.core.application.game.save.characteristics;

import static com.lsadf.core.infra.cache.CacheUtils.clearCache;
import static com.lsadf.core.infra.cache.CacheUtils.getAllEntries;
import static com.lsadf.core.infra.cache.RedisConstants.CHARACTERISTICS;
import static com.lsadf.core.infra.cache.RedisConstants.CHARACTERISTICS_HISTO;

import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.ValkeyCache;
import com.lsadf.core.infra.cache.config.ValkeyProperties;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    var builder = Characteristics.builder();
    builder.attack(
        newCharacteristics.attack() != null ? newCharacteristics.attack() : toUpdate.attack());
    builder.critChance(
        newCharacteristics.critChance() != null
            ? newCharacteristics.critChance()
            : toUpdate.critChance());
    builder.critDamage(
        newCharacteristics.critDamage() != null
            ? newCharacteristics.critDamage()
            : toUpdate.critDamage());
    builder.health(
        newCharacteristics.health() != null ? newCharacteristics.health() : toUpdate.health());
    builder.resistance(
        newCharacteristics.resistance() != null
            ? newCharacteristics.resistance()
            : toUpdate.resistance());

    return builder.build();
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
