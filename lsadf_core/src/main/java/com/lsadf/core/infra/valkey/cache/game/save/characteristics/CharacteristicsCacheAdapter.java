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
import com.lsadf.core.infra.valkey.cache.ValkeyCacheAdapter;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public class CharacteristicsCacheAdapter
    extends ValkeyCacheAdapter<CharacteristicsHash, Characteristics, UUID>
    implements CharacteristicsCachePort {

  private final CharacteristicsHashRepository characteristicsHashRepository;
  private static final CharacteristicsHashMapper characteristicsHashMapper =
      CharacteristicsHashMapper.INSTANCE;

  public CharacteristicsCacheAdapter(CharacteristicsHashRepository characteristicsHashRepository) {
    this.characteristicsHashRepository = characteristicsHashRepository;
  }

  @Override
  protected HashModelMapper<CharacteristicsHash, Characteristics> getMapper() {
    return characteristicsHashMapper;
  }

  @Override
  protected CrudRepository<CharacteristicsHash, UUID> getRepository() {
    return this.characteristicsHashRepository;
  }

  @Override
  public void set(UUID key, Characteristics value) {
    CharacteristicsHash hash =
        CharacteristicsHash.builder()
            .attack(value.attack())
            .gameSaveId(key)
            .critDamage(value.critDamage())
            .critChance(value.critChance())
            .resistance(value.resistance())
            .health(value.health())
            .build();

    this.characteristicsHashRepository.save(hash);
  }
}
