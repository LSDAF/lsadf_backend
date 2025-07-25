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

package com.lsadf.core.infra.web.request.game.game_save.creation;

import com.lsadf.core.infra.web.request.Request;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import java.util.UUID;

/**
 * Represents a request for creating a new game save. This interface defines the contract for
 * handling game save creation requests, including obtaining necessary details such as the user's
 * identifier, email, nickname, in-game characteristics, currency, and stage progress.
 *
 * <p>It extends the {@link Request} interface, ensuring compatibility with request handling
 * mechanisms.
 *
 * <p>Responsibilities of this interface include: - Obtaining the unique identifier of the game save
 * request. - Retrieving the user's email address associated with the game save. - Accessing the
 * nickname of the user relevant to the game save request. - Providing the user's in-game
 * characteristics like attack power, critical chance, critical damage, health, and resistance. -
 * Fetching the currency details related to the user in the game save, including various types of
 * in-game currencies. - Returning the user's progress details in terms of stage, such as current
 * and maximum stage completion levels.
 */
public interface GameSaveCreationRequest extends Request {
  /**
   * Retrieves the unique identifier of the game save request.
   *
   * @return a {@code UUID} representing the unique identifier of the game save.
   */
  UUID getId();

  /**
   * Retrieves the email address of the user associated with a game save.
   *
   * @return the email address of the user as a {@code String}.
   */
  String getUserEmail();

  /**
   * Retrieves the nickname of the user associated with a game save.
   *
   * @return the nickname of the user as a {@code String}.
   */
  String getNickname();

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
