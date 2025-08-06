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
package com.lsadf.core.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.currency.CurrencyServiceImpl;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.valkey.cache.service.CacheService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class CurrencyServiceTests {
  private CurrencyService currencyService;

  @Mock private CurrencyRepositoryPort currencyRepositoryPort;
  @Mock private CacheService cacheService;

  @Mock private CachePort<Currency> currencyCache;

  private static final UUID UUID = java.util.UUID.randomUUID();

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);

    currencyService = new CurrencyServiceImpl(cacheService, currencyRepositoryPort, currencyCache);
  }

  @Test
  void test_getCurrency_throwsNotFoundException_when_gameSaveNotExists() {
    // Arrange
    when(currencyRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());
    when(cacheService.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(NotFoundException.class, () -> currencyService.getCurrency(UUID));
  }

  @Test
  void test_getCurrency_returnsCachedCurrency_when_cacheEnabledAndCached() {
    // Arrange
    Currency currency = Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

    when(currencyRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(currency));
    when(cacheService.isEnabled()).thenReturn(true);
    when(currencyCache.get(anyString())).thenReturn(Optional.of(currency));

    // Act
    Currency result = currencyService.getCurrency(UUID);

    // Assert
    assertThat(result).isEqualTo(currency);
  }

  @Test
  void test_getCurrency_returnsCurrencyFromRepository_when_cacheDisabled() {
    // Arrange
    Currency currency = Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

    when(currencyRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(currency));
    when(cacheService.isEnabled()).thenReturn(false);
    when(currencyCache.get(anyString())).thenReturn(Optional.empty());

    // Act
    Currency result = currencyService.getCurrency(UUID);

    // Assert
    assertThat(result).isEqualTo(currency);
  }

  @Test
  void test_getCurrency_returnsCompleteCurrencyFromRepository_when_partiallyCached() {
    // Arrange

    Currency currencyCached = Currency.builder().gold(1L).build();

    Currency currency = Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

    when(currencyRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(currency));
    when(cacheService.isEnabled()).thenReturn(true);
    when(currencyCache.get(anyString())).thenReturn(Optional.of(currencyCached));

    // Act
    Currency result = currencyService.getCurrency(UUID);

    // Assert
    assertThat(result).isEqualTo(currency);
  }

  @Test
  void test_getCurrency_throwsIllegalArgumentException_when_gameSaveIdNull() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> currencyService.getCurrency(null));
  }

  @Test
  void test_saveCurrency_throwsIllegalArgumentException_when_gameSaveIdNullAndCacheEnabled() {
    // Arrange
    Currency currency = new Currency(1L, 2L, 3L, 4L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(null, currency, true));
  }

  @Test
  void test_saveCurrency_throwsIllegalArgumentException_when_gameSaveIdNullAndCacheDisabled() {
    // Arrange
    Currency currency = new Currency(1L, 2L, 3L, 4L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(null, currency, false));
  }

  @Test
  void test_saveCurrency_throwsIllegalArgumentException_when_currencyNullAndCacheDisabled() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(UUID, null, false));
  }

  @Test
  void test_saveCurrency_throwsIllegalArgumentException_when_currencyNullAndCacheEnabled() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(UUID, null, true));
  }

  @Test
  void
      test_saveCurrency_throwsIllegalArgumentException_when_allCurrencyPropertiesNullAndCacheEnabled() {
    // Arrange
    Currency currency = new Currency(null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(UUID, currency, true));
  }

  @Test
  void
      test_saveCurrency_throwsIllegalArgumentException_when_allCurrencyPropertiesNullAndCacheDisabled() {
    // Arrange
    Currency currency = new Currency(null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(UUID, currency, false));
  }

  @Test
  void test_saveCurrency_succeeds_when_allParametersValid() {
    // Arrange
    Currency currency = new Currency(1L, 2L, 3L, 4L);

    // Act + Assert
    assertDoesNotThrow(() -> currencyService.saveCurrency(UUID, currency, true));
  }
}
