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
package com.lsadf.core.application.game.save.currency;

import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;

public interface CurrencyService {

  /**
   * Get the currencies of a game save
   *
   * @param gameSaveId the id of the game save
   * @return the currency POJO
   * @throws NotFoundException if the currency entity is not found
   */
  Currency getCurrency(UUID gameSaveId) throws NotFoundException;

  /**
   * Save the currencies of a game save
   *
   * @param gameSaveId the id of the game save
   * @param currency the currency POJO
   * @param toCache true if the currency should be saved to cache, false otherwise
   * @throws NotFoundException
   */
  void saveCurrency(UUID gameSaveId, Currency currency, boolean toCache) throws NotFoundException;

  /**
   * Creates a new {@code Currency} instance associated with the specified game save.
   *
   * @param gameSaveId the unique identifier of the game save for which the new currency is created
   * @return the newly created {@code Currency} object
   */
  Currency createNewCurrency(UUID gameSaveId);

  /**
   * Creates a new currency instance associated with a specific game save, initialized with the
   * specified amounts for gold, diamond, emerald, and amethyst.
   *
   * @param gameSaveId the unique identifier of the game save
   * @param gold the initial amount of gold
   * @param diamond the initial amount of diamond
   * @param emerald the initial amount of emerald
   * @param amethyst the initial amount of amethyst
   * @return the newly created {@link Currency} object
   */
  Currency createNewCurrency(UUID gameSaveId, Long gold, Long diamond, Long emerald, Long amethyst);
}
