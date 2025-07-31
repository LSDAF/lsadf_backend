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

import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.flush.RedisCacheFlushServiceImpl;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ValkeyCacheFlushServiceTests {

  RedisCacheFlushServiceImpl redisCacheFlushService;

  @Mock private Cache<Characteristics> characteristicsCache;

  @Mock private Cache<Currency> currencyCache;

  @Mock private Cache<Stage> stageCache;

  @Mock private CharacteristicsService characteristicsService;

  @Mock CurrencyService currencyService;

  @Mock InventoryService inventoryService;

  @Mock StageService stageService;

  private static final UUID UUID_1 = java.util.UUID.randomUUID();
  private static final UUID UUID_2 = java.util.UUID.randomUUID();

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    this.redisCacheFlushService =
        new RedisCacheFlushServiceImpl(
            characteristicsService,
            currencyService,
            inventoryService,
            stageService,
            characteristicsCache,
            currencyCache,
            stageCache);
  }

  @Test
  void test_flushCache_flushesCorrectly_when_characteristicsCache() {
    Map<String, Characteristics> characteristicsEntries =
        Map.of(
            UUID_1.toString(), new Characteristics(1L, null, null, null, null),
            UUID_2.toString(), new Characteristics(2L, null, null, null, null));
    when(characteristicsCache.getAll()).thenReturn(characteristicsEntries);

    redisCacheFlushService.flushCharacteristics();
    verify(characteristicsService, times(1))
        .saveCharacteristics(UUID_1, new Characteristics(1L, null, null, null, null), false);
    verify(characteristicsService, times(1))
        .saveCharacteristics(UUID_2, new Characteristics(2L, null, null, null, null), false);
  }

  @Test
  void test_flushCache_flushesCorrectly_when_currenciesCache() {
    Map<String, Currency> currencyEntries =
        Map.of(
            UUID_1.toString(), new Currency(1L, 2L, null, 3L),
            UUID_2.toString(), new Currency(2L, 4L, null, 8L));
    when(currencyCache.getAll()).thenReturn(currencyEntries);

    redisCacheFlushService.flushCurrencies();
    verify(currencyService, times(1)).saveCurrency(UUID_1, new Currency(1L, 2L, null, 3L), false);
    verify(currencyService, times(1)).saveCurrency(UUID_2, new Currency(2L, 4L, null, 8L), false);
  }

  @Test
  void test_flushCache_flushesCorrectly_when_stagesCache() {
    Map<String, Stage> stageEntries =
        Map.of(
            UUID_1.toString(), new Stage(10L, 20L),
            UUID_2.toString(), new Stage(30L, 40L));
    when(stageCache.getAll()).thenReturn(stageEntries);

    redisCacheFlushService.flushStages();
    verify(stageService, times(1)).saveStage(UUID_1, new Stage(10L, 20L), false);
    verify(stageService, times(1)).saveStage(UUID_2, new Stage(30L, 40L), false);
  }

  @Test
  void test_flushCache_doesNothing_when_flushingEmptyCharacteristicsCache() {
    Map<String, Characteristics> characteristicsEntries = Collections.emptyMap();
    when(characteristicsCache.getAll()).thenReturn(characteristicsEntries);

    redisCacheFlushService.flushCharacteristics();
    verifyNoInteractions(characteristicsService);
  }

  @Test
  void test_flushCache_doesNothing_when_flushingEmptyCurrencyCache() {
    Map<String, Currency> currencyEntries = Collections.emptyMap();
    when(currencyCache.getAll()).thenReturn(currencyEntries);

    redisCacheFlushService.flushCurrencies();
    verifyNoInteractions(currencyService);
  }

  @Test
  void test_flushCache_doesNothing_when_flushingEmptyStageCache() {
    Map<String, Stage> stageEntries = Collections.emptyMap();
    when(stageCache.getAll()).thenReturn(stageEntries);

    redisCacheFlushService.flushStages();
    verifyNoInteractions(stageService);
  }
}
