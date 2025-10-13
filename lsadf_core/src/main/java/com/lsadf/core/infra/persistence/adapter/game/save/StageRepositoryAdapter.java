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
package com.lsadf.core.infra.persistence.adapter.game.save;

import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntityMapper;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageRepository;
import com.lsadf.core.infra.util.ObjectUtils;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class StageRepositoryAdapter implements StageRepositoryPort {

  private final StageRepository stageRepository;
  private static final StageEntityMapper stageEntityMapper = StageEntityMapper.INSTANCE;

  public StageRepositoryAdapter(StageRepository stageRepository) {
    this.stageRepository = stageRepository;
  }

  @Override
  public Optional<Stage> findById(UUID id) {
    return stageRepository.findStageEntityById(id).map(stageEntityMapper::map);
  }

  @Override
  public Stage create(
      UUID id,
      @Nullable Long nullableCurrentStage,
      @Nullable Long nullableMaxStage,
      @Nullable Long nullableWave) {
    Long currentStage = ObjectUtils.getOrDefault(nullableCurrentStage, 1L);
    Long maxStage = ObjectUtils.getOrDefault(nullableMaxStage, Math.max(currentStage, 1L));
    Long wave = ObjectUtils.getOrDefault(nullableWave, 1L);
    StageEntity entity = stageRepository.createNewStageEntity(id, currentStage, maxStage, wave);
    return stageEntityMapper.map(entity);
  }

  @Override
  public Stage create(UUID id) {
    StageEntity entity = stageRepository.createNewStageEntity(id);
    return stageEntityMapper.map(entity);
  }

  @Override
  public Stage update(UUID gameSaveId, Stage stage) {
    StageEntity entity =
        stageRepository.updateStage(
            gameSaveId, stage.currentStage(), stage.maxStage(), stage.wave());
    return stageEntityMapper.map(entity);
  }

  @Override
  public boolean existsById(UUID id) {
    return stageRepository.findStageEntityById(id).isPresent();
  }

  @Override
  public Long count() {
    return stageRepository.count();
  }
}
