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
package com.lsadf.core.application.game.characteristics;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.table.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.table.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.table.game.characteristics.CharacteristicsRepository;
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
        if (characteristics.getAttack() == null
            || characteristics.getCritChance() == null
            || characteristics.getCritDamage() == null
            || characteristics.getHealth() == null
            || characteristics.getResistance() == null) {
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
    if (characteristics.getAttack() == null) {
      characteristics.setAttack(characteristicsEntity.getAttack());
    }

    if (characteristics.getCritChance() == null) {
      characteristics.setCritChance(characteristicsEntity.getCritChance());
    }

    if (characteristics.getCritDamage() == null) {
      characteristics.setCritDamage(characteristicsEntity.getCritDamage());
    }

    if (characteristics.getHealth() == null) {
      characteristics.setHealth(characteristicsEntity.getHealth());
    }

    if (characteristics.getResistance() == null) {
      characteristics.setResistance(characteristicsEntity.getResistance());
    }

    return characteristics;
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
        characteristics.getAttack(),
        characteristics.getCritChance(),
        characteristics.getCritDamage(),
        characteristics.getHealth(),
        characteristics.getHealth());
  }

  /**
   * Check if the characteristics is all null
   *
   * @param characteristics the characteristics POJO
   * @return true if all fields are null, false otherwise
   */
  private static boolean isCharacteristicsNull(Characteristics characteristics) {
    return characteristics.getAttack() == null
        && characteristics.getCritChance() == null
        && characteristics.getCritDamage() == null
        && characteristics.getHealth() == null
        && characteristics.getResistance() == null;
  }
}
