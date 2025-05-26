/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.application.controllers;

import static com.lsadf.core.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.core.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

import com.lsadf.core.common.validators.annotations.Uuid;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.infra.web.responses.GenericResponse;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for characteristics operations */
@RequestMapping(value = ControllerConstants.CHARACTERISTICS)
@Tag(name = ControllerConstants.Swagger.CHARACTERISTICS_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface CharacteristicsController {
  String GAME_SAVE_ID = "game_save_id";

  /**
   * Updates the characteristics of a game save
   *
   * @param jwt Jwt
   * @param gameSaveId the game save id
   * @param characteristicsRequest the characteristics request
   * @return the characteristics
   */
  @PostMapping(value = ControllerConstants.Characteristics.GAME_SAVE_ID)
  @Operation(summary = "Updates the characteristics of a game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Characteristics updated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  ResponseEntity<GenericResponse<Void>> saveCharacteristics(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
      @RequestBody @Valid CharacteristicsRequest characteristicsRequest);

  /**
   * Gets the characteristics of a game save
   *
   * @param jwt Jwt
   * @param gameSaveId the game save id
   * @return the characteristics
   */
  @GetMapping(value = ControllerConstants.Characteristics.GAME_SAVE_ID)
  @Operation(summary = "Gets the characteristics of a game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
      })
  ResponseEntity<GenericResponse<Void>> getCharacteristics(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);
}
