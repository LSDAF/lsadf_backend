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
package com.lsadf.core.requests.stage;

import static com.lsadf.core.constants.JsonAttributes.Stage.CURRENT_STAGE;
import static com.lsadf.core.constants.JsonAttributes.Stage.MAX_STAGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.annotations.StageConsistency;
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
@StageConsistency(currentStageField = "currentStage", maxStageField = "maxStage")
@JsonPropertyOrder({CURRENT_STAGE, MAX_STAGE})
public class StageRequest implements Request {

  @Serial private static final long serialVersionUID = -2154269413949156805L;

  @Schema(description = "The Current game stage", example = "26")
  @JsonProperty(value = CURRENT_STAGE)
  @Positive
  private Long currentStage;

  @Schema(description = "The Maximum game stage", example = "260")
  @JsonProperty(value = MAX_STAGE)
  @Positive
  private Long maxStage;
}
