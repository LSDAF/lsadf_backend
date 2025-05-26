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
package com.lsadf.core.domain.game.characteristics;

import static com.lsadf.core.constants.JsonAttributes.Characteristics.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.common.models.Model;
import com.lsadf.core.constants.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Characteristics", description = "Characteristics object")
@Data
@Builder
@JsonPropertyOrder({ATTACK, CRIT_CHANCE, CRIT_DAMAGE, HEALTH, RESISTANCE})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Characteristics implements Model {
  @Serial private static final long serialVersionUID = 5623465292659597625L;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = ATTACK)
  @Schema(description = "Attack level", example = "100")
  private Long attack;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = CRIT_CHANCE)
  @Schema(description = "Crit chance level", example = "100")
  private Long critChance;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = CRIT_DAMAGE)
  @Schema(description = "Crit damage level", example = "100")
  private Long critDamage;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = HEALTH)
  @Schema(description = "Health level", example = "100")
  private Long health;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = RESISTANCE)
  @Schema(description = "Resistance level", example = "100")
  private Long resistance;
}
