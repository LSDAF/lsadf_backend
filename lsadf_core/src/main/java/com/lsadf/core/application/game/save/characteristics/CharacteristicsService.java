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
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;

public interface CharacteristicsService {
  /**
   * Get the characteristics of a game save
   *
   * @param gameSaveId the game save id
   * @return the characteristics
   * @throws NotFoundException if the characteristics are not found
   */
  Characteristics getCharacteristics(UUID gameSaveId) throws NotFoundException;

  /**
   * Save the characteristics of a game save
   *
   * @param gameSaveId the game save id
   * @param characteristics the characteristics to save
   * @param toCache true if the characteristics should be saved to cache, false otherwise
   * @throws NotFoundException if the characteristics are not found
   */
  void saveCharacteristics(UUID gameSaveId, Characteristics characteristics, boolean toCache)
      throws NotFoundException;

  /**
   * Creates a new set of characteristics for a specified game save.
   *
   * @param gameSaveId the unique identifier of the game save
   * @param attack the attack value for the characteristics
   * @param critChance the critical chance value for the characteristics
   * @param critDamage the critical damage value for the characteristics
   * @param health the health value for the characteristics
   * @param resistance the resistance value for the characteristics
   * @return the newly created Characteristics instance
   */
  Characteristics createNewCharacteristics(
      UUID gameSaveId, Long attack, Long critChance, Long critDamage, Long health, Long resistance);

  /**
   * Creates a new set of default characteristics for a specified game save. This method initializes
   * a Characteristics object with default values.
   *
   * @param gameSaveId the unique identifier of the game save
   * @return a new Characteristics instance with default values
   */
  Characteristics createNewCharacteristics(UUID gameSaveId);
}
