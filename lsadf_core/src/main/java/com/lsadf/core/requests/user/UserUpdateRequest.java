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
package com.lsadf.core.requests.user;

import static com.lsadf.core.constants.JsonAttributes.User.FIRST_NAME;
import static com.lsadf.core.constants.JsonAttributes.User.LAST_NAME;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.requests.Request;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequest implements Request {

  @Serial private static final long serialVersionUID = 3391683431995156829L;

  @JsonProperty(value = FIRST_NAME)
  @NotBlank
  private String firstName;

  @JsonProperty(value = LAST_NAME)
  @NotBlank
  private String lastName;
}
