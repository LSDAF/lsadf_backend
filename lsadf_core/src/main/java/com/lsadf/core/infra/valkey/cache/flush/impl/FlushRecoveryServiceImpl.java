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

package com.lsadf.core.infra.valkey.cache.flush.impl;

import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.FlushRecoveryService;
import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class FlushRecoveryServiceImpl implements FlushRecoveryService {

  private final RedisTemplate<String, String> redisTemplate;
  private final CacheFlushService cacheFlushService;

  public FlushRecoveryServiceImpl(
      RedisTemplate<String, String> redisTemplate, CacheFlushService cacheFlushService) {
    this.redisTemplate = redisTemplate;
    this.cacheFlushService = cacheFlushService;
  }

  @EventListener(ApplicationReadyEvent.class)
  @Override
  public void recoverPendingFlush() {
    try {
      Set<String> pendingIds = redisTemplate.opsForSet().members(FlushStatus.PROCESSING.getKey());
      if (pendingIds == null) {
        log.error("Pending flushes set is null");
        return;
      }
      if (pendingIds.isEmpty()) {
        log.debug("No pending flushes found");
        return;
      }
      for (String id : pendingIds) {
        UUID uuid = UUID.fromString(id);
        cacheFlushService.flushGameSave(uuid);
        redisTemplate.opsForSet().remove(FlushStatus.PROCESSING.getKey(), id);
      }
      log.info("Successfully recovered {} pending flushes", pendingIds.size());
    } catch (Exception e) {
      log.error("Error recovering pending flushes", e);
    }
  }
}
