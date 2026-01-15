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
package com.lsadf.core.application.game.save.currency.impl;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.currency.CurrencyQueryService;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.Optional;
import java.util.UUID;

public class CurrencyQueryServiceImpl implements CurrencyQueryService {

  private final CacheManager cacheManager;
  private final CurrencyRepositoryPort currencyRepositoryPort;
  private final CachePort<Currency> currencyCache;

  public CurrencyQueryServiceImpl(
      CacheManager cacheManager,
      CurrencyRepositoryPort currencyRepositoryPort,
      CachePort<Currency> currencyCache) {
    this.cacheManager = cacheManager;
    this.currencyRepositoryPort = currencyRepositoryPort;
    this.currencyCache = currencyCache;
  }

  @Override
  public Currency retrieveCurrency(UUID gameSaveId) throws NotFoundException {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Currency> optionalCachedCurrency = currencyCache.get(gameSaveIdString);
      if (optionalCachedCurrency.isPresent()) {
        return optionalCachedCurrency.get();
      }
    }
    return getCurrencyFromDatabase(gameSaveId);
  }

  /**
   * Get the currency from the database
   *
   * @param gameSaveId the id of the game save
   * @return the currency
   * @throws NotFoundException if the currency is not found
   */
  private Currency getCurrencyFromDatabase(UUID gameSaveId) throws NotFoundException {
    return currencyRepositoryPort
        .findById(gameSaveId)
        .orElseThrow(
            () -> new NotFoundException("Currency not found for game save id " + gameSaveId));
  }
}
