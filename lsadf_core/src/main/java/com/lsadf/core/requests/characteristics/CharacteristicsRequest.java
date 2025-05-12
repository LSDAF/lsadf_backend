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
package com.lsadf.core.requests.characteristics;

import static com.lsadf.core.constants.JsonAttributes.Characteristics.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ATTACK, CRIT_CHANCE, CRIT_DAMAGE, HEALTH, RESISTANCE})
public class CharacteristicsRequest implements Request {
  @Serial private static final long serialVersionUID = 1865696066274976174L;

  @Schema(description = "The attack level of the user", example = "100")
  @JsonProperty(value = ATTACK)
  @Positive
  private Long attack;

  @Schema(description = "The critical chance level of the user", example = "100")
  @JsonProperty(value = CRIT_CHANCE)
  @Positive
  private Long critChance;

  @Schema(description = "The critical damage level of the user", example = "100")
  @JsonProperty(value = CRIT_DAMAGE)
  @Positive
  private Long critDamage;

  @Schema(description = "The health level of the user", example = "100")
  @JsonProperty(value = HEALTH)
  @Positive
  private Long health;

  @Schema(description = "The resistance level of the user", example = "100")
  @JsonProperty(value = RESISTANCE)
  @Positive
  private Long resistance;
}
