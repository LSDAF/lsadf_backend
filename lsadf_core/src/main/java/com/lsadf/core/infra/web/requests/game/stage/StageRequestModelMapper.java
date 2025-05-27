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

package com.lsadf.core.infra.web.requests.game.stage;

import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.web.requests.RequestModelMapper;

/**
 * A mapper class responsible for converting a {@link StageRequest} object into a {@link Stage}
 * object. This class implements {@link RequestModelMapper} to facilitate the mapping process.
 *
 * <p>The {@code mapToModel} method is overridden to provide the specific logic for mapping the
 * properties of a {@code StageRequest} instance (currentStage and maxStage) to a {@code Stage}
 * instance.
 */
public class StageRequestModelMapper implements RequestModelMapper<StageRequest, Stage> {
  /** {@inheritDoc} */
  @Override
  public Stage mapToModel(StageRequest stageRequest) {
    return Stage.builder()
        .maxStage(stageRequest.getMaxStage())
        .currentStage(stageRequest.getCurrentStage())
        .build();
  }
}
