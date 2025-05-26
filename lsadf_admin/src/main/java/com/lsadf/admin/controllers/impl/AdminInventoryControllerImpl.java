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
package com.lsadf.admin.controllers.impl;

import static com.lsadf.core.common.utils.ResponseUtils.generateResponse;

import com.lsadf.admin.controllers.AdminInventoryController;
import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.entities.ItemEntity;
import com.lsadf.core.game.inventory.Inventory;
import com.lsadf.core.game.inventory.item.Item;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.requests.item.ItemRequest;
import com.lsadf.core.services.InventoryService;
import com.lsadf.core.web.responses.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdminInventoryControllerImpl extends BaseController
    implements AdminInventoryController {

  public AdminInventoryControllerImpl(InventoryService inventoryService, Mapper mapper) {
    this.inventoryService = inventoryService;
    this.mapper = mapper;
  }

  private final InventoryService inventoryService;
  private final Mapper mapper;

  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Inventory>> getInventory(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    InventoryEntity inventoryEntity = inventoryService.getInventory(gameSaveId);
    Inventory inventory = mapper.mapInventoryEntityToInventory(inventoryEntity);
    return generateResponse(HttpStatus.OK, inventory);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Item>> createItemInInventory(
      Jwt jwt, String gameSaveId, ItemRequest itemRequest) {
    validateUser(jwt);
    ItemEntity itemEntity = inventoryService.createItemInInventory(gameSaveId, itemRequest);
    Item item = mapper.mapItemEntityToItem(itemEntity);
    return generateResponse(HttpStatus.OK, item);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> deleteItemFromInventory(
      Jwt jwt, String gameSaveId, String itemClientId) {
    validateUser(jwt);
    inventoryService.deleteItemFromInventory(gameSaveId, itemClientId);
    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Item>> updateItemInInventory(
      Jwt jwt, String gameSaveId, String itemClientId, ItemRequest itemRequest) {
    validateUser(jwt);
    ItemEntity itemEntity =
        inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest);
    Item item = mapper.mapItemEntityToItem(itemEntity);
    return generateResponse(HttpStatus.OK, item);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> clearInventoryItems(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    inventoryService.clearInventory(gameSaveId);
    return generateResponse(HttpStatus.OK);
  }
}
