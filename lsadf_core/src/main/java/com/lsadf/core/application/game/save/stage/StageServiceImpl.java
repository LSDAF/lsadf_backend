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
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.valkey.cache.service.CacheService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of the stage service. */
public class StageServiceImpl implements StageService {

  private final CacheService cacheService;

  private final StageRepositoryPort stageRepositoryPort;
  private final StageCachePort stageCache;

  public StageServiceImpl(
      CacheService cacheService,
      StageRepositoryPort stageRepositoryPort,
      StageCachePort stageCache) {
    this.cacheService = cacheService;
    this.stageRepositoryPort = stageRepositoryPort;
    this.stageCache = stageCache;
  }

  /** {@inheritDoc} */
  @Override
  public Stage createNewStage(UUID gameSaveId) {
    return stageRepositoryPort.create(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  public Stage createNewStage(UUID gameSaveId, Long currentStage, Long maxStage) {
    return stageRepositoryPort.create(gameSaveId, currentStage, maxStage);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Stage getStage(UUID gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (Boolean.TRUE.equals(cacheService.isEnabled())) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Stage> optionalCachedStage = stageCache.get(gameSaveIdString);
      if (optionalCachedStage.isPresent()) {
        Stage stage = optionalCachedStage.get();
        if (stage.maxStage() == null || stage.currentStage() == null) {
          Stage dbStage = getStageFromDatabase(gameSaveId);
          return mergeStages(stage, dbStage);
        }
        return stage;
      }
    }
    return getStageFromDatabase(gameSaveId);
  }

  private Stage getStageFromDatabase(UUID gameSaveId) {
    return stageRepositoryPort
        .findById(gameSaveId)
        .orElseThrow(() -> new NotFoundException("Stage not found for game save id " + gameSaveId));
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
    Stage updatedStage =
        Stage.builder().currentStage(stage.currentStage()).maxStage(stage.maxStage()).build();
    stageRepositoryPort.update(gameSaveId, updatedStage);
  }

  /**
   * Merge the stage POJO with the stage from the database
   *
   * @param stage the stage POJO
   * @param dbStage the stage from the database
   * @return the merged stage POJO
   */
  private static Stage mergeStages(Stage stage, Stage dbStage) {
    var builder = Stage.builder();
    builder.currentStage(
        stage.currentStage() != null ? stage.currentStage() : dbStage.currentStage());
    builder.maxStage(stage.maxStage() != null ? stage.maxStage() : dbStage.maxStage());
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
