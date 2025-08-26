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

package com.lsadf.core.unit.infra.valkey.cache.listener;

import static com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHash.CharacteristicsHashAttributes.*;
import static com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHash.CurrencyHashAttributes.*;
import static com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHash.StageHashAttributes.*;
import static org.mockito.Mockito.verify;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.cache.Hash;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHash;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHashMapper;
import com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHash;
import com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHashMapper;
import com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHash;
import com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHashMapper;
import com.lsadf.core.infra.valkey.cache.listener.ValkeyRepositoryKeyExpirationListener;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;

class ValkeyRepositoryKeyExpirationListenerTests {
  @Mock private CurrencyService currencyService;
  @Mock private CharacteristicsService characteristicsService;
  @Mock private StageService stageService;

  private ValkeyRepositoryKeyExpirationListener valkeyRepositoryKeyExpirationListener;

  private static final String UUID_STRING = "1a05d603-d018-4982-bfa9-996745fe283d";
  private static final UUID UUID = java.util.UUID.fromString(UUID_STRING);

  private static final CurrencyHash CURRENCY_HASH =
      CurrencyHash.builder().id(UUID).gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

  private static final Currency CURRENCY = CurrencyHashMapper.INSTANCE.map(CURRENCY_HASH);

  private static final CharacteristicsHash CHARACTERISTICS_HASH =
      CharacteristicsHash.builder()
          .id(UUID)
          .attack(10L)
          .critChance(20L)
          .critDamage(30L)
          .health(40L)
          .resistance(50L)
          .build();

  private static final Characteristics CHARACTERISTICS =
      CharacteristicsHashMapper.INSTANCE.map(CHARACTERISTICS_HASH);

  private static final StageHash STAGE_HASH =
      StageHash.builder().id(UUID).currentStage(50L).maxStage(100L).build();

  private static final Stage STAGE = StageHashMapper.INSTANCE.map(STAGE_HASH);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    valkeyRepositoryKeyExpirationListener =
        new ValkeyRepositoryKeyExpirationListener(
            stageService, characteristicsService, currencyService);
  }

  @Test
  void test_handleExpiredCurrencyHash() {
    RedisKeyExpiredEvent<Hash<UUID>> redisKeyExpiredEvent =
        new RedisKeyExpiredEvent<>(
            (CURRENCY_HASH_KEY + ":" + UUID_STRING).getBytes(StandardCharsets.UTF_8),
            CURRENCY_HASH);

    valkeyRepositoryKeyExpirationListener.handleExpiredHash(redisKeyExpiredEvent);
    verify(currencyService).saveCurrency(UUID, CURRENCY, false);
  }

  @Test
  void test_handleExpiredCharacteristicsHash() {
    RedisKeyExpiredEvent<Hash<UUID>> redisKeyExpiredEvent =
        new RedisKeyExpiredEvent<>(
            (CHARACTERISTICS_HASH_KEY + ":" + UUID_STRING).getBytes(StandardCharsets.UTF_8),
            CHARACTERISTICS_HASH);

    valkeyRepositoryKeyExpirationListener.handleExpiredHash(redisKeyExpiredEvent);
    verify(characteristicsService).saveCharacteristics(UUID, CHARACTERISTICS, false);
  }

  @Test
  void test_handleExpiredStageHash() {
    RedisKeyExpiredEvent<Hash<UUID>> redisKeyExpiredEvent =
        new RedisKeyExpiredEvent<>(
            (STAGE_HASH_KEY + ":" + UUID_STRING).getBytes(StandardCharsets.UTF_8), STAGE_HASH);

    valkeyRepositoryKeyExpirationListener.handleExpiredHash(redisKeyExpiredEvent);
    verify(stageService).saveStage(UUID, STAGE, false);
  }
}
