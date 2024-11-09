package com.lsadf.lsadf_backend.controllers.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.lsadf_backend.annotations.Uuid;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.JsonViews;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.characteristics.CharacteristicsRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;
import static com.lsadf.lsadf_backend.constants.ControllerConstants.Params.*;

@RequestMapping(value = ControllerConstants.ADMIN_GAME_SAVES)
@Tag(name = ControllerConstants.Swagger.ADMIN_GAME_SAVES_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminGameSaveController {


    /**
     * Deletes a game save
     *
     * @param jwt        the requester JWT
     * @param gameSaveId the id of the game save
     * @return empty response
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Deletes a game save")
    @DeleteMapping(value = ControllerConstants.AdminGameSave.GAME_SAVE_ID)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<Void>> deleteGameSave(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

    /**
     * Creates a game save
     *
     * @param jwt             the requester JWT
     * @param creationRequest the creation request
     * @return the created game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Generates a new game save")
    @PostMapping
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@AuthenticationPrincipal Jwt jwt,
                                                                  @Valid @RequestBody AdminGameSaveCreationRequest creationRequest);

    /**
     * Gets a game save by its id
     *
     * @param jwt        the requester JWT
     * @param gameSaveId the id of the game save
     * @return the game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a game save by its id")
    @GetMapping(value = ControllerConstants.AdminGameSave.GAME_SAVE_ID)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<GameSave>> getGameSave(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

    /**
     * Gets all the user game saves
     * @param jwt the requester JWT
     * @param username the user username
     * @return the game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a user's game saves")
    @GetMapping(value = ControllerConstants.AdminGameSave.USER_GAME_SAVES)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(@AuthenticationPrincipal Jwt jwt,
                                                          @Valid @PathVariable(value = USERNAME) @Email String username);

    /**
     * Gets all game saves with given criterias
     *
     * @param jwt     the requester JWT
     * @param orderBy the sorting order if any
     * @return the game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets all game saves")
    @GetMapping
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@AuthenticationPrincipal Jwt jwt,
                                                                 @RequestParam(value = ORDER_BY, required = false) String orderBy);


    /**
     * Updates a game save
     *
     * @param jwt                        the requester JWT
     * @param gameSaveId                 the id of the game save
     * @param adminGameSaveUpdateRequest the game save to update
     * @return the updated game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates a new game")
    @PostMapping(value = ControllerConstants.AdminGameSave.GAME_SAVE_ID)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<GameSave>> updateGameSave(@AuthenticationPrincipal Jwt jwt,
                                                             @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                             @Valid @RequestBody AdminGameSaveUpdateRequest adminGameSaveUpdateRequest);

    /**
     * Updates the characteristics of a game save
     *
     * @param jwt Jwt
     * @param gameSaveId the game save id
     * @param characteristicsRequest the characteristics request
     * @return the characteristics
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates the characteristics of a game save")
    @PostMapping(value = ControllerConstants.AdminGameSave.UPDATE_GAME_SAVE_CHARACTERISTICS)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<Void>> updateCharacteristics(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                                @RequestBody @Valid CharacteristicsRequest characteristicsRequest);

    /**
     * Updates the currency of a game save
     *
     * @param jwt             the requester JWT
     * @param gameSaveId      the game save id
     * @param currencyRequest the currency request
     * @return the updated game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates the currency of a game save")
    @PostMapping(value = ControllerConstants.AdminGameSave.UPDATE_GAME_SAVE_CURRENCIES)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<Void>> updateGameSaveCurrencies(@AuthenticationPrincipal Jwt jwt,
                                                                   @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                                   @Valid @RequestBody CurrencyRequest currencyRequest);

    /**
     * Updates the stages of a game save
     *
     * @param jwt          the requester JWT
     * @param gameSaveId   the game save id
     * @param stageRequest the stage request
     * @return the updated game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates the stages of a game save")
    @PostMapping(value = ControllerConstants.AdminGameSave.UPDATE_GAME_SAVE_STAGES)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<Void>> updateGameSaveStages(@AuthenticationPrincipal Jwt jwt,
                                                               @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                               @Valid @RequestBody StageRequest stageRequest);
}
