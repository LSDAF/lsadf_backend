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

package com.lsadf.workflow.game.service;

import com.lsadf.workflow.game.GameSessionWorkflow;

/**
 * The GameSessionWorkflowService interface provides a method to manage game session workflows, such
 * as retrieving or creating instances of game session workflows, identified by a session ID.
 */
public interface GameSessionWorkflowService {
  /**
   * Retrieves an existing {@code GameSessionWorkflow} associated with the given session ID, or
   * creates a new one if it does not already exist.
   *
   * @param userId the unique identifier for the game session
   * @param gameSaveId
   * @return an instance of {@code GameSessionWorkflow} associated with the specified session ID
   */
  GameSessionWorkflow getOrCreateGameSessionWorkflow(String userId, String gameSaveId);
}
