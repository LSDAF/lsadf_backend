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
package com.lsadf.admin.application.auth;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.constant.AdminSwaggerConstants;
import com.lsadf.core.infra.web.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.ResponseMessages;
import com.lsadf.core.infra.web.response.jwt.JwtAuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = AdminApiPathConstants.AUTH)
@Tag(name = AdminSwaggerConstants.AUTH_CONTROLLER)
public interface AdminAuthController {

  /**
   * Logs in a user
   *
   * @return the JWT object containing the token
   */
  @GetMapping(value = Constants.ApiPaths.LOGIN)
  @Operation(
      summary = "Logins a user, returns a JWT object contaning the token to request the API",
      hidden = true)
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  ResponseEntity<ApiResponse<Void>> login();

  /**
   * Logins a new user with the password grant type
   *
   * @param userLoginRequest the user login request containing the username and the password
   * @return the JWT object containing the token
   */
  @PostMapping(value = Constants.ApiPaths.LOGIN)
  @Operation(summary = "Logins a user, returns a JWT object contaning the token to request the API")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  ResponseEntity<ApiResponse<JwtAuthenticationResponse>> login(UserLoginRequest userLoginRequest);

  /**
   * Refreshes a user token
   *
   * @param userRefreshLoginRequest the refresh token request containing the username and the
   *     refresh token
   * @return the new JWT object containing the new token
   */
  @PostMapping(value = Constants.ApiPaths.REFRESH)
  @Operation(summary = "Refreshes the token of a user")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  ResponseEntity<ApiResponse<JwtAuthenticationResponse>> refresh(
      UserRefreshLoginRequest userRefreshLoginRequest);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String LOGIN = "/login";
      public static final String REFRESH = "/refresh";
      public static final String LOGOUT = "/logout";
      public static final String REGISTER = "/register";
      public static final String VALIDATE_TOKEN = "/validate";
    }
  }
}
