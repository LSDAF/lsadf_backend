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
import com.lsadf.core.infra.valkey.cache.adapter.game.save.characteristics.CharacteristicsCacheRepositoryAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.currency.CurrencyCacheRepositoryAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.metadata.GameMetadataCacheRepositoryAdapter;
import com.lsadf.core.infra.valkey.cache.adapter.game.save.stage.StageCacheRepositoryAdapter;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHashRepository;
import com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHashRepository;
import com.lsadf.core.infra.valkey.cache.impl.save.metadata.GameMetadataHashRepository;
import com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHashRepository;
import com.lsadf.core.infra.valkey.config.properties.ValkeyCacheExpirationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(
    basePackages = "com.lsadf.core.infra.valkey.cache",
    shadowCopy = RedisKeyValueAdapter.ShadowCopy.OFF,
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.OFF)
public class ValkeyCacheRepositoryConfiguration {
  @Bean
  public CharacteristicsCachePort characteristicsCachePort(
      CharacteristicsHashRepository characteristicsHashRepository,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new CharacteristicsCacheRepositoryAdapter(
        characteristicsHashRepository, valkeyCacheExpirationProperties);
  }

  @Bean
  public CurrencyCachePort currencyCachePort(
      CurrencyHashRepository currencyHashRepository,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new CurrencyCacheRepositoryAdapter(
        currencyHashRepository, valkeyCacheExpirationProperties);
  }

  @Bean
  public GameMetadataCachePort gameMetadataCachePort(
      GameMetadataHashRepository gameMetadataHashRepository,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new GameMetadataCacheRepositoryAdapter(
        gameMetadataHashRepository, valkeyCacheExpirationProperties);
  }

  @Bean
  public StageCachePort stageCachePort(
      StageHashRepository stageHashRepository,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    return new StageCacheRepositoryAdapter(stageHashRepository, valkeyCacheExpirationProperties);
  }
}
