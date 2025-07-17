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
package com.lsadf.core.infra.web.response.jwt;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.infra.web.response.Response;
import java.io.Serial;

public record JwtAuthenticationResponse(
    @JsonProperty(value = ACCESS_TOKEN) String accessToken,
    @JsonProperty(value = EXPIRES_IN) Long expiresIn,
    @JsonProperty(value = REFRESH_TOKEN) String refreshToken,
    @JsonProperty(value = REFRESH_EXPIRES_IN) Long refreshExpiresIn)
    implements Response {

  @Serial private static final long serialVersionUID = -5360094704215801310L;
}
