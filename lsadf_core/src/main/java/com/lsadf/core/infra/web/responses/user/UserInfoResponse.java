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

package com.lsadf.core.infra.web.responses.user;

import static com.lsadf.core.infra.web.responses.user.UserInfoResponse.Attributes.*;
import static com.lsadf.core.infra.web.responses.user.UserInfoResponse.Attributes.ROLES;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.infra.web.responses.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@JsonPropertyOrder({NAME, EMAIL, VERIFIED, ROLES})
@Schema(name = "UserInfo", description = "UserInfo response object")
public record UserInfoResponse(
    @JsonProperty(value = NAME) String name,
    @JsonProperty(value = EMAIL) String email,
    @JsonProperty(value = VERIFIED) boolean verified,
    @JsonProperty(value = ROLES) Set<String> roles)
    implements Response {
  @Serial private static final long serialVersionUID = -5863483742674101453L;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Attributes {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String ROLES = "roles";
    public static final String VERIFIED = "validated";
  }
}
