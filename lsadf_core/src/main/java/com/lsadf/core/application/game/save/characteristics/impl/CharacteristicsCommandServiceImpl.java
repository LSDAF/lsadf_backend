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

import static com.lsadf.core.infra.util.ObjectUtils.getOrDefault;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsQueryService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.command.InitializeCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.InitializeDefaultCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CharacteristicsCommandServiceImpl implements CharacteristicsCommandService {

  private final CacheManager cacheManager;

  private final CharacteristicsRepositoryPort characteristicsRepositoryPort;
  private final CachePort<Characteristics> characteristicsCache;
  private final CharacteristicsQueryService characteristicsQueryService;

  public CharacteristicsCommandServiceImpl(
      CacheManager cacheManager,
      CharacteristicsRepositoryPort characteristicsRepositoryPort,
      CachePort<Characteristics> characteristicsCache,
      CharacteristicsQueryService characteristicsQueryService) {
    this.cacheManager = cacheManager;
    this.characteristicsRepositoryPort = characteristicsRepositoryPort;
    this.characteristicsCache = characteristicsCache;
    this.characteristicsQueryService = characteristicsQueryService;
  }

  @Override
  @Transactional
  public Characteristics initializeDefaultCharacteristics(
      InitializeDefaultCharacteristicsCommand command) {
    UUID gameSaveId = command.gameSaveId();
    return characteristicsRepositoryPort.create(gameSaveId);
  }

  @Override
  @Transactional
  public Characteristics initializeCharacteristics(InitializeCharacteristicsCommand command) {
    UUID gameSaveId = command.gameSaveId();
    Long attack = getOrDefault(command.attack(), 1L);
    Long critChance = getOrDefault(command.critChance(), 0L);
    Long critDamage = getOrDefault(command.critDamage(), 0L);
    Long health = getOrDefault(command.health(), 1L);
    Long resistance = getOrDefault(command.resistance(), 0L);
    return characteristicsRepositoryPort.create(
        gameSaveId, attack, critChance, critDamage, health, resistance);
  }

  @Override
  @Transactional
  public void persistCharacteristics(PersistCharacteristicsCommand command) {
    characteristicsRepositoryPort.update(
        command.gameSaveId(),
        command.attack(),
        command.critChance(),
        command.critDamage(),
        command.health(),
        command.resistance());
  }

  @Override
  public void updateCacheCharacteristics(UpdateCacheCharacteristicsCommand command) {
    Characteristics characteristics;
    if (isCharacteristicsNull(command)) {
      throw new IllegalArgumentException("Characteristics cannot be null");
    }
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      String gameSaveIdString = command.gameSaveId().toString();
      if (isCharacteristicsPartial(command)) {
        Characteristics existingCharacteristics =
            characteristicsCache
                .get(gameSaveIdString)
                .orElseGet(
                    () ->
                        characteristicsQueryService.retrieveCharacteristics(command.gameSaveId()));
        characteristics = mergeCharacteristics(command, existingCharacteristics);
      } else {
        characteristics =
            // Adding Objects.requireNonNull() since object is not partial
            new Characteristics(
                Objects.requireNonNull(command.attack()),
                Objects.requireNonNull(command.critChance()),
                Objects.requireNonNull(command.critDamage()),
                Objects.requireNonNull(command.health()),
                Objects.requireNonNull(command.resistance()));
      }
      characteristicsCache.set(gameSaveIdString, characteristics);
    } else {
      log.warn("Cache is disabled");
    }
  }

  /**
   * Merge the characteristics POJO with the characteristics from the database
   *
   * @param characteristics the characteristics POJO
   * @param dbCharacteristics the characteristics from the database
   * @return the merged characteristics POJO
   */
  private static Characteristics mergeCharacteristics(
      UpdateCacheCharacteristicsCommand characteristics, Characteristics dbCharacteristics) {
    Characteristics.CharacteristicsBuilder builder = Characteristics.builder();
    builder.attack(getOrDefault(characteristics.attack(), dbCharacteristics.attack()));
    builder.critChance(getOrDefault(characteristics.critChance(), dbCharacteristics.critChance()));
    builder.critDamage(getOrDefault(characteristics.critDamage(), dbCharacteristics.critDamage()));
    builder.health(getOrDefault(characteristics.health(), dbCharacteristics.health()));
    builder.resistance(getOrDefault(characteristics.resistance(), dbCharacteristics.resistance()));
    return builder.build();
  }

  /**
   * Checks if the given characteristics object has any null fields.
   *
   * @param characteristics the characteristics object to be checked
   * @return true if any of the fields (attack, critChance, critDamage, health, resistance) are
   *     null, false otherwise
   */
  private static boolean isCharacteristicsPartial(
      UpdateCacheCharacteristicsCommand characteristics) {
    return characteristics.attack() == null
        || characteristics.critChance() == null
        || characteristics.critDamage() == null
        || characteristics.health() == null
        || characteristics.resistance() == null;
  }

  /**
   * Checks if the given characteristics object has any null fields.
   *
   * @param characteristics the characteristics object to be checked
   * @return true if any of the fields (attack, critChance, critDamage, health, resistance) are
   *     null, false otherwise
   */
  private static boolean isCharacteristicsNull(UpdateCacheCharacteristicsCommand characteristics) {
    return characteristics.attack() == null
        && characteristics.critChance() == null
        && characteristics.critDamage() == null
        && characteristics.health() == null
        && characteristics.resistance() == null;
  }
}
