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

import com.lsadf.core.infra.valkey.cache.Hash;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHash;
import com.lsadf.core.infra.valkey.cache.impl.save.currency.CurrencyHash;
import com.lsadf.core.infra.valkey.cache.impl.save.stage.StageHash;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;

@Slf4j
public class ValkeyRepositoryKeyExpirationListener {
  @EventListener
  public void handleExpiredHash(RedisKeyExpiredEvent<Hash<UUID>> event) {
    var keyspace = event.getKeyspace();
    switch (keyspace) {
      case CharacteristicsHash.CharacteristicsHashAttributes.CHARACTERISTICS_HASH_KEY -> {
        CharacteristicsHash characteristicsHash = (CharacteristicsHash) event.getValue();
        if (characteristicsHash != null) {
          log.info("Characteristics hash with id {} expired", characteristicsHash.getId());
        } else {
          log.error("Characteristics hash is null");
        }
      }
      case CurrencyHash.CurrencyHashAttributes.CURRENCY_HASH_KEY -> {
        CurrencyHash currencyHash = (CurrencyHash) event.getValue();
        if (currencyHash != null) {
          log.info("Currency hash with id {} expired", currencyHash.getId());
        } else {
          log.error("Currency hash is null");
        }
      }
      case StageHash.StageHashAttributes.STAGE_HASH_KEY -> {
        StageHash stageHash = (StageHash) event.getValue();
        if (stageHash != null) {
          log.info("Stage hash with id {} expired", stageHash.getId());
        } else {
          log.error("Stage hash is null");
        }
      }
      default -> log.error("Unknown keyspace: {}. Ignoring expired key.", keyspace);
    }
  }
}
