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
import java.util.UUID;

/** Interface for querying stage-related data associated with game saves. */
public interface StageQueryService {
  /**
   * Retrieves the stage associated with the given game save ID.
   *
   * @param gameSaveId the unique identifier for the game save
   * @return the stage associated with the specified game save ID, or null if no stage is found
   */
  Stage retrieveStage(UUID gameSaveId);
}
