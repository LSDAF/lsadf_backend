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
package com.lsadf.admin.application.cache;

import static com.lsadf.admin.application.cache.AdminCacheController.Constants.ApiPaths.*;
import static com.lsadf.admin.application.constant.AdminSwaggerConstants.ADMIN_CACHE_CONTROLLER;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = AdminApiPathConstants.ADMIN_CACHE)
@Tag(name = ADMIN_CACHE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminCacheController extends Controller {

  /**
   * Clears all the caches of the application
   *
   * @param jwt the requester JWT
   * @return empty response
   */
  @PutMapping(value = FLUSH)
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
  @Operation(summary = "Clears all the caches of the application")
  ResponseEntity<ApiResponse<Void>> flushAndClearCache(@AuthenticationPrincipal Jwt jwt);

  /**
   * Checks if the cache is enabled
   *
   * @param jwt the requester JWT
   * @return true if the cache is enabled, false otherwise
   */
  @GetMapping(value = ENABLED)
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
  @Operation(summary = "Checks if the cache is enabled")
  ResponseEntity<ApiResponse<Boolean>> isCacheEnabled(@AuthenticationPrincipal Jwt jwt);

  /**
   * Enables/Disables the cache
   *
   * @param jwt the requester JWT
   * @return empty response
   */
  @PutMapping(value = TOGGLE)
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
  @Operation(summary = "Enables/Disables the cache")
  ResponseEntity<ApiResponse<Boolean>> toggleRedisCacheEnabling(@AuthenticationPrincipal Jwt jwt);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String FLUSH = "/flush";
      public static final String TOGGLE = "/toggle";
      public static final String ENABLED = "/enabled";
    }
  }
}
