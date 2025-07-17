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
package com.lsadf.core.infra.web.request.user.update;

import static com.lsadf.core.infra.web.JsonAttributes.FIRST_NAME;
import static com.lsadf.core.infra.web.JsonAttributes.LAST_NAME;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.util.List;
import lombok.Builder;

@Builder
public record SimpleUserUpdateRequest(
    @JsonProperty(value = FIRST_NAME) @NotBlank String firstName,
    @JsonProperty(value = LAST_NAME) @NotBlank String lastName)
    implements UserUpdateRequest {

  @Serial private static final long serialVersionUID = 3391683431995156829L;

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
    return null;
  }

  @Override
  public Boolean getEnabled() {
    return null;
  }

  @Override
  public List<String> getUserRoles() {
    return null;
  }
}
