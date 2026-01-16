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
package com.lsadf.core.infra.web.dto.response.game.save.stage;

import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.web.dto.response.ModelResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * A mapper interface for converting {@link Stage} objects to {@link StageResponse} objects.
 *
 * <p>This interface extends {@link ModelResponseMapper} to provide a specific mapper for mapping
 * `Stage` domain model instances to their corresponding API response representations. It uses
 * MapStruct as the underlying mapping framework.
 */
@Mapper
public interface StageResponseMapper extends ModelResponseMapper<Stage, StageResponse> {

  StageResponseMapper INSTANCE = Mappers.getMapper(StageResponseMapper.class);

  /**
   * Maps a {@link Stage} model object to a {@link StageResponse} response object.
   *
   * @param model the {@link Stage} model object to be mapped
   * @return the {@link StageResponse} response object that corresponds to the provided model
   */
  @Override
  StageResponse map(Stage model);
}
