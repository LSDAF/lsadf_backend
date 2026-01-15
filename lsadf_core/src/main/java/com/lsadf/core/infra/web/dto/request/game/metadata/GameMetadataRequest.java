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

package com.lsadf.core.infra.web.dto.request.game.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lsadf.core.domain.user.validation.Nickname;
import com.lsadf.core.infra.web.dto.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.UUID;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record GameMetadataRequest(
    @Nullable
        @Schema(description = "Game Save ID", example = "123e4567-e89b-12d3-a456-426655440000")
        UUID id,
    @Schema(description = "User Email", example = "toto@toto.com") @Email @NotNull String userEmail,
    @Nullable @Schema(description = "The nickname of the game save", example = "Hero2340") @Nickname
        String nickname)
    implements Request {
  @Serial private static final long serialVersionUID = 7340207014576099854L;
}
