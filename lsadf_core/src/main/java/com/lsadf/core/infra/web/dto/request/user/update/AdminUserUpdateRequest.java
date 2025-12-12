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
package com.lsadf.core.infra.web.dto.request.user.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminUserUpdateRequest(
    @Schema(description = "Name of user to update", example = "Toto Dupont") @NotBlank
        String firstName,
    @Schema(description = "Lastname of user to update", example = "Dupont") @NotBlank
        String lastName,
    @Schema(description = "Verified status of user to update", example = "true") @NotNull
        Boolean emailVerified,
    @Schema(description = "Enabled status of user to update", example = "true") @NotNull
        Boolean enabled,
    @Schema(description = "Roles of user to update", example = "[\"USER\"]") List<String> userRoles)
    implements UserUpdateRequest {
  @Serial private static final long serialVersionUID = -4515896456126778133L;

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public Boolean getEmailVerified() {
    return emailVerified;
  }

  @Override
  public Boolean getEnabled() {
    return enabled;
  }

  @Override
  public List<String> getUserRoles() {
    return userRoles;
  }
}
