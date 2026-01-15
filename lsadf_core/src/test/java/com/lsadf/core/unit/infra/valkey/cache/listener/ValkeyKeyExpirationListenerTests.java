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

import static com.lsadf.core.infra.valkey.ValkeyConstants.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.command.PersistCurrencyCommand;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.command.PersistStageCommand;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.ValkeyConstants;
import com.lsadf.core.infra.valkey.cache.listener.ValkeyKeyExpirationListener;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class ValkeyKeyExpirationListenerTests {
  @Mock CharacteristicsCommandService characteristicsService;
  @Mock CurrencyCommandService currencyService;
  @Mock StageCommandService stageService;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RedisTemplate<String, Characteristics> characteristicsRedisTemplate;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RedisTemplate<String, Currency> currencyRedisTemplate;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RedisTemplate<String, Stage> stageRedisTemplate;

  private ValkeyKeyExpirationListener valkeyKeyExpirationListener;

  private static final String UUID_STRING = "1a05d603-d018-4982-bfa9-996745fe283d";
  private static final UUID UUID = java.util.UUID.fromString(UUID_STRING);

  private static final Currency CURRENCY =
      Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

  private static final Characteristics CHARACTERISTICS =
      Characteristics.builder()
          .attack(10L)
          .critChance(20L)
          .critDamage(30L)
          .health(40L)
          .resistance(50L)
          .build();

  private static final Stage STAGE = Stage.builder().currentStage(50L).maxStage(100L).build();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    valkeyKeyExpirationListener =
        new ValkeyKeyExpirationListener(
            characteristicsService,
            currencyService,
            stageService,
            characteristicsRedisTemplate,
            currencyRedisTemplate,
            stageRedisTemplate);
  }

  @Test
  void test_onMessage_characteristics() {
    Message message =
        new DefaultMessage(
            "CHANNEL".getBytes(), (ValkeyConstants.CHARACTERISTICS + UUID_STRING).getBytes());
    when(characteristicsRedisTemplate.opsForValue().get(CHARACTERISTICS_HISTO + UUID_STRING))
        .thenReturn(CHARACTERISTICS);

    valkeyKeyExpirationListener.onMessage(message, null);
    var persistCommand = PersistCharacteristicsCommand.fromCharacteristics(UUID, CHARACTERISTICS);
    verify(characteristicsService).persistCharacteristics(persistCommand);
    verify(characteristicsRedisTemplate).delete(CHARACTERISTICS_HISTO + UUID_STRING);
  }

  @Test
  void test_onMessage_currency() {
    Message message =
        new DefaultMessage(
            "CHANNEL".getBytes(), (ValkeyConstants.CURRENCY + UUID_STRING).getBytes());
    when(currencyRedisTemplate.opsForValue().get(CURRENCY_HISTO + UUID_STRING))
        .thenReturn(CURRENCY);
    valkeyKeyExpirationListener.onMessage(message, null);
    var persistCommand = PersistCurrencyCommand.fromCurrency(UUID, CURRENCY);
    verify(currencyService).persistCurrency(persistCommand);
    verify(currencyRedisTemplate).delete(ValkeyConstants.CURRENCY_HISTO + UUID_STRING);
  }

  @Test
  void test_onMessage_stage() {
    Message message =
        new DefaultMessage("CHANNEL".getBytes(), (ValkeyConstants.STAGE + UUID_STRING).getBytes());
    when(stageRedisTemplate.opsForValue().get(STAGE_HISTO + UUID_STRING)).thenReturn(STAGE);
    valkeyKeyExpirationListener.onMessage(message, null);
    var persistCommand = PersistStageCommand.fromStage(UUID, STAGE);
    verify(stageService).persistStage(persistCommand);
    verify(stageRedisTemplate).delete(STAGE_HISTO + UUID_STRING);
  }
}
