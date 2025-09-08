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

package com.lsadf.core.infra.valkey.cache.adapter.game.save.characteristics;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.adapter.ValkeyCacheRepositoryAdapter;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHash;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHashMapper;
import com.lsadf.core.infra.valkey.cache.impl.save.characteristics.CharacteristicsHashRepository;
import com.lsadf.core.infra.valkey.config.properties.ValkeyCacheExpirationProperties;
import java.util.Optional;
import java.util.UUID;

public class CharacteristicsCacheRepositoryAdapter
    extends ValkeyCacheRepositoryAdapter<Characteristics, CharacteristicsHash, UUID>
    implements CharacteristicsCachePort {
  private static final HashModelMapper<CharacteristicsHash, Characteristics>
      CHARACTERISTICS_HASH_MAPPER = CharacteristicsHashMapper.INSTANCE;

  public CharacteristicsCacheRepositoryAdapter(
      CharacteristicsHashRepository characteristicsHashRepository,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    super(characteristicsHashRepository);
    this.hashMapper = CHARACTERISTICS_HASH_MAPPER;
    this.expirationSeconds = valkeyCacheExpirationProperties.getCharacteristicsExpirationSeconds();
  }

  @Override
  public Optional<Characteristics> get(String key) {
    UUID uuid = UUID.fromString(key);
    Optional<CharacteristicsHash> hashOptional = this.repository.findById(uuid);
    if (hashOptional.isPresent()) {
      Characteristics characteristics = CHARACTERISTICS_HASH_MAPPER.map(hashOptional.get());
      return Optional.of(characteristics);
    }
    return Optional.empty();
  }

  @Override
  public void set(String key, Characteristics value) {
    UUID uuid = UUID.fromString(key);
    CharacteristicsHash hash =
        CharacteristicsHash.builder()
            .attack(value.attack())
            .id(uuid)
            .expiration(this.expirationSeconds)
            .resistance(value.resistance())
            .critChance(value.critChance())
            .critDamage(value.critDamage())
            .health(value.health())
            .build();

    this.repository.save(hash);
  }

  @Override
  public void unset(String key) {
    repository.deleteById(UUID.fromString(key));
  }
}
