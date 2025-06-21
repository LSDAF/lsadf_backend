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
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsRepository;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public class CharacteristicsServiceImpl implements CharacteristicsService {

  private final CharacteristicsRepository characteristicsRepository;
  private final Cache<Characteristics> characteristicsCache;
  private final CharacteristicsEntityMapper characteristicsEntityModelMapper;

  public CharacteristicsServiceImpl(
      CharacteristicsRepository characteristicsRepository,
      Cache<Characteristics> characteristicsCache,
      CharacteristicsEntityMapper characteristicsEntityModelMapper) {
    this.characteristicsRepository = characteristicsRepository;
    this.characteristicsCache = characteristicsCache;
    this.characteristicsEntityModelMapper = characteristicsEntityModelMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public Characteristics getCharacteristics(String gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (characteristicsCache.isEnabled()) {
      Optional<Characteristics> optionalCachedCharacteristics =
          characteristicsCache.get(gameSaveId);
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
  public void saveCharacteristics(
      String gameSaveId, Characteristics characteristics, boolean toCache)
      throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (characteristics == null || isCharacteristicsNull(characteristics)) {
      throw new IllegalArgumentException("Characteristics cannot be null");
    }
    if (toCache) {
      characteristicsCache.set(gameSaveId, characteristics);
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
  private CharacteristicsEntity getCharacteristicsEntity(String gameSaveId)
      throws NotFoundException {
    return characteristicsRepository
        .findById(gameSaveId)
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
  private void saveCharacteristicsToDatabase(String gameSaveId, Characteristics characteristics)
      throws NotFoundException {
    CharacteristicsEntity characteristicsEntity = getCharacteristicsEntity(gameSaveId);

    boolean hasUpdates = false;

    if (characteristics.getAttack() != null) {
      characteristicsEntity.setAttack(characteristics.getAttack());
      hasUpdates = true;
    }
    if (characteristics.getCritChance() != null) {
      characteristicsEntity.setCritChance(characteristics.getCritChance());
      hasUpdates = true;
    }
    if (characteristics.getCritDamage() != null) {
      characteristicsEntity.setCritDamage(characteristics.getCritDamage());
      hasUpdates = true;
    }
    if (characteristics.getHealth() != null) {
      characteristicsEntity.setHealth(characteristics.getHealth());
      hasUpdates = true;
    }
    if (characteristics.getResistance() != null) {
      characteristicsEntity.setResistance(characteristics.getResistance());
      hasUpdates = true;
    }

    if (hasUpdates) {
      characteristicsRepository.save(characteristicsEntity);
    }
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
