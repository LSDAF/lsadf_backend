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

package com.lsadf.core.infra.persistence.table.game.save.characteristics;

import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Maps {@link CharacteristicsEntity} objects to {@link Characteristics} domain models.
 *
 * <p>This interface serves as a concrete implementation of the {@link EntityModelMapper} interface,
 * transforming persistence-layer entities into domain models used in the application layer.
 *
 * <p>The mapping process includes the following transformation details: - Maps the {@code attack}
 * attribute from {@link CharacteristicsEntity} to {@link Characteristics}. - Maps the {@code
 * critChance} attribute from {@link CharacteristicsEntity} to {@link Characteristics}. - Maps the
 * {@code critDamage} attribute from {@link CharacteristicsEntity} to {@link Characteristics}. -
 * Maps the {@code health} attribute from {@link CharacteristicsEntity} to {@link Characteristics}.
 * - Maps the {@code resistance} attribute from {@link CharacteristicsEntity} to {@link
 * Characteristics}.
 *
 * <p>Design notes: - Uses MapStruct for the mapper implementation. - Ensures type correctness and
 * proper handling of null or invalid values during the mapping process. - Promotes reusability and
 * decoupling between persistence and application layers.
 */
@Mapper
public interface CharacteristicsEntityMapper
    extends EntityModelMapper<CharacteristicsEntity, Characteristics> {
  CharacteristicsEntityMapper INSTANCE = Mappers.getMapper(CharacteristicsEntityMapper.class);

  /** {@inheritDoc} */
  @Override
  Characteristics map(CharacteristicsEntity characteristicsEntity);
}
