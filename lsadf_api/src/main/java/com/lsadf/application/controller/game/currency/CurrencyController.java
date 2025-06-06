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
package com.lsadf.application.controller.game.currency;

import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.OAUTH2_AUTHENTICATION;

import com.lsadf.core.infra.web.controllers.ControllerConstants;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.currency.CurrencyResponse;
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

/** Controller for currency related operations. */
@RequestMapping(value = ControllerConstants.CURRENCY)
@Tag(name = ControllerConstants.Swagger.CURRENCY_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface CurrencyController {

  String GAME_SAVE_ID = "game_save_id";
  String GOLD = "gold";
  String DIAMOND = "diamond";
  String EMERALD = "emerald";
  String AMETHYST = "amethyst";

  @PostMapping(value = Constants.ApiPaths.GAME_SAVE_ID)
  @Operation(summary = "Saves one or several currency amounts for a game save")
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
  ResponseEntity<ApiResponse<Void>> saveCurrency(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
      @RequestBody @Valid CurrencyRequest currencyRequest);

  @GetMapping(value = Constants.ApiPaths.GAME_SAVE_ID)
  @Operation(summary = "Gets the currency amounts for a game save")
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
  ResponseEntity<ApiResponse<CurrencyResponse>> getCurrency(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String GAME_SAVE_ID = "/{game_save_id}";
    }
  }
}
