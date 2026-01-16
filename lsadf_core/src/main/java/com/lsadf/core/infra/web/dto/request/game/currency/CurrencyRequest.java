/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.web.dto.request.game.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lsadf.core.infra.web.dto.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyRequest(
    @Nullable @Schema(description = "The amount of gold", example = "100") @PositiveOrZero
        Long gold,
    @Nullable @Schema(description = "The amount of diamond", example = "100") @PositiveOrZero
        Long diamond,
    @Nullable @Schema(description = "The amount of emerald", example = "100") @PositiveOrZero
        Long emerald,
    @Nullable @Schema(description = "The amount of amethyst", example = "100") @PositiveOrZero
        Long amethyst)
    implements Request {

  @Serial private static final long serialVersionUID = 1865696066274976174L;
}
