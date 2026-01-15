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
package com.lsadf.core.application.game.save;

import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.exception.AlreadyExistingGameSaveException;
import com.lsadf.core.exception.AlreadyTakenNicknameException;
import com.lsadf.core.exception.http.ForbiddenException;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.exception.http.UnauthorizedException;
import com.lsadf.core.infra.web.dto.request.game.save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveUpdateRequest;
import java.util.List;
import java.util.UUID;

/** Service for managing game saves */
public interface GameSaveService {

  /**
   * Creates a new game save based on the provided creation request.
   *
   * @param creationRequest the request containing the details required to create a game save,
   *     including the user's email, nickname, in-game characteristics, currency, and stage progress
   * @return the created game save instance
   * @throws NotFoundException if a necessary resource or user reference for creating the game save
   *     cannot be found
   * @throws AlreadyExistingGameSaveException if a game save with the specified parameters already
   *     exists
   */
  GameSave createGameSave(GameSaveCreationRequest creationRequest)
      throws NotFoundException, AlreadyExistingGameSaveException;

  /**
   * Gets a game save
   *
   * @param saveId the save id
   * @return the game save
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  GameSave getGameSave(UUID saveId) throws NotFoundException;

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
  GameSave updateGameSave(UUID saveId, GameSaveUpdateRequest gameSaveUpdateRequest)
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
  boolean existsById(UUID gameSaveId);

  /**
   * Deletes a game save
   *
   * @param saveId the save id
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  void deleteGameSave(UUID saveId) throws NotFoundException;

  /**
   * Gets all game saves
   *
   * @return the stream of game saves
   */
  List<GameSave> getGameSaves();

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
  List<GameSave> getGameSavesByUsername(String userEmail);

  /**
   * Checks if the user owns the game save
   *
   * @param saveId the game save id
   * @param userEmail the user email
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  void checkGameSaveOwnership(UUID saveId, String userEmail)
      throws ForbiddenException, NotFoundException;
}
