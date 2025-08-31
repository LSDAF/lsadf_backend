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
package com.lsadf.core.infra.valkey.config.cache;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.characteristics.NoOpCharacteristicsCacheAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.currency.NoOpCurrencyCacheAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.metadata.NoOpGameMetadataCacheAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.stage.NoOpStageCacheAdapter;
import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.impl.NoOpFlushServiceImpl;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import com.lsadf.core.infra.valkey.cache.manager.impl.NoOpCacheManagerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "valkey.config", name = "enabled", havingValue = "false")
public class NoValkeyCacheConfiguration {

  @Bean
  public CacheManager noOpCacheService() {
    return new NoOpCacheManagerImpl();
  }

  @Bean
  public CacheFlushService noOpCacheFlushService() {
    return new NoOpFlushServiceImpl();
  }

  @Bean
  public CharacteristicsCachePort noOpCharacteristicsCachePort() {
    return new NoOpCharacteristicsCacheAdapter();
  }

  @Bean
  public CurrencyCachePort noOpCurrencyCachePort() {
    return new NoOpCurrencyCacheAdapter();
  }

  @Bean
  public StageCachePort noOpStageCachePort() {
    return new NoOpStageCacheAdapter();
  }

  @Bean
  public GameMetadataCachePort noOpGameMetadataCachePort() {
    return new NoOpGameMetadataCacheAdapter();
  }
}
