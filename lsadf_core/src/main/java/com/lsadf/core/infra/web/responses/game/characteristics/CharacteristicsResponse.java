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

package com.lsadf.core.infra.web.responses.game.characteristics;

import static com.lsadf.core.infra.web.JsonAttributes.Characteristics.*;
import static com.lsadf.core.infra.web.JsonAttributes.Characteristics.HEALTH;
import static com.lsadf.core.infra.web.JsonAttributes.Characteristics.RESISTANCE;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.responses.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Builder;

/**
 * Represents a response object containing the characteristics of an entity.
 *
 * <p>This object includes five main attributes: attack, critical chance, critical damage, health,
 * and resistance. Each attribute is represented as a Long data type and may be null if not included
 * in the response.
 *
 * <p>The class is serializable and adheres to the {@link Response} interface. It uses various
 * annotations to define JSON serialization, ordering, inclusion policies, and view scopes while
 * adhering to the API schema for external use.
 *
 * <p>The {@code CharacteristicsResponse} is primarily utilized for API responses and is constructed
 * using a builder pattern provided by Lombok.
 */
@JsonPropertyOrder({ATTACK, CRIT_CHANCE, CRIT_DAMAGE, HEALTH, RESISTANCE})
@Builder
@Schema(name = "Characteristics", description = "Characteristics object")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public record CharacteristicsResponse(
    @JsonProperty(value = ATTACK) @Schema(description = "Attack level", example = "100")
        Long attack,
    @JsonProperty(value = CRIT_CHANCE) @Schema(description = "Crit chance level", example = "100")
        Long critChance,
    @JsonProperty(value = CRIT_DAMAGE) @Schema(description = "Crit damage level", example = "100")
        Long critDamage,
    @JsonProperty(value = HEALTH) @Schema(description = "Health level", example = "100")
        Long health,
    @JsonProperty(value = RESISTANCE) @Schema(description = "Resistance level", example = "100")
        Long resistance)
    implements Response {
  @Serial private static final long serialVersionUID = 9133503960157723249L;
}
