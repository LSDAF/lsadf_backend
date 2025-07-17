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

package com.lsadf.core.infra.web.response.game.characteristics;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.web.response.ModelResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Interface responsible for mapping {@link Characteristics} model objects to their corresponding
 * {@link CharacteristicsResponse} representations.
 *
 * <p>This interface extends the {@link ModelResponseMapper} interface, which provides a generic
 * definition for mapping domain models to their associated response objects.
 *
 * <p>The implementation of this mapper is automatically generated at runtime by the MapStruct
 * library. The generated implementation is responsible for mapping all relevant fields between the
 * Characteristics and CharacteristicsResponse objects and is accessible as a static instance
 * variable {@code INSTANCE}.
 */
@Mapper
public interface CharacteristicsResponseMapper
    extends ModelResponseMapper<Characteristics, CharacteristicsResponse> {
  CharacteristicsResponseMapper INSTANCE = Mappers.getMapper(CharacteristicsResponseMapper.class);

  /**
   * Maps a Characteristics domain model to a CharacteristicsResponse DTO.
   *
   * @param characteristics the Characteristics instance to be mapped
   * @return a CharacteristicsResponse instance containing the mapped data
   */
  @Override
  CharacteristicsResponse map(Characteristics characteristics);
}
