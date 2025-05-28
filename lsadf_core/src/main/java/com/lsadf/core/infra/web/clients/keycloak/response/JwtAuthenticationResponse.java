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
package com.lsadf.core.infra.web.clients.keycloak.response;

import static com.lsadf.core.infra.web.clients.keycloak.response.JwtAuthenticationResponse.Attributes.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({ACCESS_TOKEN, EXPIRES_IN, REFRESH_TOKEN, REFRESH_EXPIRES_IN})
public record JwtAuthenticationResponse(
    @JsonProperty(value = ACCESS_TOKEN) String accessToken,
    @JsonProperty(value = EXPIRES_IN) Long expiresIn,
    @JsonProperty(value = REFRESH_TOKEN) String refreshToken,
    @JsonProperty(value = Attributes.REFRESH_EXPIRES_IN) Long refreshExpiresIn)
    implements Model {

  @Serial private static final long serialVersionUID = -5360094704215801310L;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Attributes {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String REFRESH_EXPIRES_IN = "refresh_expires_in";
  }
}
