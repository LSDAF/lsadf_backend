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
package com.lsadf.core.infra.cache.services;

import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.exceptions.http.NotFoundException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class RedisCacheFlushServiceImpl implements CacheFlushService {

  private final CharacteristicsService characteristicsService;
  private final Cache<Characteristics> characteristicsCache;

  private final CurrencyService currencyService;
  private final Cache<Currency> currencyCache;

  private final InventoryService inventoryService;

  private final StageService stageService;
  private final Cache<Stage> stageCache;

  public RedisCacheFlushServiceImpl(
      CharacteristicsService characteristicsService,
      CurrencyService currencyService,
      InventoryService inventoryService,
      StageService stageService,
      Cache<Characteristics> characteristicsCache,
      Cache<Currency> currencyCache,
      Cache<Stage> stageCache) {
    this.characteristicsService = characteristicsService;
    this.currencyService = currencyService;
    this.inventoryService = inventoryService;
    this.stageService = stageService;
    this.characteristicsCache = characteristicsCache;
    this.currencyCache = currencyCache;
    this.stageCache = stageCache;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void flushCharacteristics() {
    log.info("Flushing characteristics cache");
    Map<String, Characteristics> characteristicsEntries = characteristicsCache.getAll();
    for (Map.Entry<String, Characteristics> entry : characteristicsEntries.entrySet()) {
      String gameSaveId = entry.getKey();
      Characteristics characteristics = entry.getValue();
      try {
        characteristicsService.saveCharacteristics(gameSaveId, characteristics, false);
      } catch (NotFoundException e) {
        log.error(
            "Error while flushing characteristics cache entry: CharacteristicsEntity with id {} not found",
            gameSaveId,
            e);
      } catch (Exception e) {
        log.error("Error while flushing characteristics cache entry", e);
      }
    }

    log.info("Flushed {} characteristics in DB", characteristicsEntries.size());
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void flushCurrencies() {
    log.info("Flushing currency cache");
    Map<String, Currency> currencyEntries = currencyCache.getAll();
    for (Map.Entry<String, Currency> entry : currencyEntries.entrySet()) {
      String gameSaveId = entry.getKey();
      Currency currency = entry.getValue();
      try {
        currencyService.saveCurrency(gameSaveId, currency, false);
      } catch (NotFoundException e) {
        log.error(
            "Error while flushing currency cache entry: CurrencyEntity with id {} not found",
            gameSaveId,
            e);
      } catch (Exception e) {
        log.error("Error while flushing currency cache entry", e);
      }
    }

    log.info("Flushed {} currencies in DB", currencyEntries.size());
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void flushStages() {
    log.info("Flushing stage cache");
    Map<String, Stage> stageEntries = stageCache.getAll();
    for (Map.Entry<String, Stage> entry : stageEntries.entrySet()) {
      String gameSaveId = entry.getKey();
      Stage stage = entry.getValue();
      try {
        stageService.saveStage(gameSaveId, stage, false);
      } catch (NotFoundException e) {
        log.error(
            "Error while flushing stage cache entry: Stage with id {} not found", gameSaveId, e);
      } catch (Exception e) {
        log.error("Error while flushing stage cache entry", e);
      }
    }

    log.info("Flushed {} stages in DB", stageEntries.size());
  }
}
