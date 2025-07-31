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

import static com.lsadf.core.infra.web.JsonAttributes.USER_EMAIL;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;

/**
 * Represents a simple game save creation request that includes the user's email address and
 * implements the {@link GameSaveCreationRequest} interface.
 *
 * <p>This record is used for creating game save requests where only user metadata is necessary such
 * as the email address associated with the game save. Other components of the game save
 * (characteristics, currency, and stage) are not included and return null in their respective
 * method implementations.
 *
 * <p>Fields: - userEmail: The email address of the user for whom the game save is being created.
 * This field is required and validated for non-null value and proper email format.
 *
 * <p>Methods: - {@code getMetadataRequest}: Constructs and returns a {@link GameMetadataRequest}
 * object containing the metadata details for the game save, specifically the user's email address.
 * - {@code getCharacteristicsRequest}: Returns null as no characteristics data is provided. -
 * {@code getCurrencyRequest}: Returns null as no currency data is provided. - {@code
 * getStageRequest}: Returns null as no stage data is provided.
 */
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
  public GameMetadataRequest getMetadataRequest() {
    return GameMetadataRequest.builder().userEmail(userEmail).build();
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
