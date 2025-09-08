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
package com.lsadf.admin.application.user;

import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;
import static com.lsadf.core.infra.web.controller.ParameterConstants.*;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.constant.AdminSwaggerConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.dto.request.user.creation.AdminUserCreationRequest;
import com.lsadf.core.infra.web.dto.request.user.update.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.ResponseMessages;
import com.lsadf.core.infra.web.dto.response.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = AdminApiPathConstants.ADMIN_USER)
@Tag(name = AdminSwaggerConstants.ADMIN_USER_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminUserController extends Controller {

  /**
   * Updates a user
   *
   * @param jwt the requester JWT
   * @param userId the id of the user
   * @param user the user to update
   * @return the updated user
   */
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @PostMapping(value = Constants.ApiPaths.USER_ID)
  @Operation(summary = "Updates a user")
  ResponseEntity<ApiResponse<UserResponse>> updateUser(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = USER_ID) UUID userId,
      @Valid @RequestBody AdminUserUpdateRequest user);

  /**
   * Creates a new user
   *
   * @param jwt the requester JWT
   * @param adminUserCreationRequest the user creation request
   * @return the created user
   */
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.BAD_REQUEST),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Creates a new user")
  @PostMapping
  ResponseEntity<ApiResponse<UserResponse>> createUser(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest);

  /**
   * Deletes a user
   *
   * @param jwt the requester JWT
   * @param userId the id of the user
   * @return empty response
   */
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Deletes a user")
  @DeleteMapping(value = Constants.ApiPaths.USER_ID)
  ResponseEntity<ApiResponse<Void>> deleteUser(
      @AuthenticationPrincipal Jwt jwt, @PathVariable(value = USER_ID) UUID userId);

  /**
   * Gets a user by its email
   *
   * @param jwt the requester JWT
   * @param username the email of the user
   * @return the user
   */
  @GetMapping(value = Constants.ApiPaths.USERNAME)
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Gets a user by its email")
  ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @Email @PathVariable(value = USERNAME) String username);

  /**
   * Gets a user by its id
   *
   * @param jwt the requester JWT
   * @param userId the id of the user
   * @return the user details
   */
  @GetMapping(value = Constants.ApiPaths.USER_ID)
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Gets a UserAdminDetails by the user id")
  ResponseEntity<ApiResponse<UserResponse>> getUserById(
      @AuthenticationPrincipal Jwt jwt, @PathVariable(value = USER_ID) UUID userId);

  /**
   * Gets all users
   *
   * @return the list of users
   */
  @GetMapping
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
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Gets all users")
  ResponseEntity<ApiResponse<List<UserResponse>>> getUsers(
      @AuthenticationPrincipal Jwt jwt,
      @RequestParam(value = ORDER_BY, required = false) List<String> orderBy);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String ME = "/me";
      public static final String USER_ID = "/id/{user_id}";
      public static final String USERNAME = "/username/{username}";
    }
  }
}
