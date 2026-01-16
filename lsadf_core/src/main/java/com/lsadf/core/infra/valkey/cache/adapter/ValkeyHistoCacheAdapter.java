/*
 * Copyright © 2024-2026 LSDAF
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

import com.lsadf.core.application.shared.HistoCachePort;
import com.lsadf.core.infra.valkey.cache.util.CacheUtils;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public abstract class ValkeyHistoCacheAdapter<T> extends ValkeyCacheAdapter<T>
    implements HistoCachePort<T> {
  private final String histoKeyType;

  protected ValkeyHistoCacheAdapter(
      RedisTemplate<String, T> redisTemplate,
      String keyType,
      String histoKeyType,
      int expirationSeconds) {
    super(redisTemplate, keyType, expirationSeconds);
    this.histoKeyType = histoKeyType;
  }

  @Override
  public Optional<T> getHisto(String key) {
    try {
      T object = redisTemplate.opsForValue().get(histoKeyType + key);
      return Optional.ofNullable(object);
    } catch (DataAccessException e) {
      log.warn("Error while getting element from redis cache", e);
      return Optional.empty();
    }
  }

  @Override
  public Map<String, T> getAllHisto() {
    return CacheUtils.getAllEntries(redisTemplate, histoKeyType);
  }
}
