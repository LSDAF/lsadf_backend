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

import static com.lsadf.core.infra.web.JsonAttributes.USER_EMAIL;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.UUID;

public record SimpleGameSaveCreationRequest(
    @JsonProperty(value = USER_EMAIL)
        @Schema(description = "Email of the user", example = "test@test.com")
        @Email
        @NotNull
        String userEmail)
    implements GameSaveCreationRequest {
  @Serial private static final long serialVersionUID = 130532544991093362L;

  /** {@inheritDoc} */
  @Override
  public UUID getId() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String getUserEmail() {
    return userEmail;
  }

  /** {@inheritDoc} */
  @Override
  public String getNickname() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public CharacteristicsRequest getCharacteristicsRequest() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public CurrencyRequest getCurrencyRequest() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public StageRequest getStageRequest() {
    return null;
  }
}
