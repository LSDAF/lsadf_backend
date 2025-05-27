/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.lsadf.core.application.game;

import com.lsadf.core.application.game.characteristics.CharacteristicsConfiguration;
import com.lsadf.core.application.game.currency.CurrencyConfiguration;
import com.lsadf.core.application.game.game_save.GameSaveConfiguration;
import com.lsadf.core.application.game.inventory.InventoryConfiguration;
import com.lsadf.core.application.game.stage.StageConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for setting up and integrating various service components within the game
 * service module. This class imports multiple configurations responsible for providing necessary
 * beans and dependencies for specific functionality areas in the game framework.
 *
 * <p>Imported Configurations:
 *
 * <p>- {@link CharacteristicsConfiguration}: Configures and provides beans related to the
 * characteristics service, facilitating management of entity characteristics and caching
 * mechanisms.
 *
 * <p>- {@link CurrencyConfiguration}: Handles the setup of the CurrencyService and related
 * dependencies for managing in-game currency operations.
 *
 * <p>- {@link GameSaveConfiguration}: Configures the GameSaveService and its associated
 * dependencies such as repositories, caches, and other service beans for managing game save data.
 *
 * <p>- {@link InventoryConfiguration}: Provides the configuration for inventory management,
 * including services for handling inventory creation, updates, and persistence.
 *
 * <p>- {@link StageConfiguration}: Manages the configuration of the StageService, enabling
 * stage-related processing and caching functionalities.
 *
 * <p>This configuration serves as a centralized module for assembling the various services required
 * for the core game logic, ensuring proper dependency wiring and efficient data management.
 */
@Configuration
@Import({
  CharacteristicsConfiguration.class,
  CurrencyConfiguration.class,
  GameSaveConfiguration.class,
  InventoryConfiguration.class,
  StageConfiguration.class
})
public class GameServiceConfiguration {}
