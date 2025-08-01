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

package com.lsadf.core.infra.valkey.listener.config;

import static com.lsadf.core.infra.valkey.cache.game.save.characteristics.CharacteristicsHash.CharacteristicsHashAttributes.*;
import static com.lsadf.core.infra.valkey.cache.game.save.currency.CurrencyHash.CurrencyHashAttributes.CURRENCY_HASH_KEY;
import static com.lsadf.core.infra.valkey.cache.game.save.stage.StageHash.StageHashAttributes.*;

import com.lsadf.core.infra.valkey.cache.config.properties.CacheExpirationProperties;
import com.lsadf.core.infra.valkey.cache.game.save.characteristics.CharacteristicsHash;
import com.lsadf.core.infra.valkey.cache.game.save.currency.CurrencyHash;
import com.lsadf.core.infra.valkey.cache.game.save.metadata.GameMetadataHash;
import com.lsadf.core.infra.valkey.cache.game.save.stage.StageHash;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.lang.NonNull;

@Configuration
public class ValkeyKeyspaceConfiguration extends KeyspaceConfiguration {

  private final CacheExpirationProperties cacheExpirationProperties;

  @Autowired
  public ValkeyKeyspaceConfiguration(CacheExpirationProperties cacheExpirationProperties) {
    this.cacheExpirationProperties = cacheExpirationProperties;
  }

  @NonNull
  @Override
  protected Iterable<KeyspaceSettings> initialConfiguration() {

    List<KeyspaceSettings> keyspaceSettings = new ArrayList<>();

    KeyspaceSettings characteristicsKeyspaceSettings =
        new KeyspaceSettings(CharacteristicsHash.class, CHARACTERISTICS_HASH_KEY);
    Long characteristicsExpiration =
        cacheExpirationProperties.getCharacteristicsExpirationSeconds().longValue();
    characteristicsKeyspaceSettings.setTimeToLive(characteristicsExpiration);
    keyspaceSettings.add(characteristicsKeyspaceSettings);

    KeyspaceSettings currenciesKeyspaceSettings =
        new KeyspaceSettings(CurrencyHash.class, CURRENCY_HASH_KEY);
    Long currenciesExpiration =
        cacheExpirationProperties.getCurrencyExpirationSeconds().longValue();
    currenciesKeyspaceSettings.setTimeToLive(currenciesExpiration);
    keyspaceSettings.add(currenciesKeyspaceSettings);

    KeyspaceSettings stagesKeyspaceSettings = new KeyspaceSettings(StageHash.class, STAGE_HASH_KEY);
    Long stagesExpiration = cacheExpirationProperties.getStageExpirationSeconds().longValue();
    stagesKeyspaceSettings.setTimeToLive(stagesExpiration);
    keyspaceSettings.add(stagesKeyspaceSettings);

    KeyspaceSettings gameMetadataKeyspaceSettings =
        new KeyspaceSettings(
            GameMetadataHash.class,
            GameMetadataHash.GameMetadataHashAttributes.GAME_METADATA_HASH_KEY);
    Long gameMetadataExpiration =
        cacheExpirationProperties.getGameMetadataExpirationSeconds().longValue();
    gameMetadataKeyspaceSettings.setTimeToLive(gameMetadataExpiration);
    keyspaceSettings.add(gameMetadataKeyspaceSettings);

    return keyspaceSettings;
  }
}
