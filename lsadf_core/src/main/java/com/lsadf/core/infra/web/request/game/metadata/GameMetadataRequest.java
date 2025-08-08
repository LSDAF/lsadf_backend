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

package com.lsadf.core.infra.web.request.game.metadata;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.domain.user.validation.Nickname;
import com.lsadf.core.infra.web.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.UUID;
import lombok.Builder;

@Builder
public record GameMetadataRequest(
    @Schema(description = "Game Save ID", example = "123e4567-e89b-12d3-a456-426655440000")
        @JsonProperty(value = ID)
        UUID id,
    @Schema(description = "User Email", example = "toto@toto.com")
        @JsonProperty(value = USER_EMAIL)
        @Email
        @NotNull
        String userEmail,
    @Schema(description = "The nickname of the game save", example = "Hero2340")
        @JsonProperty(value = NICKNAME)
        @Nickname
        String nickname)
    implements Request {
  @Serial private static final long serialVersionUID = 7340207014576099854L;
}
