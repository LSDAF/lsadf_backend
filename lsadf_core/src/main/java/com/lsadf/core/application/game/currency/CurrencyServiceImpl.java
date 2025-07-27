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
package com.lsadf.core.application.game.currency;

import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.table.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.table.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.table.game.currency.CurrencyRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class CurrencyServiceImpl implements CurrencyService {

  private final CurrencyRepository currencyRepository;
  private final Cache<Currency> currencyCache;
  private static final CurrencyEntityMapper mapper = CurrencyEntityMapper.INSTANCE;

  public CurrencyServiceImpl(CurrencyRepository currencyRepository, Cache<Currency> currencyCache) {
    this.currencyRepository = currencyRepository;
    this.currencyCache = currencyCache;
  }

  @Override
  @Transactional(readOnly = true)
  public Currency getCurrency(UUID gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    String gameSaveIdString = gameSaveId.toString();
    if (currencyCache.isEnabled()) {
      Optional<Currency> optionalCachedCurrency = currencyCache.get(gameSaveIdString);
      if (optionalCachedCurrency.isPresent()) {
        Currency currency = optionalCachedCurrency.get();
        if (currency.getAmethyst() == null
            || currency.getDiamond() == null
            || currency.getEmerald() == null
            || currency.getGold() == null) {
          CurrencyEntity currencyEntity = getCurrencyEntity(gameSaveId);
          return mergeCurrencies(currency, currencyEntity);
        }
        return currency;
      }
    }
    CurrencyEntity currencyEntity = getCurrencyEntity(gameSaveId);

    return mapper.map(currencyEntity);
  }

  /**
   * Merge the currency POJO with the currency entity from the database
   *
   * @param currency the currency POJO
   * @param currencyEntity the currency entity from the database
   * @return the merged currency POJO
   */
  private static Currency mergeCurrencies(Currency currency, CurrencyEntity currencyEntity) {
    if (currency.getAmethyst() == null) {
      currency.setAmethyst(currencyEntity.getAmethystAmount());
    }
    if (currency.getDiamond() == null) {
      currency.setDiamond(currencyEntity.getDiamondAmount());
    }
    if (currency.getEmerald() == null) {
      currency.setEmerald(currencyEntity.getEmeraldAmount());
    }
    if (currency.getGold() == null) {
      currency.setGold(currencyEntity.getGoldAmount());
    }

    return currency;
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
   * Get the currency entity from the database
   *
   * @param gameSaveId the id of the game save
   * @return the currency entity
   * @throws NotFoundException if the currency entity is not found
   */
  private CurrencyEntity getCurrencyEntity(UUID gameSaveId) throws NotFoundException {
    return currencyRepository
        .findCurrencyEntityById(gameSaveId)
        .orElseThrow(
            () ->
                new NotFoundException("Currency entity not found for game save id " + gameSaveId));
  }

  /**
   * Save the gold amount to the database
   *
   * @param gameSaveId the id of the game save
   * @param currency the currency POJO
   * @throws NotFoundException if the currency entity is not found
   */
  private void saveCurrencyToDatabase(UUID gameSaveId, Currency currency) throws NotFoundException {
    currencyRepository.updateCurrency(
        gameSaveId,
        currency.getAmethyst(),
        currency.getDiamond(),
        currency.getEmerald(),
        currency.getGold());
  }

  /**
   * Check if the currency is all null
   *
   * @param currency the currency POJO
   * @return true if all fields are null, false otherwise
   */
  private static boolean isCurrencyNull(Currency currency) {
    return currency.getAmethyst() == null
        && currency.getDiamond() == null
        && currency.getEmerald() == null
        && currency.getGold() == null;
  }
}
