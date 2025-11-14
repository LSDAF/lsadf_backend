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

package com.lsadf.core.infra.web.dto.response.game.save;

import com.lsadf.core.infra.web.dto.response.Response;
import com.lsadf.core.infra.web.dto.response.game.save.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.dto.response.game.save.currency.CurrencyResponse;
import com.lsadf.core.infra.web.dto.response.game.save.metadata.GameMetadataResponse;
import com.lsadf.core.infra.web.dto.response.game.save.stage.StageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Builder;

@Schema(name = "GameSaveResponse", description = "Game Save Object")
@Builder
public record GameSaveResponse(
    GameMetadataResponse metadata,
    CharacteristicsResponse characteristics,
    CurrencyResponse currency,
    StageResponse stage)
    implements Response {

  @Serial private static final long serialVersionUID = -2186008999020414794L;
}
