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
package com.lsadf.core.infra.cache.impl;

import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.CacheUtils;
import com.lsadf.core.infra.cache.configuration.ValkeyProperties;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class ValkeyCache<T> implements Cache<T> {

  protected final RedisTemplate<String, T> redisTemplate;
  protected final String keyType;

  protected int expirationSeconds;

  protected final AtomicBoolean isEnabled;

  public ValkeyCache(
      RedisTemplate<String, T> redisTemplate,
      String keyType,
      int expirationSeconds,
      ValkeyProperties valkeyProperties) {
    this.redisTemplate = redisTemplate;
    this.keyType = keyType;
    this.expirationSeconds = expirationSeconds;
    this.isEnabled = new AtomicBoolean(valkeyProperties.isEnabled());
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<T> get(String key) {
    try {
      T object = redisTemplate.opsForValue().get(keyType + key);
      return Optional.ofNullable(object);
    } catch (DataAccessException e) {
      log.warn("Error while getting element from redis cache", e);
      return Optional.empty();
    }
  }

  @Override
  public void set(String key, T value, int expirationSeconds) {
    try {
      redisTemplate.opsForValue().set(keyType + key, value, expirationSeconds, TimeUnit.SECONDS);
    } catch (DataAccessException e) {
      log.warn("Error while setting entry in redis cache", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void set(String key, T value) {
    try {
      if (expirationSeconds > 0) {
        redisTemplate.opsForValue().set(keyType + key, value, expirationSeconds, TimeUnit.SECONDS);
      } else {
        redisTemplate.opsForValue().set(keyType + key, value);
      }
    } catch (DataAccessException e) {
      log.warn("Error while setting entry in redis cache", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, T> getAll() {
    return CacheUtils.getAllEntries(redisTemplate, keyType);
  }

  /** {@inheritDoc} */
  @Override
  public void clear() {
    CacheUtils.clearCache(redisTemplate, keyType);
  }

  @Override
  public boolean isEnabled() {
    return this.isEnabled.get();
  }

  /** {@inheritDoc} */
  @Override
  public void setEnabled(boolean enabled) {
    this.isEnabled.set(enabled);
    String type = keyType.substring(0, keyType.length() - 1);
    log.error("Redis {} cache is now " + (enabled ? "enabled" : "disabled"), type);
  }

  /** {@inheritDoc} */
  @Override
  public void unset(String key) {
    try {
      redisTemplate.delete(keyType + key);
    } catch (DataAccessException e) {
      log.warn("Error while deleting entry from redis cache", e);
    }
  }

  @Override
  public void setExpiration(int expirationSeconds) {
    this.expirationSeconds = expirationSeconds;
  }
}
