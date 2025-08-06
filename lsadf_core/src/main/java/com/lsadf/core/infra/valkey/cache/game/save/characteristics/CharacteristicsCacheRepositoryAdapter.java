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

package com.lsadf.core.infra.valkey.cache.game.save.characteristics;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CharacteristicsCacheRepositoryAdapter implements CharacteristicsCachePort {
  private final CharacteristicsHashRepository characteristicsHashRepository;
  private static final HashModelMapper<CharacteristicsHash, Characteristics>
      CHARACTERISTICS_HASH_MAPPER = CharacteristicsHashMapper.INSTANCE;

  public CharacteristicsCacheRepositoryAdapter(
      CharacteristicsHashRepository characteristicsHashRepository) {
    this.characteristicsHashRepository = characteristicsHashRepository;
  }

  @Override
  public Optional<Characteristics> getHisto(String key) {
    return Optional.empty();
  }

  @Override
  public Map<String, Characteristics> getAllHisto() {
    return Map.of();
  }

  @Override
  public Optional<Characteristics> get(String key) {
    UUID uuid = UUID.fromString(key);
    Optional<CharacteristicsHash> hashOptional = this.characteristicsHashRepository.findById(uuid);
    if (hashOptional.isPresent()) {
      Characteristics characteristics = CHARACTERISTICS_HASH_MAPPER.map(hashOptional.get());
      return Optional.of(characteristics);
    }
    return Optional.empty();
  }

  @Override
  public void set(String key, Characteristics value, int expirationSeconds) {
    UUID uuid = UUID.fromString(key);
    Long expiration = (long) expirationSeconds;
    CharacteristicsHash hash =
        CharacteristicsHash.builder()
            .attack(value.attack())
            .gameSaveId(uuid)
            .resistance(value.resistance())
            .critChance(value.critChance())
            .expiration(expiration)
            .critDamage(value.critDamage())
            .health(value.health())
            .build();

    this.characteristicsHashRepository.save(hash);
  }

  @Override
  public void set(String key, Characteristics value) {
    UUID uuid = UUID.fromString(key);
    CharacteristicsHash hash =
        CharacteristicsHash.builder()
            .attack(value.attack())
            .gameSaveId(uuid)
            .resistance(value.resistance())
            .critChance(value.critChance())
            .critDamage(value.critDamage())
            .health(value.health())
            .build();

    this.characteristicsHashRepository.save(hash);
  }

  @Override
  public Map<String, Characteristics> getAll() {
    Map<String, Characteristics> characteristicsMap = new HashMap<>();
    characteristicsHashRepository
        .findAll()
        .forEach(
            hash ->
                characteristicsMap.put(
                    hash.getId().toString(), CHARACTERISTICS_HASH_MAPPER.map(hash)));
    return characteristicsMap;
  }

  @Override
  public void clear() {
    characteristicsHashRepository.deleteAll();
  }

  @Override
  public void unset(String key) {
    characteristicsHashRepository.deleteById(UUID.fromString(key));
  }
}
