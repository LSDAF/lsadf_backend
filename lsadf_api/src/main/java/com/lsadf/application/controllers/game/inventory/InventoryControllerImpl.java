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

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.responses.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.web.controllers.BaseController;
import com.lsadf.core.infra.web.requests.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Inventory Controller */
@RestController
@Slf4j
public class InventoryControllerImpl extends BaseController implements InventoryController {

  private final GameSaveService gameSaveService;
  private final InventoryService inventoryService;

  @Autowired
  public InventoryControllerImpl(
      GameSaveService gameSaveService, InventoryService inventoryService) {
    this.gameSaveService = gameSaveService;
    this.inventoryService = inventoryService;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Set<Item>>> getInventoryItems(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Set<Item> items = inventoryService.getInventoryItems(gameSaveId);
    return generateResponse(HttpStatus.OK, items);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Item>> createItemInInventory(
      Jwt jwt, String gameSaveId, ItemRequest itemRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Item item = inventoryService.createItemInInventory(gameSaveId, itemRequest);
    return generateResponse(HttpStatus.OK, item);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Void>> deleteItemFromInventory(
      Jwt jwt, String gameSaveId, String itemClientId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    inventoryService.deleteItemFromInventory(gameSaveId, itemClientId);
    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Item>> updateItemInInventory(
      Jwt jwt, String gameSaveId, String itemClientId, ItemRequest itemRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    var item = inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest);
    return generateResponse(HttpStatus.OK, item);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
