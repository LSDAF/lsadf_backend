/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.admin.application.game.mail;

import static com.lsadf.admin.application.game.mail.AdminGameMailTemplateController.Constants.ApiPaths.GAME_MAIL_TEMPLATE_ID;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.constant.AdminSwaggerConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.controller.ParameterConstants;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailAttachmentRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailTemplateRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailTemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tools.jackson.core.JacksonException;

/** */
@RequestMapping(value = AdminApiPathConstants.ADMIN_GAME_MAIL_TEMPLATE)
@Tag(name = AdminSwaggerConstants.ADMIN_GAME_MAIL_TEMPLATE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminGameMailTemplateController extends Controller {

  @GetMapping
  @Operation(summary = "Gets all the game mail templates")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "OK"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal Server Error")
      })
  ResponseEntity<ApiResponse<List<GameMailTemplateResponse>>> getAllTemplates(
      @AuthenticationPrincipal Jwt jwt);

  @GetMapping(value = GAME_MAIL_TEMPLATE_ID)
  @Operation(summary = "Gets a specific game mail template by its ID")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "OK"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Not Found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad Request")
      })
  ResponseEntity<ApiResponse<GameMailTemplateResponse>> getTemplateById(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = ParameterConstants.GAME_MAIL_TEMPLATE_ID) UUID id)
      throws JacksonException;

  @DeleteMapping(value = GAME_MAIL_TEMPLATE_ID)
  @Operation(summary = "Deletes a game mail template using its ID")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "OK"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Not Found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad Request")
      })
  ResponseEntity<ApiResponse<Void>> deleteMailTemplateById(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = ParameterConstants.GAME_MAIL_TEMPLATE_ID) UUID id);

  @PutMapping(value = Constants.ApiPaths.GAME_MAIL_TEMPLATE_ID)
  @Operation(summary = "Attaches a list of new attachments to an existing game mail template")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "OK"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Not Found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad Request")
      })
  ResponseEntity<ApiResponse<GameMailTemplateResponse>> createNewTemplateAttachmentToTemplate(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = ParameterConstants.GAME_MAIL_TEMPLATE_ID) UUID id,
      @RequestBody List<GameMailAttachmentRequest<?>> attachments)
      throws JacksonException;

  @PostMapping
  @Operation(summary = "Creates a new game mail template")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "OK"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad Request")
      })
  ResponseEntity<ApiResponse<GameMailTemplateResponse>> createNewMailTemplate(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody GameMailTemplateRequest gameMailTemplateRequest);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String GAME_MAIL_TEMPLATE_ID = "/{game_mail_template_id}";
    }
  }
}
