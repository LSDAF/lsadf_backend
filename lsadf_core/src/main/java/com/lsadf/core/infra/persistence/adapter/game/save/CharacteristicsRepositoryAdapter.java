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
package com.lsadf.core.infra.persistence.adapter.game.save;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsRepository;
import java.util.Optional;
import java.util.UUID;

public class CharacteristicsRepositoryAdapter implements CharacteristicsRepositoryPort {

  private final CharacteristicsRepository characteristicsRepository;
  private static final CharacteristicsEntityMapper characteristicsEntityMapper =
      CharacteristicsEntityMapper.INSTANCE;

  public CharacteristicsRepositoryAdapter(CharacteristicsRepository characteristicsRepository) {
    this.characteristicsRepository = characteristicsRepository;
  }

  @Override
  public Optional<Characteristics> findById(UUID id) {
    return characteristicsRepository
        .findCharacteristicsEntityById(id)
        .map(characteristicsEntityMapper::map);
  }

  @Override
  public Characteristics create(
      UUID id, Long attack, Long critChance, Long critDamage, Long health, Long resistance) {
    var entity =
        characteristicsRepository.createNewCharacteristicsEntity(
            id, attack, critChance, critDamage, health, resistance);
    return characteristicsEntityMapper.map(entity);
  }

  @Override
  public Characteristics create(UUID id) {
    var entity = characteristicsRepository.createNewCharacteristicsEntity(id);
    return characteristicsEntityMapper.map(entity);
  }

  @Override
  public Characteristics update(UUID gameSaveId, Characteristics characteristics) {
    var entity =
        characteristicsRepository.updateCharacteristics(
            gameSaveId,
            characteristics.attack(),
            characteristics.critChance(),
            characteristics.critDamage(),
            characteristics.health(),
            characteristics.resistance());
    return characteristicsEntityMapper.map(entity);
  }

  @Override
  public boolean existsById(UUID id) {
    return characteristicsRepository.findCharacteristicsEntityById(id).isPresent();
  }

  @Override
  public Long count() {
    return characteristicsRepository.count();
  }
}
