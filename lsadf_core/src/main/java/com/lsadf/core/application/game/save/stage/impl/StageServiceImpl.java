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
package com.lsadf.core.application.game.save.stage.impl;

import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of the stage service. */
public class StageServiceImpl implements StageService {

  private final CacheManager cacheManager;

  private final StageRepositoryPort stageRepositoryPort;
  private final StageCachePort stageCache;

  public StageServiceImpl(
      CacheManager cacheManager,
      StageRepositoryPort stageRepositoryPort,
      StageCachePort stageCache) {
    this.cacheManager = cacheManager;
    this.stageRepositoryPort = stageRepositoryPort;
    this.stageCache = stageCache;
  }

  @Override
  @Transactional
  public Stage createNewStage(UUID gameSaveId) {
    return stageRepositoryPort.create(gameSaveId);
  }

  @Override
  @Transactional
  public Stage createNewStage(UUID gameSaveId, Long currentStage, Long maxStage) {
    return stageRepositoryPort.create(gameSaveId, currentStage, maxStage);
  }

  @Override
  @Transactional(readOnly = true)
  public Stage getStage(UUID gameSaveId) throws NotFoundException {
    Stage stage;
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Stage> optionalCachedStage = stageCache.get(gameSaveIdString);
      if (optionalCachedStage.isPresent()) {
        stage = optionalCachedStage.get();
        if (isStagePartial(stage)) {
          Stage dbStage = getStageFromDatabase(gameSaveId);
          stage = mergeStages(stage, dbStage);
          stageCache.set(gameSaveIdString, stage);
          return stage;
        }
        return stage;
      }
      stage = getStageFromDatabase(gameSaveId);
      return stage;
    }
    return getStageFromDatabase(gameSaveId);
  }

  private Stage getStageFromDatabase(UUID gameSaveId) {
    return stageRepositoryPort
        .findById(gameSaveId)
        .orElseThrow(() -> new NotFoundException("Stage not found for game save id " + gameSaveId));
  }

  @Override
  @Transactional
  public void saveStage(UUID gameSaveId, Stage stage, boolean toCache) throws NotFoundException {
    if (isStageNull(stage)) {
      throw new IllegalArgumentException("Stage cannot be null");
    }
    if (toCache) {
      String gameSaveIdString = gameSaveId.toString();
      if (isStagePartial(stage)) {
        Stage existingStage =
            stageCache.get(gameSaveIdString).orElseGet(() -> getStageFromDatabase(gameSaveId));
        stage = mergeStages(stage, existingStage);
      }
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

  /**
   * Checks if the provided {@link Stage} is partially defined. A stage is considered partial if
   * either the current stage or the maximum stage is null.
   *
   * @param stage the stage to check
   * @return true if the stage is partially defined, false otherwise
   */
  private static boolean isStagePartial(Stage stage) {
    return stage.currentStage() == null || stage.maxStage() == null;
  }
}
