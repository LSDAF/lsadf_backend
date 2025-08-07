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
package com.lsadf.core.application.game.save.characteristics.impl;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.valkey.cache.service.CacheService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class CharacteristicsServiceImpl implements CharacteristicsService {

  private final CacheService cacheService;

  private final CharacteristicsRepositoryPort characteristicsRepositoryPort;
  private final CachePort<Characteristics> characteristicsCache;

  public CharacteristicsServiceImpl(
      CacheService cacheService,
      CharacteristicsRepositoryPort characteristicsRepositoryPort,
      CachePort<Characteristics> characteristicsCache) {
    this.cacheService = cacheService;
    this.characteristicsRepositoryPort = characteristicsRepositoryPort;
    this.characteristicsCache = characteristicsCache;
  }

  @Override
  @Transactional
  public Characteristics createNewCharacteristics(
      UUID gameSaveId,
      Long attack,
      Long critChance,
      Long critDamage,
      Long health,
      Long resistance) {
    return characteristicsRepositoryPort.create(
        gameSaveId, attack, critChance, critDamage, health, resistance);
  }

  @Override
  @Transactional
  public Characteristics createNewCharacteristics(UUID gameSaveId) {
    return characteristicsRepositoryPort.create(gameSaveId);
  }

  @Override
  @Transactional(readOnly = true)
  public Characteristics getCharacteristics(UUID gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (Boolean.TRUE.equals(cacheService.isEnabled())) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Characteristics> optionalCachedCharacteristics =
          characteristicsCache.get(gameSaveIdString);
      if (optionalCachedCharacteristics.isPresent()) {
        Characteristics characteristics = optionalCachedCharacteristics.get();
        if (isCharacteristicsPartial(characteristics)) {
          Characteristics dbCharacteristics = getCharacteristicsFromDatabase(gameSaveId);
          characteristics = mergeCharacteristics(characteristics, dbCharacteristics);
          characteristicsCache.set(gameSaveIdString, characteristics);
          return characteristics;
        }
        return characteristics;
      }
    }
    var characteristics = getCharacteristicsFromDatabase(gameSaveId);
    characteristicsCache.set(gameSaveId.toString(), characteristics);
    return characteristics;
  }

  /**
   * Merge the characteristics POJO with the characteristics from the database
   *
   * @param characteristics the characteristics POJO
   * @param dbCharacteristics the characteristics from the database
   * @return the merged characteristics POJO
   */
  private static Characteristics mergeCharacteristics(
      Characteristics characteristics, Characteristics dbCharacteristics) {
    var builder = Characteristics.builder();
    builder.attack(
        characteristics.attack() == null ? dbCharacteristics.attack() : characteristics.attack());

    builder.critChance(
        characteristics.critChance() == null
            ? dbCharacteristics.critChance()
            : characteristics.critChance());

    builder.critDamage(
        characteristics.critDamage() == null
            ? dbCharacteristics.critDamage()
            : characteristics.critDamage());

    builder.health(
        characteristics.health() == null ? dbCharacteristics.health() : characteristics.health());

    builder.resistance(
        characteristics.resistance() == null
            ? dbCharacteristics.resistance()
            : characteristics.resistance());

    return builder.build();
  }

  @Override
  @Transactional
  public void saveCharacteristics(UUID gameSaveId, Characteristics characteristics, boolean toCache)
      throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (characteristics == null || isCharacteristicsNull(characteristics)) {
      throw new IllegalArgumentException("Characteristics cannot be null");
    }
    if (toCache) {
      String gameSaveIdString = gameSaveId.toString();
      characteristicsCache.set(gameSaveIdString, characteristics);
    } else {
      saveCharacteristicsToDatabase(gameSaveId, characteristics);
    }
  }

  /**
   * Get the characteristics from the database
   *
   * @param gameSaveId the id of the game save
   * @return the characteristics
   * @throws NotFoundException if the characteristics is not found
   */
  private Characteristics getCharacteristicsFromDatabase(UUID gameSaveId) throws NotFoundException {
    return characteristicsRepositoryPort
        .findById(gameSaveId)
        .orElseThrow(
            () ->
                new NotFoundException("Characteristics not found for game save id " + gameSaveId));
  }

  /**
   * Save the characteristics to the database
   *
   * @param gameSaveId the id of the game save
   * @param characteristics the characteristics POJO
   * @throws NotFoundException if the characteristics entity is not found
   */
  private void saveCharacteristicsToDatabase(UUID gameSaveId, Characteristics characteristics)
      throws NotFoundException {
    Characteristics updatedCharacteristics =
        Characteristics.builder()
            .attack(characteristics.attack())
            .critChance(characteristics.critChance())
            .critDamage(characteristics.critDamage())
            .health(characteristics.health())
            .resistance(characteristics.resistance())
            .build();
    characteristicsRepositoryPort.update(gameSaveId, updatedCharacteristics);
  }

  /**
   * Check if the characteristics is all null
   *
   * @param characteristics the characteristics POJO
   * @return true if all fields are null, false otherwise
   */
  private static boolean isCharacteristicsNull(Characteristics characteristics) {
    return characteristics.attack() == null
        && characteristics.critChance() == null
        && characteristics.critDamage() == null
        && characteristics.health() == null
        && characteristics.resistance() == null;
  }

  /**
   * Checks if the given characteristics object has any null fields.
   *
   * @param characteristics the characteristics object to be checked
   * @return true if any of the fields (attack, critChance, critDamage, health, resistance) are
   *     null, false otherwise
   */
  private static boolean isCharacteristicsPartial(Characteristics characteristics) {
    return characteristics.attack() == null
        || characteristics.critChance() == null
        || characteristics.critDamage() == null
        || characteristics.health() == null
        || characteristics.resistance() == null;
  }
}
