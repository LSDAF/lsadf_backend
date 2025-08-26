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

import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.valkey.cache.Hash;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHash;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHashMapper;
import com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHash;
import com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHashMapper;
import com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHash;
import com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHashMapper;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;

@Slf4j
public class ValkeyRepositoryKeyExpirationListener {

  private final StageService stageService;
  private final CharacteristicsService characteristicsService;
  private final CurrencyService currencyService;

  private static final CharacteristicsHashMapper characteristicsHashMapper =
      CharacteristicsHashMapper.INSTANCE;
  private static final CurrencyHashMapper currencyHashMapper = CurrencyHashMapper.INSTANCE;
  private static final StageHashMapper stageHashMapper = StageHashMapper.INSTANCE;

  public ValkeyRepositoryKeyExpirationListener(
      StageService stageService,
      CharacteristicsService characteristicsService,
      CurrencyService currencyService) {
    this.stageService = stageService;
    this.characteristicsService = characteristicsService;
    this.currencyService = currencyService;
  }

  @EventListener
  public void handleExpiredHash(RedisKeyExpiredEvent<Hash<UUID>> event) {
    var keyspace = event.getKeyspace();
    switch (keyspace) {
      case CharacteristicsHash.CharacteristicsHashAttributes.CHARACTERISTICS_HASH_KEY -> {
        CharacteristicsHash characteristicsHash = (CharacteristicsHash) event.getValue();
        if (characteristicsHash != null) {
          handleExpiredCharacteristicsHash(characteristicsHash);
        } else {
          log.error("Characteristics hash is null");
        }
      }
      case CurrencyHash.CurrencyHashAttributes.CURRENCY_HASH_KEY -> {
        CurrencyHash currencyHash = (CurrencyHash) event.getValue();
        if (currencyHash != null) {
          handleExpiredCurrencyHash(currencyHash);
        } else {
          log.error("Currency hash is null");
        }
      }
      case StageHash.StageHashAttributes.STAGE_HASH_KEY -> {
        StageHash stageHash = (StageHash) event.getValue();
        if (stageHash != null) {
          handleExpiredStageHash(stageHash);
        } else {
          log.error("Stage hash is null");
        }
      }
      default -> log.error("Unknown keyspace: {}. Ignoring expired key.", keyspace);
    }
  }

  private void handleExpiredCurrencyHash(CurrencyHash currencyHash) {
    var uuid = currencyHash.getId();
    log.info("Currency hash with id {} expired", currencyHash.getId());
    Currency currency = currencyHashMapper.map(currencyHash);
    try {
      currencyService.saveCurrency(uuid, currency, false);
    } catch (Exception e) {
      log.error("Error while saving currency with id {}", uuid, e);
    }
  }

  private void handleExpiredCharacteristicsHash(CharacteristicsHash characteristicsHash) {
    UUID uuid = characteristicsHash.getId();
    log.info("Characteristics hash with id {} expired", uuid);
    com.lsadf.core.domain.game.save.characteristics.Characteristics characteristics =
        characteristicsHashMapper.map(characteristicsHash);
    try {
      characteristicsService.saveCharacteristics(uuid, characteristics, false);
    } catch (Exception e) {
      log.error("Error while saving characteristics with id {}", uuid, e);
    }
  }

  private void handleExpiredStageHash(StageHash stageHash) {
    UUID uuid = stageHash.getId();
    log.info("Stage hash with id {} expired", uuid);
    com.lsadf.core.domain.game.save.stage.Stage stage = stageHashMapper.map(stageHash);
    try {
      stageService.saveStage(uuid, stage, false);
    } catch (Exception e) {
      log.error("Error while saving stage with id {}", uuid, e);
    }
  }
}
