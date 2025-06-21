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
package com.lsadf.core.application.game.game_save;

import static com.lsadf.core.infra.cache.RedisConstants.GAME_SAVE_OWNERSHIP;

import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.ValkeyCache;
import com.lsadf.core.infra.cache.config.ValkeyProperties;
import com.lsadf.core.infra.cache.properties.CacheExpirationProperties;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntityMapper;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveRepository;
import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.stage.StageEntityMapper;
import com.lsadf.core.infra.persistence.game.stage.StageRepository;
import com.lsadf.core.infra.web.response.game.characteristics.CharacteristicsResponseMapper;
import com.lsadf.core.infra.web.response.game.currency.CurrencyResponseMapper;
import com.lsadf.core.infra.web.response.game.game_save.GameSaveResponseMapper;
import com.lsadf.core.infra.web.response.game.stage.StageResponseMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Configuration class for managing the setup of the Game Save related services and components.
 * Manages the creation and initialization of the {@link GameSaveService} and its dependencies. This
 * configuration ensures that all required repositories, caches, and services are properly injected
 * and wired to enable the functionality of the Game Save module.
 *
 * <p>Components provided include: - User Service for managing user-related operations. - Game Save
 * Repository to handle database operations for game saves. - Inventory, Stage, Characteristics, and
 * Currency Repositories for related entities. - Various caches to optimize access and historical
 * tracking for different entities.
 */
@Configuration
public class GameSaveConfiguration {

  public static final String GAME_SAVE_OWNERSHIP_CACHE = "gameSaveOwnershipCache";

  @Bean
  public GameSaveService gameSaveService(
      UserService userService,
      GameSaveRepository gameSaveRepository,
      CharacteristicsRepository characteristicsRepository,
      CurrencyRepository currencyRepository,
      StageRepository stageRepository,
      InventoryRepository inventoryRepository,
      HistoCache<Stage> stageCache,
      HistoCache<Currency> currencyCache,
      HistoCache<Characteristics> characteristicsCache,
      @Qualifier(GAME_SAVE_OWNERSHIP_CACHE) Cache<String> gameSaveOwnershipCache,
      CacheService cacheService,
      GameSaveEntityMapper gameSaveEntityModelMapper) {
    return new GameSaveServiceImpl(
        userService,
        gameSaveRepository,
        characteristicsRepository,
        currencyRepository,
        stageRepository,
        inventoryRepository,
        gameSaveEntityModelMapper,
        cacheService,
        gameSaveOwnershipCache,
        stageCache,
        currencyCache,
        characteristicsCache);
  }

  @Bean
  public GameSaveEntityMapper gameSaveEntityModelMapper(
      CharacteristicsEntityMapper characteristicsMapper,
      CurrencyEntityMapper currencyMapper,
      StageEntityMapper stageMapper) {
    return new GameSaveEntityMapper(characteristicsMapper, stageMapper, currencyMapper);
  }

  @Bean
  public GameSaveResponseMapper gameSaveResponseMapper(
      CurrencyResponseMapper currencyResponseMapper,
      CharacteristicsResponseMapper characteristicsResponseMapper,
      StageResponseMapper stageResponseMapper) {
    return new GameSaveResponseMapper(
        currencyResponseMapper, characteristicsResponseMapper, stageResponseMapper);
  }

  @Bean(name = GAME_SAVE_OWNERSHIP_CACHE)
  public Cache<String> gameSaveOwnershipCache(
      RedisTemplate<String, String> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      ValkeyProperties valkeyProperties) {
    return new ValkeyCache<>(
        redisTemplate,
        GAME_SAVE_OWNERSHIP,
        cacheExpirationProperties.getGameSaveOwnershipExpirationSeconds(),
        valkeyProperties);
  }
}
