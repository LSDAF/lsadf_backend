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

package com.lsadf.core.infra.persistence.mapper.game;

import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.persistence.game.stage.StageEntity;
import com.lsadf.core.infra.persistence.mapper.EntityModelMapper;

/**
 * A mapper implementation for converting a {@link StageEntity} to a {@link Stage}. This class
 * provides the mapping capability to transform persistence-layer entities into domain models for
 * application use.
 */
public class StageEntityMapper implements EntityModelMapper<StageEntity, Stage> {

  /** {@inheritDoc} */
  @Override
  public Stage map(StageEntity stageEntity) {
    return Stage.builder()
        .maxStage(stageEntity.getMaxStage())
        .currentStage(stageEntity.getCurrentStage())
        .build();
  }
}
