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
package com.lsadf.core.application.game.save.currency.impl;

import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.valkey.cache.service.CacheService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class CurrencyServiceImpl implements CurrencyService {

  private final CacheService cacheService;

  private final CurrencyRepositoryPort currencyRepositoryPort;
  private final CachePort<Currency> currencyCache;

  public CurrencyServiceImpl(
      CacheService cacheService,
      CurrencyRepositoryPort currencyRepositoryPort,
      CachePort<Currency> currencyCache) {
    this.cacheService = cacheService;
    this.currencyRepositoryPort = currencyRepositoryPort;
    this.currencyCache = currencyCache;
  }

  @Override
  @Transactional(readOnly = true)
  public Currency getCurrency(UUID gameSaveId) throws NotFoundException {
    Currency currency;
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    String gameSaveIdString = gameSaveId.toString();
    if (Boolean.TRUE.equals(cacheService.isEnabled())) {
      Optional<Currency> optionalCachedCurrency = currencyCache.get(gameSaveIdString);
      if (optionalCachedCurrency.isPresent()) {
        currency = optionalCachedCurrency.get();
        if (isCurrencyPartial(currency)) {
          Currency dbCurrency = getCurrencyFromDatabase(gameSaveId);
          currency = mergeCurrencies(currency, dbCurrency);
          currencyCache.set(gameSaveIdString, currency);
          return currency;
        }
        return currency;
      }
    }
    currency = getCurrencyFromDatabase(gameSaveId);
    currencyCache.set(gameSaveId.toString(), currency);
    return currency;
  }

  @Override
  @Transactional
  public Currency createNewCurrency(UUID gameSaveId) {
    return currencyRepositoryPort.create(gameSaveId);
  }

  @Override
  @Transactional
  public Currency createNewCurrency(
      UUID gameSaveId, Long gold, Long diamond, Long emerald, Long amethyst) {
    return currencyRepositoryPort.create(gameSaveId, gold, diamond, emerald, amethyst);
  }

  /**
   * Merge the currency POJO with the currency from the database
   *
   * @param currency the currency POJO
   * @param dbCurrency the currency from the database
   * @return the merged currency POJO
   */
  private static Currency mergeCurrencies(Currency currency, Currency dbCurrency) {
    var builder = Currency.builder();
    builder.gold(currency.gold() != null ? currency.gold() : dbCurrency.gold());
    builder.diamond(currency.diamond() != null ? currency.diamond() : dbCurrency.diamond());
    builder.emerald(currency.emerald() != null ? currency.emerald() : dbCurrency.emerald());
    builder.amethyst(currency.amethyst() != null ? currency.amethyst() : dbCurrency.amethyst());
    return builder.build();
  }

  @Override
  @Transactional
  public void saveCurrency(UUID gameSaveId, Currency currency, boolean toCache)
      throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (currency == null || isCurrencyNull(currency)) {
      throw new IllegalArgumentException("Currency cannot be null");
    }
    String gameSaveIdString = gameSaveId.toString();
    if (toCache) {
      currencyCache.set(gameSaveIdString, currency);
    } else {
      saveCurrencyToDatabase(gameSaveId, currency);
    }
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

  /**
   * Save the currency to the database
   *
   * @param gameSaveId the id of the game save
   * @param currency the currency POJO
   * @throws NotFoundException if the currency entity is not found
   */
  private void saveCurrencyToDatabase(UUID gameSaveId, Currency currency) throws NotFoundException {
    Currency updatedCurrency =
        Currency.builder()
            .gold(currency.gold())
            .diamond(currency.diamond())
            .emerald(currency.emerald())
            .amethyst(currency.amethyst())
            .build();
    currencyRepositoryPort.update(gameSaveId, updatedCurrency);
  }

  /**
   * Check if the currency is all null
   *
   * @param currency the currency POJO
   * @return true if all fields are null, false otherwise
   */
  private static boolean isCurrencyNull(Currency currency) {
    return currency.amethyst() == null
        && currency.diamond() == null
        && currency.emerald() == null
        && currency.gold() == null;
  }

  /**
   * Checks if the provided {@code Currency} instance has any null values for its fields.
   *
   * @param currency the {@code Currency} object to be checked
   * @return true if any of the fields (gold, diamond, emerald, amethyst) in the {@code Currency}
   *     object are null, false otherwise
   */
  private static boolean isCurrencyPartial(Currency currency) {
    return currency.amethyst() == null
        || currency.diamond() == null
        || currency.emerald() == null
        || currency.gold() == null;
  }
}
