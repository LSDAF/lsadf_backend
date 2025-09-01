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
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.characteristics.impl.CharacteristicsServiceImpl;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.currency.impl.CurrencyServiceImpl;
import com.lsadf.core.application.game.save.impl.GameSaveServiceImpl;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.metadata.impl.GameMetadataServiceImpl;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.game.save.stage.impl.StageServiceImpl;
import com.lsadf.core.application.info.GlobalInfoService;
import com.lsadf.core.application.info.impl.GlobalInfoServiceImpl;
import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.application.search.impl.SearchServiceImpl;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.application.user.impl.UserServiceImpl;
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
      CharacteristicsService characteristicsService,
      StageService stageService,
      CurrencyService currencyService,
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
  public StageService stageService(
      CacheManager cacheManager,
      StageRepositoryPort stageRepositoryPort,
      StageCachePort stageCache) {
    return new StageServiceImpl(cacheManager, stageRepositoryPort, stageCache);
  }

  @Bean
  public CharacteristicsService characteristicsService(
      CacheManager cacheManager,
      CharacteristicsRepositoryPort characteristicsRepositoryPort,
      CharacteristicsCachePort characteristicsCachePort) {
    return new CharacteristicsServiceImpl(
        cacheManager, characteristicsRepositoryPort, characteristicsCachePort);
  }

  @Bean
  public CurrencyService currencyService(
      CacheManager cacheManager,
      CurrencyRepositoryPort currencyRepositoryPort,
      CurrencyCachePort currencyCache) {
    return new CurrencyServiceImpl(cacheManager, currencyRepositoryPort, currencyCache);
  }
}
