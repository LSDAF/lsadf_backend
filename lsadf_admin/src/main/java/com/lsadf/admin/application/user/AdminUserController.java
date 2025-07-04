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

import static com.lsadf.core.infra.web.controller.ControllerConstants.Params.*;
import static com.lsadf.core.infra.web.controller.ControllerConstants.Swagger.Authentications.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.controller.ControllerConstants.Swagger.Authentications.OAUTH2_AUTHENTICATION;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.controller.ControllerConstants;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.request.user.creation.AdminUserCreationRequest;
import com.lsadf.core.infra.web.request.user.update.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.ResponseMessages;
import com.lsadf.core.shared.validation.Uuid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = ControllerConstants.ADMIN_USERS)
@Tag(name = ControllerConstants.Swagger.ADMIN_USERS_CONTROLLER)
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
  @PostMapping(value = ControllerConstants.AdminUser.USER_ID)
  @Operation(summary = "Updates a user")
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<User>> updateUser(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = USER_ID) @Uuid String userId,
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
  @JsonView(JsonViews.Admin.class)
  @PostMapping
  ResponseEntity<ApiResponse<User>> createUser(
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
  @DeleteMapping(value = ControllerConstants.AdminUser.USER_ID)
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<Void>> deleteUser(
      @AuthenticationPrincipal Jwt jwt, @PathVariable(value = USER_ID) @Uuid String userId);

  /**
   * Gets a user by its email
   *
   * @param jwt the requester JWT
   * @param username the email of the user
   * @return the user
   */
  @GetMapping(value = ControllerConstants.AdminUser.USERNAME)
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
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<User>> getUserByUsername(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @Email @PathVariable(value = USERNAME) String username);

  /**
   * Gets a user by its id
   *
   * @param jwt the requester JWT
   * @param userId the id of the user
   * @return the user details
   */
  @GetMapping(value = ControllerConstants.AdminUser.USER_ID)
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
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<User>> getUserById(
      @AuthenticationPrincipal Jwt jwt, @PathVariable(value = USER_ID) @Uuid String userId);

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
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<List<User>>> getUsers(
      @AuthenticationPrincipal Jwt jwt,
      @RequestParam(value = ORDER_BY, required = false) List<String> orderBy);
}
