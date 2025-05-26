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
package com.lsadf.core.infra.cache.impl;

import static com.lsadf.core.constants.RedisConstants.CURRENCY;
import static com.lsadf.core.constants.RedisConstants.CURRENCY_HISTO;
import static com.lsadf.core.infra.cache.CacheUtils.clearCache;
import static com.lsadf.core.infra.cache.CacheUtils.getAllEntries;

import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.models.Currency;
import com.lsadf.core.properties.RedisProperties;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisCurrencyCache extends RedisCache<Currency> implements HistoCache<Currency> {

  private static final String HISTO_KEY_TYPE = CURRENCY_HISTO;

  public RedisCurrencyCache(
      RedisTemplate<String, Currency> redisTemplate,
      int expirationSeconds,
      RedisProperties redisProperties) {
    super(redisTemplate, CURRENCY, expirationSeconds, redisProperties);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public void set(String key, Currency value) {
    set(key, value, this.expirationSeconds);
  }

  /** {@inheritDoc} */
  @Override
  public void set(String gameSaveId, Currency currency, int expirationSeconds) {
    try {
      Currency toUpdateCurrency = currency;
      Optional<Currency> optionalCurrency = get(gameSaveId);
      if (optionalCurrency.isPresent()) {
        toUpdateCurrency = mergeCurrencies(optionalCurrency.get(), currency);
      }
      if (expirationSeconds > 0) {
        redisTemplate
            .opsForValue()
            .set(keyType + gameSaveId, toUpdateCurrency, expirationSeconds, TimeUnit.SECONDS);
      } else {
        redisTemplate.opsForValue().set(keyType + gameSaveId, toUpdateCurrency);
      }
      redisTemplate.opsForValue().set(CURRENCY_HISTO + gameSaveId, currency);
    } catch (DataAccessException e) {
      log.warn("Error while setting currency in redis cache", e);
    }
  }

  private static Currency mergeCurrencies(Currency toUpdate, Currency newCurrency) {
    if (newCurrency.getGold() != null) {
      toUpdate.setGold(newCurrency.getGold());
    }
    if (newCurrency.getDiamond() != null) {
      toUpdate.setDiamond(newCurrency.getDiamond());
    }
    if (newCurrency.getEmerald() != null) {
      toUpdate.setEmerald(newCurrency.getEmerald());
    }
    if (newCurrency.getAmethyst() != null) {
      toUpdate.setAmethyst(newCurrency.getAmethyst());
    }
    return toUpdate;
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, Currency> getAllHisto() {
    return getAllEntries(redisTemplate, HISTO_KEY_TYPE);
  }

  /** {@inheritDoc} */
  @Override
  public void clear() {
    super.clear();
    clearCache(redisTemplate, HISTO_KEY_TYPE);
  }
}
