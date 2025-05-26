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
package com.lsadf.core.infra.web.requests.admin;

import static com.lsadf.core.constants.JsonAttributes.User.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.infra.web.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AdminUserCreationRequest implements Request {

  @Serial private static final long serialVersionUID = 9104893581644308116L;

  @NotBlank
  @Schema(description = "Name of user to create", example = "Toto Dupont")
  @JsonProperty(value = FIRST_NAME)
  private String firstName;

  @NotBlank
  @Schema(description = "Lastname of user to create", example = "Dupont")
  @JsonProperty(value = LAST_NAME)
  private String lastName;

  @Schema(description = "Enabled status of user to create", example = "true")
  @JsonProperty(value = ENABLED)
  @NotNull
  private Boolean enabled;

  @Schema(description = "Verified email status of user to create", example = "true")
  @JsonProperty(value = EMAIL_VERIFIED)
  @NotNull
  private Boolean emailVerified;

  @Email
  @NotBlank
  @Schema(description = "Username of user to create", example = "toto@toto.fr")
  @JsonProperty(value = USERNAME)
  private String username;

  @Schema(description = "Roles of user to create", example = "[\"ADMIN\", \"USER\"]")
  @JsonProperty(value = USER_ROLES)
  private List<String> userRoles;
}
