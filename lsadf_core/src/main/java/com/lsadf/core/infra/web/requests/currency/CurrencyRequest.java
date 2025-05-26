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
package com.lsadf.core.infra.web.requests.currency;

import static com.lsadf.core.constants.JsonAttributes.Currency.*;
import static com.lsadf.core.constants.JsonAttributes.Currency.AMETHYST;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.infra.web.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({GOLD, DIAMOND, EMERALD, AMETHYST})
public class CurrencyRequest implements Request {

  @Serial private static final long serialVersionUID = 1865696066274976174L;

  @Schema(description = "The amount of gold", example = "100")
  @JsonProperty(value = GOLD)
  @PositiveOrZero
  private Long gold;

  @Schema(description = "The amount of diamond", example = "100")
  @JsonProperty(value = DIAMOND)
  @PositiveOrZero
  private Long diamond;

  @Schema(description = "The amount of emerald", example = "100")
  @JsonProperty(value = EMERALD)
  @PositiveOrZero
  private Long emerald;

  @Schema(description = "The amount of amethyst", example = "100")
  @JsonProperty(value = AMETHYST)
  @PositiveOrZero
  private Long amethyst;
}
