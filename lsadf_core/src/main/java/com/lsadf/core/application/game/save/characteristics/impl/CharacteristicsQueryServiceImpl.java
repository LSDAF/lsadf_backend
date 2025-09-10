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

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsQueryService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.Optional;
import java.util.UUID;

public class CharacteristicsQueryServiceImpl implements CharacteristicsQueryService {

  private final CacheManager cacheManager;

  private final CharacteristicsRepositoryPort characteristicsRepositoryPort;
  private final CachePort<Characteristics> characteristicsCache;

  public CharacteristicsQueryServiceImpl(
      CacheManager cacheManager,
      CharacteristicsRepositoryPort characteristicsRepositoryPort,
      CachePort<Characteristics> characteristicsCache) {
    this.cacheManager = cacheManager;
    this.characteristicsRepositoryPort = characteristicsRepositoryPort;
    this.characteristicsCache = characteristicsCache;
  }

  @Override
  public Characteristics retrieveCharacteristics(UUID gameSaveId) throws NotFoundException {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Characteristics> optionalCachedCharacteristics =
          characteristicsCache.get(gameSaveIdString);
      if (optionalCachedCharacteristics.isPresent()) {
        return optionalCachedCharacteristics.get();
      }
    }
    return getCharacteristicsFromDatabase(gameSaveId);
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
}
