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
package com.lsadf.core.services;

import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.models.Currency;

public interface CurrencyService {

  /**
   * Get the currencies of a game save
   *
   * @param gameSaveId the id of the game save
   * @return the currency POJO
   * @throws NotFoundException if the currency entity is not found
   */
  Currency getCurrency(String gameSaveId) throws NotFoundException;

  /**
   * Save the currencies of a game save
   *
   * @param gameSaveId the id of the game save
   * @param currency the currency POJO
   * @param toCache true if the currency should be saved to cache, false otherwise
   * @throws NotFoundException
   */
  void saveCurrency(String gameSaveId, Currency currency, boolean toCache) throws NotFoundException;
}
