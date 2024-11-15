package com.lsadf.lsadf_backend.controllers;

import com.lsadf.core.annotations.Uuid;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
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

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

/**
 * Controller for currency related operations.
 */
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

    @PostMapping(value = ControllerConstants.Currency.GAME_SAVE_ID)
    @Operation(summary = "Saves one or several currency amounts for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> saveCurrency(@AuthenticationPrincipal Jwt jwt,
                                                       @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                       @RequestBody @Valid CurrencyRequest currencyRequest);

    @GetMapping(value = ControllerConstants.Currency.GAME_SAVE_ID)
    @Operation(summary = "Gets the currency amounts for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> getCurrency(@AuthenticationPrincipal Jwt jwt,
                                                      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);
}
