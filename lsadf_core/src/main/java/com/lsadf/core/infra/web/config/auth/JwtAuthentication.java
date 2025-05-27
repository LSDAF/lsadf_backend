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
package com.lsadf.core.infra.web.config.auth;

import static com.lsadf.core.infra.web.JsonAttributes.JwtAuthentication.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.domain.Model;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({ACCESS_TOKEN, EXPIRES_IN, REFRESH_TOKEN, REFRESH_EXPIRES_IN})
public class JwtAuthentication implements Model {

  @Serial private static final long serialVersionUID = -5360094704215801310L;

  @JsonProperty(value = ACCESS_TOKEN)
  private final String accessToken;

  @JsonProperty(value = EXPIRES_IN)
  private final Long expiresIn;

  @JsonProperty(value = REFRESH_TOKEN)
  private final String refreshToken;

  @JsonProperty(value = REFRESH_EXPIRES_IN)
  private final Long refreshExpiresIn;
}
