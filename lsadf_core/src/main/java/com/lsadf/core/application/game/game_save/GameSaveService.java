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
package com.lsadf.core.application.game.game_save;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.exception.AlreadyExistingGameSaveException;
import com.lsadf.core.infra.exception.AlreadyTakenNicknameException;
import com.lsadf.core.infra.exception.http.ForbiddenException;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.exception.http.UnauthorizedException;
import com.lsadf.core.infra.web.requests.game.game_save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.requests.game.game_save.update.GameSaveUpdateRequest;
import java.util.stream.Stream;

/** Service for managing game saves */
public interface GameSaveService {
  /**
   * Creates a new game save for the user
   *
   * @param userEmail the user email
   * @return the created game save
   * @throws NotFoundException the not found exception
   */
  GameSave createGameSave(String userEmail) throws NotFoundException;

  /**
   * Creates a new game save
   *
   * @param creationRequest the admin creation request
   * @return the created game save
   * @throws NotFoundException the not found exception
   */
  GameSave createGameSave(AdminGameSaveCreationRequest creationRequest)
      throws NotFoundException, AlreadyExistingGameSaveException;

  /**
   * Gets a game save
   *
   * @param saveId the save id
   * @return the game save
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  GameSave getGameSave(String saveId) throws NotFoundException;

  /**
   * Updates an existing game save with the provided update request data.
   *
   * @param saveId the unique identifier of the game save to update
   * @param gameSaveUpdateRequest the request containing the updated game save information. If
   *     objects are null, they do not need to be edited
   * @return the updated game save object
   * @throws ForbiddenException if the user is not allowed to update the game save
   * @throws NotFoundException if the game save with the specified identifier does not exist
   * @throws UnauthorizedException if the user is not authenticated or authorized
   * @throws AlreadyTakenNicknameException if the updated nickname is already taken by another user
   */
  GameSave updateGameSave(String saveId, GameSaveUpdateRequest gameSaveUpdateRequest)
      throws ForbiddenException,
          NotFoundException,
          UnauthorizedException,
          AlreadyTakenNicknameException;

  /**
   * Checks if a game save exists
   *
   * @param gameSaveId the game save id
   * @return true if the game save exists, false otherwise
   */
  boolean existsById(String gameSaveId);

  /**
   * Deletes a game save
   *
   * @param saveId the save id
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  void deleteGameSave(String saveId) throws NotFoundException;

  /**
   * Gets all game saves
   *
   * @return the stream of game saves
   */
  Stream<GameSave> getGameSaves();

  /**
   * Counts the total number of game saves available in the system.
   *
   * @return the total count of game saves
   */
  Long countGameSaves();

  /**
   * Gets all game saves of a user
   *
   * @param userEmail the user email
   * @return the stream of game saves
   */
  Stream<GameSave> getGameSavesByUsername(String userEmail);

  /**
   * Checks if the user owns the game save
   *
   * @param saveId the game save id
   * @param userEmail the user email
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  void checkGameSaveOwnership(String saveId, String userEmail)
      throws ForbiddenException, NotFoundException;
}
