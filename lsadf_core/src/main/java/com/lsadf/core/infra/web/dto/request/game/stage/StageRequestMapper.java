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

package com.lsadf.core.infra.web.dto.request.game.stage;

import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.web.dto.request.RequestModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The {@code StageRequestMapper} interface is a MapStruct mapper responsible for converting {@link
 * StageRequest} objects into {@link Stage} objects.
 *
 * <p>This mapper facilitates the transformation of client-facing request payloads into domain model
 * objects, adhering to the contract defined by the {@link RequestModelMapper} interface.
 *
 * <p>Using MapStruct's automatic mapping capabilities, this interface simplifies the mapping logic,
 * removing the need for manual transformation of properties between {@code StageRequest} and {@code
 * Stage}.
 *
 * <p>Key features: - Maps all fields between {@code StageRequest} and {@code Stage} directly,
 * provided they share compatible types. - Uses the {@link Mappers#getMapper(Class)} method to
 * automatically provide an implementation of this interface.
 *
 * <p>See {@link StageRequest} for details on the request-layer object. See {@link Stage} for
 * details on the domain-layer model.
 */
@Mapper
public interface StageRequestMapper extends RequestModelMapper<StageRequest, Stage> {
  StageRequestMapper INSTANCE = Mappers.getMapper(StageRequestMapper.class);

  @Override
  Stage map(StageRequest stageRequest);
}
