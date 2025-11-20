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
package com.lsadf.core.infra.web.dto.request.user.creation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Builder
public record SimpleUserCreationRequest(
    @Schema(description = "Name of user to create", example = "Toto Dupont") @NotBlank
        String firstName,
    @Schema(description = "Lastname of user to create", example = "Dupont") @NotBlank
        String lastName,
    @Schema(description = "Password of user to create", example = "k127F978") @Size(min = 8)
        String password,
    @Schema(description = "Username of user to create", example = "toto@toto.fr") @Email @NotBlank
        String username)
    implements UserCreationRequest {

  @Serial private static final long serialVersionUID = 7976141604912528826L;

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public Boolean getEnabled() {
    return true;
  }

  @Override
  public Boolean getEmailVerified() {
    return false;
  }

  @Override
  public List<String> getUserRoles() {
    return new ArrayList<>();
  }
}
