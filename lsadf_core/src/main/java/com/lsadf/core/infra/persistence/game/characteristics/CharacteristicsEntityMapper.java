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

package com.lsadf.core.infra.persistence.game.characteristics;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.persistence.EntityModelMapper;

/**
 * Maps a {@link CharacteristicsEntity} to a {@link Characteristics} model. This class implements
 * the {@link EntityModelMapper} interface to convert persistent entities used within the data layer
 * into domain models that are utilized in the business or application logic layer.
 *
 * <p>The mapping process involves transferring specific attributes such as attack, critical chance,
 * critical damage, health, and resistance from the entity representation into the corresponding
 * fields within the model representation.
 *
 * <p>This class is designed to be used in scenarios where characteristics information needs to be
 * transferred from the persistence layer to the application layer, ensuring data consistency and
 * proper encapsulation.
 *
 * <p>Thread Safety: This implementation is thread-safe as it performs no mutation of shared state.
 */
public class CharacteristicsEntityMapper
    implements EntityModelMapper<CharacteristicsEntity, Characteristics> {

  /** {@inheritDoc} */
  @Override
  public Characteristics map(CharacteristicsEntity characteristicsEntity) {
    return new Characteristics(
        characteristicsEntity.getAttack(),
        characteristicsEntity.getCritChance(),
        characteristicsEntity.getCritDamage(),
        characteristicsEntity.getHealth(),
        characteristicsEntity.getResistance());
  }
}
