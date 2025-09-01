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

package com.lsadf.core.application.game.save.metadata;

import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/**
 * Service interface for managing game metadata operations. This interface provides methods for
 * checking the existence of a game save, retrieving game metadata, and updating specific fields in
 * game metadata.
 */
public interface GameMetadataService {

  /**
   * Deletes the game metadata associated with the specified unique identifier.
   *
   * @param gameSaveId the unique identifier of the game save whose metadata is to be deleted
   */
  void deleteById(UUID gameSaveId);

  /**
   * Checks whether a game save exists with the specified unique identifier.
   *
   * @param gameSaveId the unique identifier of the game save to check for existence
   * @return true if a game save with the specified identifier exists, false otherwise
   */
  boolean existsById(UUID gameSaveId);

  /**
   * Checks whether a game metadata entry exists with the specified nickname.
   *
   * @param nickname the nickname to check for existence
   * @return true if a game metadata entry with the specified nickname exists, false otherwise
   */
  boolean existsByNickname(String nickname);

  /**
   * Retrieves the metadata associated with a specific game save ID.
   *
   * @param gameSaveId the unique identifier of the game save whose metadata is to be retrieved
   * @return the {@link GameMetadata} object containing metadata information for the specified game
   *     save
   */
  GameMetadata getGameMetadata(UUID gameSaveId);

  /**
   * Retrieves the total count of game metadata records.
   *
   * @return the total number of game metadata records as a {@code Long} value
   */
  Long count();

  /**
   * Updates the nickname of the game metadata associated with the specified game save ID.
   *
   * @param gameSaveId the unique identifier of the game save whose metadata is to be updated
   * @param nickname the new nickname to update in the game metadata
   * @return the updated {@link GameMetadata} object containing the new nickname
   */
  GameMetadata updateNickname(UUID gameSaveId, String nickname);

  /**
   * Creates a new instance of {@link GameMetadata} using the provided gameSaveId, username, and
   * nickname. This method associates the metadata with a specified game and user data, setting up
   * necessary fields.
   *
   * @param gameSaveId the unique identifier of the game save to be associated with the metadata
   * @param username the username to be associated with the game metadata
   * @param nickname the nickname to be assigned to the game metadata
   * @return the newly created {@link GameMetadata} object containing game and user metadata
   */
  GameMetadata createNewGameMetadata(
      @Nullable UUID gameSaveId, String username, @Nullable String nickname);
}
