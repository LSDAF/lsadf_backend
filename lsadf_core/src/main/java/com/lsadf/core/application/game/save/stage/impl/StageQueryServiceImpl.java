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

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.stage.StageQueryService;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.Optional;
import java.util.UUID;

public class StageQueryServiceImpl implements StageQueryService {

  private final CacheManager cacheManager;
  private final StageRepositoryPort stageRepositoryPort;
  private final CachePort<Stage> stageCache;

  public StageQueryServiceImpl(
      CacheManager cacheManager,
      StageRepositoryPort stageRepositoryPort,
      CachePort<Stage> stageCache) {
    this.cacheManager = cacheManager;
    this.stageRepositoryPort = stageRepositoryPort;
    this.stageCache = stageCache;
  }

  @Override
  public Stage retrieveStage(UUID gameSaveId) {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      String gameSaveIdString = gameSaveId.toString();
      Optional<Stage> optionalCachedStage = stageCache.get(gameSaveIdString);
      if (optionalCachedStage.isPresent()) {
        return optionalCachedStage.get();
      }
    }
    return getStageFromDatabase(gameSaveId);
  }

  /**
   * Get the stage from the database
   *
   * @param gameSaveId the id of the game save
   * @return the stage
   * @throws NotFoundException if the stage is not found
   */
  private Stage getStageFromDatabase(UUID gameSaveId) throws NotFoundException {
    return stageRepositoryPort
        .findById(gameSaveId)
        .orElseThrow(() -> new NotFoundException("Stage not found for game save id " + gameSaveId));
  }
}
