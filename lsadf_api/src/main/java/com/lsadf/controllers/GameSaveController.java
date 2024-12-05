package com.lsadf.controllers;

import static com.lsadf.core.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.core.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.annotations.Uuid;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.core.constants.ResponseMessages;
import com.lsadf.core.models.GameSave;
import com.lsadf.core.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.core.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/** Controller for game save operations */
@RequestMapping(value = ControllerConstants.GAME_SAVE)
@Tag(name = ControllerConstants.Swagger.GAME_SAVE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface GameSaveController {

  String GAME_SAVE_ID = "game_save_id";

  /**
   * Generates a new game, returns the generated game save
   *
   * @return the generated game save
   */
  @PostMapping(value = ControllerConstants.GameSave.GENERATE)
  @Operation(summary = "Generates a new game, returns the generated game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
        @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
        @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
        @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @JsonView(JsonViews.Internal.class)
  ResponseEntity<GenericResponse<GameSave>> generateNewGameSave(@AuthenticationPrincipal Jwt jwt);

  /**
   * Updates the nickname of a game save
   *
   * @param jwt Jwt
   * @param id id of the game save
   * @param gameSaveUpdateNicknameRequest GameSaveUpdateNicknameRequest
   * @return GenericResponse
   */
  @PostMapping(value = ControllerConstants.GameSave.UPDATE_NICKNAME)
  @Operation(summary = "Updates the nickname of a game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
        @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
        @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
        @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
        @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @JsonView(JsonViews.Internal.class)
  ResponseEntity<GenericResponse<Void>> updateNickname(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String id,
      @Valid @RequestBody GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest);

  /** Gets the user game saves */
  @GetMapping(value = ControllerConstants.GameSave.ME)
  @Operation(summary = "Gets the game saves of the logged user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
        @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
        @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
        @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
        @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
      })
  @JsonView(JsonViews.Internal.class)
  ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(Jwt jwt);
}
