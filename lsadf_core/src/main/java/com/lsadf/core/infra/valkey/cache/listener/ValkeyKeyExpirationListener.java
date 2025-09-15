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
package com.lsadf.core.infra.valkey.cache.listener;

import static com.lsadf.core.infra.valkey.ValkeyConstants.*;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.command.PersistCurrencyCommand;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.command.PersistStageCommand;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/** Listener for Redis key expiration events. */
@Slf4j
public class ValkeyKeyExpirationListener implements MessageListener {

  private final CharacteristicsCommandService characteristicsService;
  private final CurrencyCommandService currencyService;
  private final StageCommandService stageService;

  private final RedisTemplate<String, Characteristics> characteristicsRedisTemplate;
  private final RedisTemplate<String, Currency> currencyRedisTemplate;
  private final RedisTemplate<String, Stage> stageRedisTemplate;

  public ValkeyKeyExpirationListener(
      CharacteristicsCommandService characteristicsService,
      CurrencyCommandService currencyService,
      StageCommandService stageService,
      RedisTemplate<String, Characteristics> characteristicsRedisTemplate,
      RedisTemplate<String, Currency> currencyRedisTemplate,
      RedisTemplate<String, Stage> stageRedisTemplate) {
    this.characteristicsService = characteristicsService;
    this.currencyService = currencyService;
    this.stageService = stageService;
    this.characteristicsRedisTemplate = characteristicsRedisTemplate;
    this.currencyRedisTemplate = currencyRedisTemplate;
    this.stageRedisTemplate = stageRedisTemplate;
  }

  /**
   * Callback for processing received objects through Redis.
   *
   * @param message message must not be {@literal null}.
   * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
   */
  @Override
  public void onMessage(Message message, byte @Nullable [] pattern) {
    String expiredKey = message.toString();
    log.info("Redis cache entry expired -> {}", expiredKey);
    if (expiredKey.startsWith(CHARACTERISTICS)) {
      String gameSaveId = expiredKey.substring(CHARACTERISTICS.length());
      handleExpiredCharacteristics(gameSaveId);
    } else if (expiredKey.startsWith(CURRENCY)) {
      String gameSaveId = expiredKey.substring(CURRENCY.length());
      handleExpiredCurrency(gameSaveId);
    } else if (expiredKey.startsWith(STAGE)) {
      String gameSaveId = expiredKey.substring(STAGE.length());
      handleExpiredStage(gameSaveId);
    }
  }

  /**
   * Handles expired stage by saving it to DB.
   *
   * @param gameSaveId game save id
   */
  private void handleExpiredStage(String gameSaveId) {
    try {
      Stage stage = stageRedisTemplate.opsForValue().get(STAGE_HISTO + gameSaveId);
      if (stage == null) {
        throw new NotFoundException("Stage not found in cache");
      }
      UUID uuid = UUID.fromString(gameSaveId);
      PersistStageCommand command = PersistStageCommand.fromStage(uuid, stage);
      stageService.persistStage(command);
      Boolean result = stageRedisTemplate.delete(STAGE_HISTO + gameSaveId);
      if (Boolean.TRUE.equals(result)) {
        log.info("Deleted entry {}", STAGE_HISTO + gameSaveId);
      }
    } catch (DataAccessException | NotFoundException e) {
      log.error("Error while handling expired stage", e);
      throw e;
    }
    log.info("Stage of game save {} has been saved to DB", gameSaveId);
  }

  /**
   * Handles expired characteristics by saving it to DB.
   *
   * @param gameSaveId game save id
   */
  private void handleExpiredCharacteristics(String gameSaveId) {
    try {
      Characteristics characteristics =
          characteristicsRedisTemplate.opsForValue().get(CHARACTERISTICS_HISTO + gameSaveId);
      if (characteristics == null) {
        throw new NotFoundException("Characteristics not found in cache");
      }
      UUID uuid = UUID.fromString(gameSaveId);
      PersistCharacteristicsCommand command =
          PersistCharacteristicsCommand.fromCharacteristics(uuid, characteristics);
      characteristicsService.persistCharacteristics(command);
      Boolean result = characteristicsRedisTemplate.delete(CHARACTERISTICS_HISTO + gameSaveId);
      if (Boolean.TRUE.equals(result)) {
        log.info("Deleted entry {}", CHARACTERISTICS_HISTO + gameSaveId);
      }
    } catch (DataAccessException | NotFoundException e) {
      log.error("Error while handling expired characteristics", e);
      throw e;
    }
    log.info("Characteristics of game save {} has been saved to DB", gameSaveId);
  }

  /**
   * Handles expired currency by saving it to DB.
   *
   * @param gameSaveId game save id
   */
  private void handleExpiredCurrency(String gameSaveId) {
    try {
      Currency currency = currencyRedisTemplate.opsForValue().get(CURRENCY_HISTO + gameSaveId);
      if (currency == null) {
        throw new NotFoundException("Currency not found in cache");
      }
      UUID uuid = UUID.fromString(gameSaveId);
      var command = PersistCurrencyCommand.fromCurrency(uuid, currency);
      currencyService.persistCurrency(command);
      Boolean result = currencyRedisTemplate.delete(CURRENCY_HISTO + gameSaveId);
      if (Boolean.TRUE.equals(result)) {
        log.info("Deleted entry {}", CURRENCY_HISTO + gameSaveId);
      }
    } catch (DataAccessException | NotFoundException e) {
      log.error("Error while handling expired currency", e);
      throw e;
    }
    log.info("Currency of game save {} has been saved to DB", gameSaveId);
  }
}
