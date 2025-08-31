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
package com.lsadf.core.application.game.save.config;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.characteristics.config.CharacteristicsConfiguration;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.currency.config.CurrencyConfiguration;
import com.lsadf.core.application.game.save.impl.GameSaveServiceImpl;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.metadata.config.GameMetadataConfiguration;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.game.save.stage.config.StageConfiguration;
import com.lsadf.core.application.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
@Import({
  CharacteristicsConfiguration.class,
  StageConfiguration.class,
  CurrencyConfiguration.class,
  GameMetadataConfiguration.class
})
public class GameSaveConfiguration {

  public static final String GAME_SAVE_OWNERSHIP_CACHE = "gameSaveOwnershipCache";

  @Bean
  public GameSaveService gameSaveService(
      UserService userService,
      GameMetadataService gameMetadataService,
      GameSaveRepositoryPort gameSaveRepositoryPort,
      CharacteristicsService characteristicsService,
      StageService stageService,
      CurrencyService currencyService,
      CharacteristicsCachePort characteristicsCache,
      CurrencyCachePort currencyCache,
      StageCachePort stageCache,
      GameMetadataCachePort gameMetadataCache,
      CacheManager cacheManager) {
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
}
