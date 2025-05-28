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
package com.lsadf.admin.application.game;

import static com.lsadf.core.infra.web.controllers.ControllerConstants.Params.*;
import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.OAUTH2_AUTHENTICATION;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.web.controllers.Controller;
import com.lsadf.core.infra.web.controllers.ControllerConstants;
import com.lsadf.core.infra.web.controllers.JsonViews;
import com.lsadf.core.infra.web.requests.game.game_save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.requests.game.game_save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.ResponseMessages;
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

@RequestMapping(value = ControllerConstants.ADMIN_GAME_SAVES)
@Tag(name = ControllerConstants.Swagger.ADMIN_GAME_SAVES_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminGameSaveController extends Controller {

  /**
   * Deletes a game save
   *
   * @param jwt the requester JWT
   * @param gameSaveId the id of the game save
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
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Deletes a game save")
  @DeleteMapping(value = ControllerConstants.AdminGameSave.GAME_SAVE_ID)
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<Void>> deleteGameSave(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

  /**
   * Creates a game save
   *
   * @param jwt the requester JWT
   * @param creationRequest the creation request
   * @return the created game save
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
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Generates a new game save")
  @PostMapping
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<GameSave>> generateNewSaveGame(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody AdminGameSaveCreationRequest creationRequest);

  /**
   * Gets a game save by its id
   *
   * @param jwt the requester JWT
   * @param gameSaveId the id of the game save
   * @return the game save
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
  @Operation(summary = "Gets a game save by its id")
  @GetMapping(value = ControllerConstants.AdminGameSave.GAME_SAVE_ID)
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<GameSave>> getGameSave(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

  /**
   * Gets all the user game saves
   *
   * @param jwt the requester JWT
   * @param username the user username
   * @return the game saves
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
  @Operation(summary = "Gets a user's game saves")
  @GetMapping(value = ControllerConstants.AdminGameSave.USER_GAME_SAVES)
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<List<GameSave>>> getUserGameSaves(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @PathVariable(value = USERNAME) @Email String username);

  /**
   * Gets all game saves with given criterias
   *
   * @param jwt the requester JWT
   * @param orderBy the sorting order if any
   * @return the game saves
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
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Gets all game saves")
  @GetMapping
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<List<GameSave>>> getSaveGames(
      @AuthenticationPrincipal Jwt jwt,
      @RequestParam(value = ORDER_BY, required = false) List<String> orderBy);

  /**
   * Updates a game save
   *
   * @param jwt the requester JWT
   * @param gameSaveId the id of the game save
   * @param adminGameSaveUpdateRequest the game save to update
   * @return the updated game save
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
            responseCode = "200",
            description = ResponseMessages.OK),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = ResponseMessages.NOT_FOUND),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @Operation(summary = "Updates a new game")
  @PostMapping(value = ControllerConstants.AdminGameSave.GAME_SAVE_ID)
  @JsonView(JsonViews.Admin.class)
  ResponseEntity<ApiResponse<GameSave>> updateGameSave(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
      @Valid @RequestBody AdminGameSaveUpdateRequest adminGameSaveUpdateRequest);
}
