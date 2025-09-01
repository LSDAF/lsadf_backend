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
package com.lsadf.core.infra.valkey.cache.manager;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.infra.valkey.config.properties.ValkeyProperties;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValkeyCacheManager implements CacheManager {

  private final GameMetadataCachePort gameMetadataCache;
  private final CharacteristicsCachePort characteristicsCache;
  private final CurrencyCachePort currencyCache;
  private final StageCachePort stageCache;

  private final AtomicBoolean isEnabled;

  public ValkeyCacheManager(
      GameMetadataCachePort gameMetadataCache,
      CharacteristicsCachePort characteristicsCache,
      CurrencyCachePort currencyCache,
      StageCachePort stageCache,
      ValkeyProperties valkeyProperties) {
    this.characteristicsCache = characteristicsCache;
    this.currencyCache = currencyCache;
    this.stageCache = stageCache;
    this.gameMetadataCache = gameMetadataCache;
    this.isEnabled = new AtomicBoolean(valkeyProperties.isEnabled());
  }

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
  }

  @Override
  public void clearCaches() {
    log.info("Clearing all caches");
    characteristicsCache.clear();
    currencyCache.clear();
    stageCache.clear();
    gameMetadataCache.clear();
    log.info("Caches cleared");
  }

  @Override
  public void clearGameSaveValues(String key) {
    log.info("Clearing game caches for key: {}", key);
    characteristicsCache.unset(key);
    currencyCache.unset(key);
    stageCache.unset(key);
    gameMetadataCache.unset(key);
    log.info("CachePort cleared for key: {}", key);
  }
}
