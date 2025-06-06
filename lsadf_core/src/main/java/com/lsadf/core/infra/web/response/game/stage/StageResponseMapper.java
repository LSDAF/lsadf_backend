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

package com.lsadf.core.infra.web.response.game.stage;

import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.web.response.ModelResponseMapper;

/**
 * Concrete implementation of {@link ModelResponseMapper} for mapping {@link Stage} model objects to
 * {@link StageResponse} response objects.
 *
 * <p>This class handles the transformation of Stage model data, such as currentStage and maxStage,
 * into their corresponding representation suited for API responses through StageResponse.
 */
public class StageResponseMapper implements ModelResponseMapper<Stage, StageResponse> {
  /**
   * Maps a {@link Stage} model object to a {@link StageResponse} response object.
   *
   * @param model the {@link Stage} model object to be mapped
   * @return the {@link StageResponse} response object that corresponds to the provided model
   */
  @Override
  public StageResponse mapToResponse(Stage model) {
    return StageResponse.builder()
        .currentStage(model.getCurrentStage())
        .maxStage(model.getMaxStage())
        .build();
  }
}
