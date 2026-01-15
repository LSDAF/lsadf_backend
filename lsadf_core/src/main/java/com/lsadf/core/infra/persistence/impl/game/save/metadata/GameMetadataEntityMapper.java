/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.persistence.impl.game.save.metadata;

import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import java.util.Date;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for mapping between {@link GameMetadataEntity} and {@link GameMetadata}.
 * Utilizes MapStruct for generating the implementation.
 *
 * <p>This interface extends {@link EntityModelMapper} to provide the mapping logic for transforming
 * persistence-layer entities into domain-layer models. Additionally, it includes custom mappings
 * for specific field conversions.
 *
 * <p>Features: - Maps entity fields from {@link GameMetadataEntity} to corresponding fields in
 * {@link GameMetadata}. - Provides additional utility for converting long values representing dates
 * into {@link Date} objects.
 *
 * <p>Responsibilities: - Converts all relevant details from the entity to create an equivalent
 * domain model. - Ensures that null safety and appropriate transformations are handled during the
 * mapping process.
 *
 * <p>Notes: - The default implementation for mapping a {@code Long} to a {@code Date} is included.
 */
@Mapper
public interface GameMetadataEntityMapper
    extends EntityModelMapper<GameMetadataEntity, GameMetadata> {
  GameMetadataEntityMapper INSTANCE = Mappers.getMapper(GameMetadataEntityMapper.class);

  @Override
  GameMetadata map(GameMetadataEntity gameMetadataEntity);

  default Date map(Long value) {
    return value == null ? null : new Date(value);
  }
}
