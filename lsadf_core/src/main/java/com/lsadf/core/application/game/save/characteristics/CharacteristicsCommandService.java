/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.application.game.save.characteristics;

import com.lsadf.core.application.game.save.characteristics.command.InitializeCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.InitializeDefaultCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;

/**
 * Service interface for handling commands related to the management and initialization of game save
 * characteristics. Provides methods for initializing, persisting, and updating characteristics
 * based on specific command inputs.
 */
public interface CharacteristicsCommandService {
  /**
   * Initializes the default characteristics for a specified game save. The characteristics are set
   * to predefined default values.
   *
   * @param command an instance of {@code InitializeDefaultCharacteristicsCommand} containing the
   *     unique identifier of the game save
   * @return the initialized {@code Characteristics} instance with default values
   */
  Characteristics initializeDefaultCharacteristics(InitializeDefaultCharacteristicsCommand command);

  /**
   * Initializes the characteristics of a game save based on the provided command. This method uses
   * the values supplied in the command to create or update a set of {@code Characteristics}.
   *
   * @param command an instance of {@code InitializeCharacteristicsCommand} containing the game save
   *     ID and optional characteristic values (attack, critChance, critDamage, health, resistance)
   * @return the initialized {@code Characteristics} reflecting the provided values or default state
   */
  Characteristics initializeCharacteristics(InitializeCharacteristicsCommand command);

  /**
   * Persists the characteristics of a game save based on the provided command. The method saves or
   * updates the characteristics values in the underlying storage.
   *
   * @param command an instance of {@code PersistCharacteristicsCommand} containing the game save ID
   *     and the characteristics values to be persisted
   */
  void persistCharacteristics(PersistCharacteristicsCommand command);

  /**
   * Updates the cached characteristics with the provided values from the given command.
   *
   * @param command an instance of {@code UpdateCacheCharacteristicsCommand} containing the game
   *     save ID and the new characteristic values to apply to the cache
   */
  void updateCacheCharacteristics(UpdateCacheCharacteristicsCommand command);
}
