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
package com.lsadf.admin.application.game.inventory;

import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.config.swagger.SwaggerAuthenticationStrategies.OAUTH2_AUTHENTICATION;
import static com.lsadf.core.infra.web.controller.ParameterConstants.CLIENT_ID;
import static com.lsadf.core.infra.web.controller.ParameterConstants.GAME_SAVE_ID;

import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.constant.AdminSwaggerConstants;
import com.lsadf.core.infra.web.controller.Controller;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * AdminInventoryController provides RESTful APIs for managing game inventories for administrators,
 * including retrieving, creating, updating, and deleting items in a game's inventory.
 *
 * <p>This interface includes operations secured with authentication and authorization, requiring
 * valid JWT tokens and proper permissions.
 */
@RequestMapping(value = AdminApiPathConstants.ADMIN_INVENTORY)
@Tag(name = AdminSwaggerConstants.ADMIN_INVENTORY_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminInventoryController extends Controller {

  /**
   * Retrieves the inventory associated with a specific game save.
   *
   * @param jwt The JWT token of the authenticated user.
   * @param gameSaveId The UUID of the game save whose inventory is to be retrieved.
   * @return ResponseEntity containing the inventory data as a set of ItemResponse objects.
   */
  @GetMapping(value = Constants.ApiPaths.GAME_SAVE_ID)
  @Operation(summary = "Gets the inventory for a game save")
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
  ResponseEntity<ApiResponse<Set<ItemResponse>>> getInventory(
      @AuthenticationPrincipal Jwt jwt, @PathVariable(value = GAME_SAVE_ID) UUID gameSaveId);

  /**
   * Creates an item in the inventory of a specified game save.
   *
   * @param jwt The JWT token of the authenticated user.
   * @param gameSaveId The UUID of the game save where the item will be created.
   * @param itemRequest The request containing the details of the item to create.
   * @return ResponseEntity containing the created item details.
   */
  @PostMapping(value = Constants.ApiPaths.ITEMS)
  @Operation(summary = "Creates an item in the inventory of a game save")
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
  ResponseEntity<ApiResponse<ItemResponse>> createItemInInventory(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) UUID gameSaveId,
      @RequestBody @Valid ItemRequest itemRequest);

  /**
   * Deletes an item from the inventory of a specific game save.
   *
   * @param jwt The JWT token of the authenticated user
   * @param gameSaveId The UUID of the game save to remove the item from
   * @param itemClientId The client-side identifier of the item to delete
   * @return ResponseEntity containing the updated inventory data
   */
  @DeleteMapping(value = Constants.ApiPaths.CLIENT_ID)
  @Operation(summary = "Deletes an item from the inventory of a game save")
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
  ResponseEntity<ApiResponse<Void>> deleteItemFromInventory(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) UUID gameSaveId,
      @PathVariable(value = CLIENT_ID) String itemClientId);

  /**
   * Updates an item in the inventory of a specific game save.
   *
   * @param jwt The JWT token of the authenticated user.
   * @param gameSaveId The UUID of the game save containing the inventory to update.
   * @param itemClientId The client-side identifier of the item to update.
   * @param itemRequest The request containing the updated item details.
   * @return ResponseEntity containing the updated item data.
   */
  @PutMapping(value = Constants.ApiPaths.CLIENT_ID)
  @Operation(summary = "Updates an item in the inventory of a game save")
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
  ResponseEntity<ApiResponse<ItemResponse>> updateItemInInventory(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) UUID gameSaveId,
      @PathVariable(value = CLIENT_ID) String itemClientId,
      @RequestBody @Valid ItemRequest itemRequest);

  /**
   * Clears the inventory of a specific game save.
   *
   * @param jwt The JWT token of the authenticated user
   * @param gameSaveId The UUID of the game save to clear the inventory of
   * @return ResponseEntity containing the updated inventory data
   */
  @DeleteMapping(value = Constants.ApiPaths.GAME_SAVE_ID)
  @Operation(summary = "Clears the inventory of a specific game save")
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
  ResponseEntity<ApiResponse<Void>> clearInventoryItems(
      @AuthenticationPrincipal Jwt jwt, @PathVariable(value = GAME_SAVE_ID) UUID gameSaveId);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Constants {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApiPaths {
      public static final String GAME_SAVE_ID = "/{game_save_id}";
      public static final String ITEMS = "/{game_save_id}/item";
      public static final String CLIENT_ID = "/{game_save_id}/item/{client_id}";
    }
  }
}
