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
package com.lsadf.core.domain.game.stage;

import static com.lsadf.core.constants.JsonAttributes.Stage.CURRENT_STAGE;
import static com.lsadf.core.constants.JsonAttributes.Stage.MAX_STAGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.core.models.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(name = "Stage", description = "Stage object containing the player's game progress")
@JsonPropertyOrder({CURRENT_STAGE, MAX_STAGE})
@JsonView(JsonViews.External.class)
public class Stage implements Model {

  @Serial private static final long serialVersionUID = -7126306428235414817L;

  @Schema(description = "The Current game stage", example = "26")
  @JsonProperty(value = CURRENT_STAGE)
  private Long currentStage;

  @Schema(description = "The Maximum game stage", example = "26")
  @JsonProperty(value = MAX_STAGE)
  private Long maxStage;
}
