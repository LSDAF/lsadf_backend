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
package com.lsadf.core.infra.config;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.inventory.impl.InventoryServiceImpl;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsQueryService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.impl.CharacteristicsCommandServiceImpl;
import com.lsadf.core.application.game.save.characteristics.impl.CharacteristicsQueryServiceImpl;
import com.lsadf.core.application.game.save.currency.*;
import com.lsadf.core.application.game.save.currency.impl.CurrencyCommandServiceImpl;
import com.lsadf.core.application.game.save.currency.impl.CurrencyQueryServiceImpl;
import com.lsadf.core.application.game.save.impl.GameSaveServiceImpl;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.metadata.impl.GameMetadataServiceImpl;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.StageQueryService;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.impl.StageCommandServiceImpl;
import com.lsadf.core.application.game.save.stage.impl.StageQueryServiceImpl;
import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.application.game.session.GameSessionCommandService;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.application.game.session.impl.GameSessionCommandServiceImpl;
import com.lsadf.core.application.game.session.impl.GameSessionQueryServiceImpl;
import com.lsadf.core.application.info.GlobalInfoService;
import com.lsadf.core.application.info.impl.GlobalInfoServiceImpl;
import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.application.search.impl.SearchServiceImpl;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.application.user.impl.UserServiceImpl;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.web.config.keycloak.properties.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for the services. */
@Configuration
public class ApplicationServiceConfiguration {

  @Bean
  public GameSaveService gameSaveService(
      GameMetadataService gameMetadataService,
      CharacteristicsCommandService characteristicsService,
      StageCommandService stageService,
      CurrencyCommandService currencyService,
      UserService userService,
      GameSaveRepositoryPort gameSaveRepositoryPort,
      CacheManager cacheManager,
      GameMetadataCachePort gameMetadataCache,
      StageCachePort stageCache,
      CurrencyCachePort currencyCache,
      CharacteristicsCachePort characteristicsCache) {
    return new GameSaveServiceImpl(
        gameMetadataService,
        characteristicsService,
        stageService,
        currencyService,
        userService,
        gameSaveRepositoryPort,
        cacheManager,
        gameMetadataCache,
        stageCache,
        currencyCache,
        characteristicsCache);
  }

  @Bean
  public CharacteristicsQueryService characteristicsQueryService(
      CacheManager cacheManager,
      CharacteristicsRepositoryPort characteristicsRepositoryPort,
      CachePort<Characteristics> characteristicsCache) {
    return new CharacteristicsQueryServiceImpl(
        cacheManager, characteristicsRepositoryPort, characteristicsCache);
  }

  @Bean
  public CharacteristicsCommandService characteristicsCommandService(
      CacheManager cacheManager,
      CharacteristicsRepositoryPort characteristicsRepositoryPort,
      CachePort<Characteristics> characteristicsCache,
      CharacteristicsQueryService characteristicsQueryService) {
    return new CharacteristicsCommandServiceImpl(
        cacheManager,
        characteristicsRepositoryPort,
        characteristicsCache,
        characteristicsQueryService);
  }

  @Bean
  public StageQueryService stageQueryService(
      CacheManager cacheManager,
      StageRepositoryPort stageRepositoryPort,
      CachePort<com.lsadf.core.domain.game.save.stage.Stage> stageCache) {
    return new StageQueryServiceImpl(cacheManager, stageRepositoryPort, stageCache);
  }

  @Bean
  public StageCommandService stageCommandService(
      CacheManager cacheManager,
      StageRepositoryPort stageRepositoryPort,
      CachePort<com.lsadf.core.domain.game.save.stage.Stage> stageCache,
      StageQueryService stageQueryService) {
    return new StageCommandServiceImpl(
        cacheManager, stageRepositoryPort, stageCache, stageQueryService);
  }

  @Bean
  public CurrencyQueryService currencyQueryService(
      CacheManager cacheManager,
      CurrencyRepositoryPort currencyRepositoryPort,
      CachePort<com.lsadf.core.domain.game.save.currency.Currency> currencyCache) {
    return new CurrencyQueryServiceImpl(cacheManager, currencyRepositoryPort, currencyCache);
  }

  @Bean
  public CurrencyCommandService currencyCommandService(
      CacheManager cacheManager,
      CurrencyRepositoryPort currencyRepositoryPort,
      CachePort<com.lsadf.core.domain.game.save.currency.Currency> currencyCache,
      CurrencyQueryService currencyQueryService) {
    return new CurrencyCommandServiceImpl(
        cacheManager, currencyRepositoryPort, currencyCache, currencyQueryService);
  }

  @Bean
  public InventoryService inventoryService(
      InventoryRepositoryPort inventoryRepositoryPort, GameMetadataService gameMetadataService) {
    return new InventoryServiceImpl(inventoryRepositoryPort, gameMetadataService);
  }

  @Bean
  public SearchService searchService(UserService userService, GameSaveService gameSaveService) {
    return new SearchServiceImpl(userService, gameSaveService);
  }

  @Bean
  public UserService userService(
      Keycloak keycloak, KeycloakProperties keycloakProperties, ClockService clockService) {
    return new UserServiceImpl(keycloak, keycloakProperties, clockService);
  }

  @Bean
  public GlobalInfoService globalInfoService(
      UserService userService, GameSaveService gameSaveService, ClockService clockService) {
    return new GlobalInfoServiceImpl(userService, clockService, gameSaveService);
  }

  @Bean
  public GameMetadataService gameMetadataService(
      CacheManager cacheManager,
      GameMetadataRepositoryPort gameMetadataRepositoryPort,
      GameMetadataCachePort gameMetadataCachePort) {
    return new GameMetadataServiceImpl(
        cacheManager, gameMetadataRepositoryPort, gameMetadataCachePort);
  }

  @Bean
  public GameSessionCommandService gameSessionCommandService(
      GameSessionRepositoryPort gameSessionRepositoryPort,
      CacheManager cacheManager,
      GameSessionCachePort gameSessionCachePort,
      ClockService clockService) {
    return new GameSessionCommandServiceImpl(
        gameSessionRepositoryPort, cacheManager, gameSessionCachePort, clockService);
  }

  @Bean
  public GameSessionQueryService gameSessionQueryService(
      GameSessionRepositoryPort gameSessionRepositoryPort,
      GameSessionCachePort gameSessionCachePort,
      CacheManager cacheManager) {
    return new GameSessionQueryServiceImpl(
        gameSessionRepositoryPort, gameSessionCachePort, cacheManager);
  }
}
