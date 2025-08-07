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
package com.lsadf.core.application.game;

import com.lsadf.core.application.game.inventory.InventoryConfiguration;
import com.lsadf.core.application.game.save.config.GameSaveConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for initializing game service functionalities.
 *
 * <p>Aggregates and imports configurations required for game-related services, such as game saves
 * and inventory management. By importing the required configurations, it ensures proper setup and
 * integration of the Game Save and Inventory service modules into the Spring application context.
 *
 * <p>GameSaveConfiguration handles the setup of components and dependencies relevant to game save
 * management, including services, repositories, and caches.
 *
 * <p>InventoryConfiguration manages the initialization of inventory service components, responsible
 * for functionalities related to item repositories and metadata management.
 *
 * <p>This class serves as a central configuration hub for game-related service management and is
 * utilized in higher-level application configurations.
 */
@Configuration
@Import({GameSaveConfiguration.class, InventoryConfiguration.class})
public class GameServiceConfiguration {}
