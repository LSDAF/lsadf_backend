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

import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.core.infra.config.ServerProperties;
import com.lsadf.core.infra.web.client.keycloak.KeycloakRestClient;
import com.lsadf.core.infra.web.config.keycloak.properties.KeycloakProperties;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.dto.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Auth Controller */
@RestController
@ConditionalOnProperty(prefix = "api", name = "enabled", havingValue = "true")
@Slf4j
public class AuthControllerImpl extends BaseController implements AuthController {

  private final KeycloakProperties keycloakProperties;
  private final KeycloakRestClient keycloakRestClient;
  private final ServerProperties serverProperties;
  private final OAuth2ClientProperties.Provider keycloakProvider;

  private static final String KEYCLOAK = "keycloak";
  private static final String CODE = "code";
  private static final String SCOPES = "openid profile";

  public AuthControllerImpl(
      KeycloakRestClient keycloakRestClient,
      KeycloakProperties keycloakProperties,
      ServerProperties serverProperties,
      OAuth2ClientProperties oAuth2ClientProperties) {
    this.keycloakRestClient = keycloakRestClient;
    this.keycloakProperties = keycloakProperties;
    this.serverProperties = serverProperties;
    this.keycloakProvider = oAuth2ClientProperties.getProvider().get(KEYCLOAK);
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> login() {
    log.info("Anonymous user wants to login with grant_type=authorization_code");
    HttpHeaders headers = new HttpHeaders();
    String newUrl = buildRedirectUrl();
    headers.setLocation(URI.create(newUrl));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @Override
  public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> login(
      @RequestBody @Valid UserLoginRequest userLoginRequest) {
    log.info("User {} wants to login with grant_type=password", userLoginRequest.username());

    String clientId = keycloakProperties.getClientId();
    String clientSecret = keycloakProperties.getClientSecret();

    String bodyString =
        "grant_type=password"
            + "&client_id="
            + clientId
            + "&client_secret="
            + clientSecret
            + "&username="
            + userLoginRequest.username()
            + "&password="
            + userLoginRequest.password();

    JwtAuthenticationResponse jwt =
        keycloakRestClient.getToken(keycloakProperties.getRealm(), bodyString);

    log.info("Received token: {}", jwt);
    return generateResponse(HttpStatus.OK, jwt);
  }

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
            + userRefreshLoginRequest.refreshToken();

    JwtAuthenticationResponse response =
        keycloakRestClient.getToken(keycloakProperties.getRealm(), bodyString);

    log.info("Received token: {}", response);
    return generateResponse(HttpStatus.OK, response);
  }

  /**
   * Constructs a redirect URL for the OAuth2 authorization process by appending the necessary query
   * parameters, including client ID, response type, scope, and redirect URI, all properly encoded.
   *
   * @return the constructed redirect URL as a string
   */
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
                + ApiPathConstants.OAUTH2
                + OAuth2Controller.Constants.ApiPaths.CALLBACK;
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
