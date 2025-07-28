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

package com.lsadf.core.infra.web.request.game.save.creation;

import com.lsadf.core.infra.web.request.Request;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;

/**
 * Represents a game save creation request that encapsulates multiple components of a game save,
 * including metadata, user characteristics, in-game currencies, and stage progress.
 *
 * <p>This interface extends the {@link Request} interface to ensure compatibility with request
 * handling mechanisms. Implementations of this interface provide the necessary details for creating
 * or managing game save records, with methods to retrieve specific parts of the game save data.
 *
 * <p>Methods: - {@code getMetadataRequest}: Obtains the metadata details such as user information
 * and identifiers. - {@code getCharacteristicsRequest}: Retrieves the user's in-game
 * characteristics, including attributes like attack, health, and resistance. - {@code
 * getCurrencyRequest}: Provides information about the user's in-game currencies, such as gold and
 * diamonds. - {@code getStageRequest}: Supplies data about the user's progress in terms of current
 * and maximum stage levels.
 */
public interface GameSaveCreationRequest extends Request {
  /**
   * Retrieves the metadata request associated with the game save. This provides details about the
   * game save's metadata including identifiers and user information such as username and nickname.
   *
   * @return a {@code GameMetadataRequest} object containing metadata details related to the game
   *     save.
   */
  GameMetadataRequest getMetadataRequest();

  /**
   * Retrieves the characteristics request associated with the game save. This request contains
   * detailed information about the user's in-game characteristics such as attack level, critical
   * chance, critical damage, health, and resistance.
   *
   * @return a {@code CharacteristicsRequest} object containing the user's in-game attribute levels.
   */
  CharacteristicsRequest getCharacteristicsRequest();

  /**
   * Retrieves the currency request associated with the game save. This request contains detailed
   * information about the user's in-game currencies, such as gold, diamond, emerald, and amethyst.
   *
   * @return a {@code CurrencyRequest} object containing the user's in-game currency details.
   */
  CurrencyRequest getCurrencyRequest();

  /**
   * Retrieves the stage request associated with the game save. The stage request contains detailed
   * information about the user's stage progress, including the current stage and the maximum stage
   * achieved within the game.
   *
   * @return a {@code StageRequest} object representing the user's current and maximum stage
   *     progress details.
   */
  StageRequest getStageRequest();
}
