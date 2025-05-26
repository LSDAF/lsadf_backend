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
package com.lsadf.core.user;

import static com.lsadf.core.constants.JsonAttributes.UserInfo.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.common.models.Model;
import java.io.Serial;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({NAME, EMAIL, VERIFIED, ROLES})
public class UserInfo implements Model {

  @Serial private static final long serialVersionUID = -3162522781668155748L;

  @JsonProperty(value = NAME)
  private String name;

  @JsonProperty(value = EMAIL)
  private String email;

  @JsonProperty(value = VERIFIED)
  private boolean verified;

  @JsonProperty(value = ROLES)
  private Set<String> roles;
}
