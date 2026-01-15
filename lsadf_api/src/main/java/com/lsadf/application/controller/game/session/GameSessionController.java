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
package com.lsadf.application.controller.game.session;

import static com.lsadf.application.controller.constant.SwaggerConstants.GAME_SESSION_CONTROLLER;
import static com.lsadf.application.controller.game.session.GameSessionController.Constants.ApiPaths.GAME_SESSION_ID;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;
import static com.lsadf.core.infra.web.controller.ParameterConstants.GAME_SAVE_ID;

import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.controller.ParameterConstants;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.session.GameSessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/** Controller interface that handles game session-related operations for game saves */
@RequestMapping(value = ApiPathConstants.GAME_SESSION)
@Tag(name = GAME_SESSION_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface GameSessionController extends Controller {

  /**
   * Opens a new game session for a specified game save.
   *
   * @param jwt The JWT token of the authenticated user.
   * @param gameSaveId The UUID of the game save to open a new game session for.
   * @return ResponseEntity containing the updated game session data.
   */
  @PostMapping
  @Operation(summary = "Opens a new game session for a specified game save")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Game Session Created"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Not Found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal Server Error")
      })
  ResponseEntity<ApiResponse<GameSessionResponse>> openNewGameSession(
      @AuthenticationPrincipal Jwt jwt, @RequestParam(value = GAME_SAVE_ID) UUID gameSaveId);

  /**
   * Refreshes the game session data for a specified game session.
   *
   * @param jwt The JWT token of the authenticated user.
   * @param gameSessionId The UUID of the game session to refresh.
   * @return ResponseEntity containing the updated game session data.
   */
  @PatchMapping(value = GAME_SESSION_ID)
  @Operation(summary = "Refreshes the game session data for a specified game session")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Game Session Refreshed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Not Found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal Server Error")
      })
  ResponseEntity<ApiResponse<GameSessionResponse>> refreshGameSession(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = ParameterConstants.GAME_SESSION_ID) UUID gameSessionId);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String GAME_SESSION_ID = "/{game_session_id}";
    }
  }
}
