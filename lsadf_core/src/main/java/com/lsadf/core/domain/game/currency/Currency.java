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
package com.lsadf.core.domain.game.currency;

import static com.lsadf.core.infra.web.JsonAttributes.Currency.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.domain.Model;
import com.lsadf.core.infra.web.controllers.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Currency", description = "Currency object")
@Data
@Builder
@JsonPropertyOrder({GOLD, DIAMOND, EMERALD, AMETHYST})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Currency implements Model {

  @Serial private static final long serialVersionUID = 3614717300669193588L;

  private Long gold;

  @JsonView(JsonViews.External.class)
  @Schema(description = "The amount of diamond", example = "100")
  private Long diamond;

  @Schema(description = "The amount of emerald", example = "100")
  private Long emerald;

  @Schema(description = "The amount of amethyst", example = "100")
  private Long amethyst;
}
