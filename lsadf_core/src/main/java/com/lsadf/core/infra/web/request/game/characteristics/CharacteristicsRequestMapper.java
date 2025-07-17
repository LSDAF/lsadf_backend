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

package com.lsadf.core.infra.web.request.game.characteristics;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.web.request.RequestModelMapper;
import org.mapstruct.Mapper;

/**
 * Maps {@link CharacteristicsRequest} objects to {@link Characteristics} domain models. This class
 * serves as a concrete implementation of the {@link RequestModelMapper} interface, transforming
 * request data into the application's internal representation.
 *
 * <p>The mapping logic extracts the following properties from the {@code CharacteristicsRequest}
 * object and constructs a {@code Characteristics} object: - Attack level - Critical chance level -
 * Critical damage level - Health level - Resistance level
 *
 * <p>Implements the {@code mapToModel} method to define the specific transformation logic. This
 * ensures that input values from the request are accurately translated into the corresponding
 * fields of the domain model.
 */
@Mapper
public interface CharacteristicsRequestMapper
    extends RequestModelMapper<CharacteristicsRequest, Characteristics> {
  CharacteristicsRequestMapper INSTANCE =
      org.mapstruct.factory.Mappers.getMapper(CharacteristicsRequestMapper.class);

  /** {@inheritDoc} */
  @Override
  Characteristics map(CharacteristicsRequest characteristicsRequest);
}
