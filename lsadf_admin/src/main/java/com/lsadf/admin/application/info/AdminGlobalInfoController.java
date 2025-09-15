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
package com.lsadf.admin.application.info;

import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.constant.AdminSwaggerConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.ResponseMessages;
import com.lsadf.core.infra.web.dto.response.info.GlobalInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = AdminApiPathConstants.ADMIN_GLOBAL_INFO)
@Tag(name = AdminSwaggerConstants.ADMIN_GLOBAL_INFO_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminGlobalInfoController extends Controller {

  /**
   * Gets the global info of the application
   *
   * @return the global info
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
  @Operation(summary = "Gets the global info of the application")
  ResponseEntity<ApiResponse<GlobalInfoResponse>> getGlobalInfo(@AuthenticationPrincipal Jwt jwt);
}
