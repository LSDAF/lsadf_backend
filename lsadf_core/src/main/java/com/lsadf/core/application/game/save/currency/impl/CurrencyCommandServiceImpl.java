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

import static com.lsadf.core.infra.util.ObjectUtils.getOrDefault;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.CurrencyQueryService;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.currency.command.InitializeCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.InitializeDefaultCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.PersistCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.UpdateCacheCurrencyCommand;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.currency.Currency;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CurrencyCommandServiceImpl implements CurrencyCommandService {

  private final CacheManager cacheManager;
  private final CurrencyRepositoryPort currencyRepositoryPort;
  private final CachePort<Currency> currencyCache;
  private final CurrencyQueryService currencyQueryService;

  public CurrencyCommandServiceImpl(
      CacheManager cacheManager,
      CurrencyRepositoryPort currencyRepositoryPort,
      CachePort<Currency> currencyCache,
      CurrencyQueryService currencyQueryService) {
    this.cacheManager = cacheManager;
    this.currencyRepositoryPort = currencyRepositoryPort;
    this.currencyCache = currencyCache;
    this.currencyQueryService = currencyQueryService;
  }

  @Override
  @Transactional
  public Currency initializeDefaultCurrency(InitializeDefaultCurrencyCommand command) {
    UUID gameSaveId = command.gameSaveId();
    return currencyRepositoryPort.create(gameSaveId);
  }

  @Override
  @Transactional
  public Currency initializeCurrency(InitializeCurrencyCommand command) {
    UUID gameSaveId = command.gameSaveId();
    Long gold = getOrDefault(command.gold(), 0L);
    Long diamond = getOrDefault(command.diamond(), 0L);
    Long emerald = getOrDefault(command.emerald(), 0L);
    Long amethyst = getOrDefault(command.amethyst(), 0L);
    return currencyRepositoryPort.create(gameSaveId, gold, diamond, emerald, amethyst);
  }

  @Override
  @Transactional
  public void persistCurrency(PersistCurrencyCommand command) {
    currencyRepositoryPort.update(
        command.gameSaveId(),
        command.gold(),
        command.diamond(),
        command.emerald(),
        command.amethyst());
  }

  @Override
  public void updateCacheCurrency(UpdateCacheCurrencyCommand command) {
    Currency currency;
    if (isCurrencyNull(command)) {
      throw new IllegalArgumentException("Currency cannot be null");
    }
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      String gameSaveIdString = command.gameSaveId().toString();
      if (isCurrencyPartial(command)) {
        Currency existingCurrency =
            currencyCache
                .get(gameSaveIdString)
                .orElseGet(() -> currencyQueryService.retrieveCurrency(command.gameSaveId()));
        currency = mergeCurrency(command, existingCurrency);
      } else {
        currency =
            // Adding Objects.requireNonNull() since object is not partial
            new Currency(
                Objects.requireNonNull(command.gold()),
                Objects.requireNonNull(command.diamond()),
                Objects.requireNonNull(command.emerald()),
                Objects.requireNonNull(command.amethyst()));
      }
      currencyCache.set(gameSaveIdString, currency);
    } else {
      log.warn("Cache is disabled");
    }
  }

  /**
   * Merge the currency POJO with the currency from the database
   *
   * @param currency the currency command
   * @param dbCurrency the currency from the database
   * @return the merged currency POJO
   */
  private static Currency mergeCurrency(UpdateCacheCurrencyCommand currency, Currency dbCurrency) {
    Currency.CurrencyBuilder builder = Currency.builder();
    builder.gold(getOrDefault(currency.gold(), dbCurrency.gold()));
    builder.diamond(getOrDefault(currency.diamond(), dbCurrency.diamond()));
    builder.emerald(getOrDefault(currency.emerald(), dbCurrency.emerald()));
    builder.amethyst(getOrDefault(currency.amethyst(), dbCurrency.amethyst()));
    return builder.build();
  }

  /**
   * Checks if the given currency object has any null fields.
   *
   * @param currency the currency object to be checked
   * @return true if any of the fields (gold, diamond, emerald, amethyst) are null, false otherwise
   */
  private static boolean isCurrencyPartial(UpdateCacheCurrencyCommand currency) {
    return currency.gold() == null
        || currency.diamond() == null
        || currency.emerald() == null
        || currency.amethyst() == null;
  }

  /**
   * Checks if the given currency object has all null fields.
   *
   * @param currency the currency object to be checked
   * @return true if all of the fields (gold, diamond, emerald, amethyst) are null, false otherwise
   */
  private static boolean isCurrencyNull(UpdateCacheCurrencyCommand currency) {
    return currency.gold() == null
        && currency.diamond() == null
        && currency.emerald() == null
        && currency.amethyst() == null;
  }
}
