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
package com.lsadf.core.services.impl;

import com.lsadf.core.cache.Cache;
import com.lsadf.core.cache.HistoCache;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.models.Currency;
import com.lsadf.core.models.Stage;
import com.lsadf.core.services.CacheService;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisCacheServiceImpl implements CacheService {

  private final Cache<String> gameSaveOwnershipCache;
  private final HistoCache<Characteristics> characteristicsCache;
  private final HistoCache<Currency> currencyCache;
  private final HistoCache<Stage> stageCache;

  private final AtomicBoolean isEnabled = new AtomicBoolean(true);

  public RedisCacheServiceImpl(
      Cache<String> gameSaveOwnershipCache,
      HistoCache<Characteristics> characteristicsCache,
      HistoCache<Currency> currencyCache,
      HistoCache<Stage> stageCache) {
    this.characteristicsCache = characteristicsCache;
    this.currencyCache = currencyCache;
    this.stageCache = stageCache;
    this.gameSaveOwnershipCache = gameSaveOwnershipCache;
  }

  /** {@inheritDoc} */
  @Override
  public Boolean isEnabled() {
    return this.isEnabled.get();
  }

  @Override
  public void toggleCacheEnabling() {
    boolean oldValue = isEnabled.get();
    boolean newValue = !oldValue;
    log.info(oldValue ? "Disabling redis caches" : "Enabling redis caches");
    isEnabled.set(newValue);
    characteristicsCache.setEnabled(newValue);
    currencyCache.setEnabled(newValue);
    stageCache.setEnabled(newValue);
    gameSaveOwnershipCache.setEnabled(newValue);
  }

  /** {@inheritDoc} */
  @Override
  public void clearCaches() {
    log.info("Clearing all caches");
    characteristicsCache.clear();
    currencyCache.clear();
    stageCache.clear();
    gameSaveOwnershipCache.clear();
    log.info("Caches cleared");
  }
}
