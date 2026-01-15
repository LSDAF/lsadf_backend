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
package com.lsadf.core.unit.application.game.save;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.CurrencyQueryService;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.currency.command.InitializeCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.InitializeDefaultCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.PersistCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.UpdateCacheCurrencyCommand;
import com.lsadf.core.application.game.save.currency.impl.CurrencyCommandServiceImpl;
import com.lsadf.core.domain.game.save.currency.Currency;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class CurrencyCommandServiceTests {
  private CurrencyCommandService currencyService;

  @Mock private CurrencyRepositoryPort currencyRepositoryPort;
  @Mock private CacheManager cacheManager;
  @Mock private CurrencyCachePort currencyCache;
  @Mock private CurrencyQueryService currencyQueryService;

  private static final UUID UUID = java.util.UUID.randomUUID();
  private static final Currency DEFAULT_CURRENCY =
      Currency.builder().gold(0L).diamond(0L).emerald(0L).amethyst(0L).build();

  private static final Currency CACHED_CURRENCY =
      Currency.builder().gold(100L).diamond(50L).emerald(25L).amethyst(10L).build();

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    openMocks = MockitoAnnotations.openMocks(this);
    Mockito.reset(currencyRepositoryPort, cacheManager, currencyCache, currencyQueryService);

    currencyService =
        new CurrencyCommandServiceImpl(
            cacheManager, currencyRepositoryPort, currencyCache, currencyQueryService);
  }

  @Test
  void
      test_updateCacheCurrency_throwsIllegalArgumentException_when_allPropertiesNullAndCacheTrue() {
    // Arrange
    var command = new UpdateCacheCurrencyCommand(UUID, null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.updateCacheCurrency(command));
  }

  @Test
  void test_updateCacheCurrency_savesSuccessfully_when_partialCurrency() {
    // Arrange
    Currency currency = new Currency(10L, 25L, null, null);
    when(cacheManager.isEnabled()).thenReturn(true);
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CURRENCY));
    var command = UpdateCacheCurrencyCommand.fromCurrency(UUID, currency);

    // Act
    currencyService.updateCacheCurrency(command);

    // Assert
    Currency expectedMergedCurrency =
        new Currency(10L, 25L, CACHED_CURRENCY.emerald(), CACHED_CURRENCY.amethyst());
    verify(currencyCache).set(UUID.toString(), expectedMergedCurrency);
  }

  @Test
  void test_updateCacheCurrency_savesSuccessfully_when_cacheDisabled() {
    // Arrange
    Currency currency = new Currency(10L, 25L, 5L, 2L);
    when(cacheManager.isEnabled()).thenReturn(false);
    var command = UpdateCacheCurrencyCommand.fromCurrency(UUID, currency);

    // Act & Assert
    assertDoesNotThrow(() -> currencyService.updateCacheCurrency(command));
    verify(currencyCache, times(0)).set(anyString(), any(Currency.class));
  }

  @Test
  void test_updateCacheCurrency_savesSuccessfully_when_validCurrency() {
    // Arrange
    Currency currency = new Currency(100L, 50L, 25L, 10L);
    when(cacheManager.isEnabled()).thenReturn(true);
    var command = UpdateCacheCurrencyCommand.fromCurrency(UUID, currency);

    // Act
    currencyService.updateCacheCurrency(command);

    // Assert
    verify(currencyCache).set(UUID.toString(), currency);
  }

  @Test
  void test_updateCacheCurrency_savesSuccessfully_when_cacheMissAndQueryServiceCalled() {
    // Arrange
    Currency currency = new Currency(10L, null, null, null);
    when(cacheManager.isEnabled()).thenReturn(true);
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(currencyQueryService.retrieveCurrency(UUID)).thenReturn(CACHED_CURRENCY);
    var command = UpdateCacheCurrencyCommand.fromCurrency(UUID, currency);

    // Act
    currencyService.updateCacheCurrency(command);

    // Assert
    Currency expectedMergedCurrency =
        new Currency(
            10L, CACHED_CURRENCY.diamond(), CACHED_CURRENCY.emerald(), CACHED_CURRENCY.amethyst());
    verify(currencyCache).set(UUID.toString(), expectedMergedCurrency);
    verify(currencyQueryService).retrieveCurrency(UUID);
  }

  @Test
  void test_initializeDefaultCurrency_returnsDefaultCurrency() {
    // Arrange
    InitializeDefaultCurrencyCommand command = new InitializeDefaultCurrencyCommand(UUID);
    when(currencyRepositoryPort.create(UUID)).thenReturn(DEFAULT_CURRENCY);

    // Act
    Currency result = currencyService.initializeDefaultCurrency(command);

    // Assert
    assertEquals(DEFAULT_CURRENCY, result);
    verify(currencyRepositoryPort).create(UUID);
  }

  @Test
  void test_initializeCurrency_returnsCustomCurrency() {
    // Arrange
    Long gold = 50L;
    Long diamond = 25L;
    Long emerald = 10L;
    Long amethyst = 5L;
    InitializeCurrencyCommand command =
        new InitializeCurrencyCommand(UUID, gold, diamond, emerald, amethyst);
    Currency expectedCurrency = new Currency(gold, diamond, emerald, amethyst);
    when(currencyRepositoryPort.create(UUID, gold, diamond, emerald, amethyst))
        .thenReturn(expectedCurrency);

    // Act
    Currency result = currencyService.initializeCurrency(command);

    // Assert
    assertEquals(expectedCurrency, result);
    verify(currencyRepositoryPort).create(UUID, gold, diamond, emerald, amethyst);
  }

  @Test
  void test_initializeCurrency_handlesNullValues() {
    // Arrange
    InitializeCurrencyCommand command = new InitializeCurrencyCommand(UUID, null, null, null, null);
    Currency expectedCurrency = new Currency(0L, 0L, 0L, 0L);
    when(currencyRepositoryPort.create(UUID, 0L, 0L, 0L, 0L)).thenReturn(expectedCurrency);

    // Act
    Currency result = currencyService.initializeCurrency(command);

    // Assert
    assertEquals(expectedCurrency, result);
  }

  @Test
  void test_persistCurrency_persistsSuccessfully() {
    // Arrange
    PersistCurrencyCommand command = new PersistCurrencyCommand(UUID, 100L, 50L, 25L, 10L);

    // Act
    currencyService.persistCurrency(command);

    // Assert
    verify(currencyRepositoryPort).update(UUID, 100L, 50L, 25L, 10L);
  }
}
