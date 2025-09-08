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
package com.lsadf.core.infra.web.dto.request.game.stage;

import static com.lsadf.core.infra.web.JsonAttributes.CURRENT_STAGE;
import static com.lsadf.core.infra.web.JsonAttributes.MAX_STAGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.domain.game.save.stage.validation.StageConsistency;
import com.lsadf.core.infra.web.dto.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serial;

@StageConsistency(currentStageField = "currentStage", maxStageField = "maxStage")
public record StageRequest(
    @Schema(description = "The Current game stage", example = "26")
        @JsonProperty(value = CURRENT_STAGE)
        @Positive
        @NotNull
        Long currentStage,
    @Schema(description = "The Maximum game stage", example = "260")
        @JsonProperty(value = MAX_STAGE)
        @Positive
        @NotNull
        Long maxStage)
    implements Request {

  @Serial private static final long serialVersionUID = -2154269413949156805L;
}
