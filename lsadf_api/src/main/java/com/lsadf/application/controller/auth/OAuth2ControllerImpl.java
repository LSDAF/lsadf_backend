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
package com.lsadf.application.controller.auth;

import static com.lsadf.core.infra.web.responses.ResponseUtils.generateResponse;

import com.lsadf.core.infra.config.ServerProperties;
import com.lsadf.core.infra.web.client.keycloak.KeycloakClient;
import com.lsadf.core.infra.web.client.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.config.keycloak.KeycloakProperties;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.controller.ControllerConstants;
import com.lsadf.core.infra.web.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OAuth2ControllerImpl extends BaseController implements OAuth2Controller {

  private final KeycloakClient keycloakClient;
  private final KeycloakProperties keycloakProperties;
  private final ServerProperties serverProperties;

  public OAuth2ControllerImpl(
      KeycloakClient keycloakClient,
      KeycloakProperties keycloakProperties,
      ServerProperties serverProperties) {
    this.keycloakClient = keycloakClient;
    this.serverProperties = serverProperties;
    this.keycloakProperties = keycloakProperties;
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> handleOAuth2Callback(
      @RequestParam(CODE) String code) {
    // Handle the code returned from Keycloak here
    log.info("Received code: {}", code);
    // request token to keycloak using code

    String clientId = keycloakProperties.getClientId();
    String clientSecret = keycloakProperties.getClientSecret();
    String redirectUri =
        serverProperties.isHttps()
            ? "https://"
            : "http://"
                + serverProperties.getHostName()
                + ":"
                + serverProperties.getPort()
                + ControllerConstants.OAUTH2
                + ControllerConstants.OAuth2.CALLBACK;

    String bodyString =
        "grant_type=authorization_code"
            + "&client_id="
            + clientId
            + "&client_secret="
            + clientSecret
            + "&code="
            + code
            + "&redirect_uri="
            + redirectUri;

    JwtAuthenticationResponse jwt =
        keycloakClient.getToken(keycloakProperties.getRealm(), bodyString);
    log.info("Received token: {}", jwt);
    if (jwt == null) {
      return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // return token
    return generateResponse(HttpStatus.OK, jwt);
  }
}
