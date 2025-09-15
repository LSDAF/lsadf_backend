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

import static com.lsadf.core.infra.util.ObjectUtils.getOrDefault;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.StageQueryService;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.command.InitializeDefaultStageCommand;
import com.lsadf.core.application.game.save.stage.command.InitializeStageCommand;
import com.lsadf.core.application.game.save.stage.command.PersistStageCommand;
import com.lsadf.core.application.game.save.stage.command.UpdateCacheStageCommand;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.stage.Stage;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class StageCommandServiceImpl implements StageCommandService {

  private final CacheManager cacheManager;
  private final StageRepositoryPort stageRepositoryPort;
  private final CachePort<Stage> stageCache;
  private final StageQueryService stageQueryService;

  public StageCommandServiceImpl(
      CacheManager cacheManager,
      StageRepositoryPort stageRepositoryPort,
      CachePort<Stage> stageCache,
      StageQueryService stageQueryService) {
    this.cacheManager = cacheManager;
    this.stageRepositoryPort = stageRepositoryPort;
    this.stageCache = stageCache;
    this.stageQueryService = stageQueryService;
  }

  @Override
  @Transactional
  public Stage initializeDefaultStage(InitializeDefaultStageCommand command) {
    UUID gameSaveId = command.gameSaveId();
    return stageRepositoryPort.create(gameSaveId);
  }

  @Override
  @Transactional
  public Stage initializeStage(InitializeStageCommand command) {
    UUID gameSaveId = command.gameSaveId();
    Long currentStage = command.currentStage();
    Long maxStage = command.maxStage();
    return stageRepositoryPort.create(gameSaveId, currentStage, maxStage);
  }

  @Override
  @Transactional
  public void persistStage(PersistStageCommand command) {
    Stage stage =
        Stage.builder().currentStage(command.currentStage()).maxStage(command.maxStage()).build();
    stageRepositoryPort.update(command.gameSaveId(), stage);
  }

  @Override
  public void updateCacheStage(UpdateCacheStageCommand command) {
    Stage stage;
    if (isStageNull(command)) {
      throw new IllegalArgumentException("Stage cannot be null");
    }
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      String gameSaveIdString = command.gameSaveId().toString();
      if (isStagePartial(command)) {
        Stage existingStage =
            stageCache
                .get(gameSaveIdString)
                .orElseGet(() -> stageQueryService.retrieveStage(command.gameSaveId()));
        stage = mergeStage(command, existingStage);
      } else {
        stage =
            // Adding Objects.requireNonNull() since object is not partial
            new Stage(
                Objects.requireNonNull(command.currentStage()),
                Objects.requireNonNull(command.maxStage()));
      }
      stageCache.set(gameSaveIdString, stage);
    } else {
      log.warn("Cache is disabled");
    }
  }

  /**
   * Merge the stage POJO with the stage from the database
   *
   * @param command the stage command
   * @param dbStage the stage from the database
   * @return the merged stage POJO
   */
  private static Stage mergeStage(UpdateCacheStageCommand command, Stage dbStage) {
    Stage.StageBuilder builder = Stage.builder();
    builder.currentStage(getOrDefault(command.currentStage(), dbStage.currentStage()));
    builder.maxStage(getOrDefault(command.maxStage(), dbStage.maxStage()));
    return builder.build();
  }

  /**
   * Checks if the given stage object has any null fields.
   *
   * @param command the stage command to be checked
   * @return true if any of the fields (currentStage, maxStage) are null, false otherwise
   */
  private static boolean isStagePartial(UpdateCacheStageCommand command) {
    return command.currentStage() == null || command.maxStage() == null;
  }

  /**
   * Checks if the given stage object has all null fields.
   *
   * @param command the stage command to be checked
   * @return true if all of the fields (currentStage, maxStage) are null, false otherwise
   */
  private static boolean isStageNull(UpdateCacheStageCommand command) {
    return command.currentStage() == null && command.maxStage() == null;
  }
}
