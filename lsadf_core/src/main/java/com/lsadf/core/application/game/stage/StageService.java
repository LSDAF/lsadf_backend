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
package com.lsadf.core.application.game.stage;

import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.exception.http.NotFoundException;
import java.util.UUID;

/** Service for managing stages. */
public interface StageService {

  /**
   * Get the stage for the given game save id.
   *
   * @param gameSaveId the game save id
   * @return the stage
   * @throws NotFoundException if the stage is not found
   */
  Stage getStage(UUID gameSaveId) throws NotFoundException;

  /**
   * Save the stage for the given game save id.
   *
   * @param gameSaveId the game save id
   * @param stage the stage to save
   * @param toCache whether to save to cache
   * @throws NotFoundException if the stage is not found
   */
  void saveStage(UUID gameSaveId, Stage stage, boolean toCache) throws NotFoundException;
}
