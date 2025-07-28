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
package com.lsadf.core.application.game.save.characteristics;

import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.table.game.save.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.table.game.save.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.table.game.save.characteristics.CharacteristicsRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class CharacteristicsServiceImpl implements CharacteristicsService {

  private final CharacteristicsRepository characteristicsRepository;
  private final Cache<Characteristics> characteristicsCache;
  private static final CharacteristicsEntityMapper characteristicsEntityModelMapper =
      CharacteristicsEntityMapper.INSTANCE;

  public CharacteristicsServiceImpl(
      CharacteristicsRepository characteristicsRepository,
      Cache<Characteristics> characteristicsCache) {
    this.characteristicsRepository = characteristicsRepository;
    this.characteristicsCache = characteristicsCache;
  }

  /** {@inheritDoc} */
  @Override
  public Characteristics createNewCharacteristics(
      UUID gameSaveId,
      Long attack,
      Long critChance,
      Long critDamage,
      Long health,
      Long resistance) {
    var newEntity =
        characteristicsRepository.createNewCharacteristicsEntity(
            gameSaveId, attack, critChance, critDamage, health, resistance);
    return characteristicsEntityModelMapper.map(newEntity);
  }

  /** {@inheritDoc} */
  @Override
  public Characteristics createNewCharacteristics(UUID gameSaveId) {
    var newEntity = characteristicsRepository.createNewCharacteristicsEntity(gameSaveId);
    return characteristicsEntityModelMapper.map(newEntity);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Characteristics getCharacteristics(UUID gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (characteristicsCache.isEnabled()) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Characteristics> optionalCachedCharacteristics =
          characteristicsCache.get(gameSaveIdString);
      if (optionalCachedCharacteristics.isPresent()) {
        Characteristics characteristics = optionalCachedCharacteristics.get();
        if (characteristics.attack() == null
            || characteristics.critChance() == null
            || characteristics.critDamage() == null
            || characteristics.health() == null
            || characteristics.resistance() == null) {
          CharacteristicsEntity characteristicsEntity = getCharacteristicsEntity(gameSaveId);
          return mergeCharacteristics(characteristics, characteristicsEntity);
        }
        return characteristics;
      }
    }
    CharacteristicsEntity characteristicsEntity = getCharacteristicsEntity(gameSaveId);

    return characteristicsEntityModelMapper.map(characteristicsEntity);
  }

  /**
   * Merge the characteristics POJO with the characteristics entity from the database
   *
   * @param characteristics the characteristics POJO
   * @param characteristicsEntity the characteristics entity from the database
   * @return the merged characteristics POJO
   */
  private static Characteristics mergeCharacteristics(
      Characteristics characteristics, CharacteristicsEntity characteristicsEntity) {
    var builder = Characteristics.builder();
    builder.attack(
        characteristics.attack() == null
            ? characteristicsEntity.getAttack()
            : characteristics.attack());

    builder.critChance(
        characteristics.critChance() == null
            ? characteristicsEntity.getCritChance()
            : characteristics.critChance());

    builder.critDamage(
        characteristics.critDamage() == null
            ? characteristicsEntity.getCritDamage()
            : characteristics.critDamage());

    builder.health(
        characteristics.health() == null
            ? characteristicsEntity.getHealth()
            : characteristics.health());

    builder.resistance(
        characteristics.resistance() == null
            ? characteristicsEntity.getResistance()
            : characteristics.resistance());

    return builder.build();
  }

  /** {@inheritDoc} */
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
   * Get the characteristics entity from the database
   *
   * @param gameSaveId the id of the game save
   * @return the characteristics entity
   * @throws NotFoundException if the characteristics entity is not found
   */
  private CharacteristicsEntity getCharacteristicsEntity(UUID gameSaveId) throws NotFoundException {
    return characteristicsRepository
        .findCharacteristicsEntityById(gameSaveId)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Characteristics entity not found for game save id " + gameSaveId));
  }

  /**
   * Save the gold amount to the database
   *
   * @param gameSaveId the id of the game save
   * @param characteristics the characteristics POJO
   * @throws NotFoundException if the characteristics entity is not found
   */
  private void saveCharacteristicsToDatabase(UUID gameSaveId, Characteristics characteristics)
      throws NotFoundException {
    characteristicsRepository.updateCharacteristics(
        gameSaveId,
        characteristics.attack(),
        characteristics.critChance(),
        characteristics.critDamage(),
        characteristics.health(),
        characteristics.health());
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
}
