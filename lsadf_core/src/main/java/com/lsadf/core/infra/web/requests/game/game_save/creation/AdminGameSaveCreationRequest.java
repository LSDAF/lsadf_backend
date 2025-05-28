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
package com.lsadf.core.infra.web.requests.game.game_save.creation;

import static com.lsadf.core.infra.web.JsonAttributes.GameSave.*;
import static com.lsadf.core.infra.web.JsonAttributes.ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.domain.user.validation.Nickname;
import com.lsadf.core.infra.web.requests.Request;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.requests.game.stage.StageRequest;
import com.lsadf.core.shared.validation.Uuid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Request for creating a new game save */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminGameSaveCreationRequest implements Request {

  @Serial private static final long serialVersionUID = 2925109471468959138L;

  @Uuid(nullable = true)
  @JsonProperty(value = ID)
  @Schema(description = "Id of the game save", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
  private String id;

  @Email
  @NotNull
  @JsonProperty(value = USER_EMAIL)
  @Schema(description = "Email of the user", example = "test@test.com")
  private String userEmail;

  @Nickname
  @Schema(description = "Nickname of the user in the game save", example = "Toto")
  private String nickname;

  @Valid @NotNull private CharacteristicsRequest characteristics;

  @Valid @NotNull private CurrencyRequest currency;

  @Valid @NotNull private StageRequest stage;
}
