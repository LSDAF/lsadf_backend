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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyQueryService;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.currency.impl.CurrencyQueryServiceImpl;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyQueryServiceTests {
  private CurrencyQueryService currencyQueryService;

  @Mock private CurrencyRepositoryPort currencyRepositoryPort;
  @Mock private CacheManager cacheManager;
  @Mock private CurrencyCachePort currencyCache;

  private static final UUID UUID = java.util.UUID.randomUUID();

  private AutoCloseable openMocks;

  private static final Currency CURRENCY =
      Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    Mockito.reset(currencyRepositoryPort, cacheManager, currencyCache);
    currencyQueryService =
        new CurrencyQueryServiceImpl(cacheManager, currencyRepositoryPort, currencyCache);
  }

  @Test
  void test_getCurrency_throwsNotFoundException_when_nonExistingGameSaveId() {
    // Arrange
    when(currencyRepositoryPort.findById(any(java.util.UUID.class)))
        .thenReturn(java.util.Optional.empty());
    when(cacheManager.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(NotFoundException.class, () -> currencyQueryService.retrieveCurrency(UUID));
  }

  @Test
  void test_getCurrency_returnsCurrency_when_existingGameSaveIdAndCached() {
    // Arrange
    when(cacheManager.isEnabled()).thenReturn(true);
    when(currencyCache.get(UUID.toString())).thenReturn(java.util.Optional.of(CURRENCY));
    // Assert
    assertThat(currencyQueryService.retrieveCurrency(UUID)).isEqualTo(CURRENCY);
  }

  @Test
  void test_getCurrency_returnsCurrency_when_existingGameSaveIdAndNotCached() {
    // Arrange
    when(currencyRepositoryPort.findById(any(java.util.UUID.class)))
        .thenReturn(java.util.Optional.of(CURRENCY));
    when(cacheManager.isEnabled()).thenReturn(false);

    // Assert
    assertThat(currencyQueryService.retrieveCurrency(UUID)).isEqualTo(CURRENCY);
  }
}
