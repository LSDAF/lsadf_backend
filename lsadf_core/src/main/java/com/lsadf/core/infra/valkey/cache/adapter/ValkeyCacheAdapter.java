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
package com.lsadf.core.infra.valkey.cache.adapter;

import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.infra.valkey.cache.util.CacheUtils;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public abstract class ValkeyCacheAdapter<T> implements CachePort<T> {

  protected final RedisTemplate<String, T> redisTemplate;
  protected final String keyType;

  protected int expirationSeconds;

  protected ValkeyCacheAdapter(
      RedisTemplate<String, T> redisTemplate, String keyType, int expirationSeconds) {
    this.redisTemplate = redisTemplate;
    this.keyType = keyType;
    this.expirationSeconds = expirationSeconds;
  }

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

  @Override
  public Map<String, T> getAll() {
    return CacheUtils.getAllEntries(redisTemplate, keyType);
  }

  @Override
  public void clear() {
    CacheUtils.clearCache(redisTemplate, keyType);
  }

  @Override
  public void unset(String key) {
    try {
      redisTemplate.delete(keyType + key);
    } catch (DataAccessException e) {
      log.warn("Error while deleting entry from redis cache", e);
    }
  }
}
