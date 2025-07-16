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

package com.lsadf.core.infra.web.response.user;

import static com.lsadf.core.infra.web.JsonAttributes.User.*;
import static com.lsadf.core.infra.web.JsonAttributes.User.CREATED_TIMESTAMP;
import static com.lsadf.core.infra.web.JsonAttributes.User.EMAIL_VERIFIED;
import static com.lsadf.core.infra.web.JsonAttributes.User.ENABLED;
import static com.lsadf.core.infra.web.JsonAttributes.User.USER_ROLES;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lsadf.core.infra.serializer.DateToLongSerializer;
import com.lsadf.core.infra.serializer.LongToDateSerializer;
import com.lsadf.core.infra.web.JsonAttributes;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Date;
import java.util.List;
import lombok.Builder;

@Builder
public record UserResponse(
    @JsonView(JsonViews.Admin.class)
        @JsonProperty(value = JsonAttributes.ID)
        @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
        String id,
    @JsonView(JsonViews.Internal.class)
        @JsonProperty(value = FIRST_NAME)
        @Schema(description = "First name", example = "Toto")
        String firstName,
    @JsonView(JsonViews.Internal.class)
        @JsonProperty(value = LAST_NAME)
        @Schema(description = "Last name", example = "TUTU")
        String lastName,
    @JsonView(JsonViews.Internal.class)
        @JsonProperty(value = USERNAME)
        @Schema(description = "User username", example = "toto@toto.com")
        String username,
    @JsonView(JsonViews.Internal.class)
        @JsonProperty(value = ENABLED)
        @Schema(description = "User enabled", example = "true")
        Boolean enabled,
    @JsonView(JsonViews.Internal.class)
        @JsonProperty(value = EMAIL_VERIFIED)
        @Schema(description = "Email verified", example = "true")
        Boolean emailVerified,
    @JsonView(JsonViews.Admin.class)
        @JsonProperty(value = USER_ROLES)
        @Schema(description = "User roles", example = "[\"USER\"]")
        List<String> userRoles,
    @JsonView(JsonViews.Admin.class)
        @JsonProperty(value = CREATED_TIMESTAMP)
        @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
        @JsonSerialize(using = DateToLongSerializer.class)
        @JsonDeserialize(using = LongToDateSerializer.class)
        Date createdTimestamp)
    implements Response {
  @Serial private static final long serialVersionUID = 214268445410957868L;
}
