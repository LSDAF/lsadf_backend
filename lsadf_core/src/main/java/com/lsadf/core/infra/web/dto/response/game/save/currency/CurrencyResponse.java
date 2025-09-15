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

package com.lsadf.core.infra.web.dto.response.game.save.currency;

import static com.lsadf.core.infra.web.JsonAttributes.AMETHYST;
import static com.lsadf.core.infra.web.JsonAttributes.DIAMOND;
import static com.lsadf.core.infra.web.JsonAttributes.EMERALD;
import static com.lsadf.core.infra.web.JsonAttributes.GOLD;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.infra.web.dto.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "Currency", description = "Currency object")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CurrencyResponse(
    @Schema(description = "The amount of gold", example = "100") @JsonProperty(value = GOLD)
        Long gold,
    @Schema(description = "The amount of diamond", example = "100") @JsonProperty(value = DIAMOND)
        Long diamond,
    @Schema(description = "The amount of emerald", example = "100") @JsonProperty(value = EMERALD)
        Long emerald,
    @Schema(description = "The amount of amethyst", example = "100") @JsonProperty(value = AMETHYST)
        Long amethyst)
    implements Response {}
