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

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import com.lsadf.core.infra.valkey.cache.util.ValkeyFlushUtils;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class RedisCacheFlushServiceImpl implements CacheFlushService {

  private final CharacteristicsCommandService characteristicsService;
  private final CharacteristicsCachePort characteristicsCache;

  private final CurrencyService currencyService;
  private final CurrencyCachePort currencyCache;

  private final StageService stageService;
  private final StageCachePort stageCache;

  private final RedisTemplate<String, String> redisTemplate;

  public RedisCacheFlushServiceImpl(
      CharacteristicsCommandService characteristicsService,
      CurrencyService currencyService,
      StageService stageService,
      CharacteristicsCachePort characteristicsCache,
      CurrencyCachePort currencyCache,
      StageCachePort stageCache,
      RedisTemplate<String, String> redisTemplate) {
    this.characteristicsService = characteristicsService;
    this.currencyService = currencyService;
    this.stageService = stageService;
    this.characteristicsCache = characteristicsCache;
    this.currencyCache = currencyCache;
    this.stageCache = stageCache;
    this.redisTemplate = redisTemplate;
  }

  private void flushCharacteristics(UUID gameSaveId) {
    try {
      Optional<Characteristics> characteristicsOpt =
          characteristicsCache.get(gameSaveId.toString());
      if (characteristicsOpt.isPresent()) {
        Characteristics characteristics = characteristicsOpt.get();
        PersistCharacteristicsCommand command =
            PersistCharacteristicsCommand.fromCharacteristics(gameSaveId, characteristics);
        characteristicsService.persistCharacteristics(command);
        characteristicsCache.unset(gameSaveId.toString());
        log.debug("Successfully flushed characteristics for game save {}", gameSaveId);
      }
    } catch (Exception e) {
      log.error("Error flushing characteristics for game save {}", gameSaveId, e);
      throw e;
    }
  }

  private void flushCurrency(UUID gameSaveId) {
    try {
      Optional<Currency> currencyOpt = currencyCache.get(gameSaveId.toString());
      if (currencyOpt.isPresent()) {
        Currency currency = currencyOpt.get();
        currencyService.saveCurrency(gameSaveId, currency, false);
        currencyCache.unset(gameSaveId.toString());
        log.debug("Successfully flushed currency for game save {}", gameSaveId);
      }
    } catch (Exception e) {
      log.error("Error flushing currency for game save {}", gameSaveId, e);
      throw e;
    }
  }

  private void flushStage(UUID gameSaveId) {
    try {
      Optional<Stage> stageOpt = stageCache.get(gameSaveId.toString());
      if (stageOpt.isPresent()) {
        Stage stage = stageOpt.get();
        stageService.saveStage(gameSaveId, stage, false);
        stageCache.unset(gameSaveId.toString());
        log.debug("Successfully flushed stage for game save {}", gameSaveId);
      }
    } catch (Exception e) {
      log.error("Error flushing stage for game save {}", gameSaveId, e);
      throw e;
    }
  }

  @Override
  @Transactional
  public void flushGameSave(UUID gameSaveId) {
    flush(gameSaveId);
  }

  @Override
  @Transactional
  public void flushGameSaves() {
    long currentTime = System.currentTimeMillis();

    // Get all entries from both pending and processing sets
    Set<String> pendingEntries =
        redisTemplate.opsForZSet().rangeByScore(FlushStatus.PENDING.getKey(), 0, -1);
    Set<String> processingEntries =
        redisTemplate.opsForSet().members(FlushStatus.PROCESSING.getKey());

    // Process pending entries
    if (pendingEntries != null) {
      pendingEntries.forEach(
          gameSaveId -> {
            if (ValkeyFlushUtils.moveToProcessingWithTransaction(
                redisTemplate, gameSaveId, currentTime)) {
              try {
                flush(UUID.fromString(gameSaveId));
                ValkeyFlushUtils.removeFromProcessing(redisTemplate, gameSaveId);
              } catch (Exception e) {
                log.error("Error processing pending flush for game save {}", gameSaveId, e);
              }
            }
          });
    }

    // Process entries that were already in processing (recovery)
    if (processingEntries != null) {
      processingEntries.forEach(
          gameSaveId -> {
            try {
              flush(UUID.fromString(gameSaveId));
              ValkeyFlushUtils.removeFromProcessing(redisTemplate, gameSaveId);
            } catch (Exception e) {
              log.error("Error processing existing flush for game save {}", gameSaveId, e);
            }
          });
    }
  }

  private void flush(UUID gameSaveId) {
    log.debug("Flushing all data for game save {}", gameSaveId);
    try {
      flushCharacteristics(gameSaveId);
      flushCurrency(gameSaveId);
      flushStage(gameSaveId);
    } catch (Exception e) {
      log.error("Error flushing game save {}", gameSaveId, e);
      throw e;
    }
  }
}
