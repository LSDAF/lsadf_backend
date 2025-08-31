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
package com.lsadf.core.infra.valkey.cache.util;

import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

@UtilityClass
@Slf4j
public class CacheUtils {

  /**
   * Get all keyName entries
   *
   * @param redisTemplate the redis template
   * @param keyName the keyName
   * @return a map of game save id to keyName amount
   */
  public static <T> Map<String, T> getAllEntries(
      RedisTemplate<String, T> redisTemplate, String keyName) {
    String entryType = keyName.substring(0, keyName.length() - 1);
    Map<String, T> map = new HashMap<>();
    ScanOptions scanOptions = ScanOptions.scanOptions().match(keyName + "*").build();
    try {
      RedisKeyCommands commands = initRedisKeyCommands(redisTemplate);
      iterateOverKeys(commands, scanOptions, keyName, redisTemplate, map::put);
    } catch (DataAccessException e) {
      log.warn("Error while getting " + entryType + " from redis cache", e);
    }

    return map;
  }

  /**
   * Iterates over the keys of the cache
   *
   * @param commands the RedisKeyCommands
   * @param scanOptions the scan options
   * @param pattern the pattern to match
   * @param redisTemplate the redis template
   * @param consumer the consumer
   * @param <T> the type of the value
   */
  private static <T> void iterateOverKeys(
      RedisKeyCommands commands,
      ScanOptions scanOptions,
      String pattern,
      RedisTemplate<String, T> redisTemplate,
      KeyValueConsumer<T> consumer) {
    try (Cursor<byte[]> cursor = commands.scan(scanOptions)) {
      while (cursor.hasNext()) {
        String key = new String(cursor.next());
        String gameSaveId = key.substring(pattern.length());
        T value = redisTemplate.opsForValue().get(key);
        if (value != null) {
          consumer.accept(gameSaveId, value);
        }
      }
    } catch (Exception e) {
      log.warn("Error while scanning entries from gold cache", e);
    }
  }

  /**
   * Initialize the RedisKeyCommands
   *
   * @param redisTemplate the redis template
   * @return the RedisKeyCommands
   */
  private static RedisKeyCommands initRedisKeyCommands(RedisTemplate<?, ?> redisTemplate) {
    RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
    if (connectionFactory == null) {
      throw new RedisConnectionFailureException("No RedisConnectionFactory available");
    }
    RedisConnection connection = connectionFactory.getConnection();
    return connection.keyCommands();
  }

  /**
   * Clear the cache
   *
   * @param redisTemplate the redis template
   * @param keyName the key name
   * @param <T> the type of the value
   */
  public static <T> void clearCache(RedisTemplate<String, T> redisTemplate, String keyName) {
    String entryType = keyName.substring(0, keyName.length() - 1);
    log.info("Clearing {} cache", entryType);
    RedisKeyCommands commands = initRedisKeyCommands(redisTemplate);
    ScanOptions scanOptions = ScanOptions.scanOptions().match(keyName + "*").build();
    try {
      iterateOverKeys(
          commands,
          scanOptions,
          keyName,
          redisTemplate,
          (gameSaveId, value) -> {
            var result = redisTemplate.delete(keyName + gameSaveId);
            log.info("Deleted key: {} with result: {}", gameSaveId, result);
          });
      log.info("{} cache cleared", entryType);
    } catch (DataAccessException e) {
      log.warn("Error while clearing {} cache", entryType, e);
    }
  }
}
