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
package com.lsadf.core.infra.valkey.cache.flush.scheduler.impl;

import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import com.lsadf.core.infra.valkey.cache.flush.scheduler.FlushScheduler;
import com.lsadf.core.infra.valkey.cache.util.ValkeyFlushUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Implementation of the {@link FlushScheduler} interface that manages the periodic flushing of game
 * saves to persistent storage using a Redis-based solution. The class schedules tasks to check for
 * game saves ready for flushing and processes them accordingly.
 *
 * <p>The scheduler relies on a Redis sorted set to track pending flush game saves and their
 * corresponding timestamps. Game saves that are ready for flushing are moved to a separate
 * processing set before being flushed to ensure atomicity and prevent race conditions.
 */
@Slf4j
public class FlushSchedulerImpl implements FlushScheduler {

  private final RedisTemplate<String, String> redisTemplate;
  private final CacheFlushService cacheFlushService;
  private final ScheduledExecutorService scheduler;
  private final long checkIntervalSeconds;

  public FlushSchedulerImpl(
      RedisTemplate<String, String> redisTemplate,
      CacheFlushService cacheFlushService,
      ScheduledExecutorService scheduler,
      long checkIntervalSeconds) {
    this.redisTemplate = redisTemplate;
    this.cacheFlushService = cacheFlushService;
    this.scheduler = scheduler;
    this.checkIntervalSeconds = checkIntervalSeconds;
  }

  @PostConstruct
  public void startScheduler() {
    log.info(
        "Starting ZSET flush scheduler with check interval of {} seconds", checkIntervalSeconds);
    scheduler.scheduleWithFixedDelay(
        this::processEntriesToFlush, 0, checkIntervalSeconds, TimeUnit.SECONDS);
  }

  @PreDestroy
  public void stopScheduler() {
    log.info("Stopping ZSET flush scheduler");
    scheduler.shutdown();
    try {
      if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
        scheduler.shutdownNow();
      }
    } catch (InterruptedException e) {
      scheduler.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public void processEntriesToFlush() {
    long currentTime = System.currentTimeMillis();

    try {
      // Get entries ready to flush (score <= currentTime)
      Set<String> readyEntries =
          redisTemplate.opsForZSet().rangeByScore(FlushStatus.PENDING.getKey(), 0, currentTime);

      if (readyEntries == null || readyEntries.isEmpty()) {
        log.debug("No entries ready for flushing");
        return;
      }

      log.info("Found {} entries ready for flushing", readyEntries.size());

      int successProcessed = 0;

      for (String gameSaveId : readyEntries) {
        if (processGameSaveFlush(gameSaveId, currentTime)) successProcessed += 1;
      }

      log.info("Successfully flushed {} entries", successProcessed);
    } catch (Exception e) {
      log.error("Error processing ready entries for flushing", e);
    }
  }

  private boolean processGameSaveFlush(String gameSaveId, long currentTime) {
    if (!ValkeyFlushUtils.moveToProcessingWithTransaction(redisTemplate, gameSaveId, currentTime)) {
      log.error("Failed to move game save {} to processing set", gameSaveId);
      return false;
    }
    flushGameSave(gameSaveId);
    ValkeyFlushUtils.removeFromProcessing(redisTemplate, gameSaveId);
    log.debug("Successfully processed flush for game save {}", gameSaveId);
    return true;
  }

  private void flushGameSave(String gameSaveId) {
    try {
      UUID gameSaveUuid = UUID.fromString(gameSaveId);
      cacheFlushService.flushGameSave(gameSaveUuid);
      log.debug("Successfully flushed game save {} to database", gameSaveId);
    } catch (Exception e) {
      log.error("Error flushing game save {} to database", gameSaveId, e);
      throw e;
    }
  }
}
