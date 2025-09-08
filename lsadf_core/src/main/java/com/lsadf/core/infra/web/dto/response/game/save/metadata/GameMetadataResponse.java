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

package com.lsadf.core.infra.web.dto.response.game.save.metadata;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.infra.web.dto.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Date;
import java.util.UUID;
import lombok.Builder;

@Schema(name = "GameMetadataResponse", description = "Game Metadata Object")
@Builder
public record GameMetadataResponse(
    @Schema(description = "Game Save ID", example = "6459ce33-5531-4f3e-bb9b-53167893d5c2")
        @JsonProperty(value = ID)
        UUID id,
    @JsonProperty(value = CREATED_AT) Date createdAt,
    @JsonProperty(value = UPDATED_AT) Date updatedAt,
    @Schema(description = "User email", example = "6459ce33-5531-4f3e-bb9b-53167893d5c2")
        @JsonProperty(value = USER_EMAIL)
        String userEmail,
    @Schema(description = "Game nickname", example = "MyHero420") @JsonProperty(value = NICKNAME)
        String nickname)
    implements Response {
  @Serial private static final long serialVersionUID = -1601333638397368281L;
}
