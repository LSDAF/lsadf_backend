/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.web.client.keycloak;

import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/** RestClient-based implementation for Keycloak token endpoint communication. */
@Slf4j
public class KeycloakRestClient {

  private static final String TOKEN_ENDPOINT = "/realms/{realm}/protocol/openid-connect/token";

  private final RestClient restClient;

  public KeycloakRestClient(RestClient keycloakRestClient) {
    this.restClient = keycloakRestClient;
  }

  /**
   * Get JWT token from Keycloak token endpoint.
   *
   * @param realm the Keycloak realm
   * @param body the form-encoded body (grant_type, client_id, etc.)
   * @return JWT authentication response
   */
  public JwtAuthenticationResponse getToken(String realm, String body) {
    log.debug("Requesting token from Keycloak for realm: {}", realm);

    JwtAuthenticationResponse response =
        restClient
            .post()
            .uri(TOKEN_ENDPOINT, realm)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(JwtAuthenticationResponse.class);

    return Objects.requireNonNull(response, "Token response cannot be null");
  }
}
