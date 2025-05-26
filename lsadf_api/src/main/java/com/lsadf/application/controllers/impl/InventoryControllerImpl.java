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
package com.lsadf.application.controllers.impl;

import static com.lsadf.core.common.utils.ResponseUtils.generateResponse;
import static com.lsadf.core.common.utils.TokenUtils.getUsernameFromJwt;

import com.lsadf.application.controllers.InventoryController;
import com.lsadf.core.domain.game.inventory.Inventory;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.cache.services.CacheService;
import com.lsadf.core.infra.persistence.config.mappers.Mapper;
import com.lsadf.core.infra.persistence.game.InventoryEntity;
import com.lsadf.core.infra.persistence.game.ItemEntity;
import com.lsadf.core.infra.web.config.controllers.BaseController;
import com.lsadf.core.infra.web.requests.game.inventory.item.ItemRequest;
import com.lsadf.core.infra.web.responses.GenericResponse;
import com.lsadf.core.services.GameSaveService;
import com.lsadf.core.services.InventoryService;
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
  private final CacheService cacheService;

  private final Mapper mapper;

  @Autowired
  public InventoryControllerImpl(
      GameSaveService gameSaveService,
      InventoryService inventoryService,
      CacheService cacheService,
      Mapper mapper) {
    this.gameSaveService = gameSaveService;
    this.inventoryService = inventoryService;
    this.cacheService = cacheService;
    this.mapper = mapper;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Inventory>> getInventory(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    InventoryEntity inventoryEntity = inventoryService.getInventory(gameSaveId);
    Inventory inventory = mapper.mapInventoryEntityToInventory(inventoryEntity);
    return generateResponse(HttpStatus.OK, inventory);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Item>> createItemInInventory(
      Jwt jwt, String gameSaveId, ItemRequest itemRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    ItemEntity itemEntity = inventoryService.createItemInInventory(gameSaveId, itemRequest);
    Item item = mapper.mapItemEntityToItem(itemEntity);
    return generateResponse(HttpStatus.OK, item);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> deleteItemFromInventory(
      Jwt jwt, String gameSaveId, String itemClientId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    inventoryService.deleteItemFromInventory(gameSaveId, itemClientId);
    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Item>> updateItemInInventory(
      Jwt jwt, String gameSaveId, String itemClientId, ItemRequest itemRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    var itemEntity = inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest);
    var item = mapper.mapItemEntityToItem(itemEntity);
    return generateResponse(HttpStatus.OK, item);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
