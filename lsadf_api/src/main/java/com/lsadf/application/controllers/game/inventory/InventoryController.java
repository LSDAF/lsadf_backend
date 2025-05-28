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
package com.lsadf.application.controllers.game.inventory;

import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.BEARER_AUTHENTICATION;
import static com.lsadf.core.infra.web.controllers.ControllerConstants.Swagger.Authentications.OAUTH2_AUTHENTICATION;

import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.web.controllers.ControllerConstants;
import com.lsadf.core.infra.web.requests.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.responses.GenericResponse;
import com.lsadf.core.shared.validation.Uuid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Controller interface that handles inventory-related operations for game saves. This controller
 * requires bearer token or OAuth2 authentication for all operations.
 */
@RequestMapping(value = ControllerConstants.INVENTORY)
@Tag(name = ControllerConstants.Swagger.INVENTORY_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface InventoryController {

  /** Path variable name for the game save identifier. */
  String GAME_SAVE_ID = "game_save_id";

  /** Path variable name for the client-side item identifier. */
  String CLIENT_ID = "client_id";

  /**
   * Retrieves the inventory associated with a specific game save.
   *
   * @param jwt The JWT token of the authenticated user
   * @param gameSaveId The UUID of the game save to retrieve the inventory from
   * @return ResponseEntity containing the inventory data
   */
  @GetMapping(value = ControllerConstants.Inventory.GAME_SAVE_ID)
  @Operation(summary = "Gets the inventory for a game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
      })
  ResponseEntity<GenericResponse<Set<Item>>> getInventoryItems(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

  /**
   * Creates an item in the inventory of a specific game save.
   *
   * @param jwt The JWT token of the authenticated user.
   * @param gameSaveId The UUID of the game save to add the item to.
   * @param itemRequest The request containing the item details.
   * @return ResponseEntity containing the updated inventory data.
   */
  @PostMapping(value = ControllerConstants.Inventory.ITEMS)
  @Operation(summary = "Creates an item in the inventory of a game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
      })
  ResponseEntity<GenericResponse<Item>> createItemInInventory(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
      @RequestBody @Valid ItemRequest itemRequest);

  /**
   * Deletes an item from the inventory of a specific game save.
   *
   * @param jwt The JWT token of the authenticated user
   * @param gameSaveId The UUID of the game save to remove the item from
   * @param itemClientId The client-side identifier of the item to delete
   * @return ResponseEntity containing the updated inventory data
   */
  @DeleteMapping(value = ControllerConstants.Inventory.CLIENT_ID)
  @Operation(summary = "Deletes an item from the inventory of a game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
      })
  ResponseEntity<GenericResponse<Void>> deleteItemFromInventory(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
      @PathVariable(value = CLIENT_ID) String itemClientId);

  /**
   * Updates an item in the inventory of a specific game save.
   *
   * @param jwt The JWT token of the authenticated user.
   * @param gameSaveId The UUID of the game save containing the inventory to update.
   * @param itemClientId The client-side identi fier of the item to update.
   * @param itemRequest The request containing the updated item details.
   * @return ResponseEntity containing the updated item data.
   */
  @PutMapping(value = ControllerConstants.Inventory.CLIENT_ID)
  @Operation(summary = "Updates an item in the inventory of a game save")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
      })
  ResponseEntity<GenericResponse<Item>> updateItemInInventory(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
      @PathVariable(value = CLIENT_ID) String itemClientId,
      @RequestBody @Valid ItemRequest itemRequest);
}
