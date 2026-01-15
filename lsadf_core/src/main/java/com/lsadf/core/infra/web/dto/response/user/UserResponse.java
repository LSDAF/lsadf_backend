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

package com.lsadf.core.infra.web.dto.response.user;

import com.lsadf.core.infra.serializer.DateToLongSerializer;
import com.lsadf.core.infra.serializer.LongToDateSerializer;
import com.lsadf.core.infra.web.dto.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Builder
public record UserResponse(
    @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb") String id,
    @Schema(description = "First name", example = "Toto") String firstName,
    @Schema(description = "Last name", example = "TUTU") String lastName,
    @Schema(description = "User username", example = "toto@toto.com") String username,
    @Schema(description = "User enabled", example = "true") Boolean enabled,
    @Schema(description = "Email verified", example = "true") Boolean emailVerified,
    @Schema(description = "User roles", example = "[\"USER\"]") List<String> userRoles,
    @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
        @JsonSerialize(using = DateToLongSerializer.class)
        @JsonDeserialize(using = LongToDateSerializer.class)
        Date createdTimestamp)
    implements Response {
  @Serial private static final long serialVersionUID = 214268445410957868L;
}
