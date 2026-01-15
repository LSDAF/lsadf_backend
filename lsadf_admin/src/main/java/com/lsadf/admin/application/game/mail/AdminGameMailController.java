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

import static com.lsadf.admin.application.constant.AdminSwaggerConstants.ADMIN_GAME_MAIL_CONTROLLER;
import static com.lsadf.admin.application.game.mail.AdminGameMailController.Constants.ApiPaths.*;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.controller.ParameterConstants;
import com.lsadf.core.infra.web.dto.request.game.mail.DeleteGameMailsRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.SendGameMailRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Admin Game Mail Controller for sending game mails to game saves */
@RequestMapping(value = AdminApiPathConstants.ADMIN_GAME_MAIL)
@Tag(name = ADMIN_GAME_MAIL_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminGameMailController extends Controller {

  /**
   * Sends a game mail to all game saves using the specified email template
   *
   * @param request the send game mail request containing the email template ID
   * @param jwt the requester JWT
   * @return empty response
   */
  @PostMapping(value = SEND)
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
            responseCode = "400",
            description = ResponseMessages.BAD_REQUEST),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Sends a game mail to all game saves using the specified email template")
  ResponseEntity<ApiResponse<Void>> sendGameMailToGameSaves(
      @Valid @RequestBody SendGameMailRequest request, @AuthenticationPrincipal Jwt jwt);

  /**
   * Deletes game mails by expiration timestamp or specific IDs.
   *
   * @param expiration the expiration timestamp to delete mails before
   * @param request the request containing specific mail IDs to delete
   * @param jwt the requester JWT
   * @return empty response
   */
  @PostMapping(DELETE)
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = ResponseMessages.FORBIDDEN),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = ResponseMessages.BAD_REQUEST),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(
      summary =
          "Deletes game mails by expiration timestamp or specific IDs. Provide either 'expired' parameter or request body with mailIds.")
  ResponseEntity<ApiResponse<Void>> deleteGameMails(
      @Nullable @RequestParam(value = ParameterConstants.EXPIRED, required = false)
          Instant expiration,
      @Nullable @Valid @RequestBody(required = false) DeleteGameMailsRequest request,
      @AuthenticationPrincipal Jwt jwt);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String SEND = "/send";
      public static final String DELETE = "/delete";
    }
  }
}
