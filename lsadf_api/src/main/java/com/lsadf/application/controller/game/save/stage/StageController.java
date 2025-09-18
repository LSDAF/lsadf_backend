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
package com.lsadf.application.controller.game.save.stage;

import static com.lsadf.application.controller.constant.SwaggerConstants.STAGE_CONTROLLER;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;
import static com.lsadf.core.infra.web.controller.ParameterConstants.GAME_SAVE_ID;

import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.stage.StageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/** Controller for stage related operations. */
@RequestMapping(value = ApiPathConstants.STAGE)
@Tag(name = STAGE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface StageController {

  /**
   * Saves the current stage for a specified game save.
   *
   * <p>This method allows authenticated users to save the game stage data for a specific game save
   * ID. The stage details must be provided within a valid {@link StageRequest} object.
   *
   * @param jwt the authentication principal containing user details.
   * @param gameSaveId the unique identifier of the game save.
   * @param stageRequest the request body containing stage details, including current stage and
   *     maximum stage.
   * @return a {@link ResponseEntity} containing an {@link ApiResponse} with no data. The response
   *     status code indicates the result of the operation.
   */
  @PostMapping(value = Constants.ApiPaths.GAME_SAVE_ID)
  @Operation(summary = "Saves the stage for a game save")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad Request"),
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
            description = "Internal Server Error")
      })
  ResponseEntity<ApiResponse<Void>> saveStage(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) UUID gameSaveId,
      @Valid @RequestBody StageRequest stageRequest);

  /**
   * Retrieves the current and maximum stage details for a specified game save.
   *
   * @param jwt the authentication principal containing user details.
   * @param gameSaveId the unique identifier of the game save.
   * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a {@link StageResponse}
   *     object that holds the current and maximum stage details. The response status code indicates
   *     the result of the operation.
   */
  @GetMapping(value = Constants.ApiPaths.GAME_SAVE_ID)
  @Operation(summary = "Gets the stage for a game save")
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
            description = "Internal Server Error")
      })
  ResponseEntity<ApiResponse<StageResponse>> getStage(
      @AuthenticationPrincipal Jwt jwt, @PathVariable(value = GAME_SAVE_ID) UUID gameSaveId);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ApiPaths {
      public static final String GAME_SAVE_ID = "/{game_save_id}";
    }
  }
}
