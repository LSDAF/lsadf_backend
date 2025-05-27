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
package com.lsadf.core.configurations;

import static com.lsadf.core.infra.config.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;

import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.characteristics.CharacteristicsServiceImpl;
import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.application.game.currency.CurrencyServiceImpl;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.game.game_save.GameSaveServiceImpl;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.inventory.InventoryServiceImpl;
import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.core.application.game.stage.StageServiceImpl;
import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.application.search.SearchServiceImpl;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.application.user.UserServiceImpl;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.clock.ClockService;
import com.lsadf.core.infra.clock.ClockServiceImpl;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.game.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveRepository;
import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemRepository;
import com.lsadf.core.infra.persistence.game.stage.StageRepository;
import com.lsadf.core.infra.persistence.mappers.Mapper;
import com.lsadf.core.infra.persistence.mappers.MapperImpl;
import com.lsadf.core.infra.web.config.auth.keycloak.KeycloakProperties;
import java.time.Clock;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for the services. */
@Configuration
public class ServiceConfiguration {

  @Bean
  public CharacteristicsService characteristicsService(
      CharacteristicsRepository characteristicsRepository,
      Cache<Characteristics> characteristicsCache,
      Mapper mapper) {
    return new CharacteristicsServiceImpl(characteristicsRepository, characteristicsCache, mapper);
  }

  @Bean
  public CurrencyService currencyService(
      CurrencyRepository currencyRepository, Cache<Currency> currencyCache, Mapper mapper) {
    return new CurrencyServiceImpl(currencyRepository, currencyCache, mapper);
  }

  @Bean
  public GameSaveService gameSaveService(
      UserService userService,
      GameSaveRepository gameSaveRepository,
      InventoryRepository inventoryRepository,
      StageRepository stageRepository,
      CharacteristicsRepository characteristicsRepository,
      CurrencyRepository currencyRepository,
      @Qualifier(GAME_SAVE_OWNERSHIP_CACHE) Cache<String> gameSaveOwnershipCache,
      HistoCache<Stage> stageHistoCache,
      HistoCache<Characteristics> characteristicsHistoCache,
      HistoCache<Currency> currencyHistoCache) {
    return new GameSaveServiceImpl(
        userService,
        gameSaveRepository,
        inventoryRepository,
        stageRepository,
        characteristicsRepository,
        currencyRepository,
        gameSaveOwnershipCache,
        stageHistoCache,
        characteristicsHistoCache,
        currencyHistoCache);
  }

  @Bean
  public InventoryService inventoryService(
      InventoryRepository inventoryRepository, ItemRepository itemRepository) {
    return new InventoryServiceImpl(inventoryRepository, itemRepository);
  }

  @Bean
  public StageService stageService(
      StageRepository stageRepository, Cache<Stage> stageCache, Mapper mapper) {
    return new StageServiceImpl(stageRepository, stageCache, mapper);
  }

  @Bean
  public SearchService searchService(
      UserService userService, GameSaveService gameSaveService, Mapper mapper) {
    return new SearchServiceImpl(userService, gameSaveService, mapper);
  }

  @Bean
  public UserService userService(
      Keycloak keycloak,
      KeycloakProperties keycloakProperties,
      ClockService clockService,
      Mapper mapper) {
    return new UserServiceImpl(keycloak, keycloakProperties, clockService, mapper);
  }

  @Bean
  public Mapper mapper() {
    return new MapperImpl();
  }

  @Bean
  public ClockService clockService(Clock clock) {
    return new ClockServiceImpl(clock);
  }
}
