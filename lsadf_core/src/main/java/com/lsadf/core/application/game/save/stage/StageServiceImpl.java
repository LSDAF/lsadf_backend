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
package com.lsadf.core.application.game.save.stage;

import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.table.game.save.stage.StageEntity;
import com.lsadf.core.infra.persistence.table.game.save.stage.StageEntityMapper;
import com.lsadf.core.infra.persistence.table.game.save.stage.StageRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of the stage service. */
public class StageServiceImpl implements StageService {

  private final StageRepository stageRepository;
  private final Cache<Stage> stageCache;
  private static final StageEntityMapper mapper = StageEntityMapper.INSTANCE;

  public StageServiceImpl(StageRepository stageRepository, Cache<Stage> stageCache) {
    this.stageRepository = stageRepository;
    this.stageCache = stageCache;
  }

  /** {@inheritDoc} */
  @Override
  public Stage createNewStage(UUID gameSaveId) {
    var newEntity = stageRepository.createNewStageEntity(gameSaveId);
    return mapper.map(newEntity);
  }

  /** {@inheritDoc} */
  @Override
  public Stage createNewStage(UUID gameSaveId, Long currentStage, Long maxStage) {
    var newEntity = stageRepository.createNewStageEntity(gameSaveId, currentStage, maxStage);
    return mapper.map(newEntity);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Stage getStage(UUID gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (stageCache.isEnabled()) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Stage> optionalCachedStage = stageCache.get(gameSaveIdString);
      if (optionalCachedStage.isPresent()) {
        Stage stage = optionalCachedStage.get();
        if (stage.maxStage() == null || stage.currentStage() == null) {
          StageEntity stageEntity = getStageEntity(gameSaveId);
          return mergeStages(stage, stageEntity);
        }
        return stage;
      }
    }
    StageEntity stageEntity = getStageEntity(gameSaveId);
    return mapper.map(stageEntity);
  }

  private StageEntity getStageEntity(UUID gameSaveId) {
    return stageRepository
        .findStageEntityById(gameSaveId)
        .orElseThrow(() -> new NotFoundException("Stage not found"));
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void saveStage(UUID gameSaveId, Stage stage, boolean toCache) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (stage == null || isStageNull(stage)) {
      throw new IllegalArgumentException("Stage cannot be null");
    }
    if (toCache) {
      String gameSaveIdString = gameSaveId.toString();
      stageCache.set(gameSaveIdString, stage);
    } else {
      saveStageToDatabase(gameSaveId, stage);
    }
  }

  /**
   * Save the stage to the database
   *
   * @param gameSaveId the game save id
   * @param stage the stage to save
   */
  private void saveStageToDatabase(UUID gameSaveId, Stage stage) {
    stageRepository.updateStage(gameSaveId, stage.currentStage(), stage.maxStage());
  }

  /**
   * Merge the stage POJO with the stage entity from the database
   *
   * @param stage the stage POJO
   * @param stageEntity the stage entity from the database
   * @return the merged stage POJO
   */
  private static Stage mergeStages(Stage stage, StageEntity stageEntity) {
    var builder = Stage.builder();
    builder.currentStage(
        stage.currentStage() != null ? stage.currentStage() : stageEntity.getCurrentStage());
    builder.maxStage(stage.maxStage() != null ? stage.maxStage() : stageEntity.getMaxStage());
    return builder.build();
  }

  /**
   * Check if the stage is null
   *
   * @param stage the stage
   * @return true if all the stage fields are null, false otherwise
   */
  private static boolean isStageNull(Stage stage) {
    return stage.currentStage() == null && stage.maxStage() == null;
  }
}
