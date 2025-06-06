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

package com.lsadf.core.infra.web.request.game.game_save.update;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.web.request.Request;

/**
 * Represents a request for updating a game save. This interface provides methods to retrieve
 * details about the game's save state, including characteristics, currency, stage progress, and the
 * player's nickname. Implementing classes should provide the functionality to manage and update
 * specific components of the game save.
 */
public interface GameSaveUpdateRequest extends Request {

  /**
   * Retrieves the characteristics associated with the game save update request.
   *
   * @return the characteristics object containing details such as attack, crit chance, crit damage,
   *     health, and resistance
   */
  Characteristics getCharacteristics();

  /**
   * Retrieves the currency information associated with the game save update request.
   *
   * @return the currency object containing details such as gold, diamond, emerald, and amethyst
   */
  Currency getCurrency();

  /**
   * Retrieves the stage object associated with the game save update request.
   *
   * @return the stage representing the player's game progress
   */
  Stage getStage();

  /**
   * Retrieves the nickname associated with the game save update request.
   *
   * @return the nickname of the user as a string
   */
  String getNickname();
}
