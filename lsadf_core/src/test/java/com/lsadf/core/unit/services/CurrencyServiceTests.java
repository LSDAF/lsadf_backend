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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.lsadf.core.common.exceptions.http.NotFoundException;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.persistence.config.mappers.Mapper;
import com.lsadf.core.infra.persistence.config.mappers.MapperImpl;
import com.lsadf.core.infra.persistence.game.CurrencyEntity;
import com.lsadf.core.infra.persistence.game.CurrencyRepository;
import com.lsadf.core.services.CurrencyService;
import com.lsadf.core.services.impl.CurrencyServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class CurrencyServiceTests {
  private CurrencyService currencyService;

  @Mock private CurrencyRepository currencyRepository;

  @Mock private Cache<Currency> currencyCache;

  private final Mapper mapper = new MapperImpl();

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);

    currencyService = new CurrencyServiceImpl(currencyRepository, currencyCache, mapper);
  }

  @Test
  void get_currency_on_non_existing_gamesave_id() {
    // Arrange
    when(currencyRepository.findById(anyString())).thenReturn(Optional.empty());
    when(currencyCache.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(NotFoundException.class, () -> currencyService.getCurrency("1"));
  }

  @Test
  void get_currency_on_existing_gamesave_id_when_cached() {
    // Arrange
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .userEmail("test@test.com")
            .goldAmount(1L)
            .diamondAmount(2L)
            .emeraldAmount(3L)
            .amethystAmount(4L)
            .build();

    Currency currency = Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

    when(currencyRepository.findById(anyString())).thenReturn(Optional.of(currencyEntity));
    when(currencyCache.isEnabled()).thenReturn(true);
    when(currencyCache.get(anyString())).thenReturn(Optional.of(currency));

    // Act
    Currency result = currencyService.getCurrency("1");

    // Assert
    assertThat(result).isEqualTo(currency);
  }

  @Test
  void get_currency_on_existing_gamesave_id_when_not_cached() {
    // Arrange
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .userEmail("test@test.com")
            .goldAmount(1L)
            .diamondAmount(2L)
            .emeraldAmount(3L)
            .amethystAmount(4L)
            .build();

    Currency currency = Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

    when(currencyRepository.findById(anyString())).thenReturn(Optional.of(currencyEntity));
    when(currencyCache.isEnabled()).thenReturn(false);
    when(currencyCache.get(anyString())).thenReturn(Optional.empty());

    // Act
    Currency result = currencyService.getCurrency("1");

    // Assert
    assertThat(result).isEqualTo(currency);
  }

  @Test
  void get_currency_on_existing_gamesave_id_when_partially_cached() {
    // Arrange
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .userEmail("test@test.com")
            .goldAmount(1L)
            .diamondAmount(2L)
            .emeraldAmount(3L)
            .amethystAmount(4L)
            .build();

    Currency currencyCached = Currency.builder().gold(1L).build();

    Currency currency = Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

    when(currencyRepository.findById(anyString())).thenReturn(Optional.of(currencyEntity));
    when(currencyCache.isEnabled()).thenReturn(true);
    when(currencyCache.get(anyString())).thenReturn(Optional.of(currencyCached));

    // Act
    Currency result = currencyService.getCurrency("1");

    // Assert
    assertThat(result).isEqualTo(currency);
  }

  @Test
  void get_currency_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> currencyService.getCurrency(null));
  }

  @Test
  void save_currency_on_null_game_save_id_with_to_cache_to_true() {
    // Arrange
    Currency currency = new Currency(1L, 2L, 3L, 4L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(null, currency, true));
  }

  @Test
  void save_currency_on_null_game_save_id_with_to_cache_to_false() {
    // Arrange
    Currency currency = new Currency(1L, 2L, 3L, 4L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency(null, currency, false));
  }

  @Test
  void save_currency_on_null_currency_with_to_cache_to_false() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency("1", null, false));
  }

  @Test
  void save_currency_on_null_currency_with_to_cache_to_true() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency("1", null, true));
  }

  @Test
  void save_currency_where_all_properties_are_null_with_cache_to_true() {
    // Arrange
    Currency currency = new Currency(null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency("1", currency, true));
  }

  @Test
  void save_currency_where_all_properties_are_null_with_cache_to_false() {
    // Arrange
    Currency currency = new Currency(null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> currencyService.saveCurrency("1", currency, false));
  }

  @Test
  void save_currency_on_existing_gamesave_with_all_valid_currencies_value() {
    // Arrange
    Currency currency = new Currency(1L, 2L, 3L, 4L);

    // Act + Assert
    assertDoesNotThrow(() -> currencyService.saveCurrency("1", currency, true));
  }
}
