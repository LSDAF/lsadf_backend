/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.domain.user;

import static com.lsadf.core.constants.JsonAttributes.User.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lsadf.core.common.serializers.DateToLongSerializer;
import com.lsadf.core.common.serializers.LongToDateSerializer;
import com.lsadf.core.constants.JsonAttributes;
import com.lsadf.core.domain.Model;
import com.lsadf.core.infra.web.config.controllers.JsonViews;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/** User DTO */
@SuperBuilder
@Data
@AllArgsConstructor
public class User implements Model {

  @Serial private static final long serialVersionUID = 144315795668992686L;

  @JsonView(JsonViews.Admin.class)
  @JsonProperty(value = JsonAttributes.ID)
  @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb")
  private final String id;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = FIRST_NAME)
  @Schema(description = "First name", example = "Toto")
  private String firstName;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = LAST_NAME)
  @Schema(description = "Last name", example = "TUTU")
  private String lastName;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = USERNAME)
  @Schema(description = "User username", example = "toto@toto.com")
  private final String username;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = ENABLED)
  @Schema(description = "User enabled", example = "true")
  private Boolean enabled;

  @JsonView(JsonViews.Internal.class)
  @JsonProperty(value = EMAIL_VERIFIED)
  @Schema(description = "Email verified", example = "true")
  private Boolean emailVerified;

  @JsonView(JsonViews.Admin.class)
  @JsonProperty(value = USER_ROLES)
  @Schema(description = "User roles", example = "[\"USER\"]")
  private List<String> userRoles;

  @JsonView(JsonViews.Admin.class)
  @JsonProperty(value = CREATED_TIMESTAMP)
  @Schema(description = "Creation date", example = "2022-01-01 00:00:00.000")
  @JsonSerialize(using = DateToLongSerializer.class)
  @JsonDeserialize(using = LongToDateSerializer.class)
  private final Date createdTimestamp;
}
