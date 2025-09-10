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
package com.lsadf.core.unit.infra.valkey.cache.flush;

import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import com.lsadf.core.infra.valkey.cache.flush.impl.RedisCacheFlushServiceImpl;
import com.lsadf.core.infra.valkey.cache.util.ValkeyFlushUtils;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ValkeyCacheFlushServiceTests {

  RedisCacheFlushServiceImpl redisCacheFlushService;

  @Mock private CharacteristicsCachePort characteristicsCache;

  @Mock private CurrencyCachePort currencyCache;

  @Mock private StageCachePort stageCache;

  @Mock private CharacteristicsCommandService characteristicsService;

  @Mock CurrencyService currencyService;

  @Mock StageService stageService;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RedisTemplate<String, String> redisTemplate;

  private AutoCloseable openMocks;

  private static final UUID UUID_1 = java.util.UUID.randomUUID();
  private static final UUID UUID_2 = java.util.UUID.randomUUID();

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    openMocks = MockitoAnnotations.openMocks(this);
    this.redisCacheFlushService =
        new RedisCacheFlushServiceImpl(
            characteristicsService,
            currencyService,
            stageService,
            characteristicsCache,
            currencyCache,
            stageCache,
            redisTemplate);
  }

  @Test
  void test_flushGameSave() {
    Stage stage = Stage.builder().currentStage(1L).maxStage(2L).build();
    Characteristics characteristics =
        Characteristics.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();
    Currency currency = Currency.builder().gold(1L).amethyst(2L).diamond(3L).emerald(4L).build();
    when(characteristicsCache.get(UUID_1.toString())).thenReturn(Optional.of(characteristics));
    when(currencyCache.get(UUID_1.toString())).thenReturn(Optional.of(currency));
    when(stageCache.get(UUID_1.toString())).thenReturn(Optional.of(stage));

    redisCacheFlushService.flushGameSave(UUID_1);
    var command = PersistCharacteristicsCommand.fromCharacteristics(UUID_1, characteristics);
    verify(characteristicsService).persistCharacteristics(command);
    verify(currencyService).saveCurrency(UUID_1, currency, false);
    verify(stageService).saveStage(UUID_1, stage, false);
  }

  @Test
  void test_flushGameSaves() {
    Stage stage1 = Stage.builder().currentStage(1L).maxStage(2L).build();
    Characteristics characteristics1 =
        Characteristics.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();
    Currency currency1 = Currency.builder().gold(1L).amethyst(2L).diamond(3L).emerald(4L).build();
    Stage stage2 = Stage.builder().currentStage(10L).maxStage(20L).build();
    Characteristics characteristics2 =
        Characteristics.builder()
            .attack(10L)
            .critChance(20L)
            .critDamage(30L)
            .health(40L)
            .resistance(50L)
            .build();
    Currency currency2 =
        Currency.builder().gold(10L).amethyst(20L).diamond(30L).emerald(40L).build();

    when(characteristicsCache.get(UUID_1.toString())).thenReturn(Optional.of(characteristics1));
    when(currencyCache.get(UUID_1.toString())).thenReturn(Optional.of(currency1));
    when(stageCache.get(UUID_1.toString())).thenReturn(Optional.of(stage1));
    when(characteristicsCache.get(UUID_2.toString())).thenReturn(Optional.of(characteristics2));
    when(currencyCache.get(UUID_2.toString())).thenReturn(Optional.of(currency2));
    when(stageCache.get(UUID_2.toString())).thenReturn(Optional.of(stage2));

    when(redisTemplate.opsForZSet().rangeByScore(FlushStatus.PENDING.getKey(), 0, -1))
        .thenReturn(Set.of(UUID_1.toString()));
    when(redisTemplate.opsForSet().members(FlushStatus.PROCESSING.getKey()))
        .thenReturn(Set.of(UUID_2.toString()));

    try (MockedStatic<ValkeyFlushUtils> mockedFlushUtils = mockStatic(ValkeyFlushUtils.class)) {

      mockedFlushUtils
          .when(
              () ->
                  ValkeyFlushUtils.moveToProcessingWithTransaction(
                      eq(redisTemplate), any(), anyLong()))
          .thenReturn(true);

      redisCacheFlushService.flushGameSaves();
      var command1 = PersistCharacteristicsCommand.fromCharacteristics(UUID_1, characteristics1);
      var command2 = PersistCharacteristicsCommand.fromCharacteristics(UUID_2, characteristics2);
      verify(characteristicsService).persistCharacteristics(command1);
      verify(currencyService).saveCurrency(UUID_1, currency1, false);
      verify(stageService).saveStage(UUID_1, stage1, false);
      verify(characteristicsService).persistCharacteristics(command2);
      verify(currencyService).saveCurrency(UUID_2, currency2, false);
      verify(stageService).saveStage(UUID_2, stage2, false);
    }
  }
}
