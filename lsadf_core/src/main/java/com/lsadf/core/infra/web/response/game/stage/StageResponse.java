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

package com.lsadf.core.infra.web.response.game.stage;

import static com.lsadf.core.infra.web.JsonAttributes.Stage.CURRENT_STAGE;
import static com.lsadf.core.infra.web.JsonAttributes.Stage.MAX_STAGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Schema(name = "Stage", description = "Stage object containing the player's game progress")
@JsonView(JsonViews.External.class)
public record StageResponse(
    @Schema(description = "The Current game stage", example = "26")
        @JsonProperty(value = CURRENT_STAGE)
        Long currentStage,
    @Schema(description = "The Maximum game stage", example = "26") @JsonProperty(value = MAX_STAGE)
        Long maxStage)
    implements Response {}
