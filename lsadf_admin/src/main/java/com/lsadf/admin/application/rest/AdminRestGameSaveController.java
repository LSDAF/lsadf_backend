package com.lsadf.admin.application.rest;

import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.constant.AdminSwaggerConstants;
import com.lsadf.core.infra.web.dto.request.game.save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveUpdateRequest;
import com.lsadf.core.infra.web.dto.request.search.GameSaveFilter;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = AdminApiPathConstants.ADMIN_REST_GAME_SAVE)
@Tag(name = AdminSwaggerConstants.ADMIN_REST_GAME_SAVE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminRestGameSaveController {

  @Operation(
      summary = "Get all game saves",
      description = "Retrieve a paginated list of all game saves with optional filtering")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved game saves",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @GetMapping
  ResponseEntity<ApiResponse<Page<GameSaveResponse>>> getAllGameSaves(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "Pagination information") Pageable pageable,
      @Nullable @Parameter(description = "Filter criteria for game saves") GameSaveFilter filter);

  @Operation(
      summary = "Get game save by ID",
      description = "Retrieve a specific game save by its unique identifier")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved game save",
            content = @Content(schema = @Schema(implementation = GameSaveResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Game save not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @GetMapping("/{id}")
  ResponseEntity<ApiResponse<Page<GameSaveResponse>>> getGameSaveById(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "Game save ID", required = true) @PathVariable String id);

  @Operation(
      summary = "Create new game save",
      description = "Create a new game save with the provided information")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Game save created successfully",
            content = @Content(schema = @Schema(implementation = GameSaveResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Game save already exists"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @PostMapping
  ResponseEntity<ApiResponse<GameSaveResponse>> createGameSave(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "Game save creation data", required = true) @Valid @RequestBody
          GameSaveCreationRequest request);

  @Operation(
      summary = "Update game save",
      description = "Update an existing game save with new information")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Game save updated successfully",
            content = @Content(schema = @Schema(implementation = GameSaveResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Game save not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @PutMapping("/{id}")
  ResponseEntity<ApiResponse<GameSaveResponse>> updateGameSave(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "Game save ID", required = true) @PathVariable String id,
      @Parameter(description = "Game save update data", required = true) @Valid @RequestBody
          GameSaveUpdateRequest request);

  @Operation(
      summary = "Partially update game save",
      description = "Partially update an existing game save (PATCH operation)")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Game save updated successfully",
            content = @Content(schema = @Schema(implementation = GameSaveResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Game save not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @PatchMapping("/{id}")
  ResponseEntity<ApiResponse<GameSaveResponse>> patchGameSave(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "Game save ID", required = true) @PathVariable String id,
      @Parameter(description = "Partial game save update data", required = true) @Valid @RequestBody
          GameSaveUpdateRequest request);

  @Operation(
      summary = "Delete game save",
      description = "Delete a game save by its unique identifier")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "Game save deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Game save not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @DeleteMapping("/{id}")
  ResponseEntity<ApiResponse<Void>> deleteGameSave(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "Game save ID", required = true) @PathVariable String id);

  @Operation(
      summary = "Get game saves by user email",
      description = "Retrieve all game saves for a specific user by their email address")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved user's game saves",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @GetMapping("/user/{userEmail}")
  ResponseEntity<ApiResponse<Page<GameSaveResponse>>> getGameSavesByUserEmail(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "User email address", required = true) @PathVariable
          String userEmail,
      @Parameter(description = "Pagination information") Pageable pageable);

  @Operation(
      summary = "Delete all game saves for user",
      description = "Delete all game saves belonging to a specific user")
  @ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "User's game saves deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error")
      })
  @DeleteMapping("/user/{userEmail}")
  ResponseEntity<ApiResponse<Void>> deleteAllGameSavesForUser(
      @AuthenticationPrincipal Jwt jwt,
      @Parameter(description = "User email address", required = true) @PathVariable
          String userEmail);
}
