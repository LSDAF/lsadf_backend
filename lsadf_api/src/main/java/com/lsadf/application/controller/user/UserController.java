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
package com.lsadf.application.controller.user;

import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.OAUTH2_AUTHENTICATION;

import com.lsadf.core.infra.web.controllers.ControllerConstants;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.ResponseMessages;
import com.lsadf.core.infra.web.responses.user.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for user operations */
@RequestMapping(value = ControllerConstants.USER)
@Tag(name = ControllerConstants.Swagger.USER_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface UserController {

  /**
   * Retrieves the information of the logged-in user.
   *
   * @param jwt the JWT token of the authenticated user, provided automatically via Spring Security
   * @return a ResponseEntity containing a ApiResponse object with user information encapsulated as
   *     a GlobalInfoResponse. The response status and message will also be included.
   */
  @GetMapping(value = ControllerConstants.User.ME)
  @Operation(summary = "Gets the logged UserInfo user info")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal Jwt jwt);
}
