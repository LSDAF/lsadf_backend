/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.application.game.game_save;

import com.lsadf.core.common.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.core.common.exceptions.AlreadyTakenNicknameException;
import com.lsadf.core.common.exceptions.http.ForbiddenException;
import com.lsadf.core.common.exceptions.http.NotFoundException;
import com.lsadf.core.common.exceptions.http.UnauthorizedException;
import com.lsadf.core.infra.persistence.game.GameSaveEntity;
import com.lsadf.core.infra.web.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.requests.game.game_save.GameSaveUpdateNicknameRequest;
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
  GameSaveEntity createGameSave(String userEmail) throws NotFoundException;

  /**
   * Creates a new game save
   *
   * @param creationRequest the admin creation request
   * @return the created game save
   * @throws NotFoundException the not found exception
   */
  GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest)
      throws NotFoundException, AlreadyExistingGameSaveException;

  /**
   * Gets a game save
   *
   * @param saveId the save id
   * @return the game save
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  GameSaveEntity getGameSave(String saveId) throws NotFoundException;

  /**
   * Updates a game save
   *
   * @param saveId the save id
   * @param gameSaveUpdateNicknameRequest the nickname update request
   * @return the updated game save
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  GameSaveEntity updateNickname(
      String saveId, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest)
      throws ForbiddenException,
          NotFoundException,
          UnauthorizedException,
          AlreadyTakenNicknameException;

  /**
   * Updates a game save from admin side
   *
   * @param saveId the save id
   * @param updateRequest the admin update request
   * @return the updated game save
   * @throws ForbiddenException the forbidden exception
   * @throws NotFoundException the not found exception
   */
  GameSaveEntity updateNickname(String saveId, AdminGameSaveUpdateRequest updateRequest)
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
  Stream<GameSaveEntity> getGameSaves();

  /**
   * Gets all game saves of a user
   *
   * @param userEmail the user email
   * @return the stream of game saves
   */
  Stream<GameSaveEntity> getGameSavesByUsername(String userEmail);

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
