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
package com.lsadf.core.application.game.save.currency;

import com.lsadf.core.application.game.save.currency.command.InitializeCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.InitializeDefaultCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.PersistCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.UpdateCacheCurrencyCommand;
import com.lsadf.core.domain.game.save.currency.Currency;

/**
 * Defines the contract for handling commands related to {@code Currency} objects in the context of
 * game saves. This service is responsible for initializing, persisting, and updating
 * currency-related data both in storage and in cache.
 */
public interface CurrencyCommandService {
  /**
   * Initializes the default currency for a specific game save.
   *
   * @param command an instance of {@code InitializeDefaultCurrencyCommand} that contains the unique
   *     identifier of the game save
   * @return a {@code Currency} instance initialized with default values
   */
  Currency initializeDefaultCurrency(InitializeDefaultCurrencyCommand command);

  /**
   * Initializes a new currency instance for a given game save with specified values for gold,
   * diamond, emerald, and amethyst.
   *
   * @param command an instance of {@code InitializeCurrencyCommand} that contains the game save ID
   *     and the initial amounts for gold, diamond, emerald, and amethyst
   * @return a new {@code Currency} instance initialized with the provided values
   */
  Currency initializeCurrency(InitializeCurrencyCommand command);

  /**
   * Persists the currency details associated with a specific game save to storage.
   *
   * @param command an instance of {@code PersistCurrencyCommand} containing the game save ID and
   *     the currency amounts for gold, diamond, emerald, and amethyst to be persisted
   */
  void persistCurrency(PersistCurrencyCommand command);

  /**
   * Updates the cached currency values for a specific game save.
   *
   * @param command an instance of {@code UpdateCacheCurrencyCommand} containing the game save ID
   *     and the updated currency values for gold, diamond, emerald, and amethyst
   */
  void updateCacheCurrency(UpdateCacheCurrencyCommand command);
}
