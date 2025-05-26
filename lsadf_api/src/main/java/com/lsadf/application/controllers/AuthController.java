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
package com.lsadf.application.controllers;

import com.lsadf.core.auth.JwtAuthentication;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.constants.ResponseMessages;
import com.lsadf.core.requests.user.UserLoginRequest;
import com.lsadf.core.requests.user.UserRefreshLoginRequest;
import com.lsadf.core.web.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = ControllerConstants.AUTH)
@Tag(name = ControllerConstants.Swagger.AUTH_CONTROLLER)
public interface AuthController {

  /**
   * Logs in a user
   *
   * @return the JWT object containing the token
   */
  @GetMapping(value = ControllerConstants.Auth.LOGIN)
  @Operation(
      summary = "Logins a user, returns a JWT object contaning the token to request the API",
      hidden = true)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
        @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  ResponseEntity<GenericResponse<Void>> login();

  /**
   * Logins a new user with the password grant type
   *
   * @param userLoginRequest the user login request containing the username and the password
   * @return the JWT object containing the token
   */
  @PostMapping(value = ControllerConstants.Auth.LOGIN)
  @Operation(summary = "Logins a user, returns a JWT object contaning the token to request the API")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
        @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
        @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  ResponseEntity<GenericResponse<JwtAuthentication>> login(UserLoginRequest userLoginRequest);

  /**
   * Refreshes a user token
   *
   * @param userRefreshLoginRequest the refresh token request containing the username and the
   *     refresh token
   * @return the new JWT object containing the new token
   */
  @PostMapping(value = ControllerConstants.Auth.REFRESH)
  @Operation(summary = "Refreshes the token of a user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
        @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
        @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  ResponseEntity<GenericResponse<JwtAuthentication>> refresh(
      UserRefreshLoginRequest userRefreshLoginRequest);
}
