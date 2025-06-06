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
package com.lsadf.application.controller.game.stage;

import static com.lsadf.core.infra.web.controllers.ControllerConstants.STAGE;
import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.OAUTH2_AUTHENTICATION;

import com.lsadf.core.infra.web.requests.game.stage.StageRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.stage.StageResponse;
import com.lsadf.core.shared.validation.Uuid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/** Controller for stage related operations. */
@RequestMapping(value = STAGE)
@Tag(name = StageController.Constants.Swagger.STAGE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface StageController {
  String GAME_SAVE_ID = "game_save_id";

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
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
      @Valid @RequestBody StageRequest stageRequest);

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
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ApiPaths {
      public static final String GAME_SAVE_ID = "/{game_save_id}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Swagger {
      public static final String STAGE_CONTROLLER = "Stage Controller";
    }
  }
}
