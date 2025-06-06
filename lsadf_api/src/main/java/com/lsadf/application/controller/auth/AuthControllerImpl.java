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
import com.lsadf.core.infra.web.clients.keycloak.KeycloakClient;
import com.lsadf.core.infra.web.clients.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.config.keycloak.KeycloakProperties;
import com.lsadf.core.infra.web.controllers.BaseController;
import com.lsadf.core.infra.web.controllers.ControllerConstants;
import com.lsadf.core.infra.web.requests.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.requests.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Auth Controller */
@RestController
@Slf4j
public class AuthControllerImpl extends BaseController implements AuthController {

  private final KeycloakProperties keycloakProperties;
  private final KeycloakClient keycloakClient;
  private final ServerProperties serverProperties;
  private final OAuth2ClientProperties.Provider keycloakProvider;

  private static final String KEYCLOAK = "keycloak";
  private static final String CODE = "code";
  private static final String SCOPES = "openid profile";

  public AuthControllerImpl(
      KeycloakClient keycloakClient,
      KeycloakProperties keycloakProperties,
      ServerProperties serverProperties,
      OAuth2ClientProperties oAuth2ClientProperties) {
    this.keycloakClient = keycloakClient;
    this.keycloakProperties = keycloakProperties;
    this.serverProperties = serverProperties;
    this.keycloakProvider = oAuth2ClientProperties.getProvider().get(KEYCLOAK);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Void>> login() {
    log.info("Anonymous user wants to login with grant_type=authorization_code");
    HttpHeaders headers = new HttpHeaders();
    String newUrl = buildRedirectUrl();
    headers.setLocation(URI.create(newUrl));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> login(
      @RequestBody @Valid UserLoginRequest userLoginRequest) {
    log.info("User {} wants to login with grant_type=password", userLoginRequest.getUsername());

    String clientId = keycloakProperties.getClientId();
    String clientSecret = keycloakProperties.getClientSecret();

    String bodyString =
        "grant_type=password"
            + "&client_id="
            + clientId
            + "&client_secret="
            + clientSecret
            + "&username="
            + userLoginRequest.getUsername()
            + "&password="
            + userLoginRequest.getPassword();

    JwtAuthenticationResponse jwt =
        keycloakClient.getToken(keycloakProperties.getRealm(), bodyString);

    log.info("Received token: {}", jwt);
    return generateResponse(HttpStatus.OK, jwt);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> refresh(
      @RequestBody @Valid UserRefreshLoginRequest userRefreshLoginRequest) {
    log.info("Anonymous user wants to login with grant_type=refresh_token");

    String clientId = keycloakProperties.getClientId();
    String clientSecret = keycloakProperties.getClientSecret();

    String bodyString =
        "grant_type=refresh_token"
            + "&client_id="
            + clientId
            + "&client_secret="
            + clientSecret
            + "&refresh_token="
            + userRefreshLoginRequest.getRefreshToken();

    JwtAuthenticationResponse response =
        keycloakClient.getToken(keycloakProperties.getRealm(), bodyString);

    log.info("Received token: {}", response);
    return generateResponse(HttpStatus.OK, response);
  }

  private String buildRedirectUrl() {
    StringBuilder sb = new StringBuilder();
    sb.append(keycloakProvider.getAuthorizationUri());
    sb.append("?client_id=").append(encode(keycloakProperties.getClientId()));
    sb.append("&response_type=").append(encode(CODE));
    sb.append("&scope=").append(encode(SCOPES));
    String redirectUri =
        serverProperties.isHttps()
            ? "https://"
            : "http://"
                + serverProperties.getHostName()
                + ":"
                + serverProperties.getPort()
                + ControllerConstants.OAUTH2
                + ControllerConstants.OAuth2.CALLBACK;
    sb.append("&redirect_uri=").append(encode(redirectUri));
    return sb.toString();
  }

  /**
   * Encodes the given value
   *
   * @param value the value to encode
   * @return the encoded value
   */
  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
