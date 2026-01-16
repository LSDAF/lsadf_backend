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
package com.lsadf.application.controller.game.save.game_save;

import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;
import static com.lsadf.core.infra.web.controller.ParameterConstants.GAME_SAVE_ID;

import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.constant.SwaggerConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.ResponseMessages;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
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

/** Controller for game save operations */
@RequestMapping(value = ApiPathConstants.GAME_SAVE)
@Tag(name = SwaggerConstants.GAME_SAVE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface GameSaveController extends Controller {

  /**
   * Generates a new game, returns the generated game save
   *
   * @return the generated game save
   */
  @PostMapping(value = Constants.ApiPaths.GENERATE)
  @Operation(summary = "Generates a new game, returns the generated game save")
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
  ResponseEntity<ApiResponse<GameSaveResponse>> generateNewGameSave(
      @AuthenticationPrincipal Jwt jwt);

  /**
   * Updates the nickname of a game save
   *
   * @param jwt Jwt
   * @param id id of the game save
   * @param gameSaveNicknameUpdateRequest GameSaveNicknameUpdateRequest
   * @return ApiResponse
   */
  @PostMapping(value = Constants.ApiPaths.UPDATE_NICKNAME)
  @Operation(summary = "Updates the nickname of a game save")
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
  ResponseEntity<ApiResponse<Void>> updateNickname(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) UUID id,
      @Valid @RequestBody GameSaveNicknameUpdateRequest gameSaveNicknameUpdateRequest);

  /** Gets the user game saves */
  @GetMapping(value = Constants.ApiPaths.ME)
  @Operation(summary = "Gets the game saves of the logged user")
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
  ResponseEntity<ApiResponse<List<GameSaveResponse>>> getUserGameSaves(Jwt jwt);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String GENERATE = "/generate";
      public static final String ME = "/me";
      public static final String UPDATE_NICKNAME = "/{game_save_id}/nickname";
    }
  }
}
