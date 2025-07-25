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

import static com.lsadf.core.infra.web.JsonAttributes.ID;
import static com.lsadf.core.infra.web.JsonAttributes.USER_EMAIL;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.domain.user.validation.Nickname;
import com.lsadf.core.infra.web.request.Request;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.UUID;
import lombok.Builder;

/**
 * Represents a request for creating or updating an admin-managed game save. This record
 * encapsulates the details necessary to define the state of a game save for a particular user,
 * including user information, game characteristics, currency, and stage progress.
 *
 * <p>It implements the {@link Request} interface to ensure compatibility with the request handling
 * mechanism.
 *
 * <p>Fields: - `id`: The UUID of the game save. It is optional and may be null. - `userEmail`: The
 * email address of the user associated with the game save. It is required. - `getNickname`: The
 * getNickname of the user within the game save. It is optional but validated to meet specific rules
 * for nicknames. - `characteristics`: An object containing the user's in-game characteristics such
 * as attack, critical chance, critical damage, health, and resistance. It is required and must
 * follow specific validation rules. - `currency`: An object specifying the user's in-game
 * currencies, such as gold, diamond, emerald, and amethyst. It is required and must meet the
 * validation rules. - `stage`: An object representing the user's progress in terms of current and
 * maximum stages. It is required and validated against consistency rules for stage progress.
 */
@Builder
public record AdminGameSaveCreationRequest(
    @JsonProperty(value = ID)
        @Schema(
            description = "Id of the game save",
            example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
        UUID id,
    @JsonProperty(value = USER_EMAIL)
        @Schema(description = "Email of the user", example = "test@test.com")
        @Email
        @NotNull
        String userEmail,
    @Nickname @Schema(description = "Nickname of the user in the game save", example = "Toto")
        String nickname,
    @Valid @NotNull CharacteristicsRequest characteristics,
    @Valid @NotNull CurrencyRequest currency,
    @Valid @NotNull StageRequest stage)
    implements GameSaveCreationRequest {

  @Serial private static final long serialVersionUID = 2925109471468959138L;

  /** {@inheritDoc} */
  @Override
  public UUID getId() {
    return id;
  }

  /** {@inheritDoc} */
  @Override
  public String getUserEmail() {
    return userEmail;
  }

  /** {@inheritDoc} */
  @Override
  public String getNickname() {
    return nickname;
  }

  /** {@inheritDoc} */
  @Override
  public CharacteristicsRequest getCharacteristicsRequest() {
    return characteristics;
  }

  /** {@inheritDoc} */
  @Override
  public CurrencyRequest getCurrencyRequest() {
    return currency;
  }

  /** {@inheritDoc} */
  @Override
  public StageRequest getStageRequest() {
    return stage;
  }
}
