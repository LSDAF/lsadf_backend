/*
 * Copyright © 2024-2026 LSDAF
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

import com.lsadf.core.application.game.save.stage.command.InitializeDefaultStageCommand;
import com.lsadf.core.application.game.save.stage.command.InitializeStageCommand;
import com.lsadf.core.application.game.save.stage.command.PersistStageCommand;
import com.lsadf.core.application.game.save.stage.command.UpdateCacheStageCommand;
import com.lsadf.core.domain.game.save.stage.Stage;

/**
 * Service interface for managing and processing stage-related commands. This typically includes
 * initializing, persisting, and updating stages associated with a game save.
 */
public interface StageCommandService {
  /**
   * Initializes a default {@code Stage} instance based on the provided command.
   *
   * @param command the command containing the unique identifier for the game save
   * @return a default {@code Stage} instance with initialized parameters
   */
  Stage initializeDefaultStage(InitializeDefaultStageCommand command);

  /**
   * Initializes a new stage based on the provided command details.
   *
   * @param command the command containing the game save ID and stage parameters such as current
   *     stage and maximum stage
   * @return the newly created {@code Stage} instance
   */
  Stage initializeStage(InitializeStageCommand command);

  /**
   * Persists the stage information based on the provided command.
   *
   * @param command the command containing the details required for persisting the stage, including
   *     the game save ID and stage parameters such as current and max stage
   */
  void persistStage(PersistStageCommand command);

  /**
   * Updates the cached stage information based on the given command.
   *
   * @param command the command containing the game save ID and updated stage details
   */
  void updateCacheStage(UpdateCacheStageCommand command);
}
