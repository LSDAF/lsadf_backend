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

import static com.lsadf.core.infra.config.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;

import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveRepository;
import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.stage.StageRepository;
import com.lsadf.core.infra.persistence.mappers.game.GameSaveEntityMapper;
import com.lsadf.core.infra.persistence.mappers.game.StageEntityMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
  public GameSaveEntityMapper gameSaveEntityModelMapper(
      CharacteristicsEntityMapper characteristicsMapper,
      CurrencyEntityMapper currencyMapper,
      StageEntityMapper stageMapper) {
    return new GameSaveEntityMapper(characteristicsMapper, stageMapper, currencyMapper);
  }
}
