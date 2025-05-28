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
package com.lsadf.core.domain.game;

import static com.lsadf.core.infra.web.JsonAttributes.GameSave.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.web.JsonAttributes;
import com.lsadf.core.infra.web.controllers.JsonViews;
import com.lsadf.core.shared.model.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Game Save DTO */
@Data
@Schema(name = "GameSave", description = "Game Save Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSave implements Model {

  @Serial private static final long serialVersionUID = -2686370647354845265L;

  // Admin fields

  // Internal fields

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = JsonAttributes.ID)
  @Schema(description = "Game Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
  private String id;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = USER_EMAIL)
  private String userEmail;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = JsonAttributes.CREATED_AT)
  @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
  private Date createdAt;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = JsonAttributes.UPDATED_AT)
  @Schema(description = "Update date", example = "2022-01-01 00:00:00.000")
  private Date updatedAt;

  // External fields

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = NICKNAME)
  private String nickname;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = CHARACTERISTICS)
  private Characteristics characteristics;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = CURRENCY)
  private Currency currency;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = STAGE)
  private Stage stage;
}
