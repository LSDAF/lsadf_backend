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

import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@UtilityClass
@Slf4j
public class ValkeyFlushUtils {

  public static void removeFromProcessing(
      RedisTemplate<String, String> redisTemplate, String gameSaveId) {
    try {
      redisTemplate.opsForSet().remove(FlushStatus.PROCESSING.getKey(), gameSaveId);
    } catch (Exception e) {
      log.error("Error removing game save {} from processing set", gameSaveId, e);
    }
  }

  /**
   * Moves a game save from the pending flush sorted set to the processing set using Redis
   * MULTI/EXEC. This alternative implementation uses Redis transactions instead of Lua script.
   *
   * @param redisTemplate Redis template to use for operations
   * @param gameSaveId ID of the game save to move
   * @param currentTime Current time to compare against score in the sorted set
   * @return true if the game save was moved successfully, false otherwise
   */
  public static boolean moveToProcessingWithTransaction(
      RedisTemplate<String, String> redisTemplate, String gameSaveId, long currentTime) {
    try {
      return redisTemplate.execute(
          new SessionCallback<>() {
            @Override
            @SuppressWarnings("unchecked")
            public Boolean execute(RedisOperations operations) throws DataAccessException {
              // First check the score to see if this entry should be processed
              Double score =
                  operations.opsForZSet().score(FlushStatus.PENDING.getKey(), gameSaveId);
              if (score == null || score > currentTime) {
                return false;
              }

              // Start transaction
              operations.multi();

              // Remove from sorted set and add to processing set
              operations.opsForZSet().remove(FlushStatus.PENDING.getKey(), gameSaveId);
              operations.opsForSet().add(FlushStatus.PROCESSING.getKey(), gameSaveId);

              // Execute transaction
              List<Object> results = operations.exec();
              // Check if both operations were successful
              var result1 = (Long) results.get(0);
              var result2 = (Long) results.get(1);
              return result1 == 1 && result2 == 1;
            }
          });
    } catch (Exception e) {
      log.error("Error moving game save {} to processing set using transaction", gameSaveId, e);
      return false;
    }
  }
}
