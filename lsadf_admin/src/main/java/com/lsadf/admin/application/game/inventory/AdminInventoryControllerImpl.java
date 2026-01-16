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

import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.domain.game.inventory.Item;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponseMapper;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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

  private static final ItemResponseMapper itemResponseMapper = ItemResponseMapper.INSTANCE;

  public AdminInventoryControllerImpl(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  private final InventoryService inventoryService;

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public ResponseEntity<ApiResponse<Set<ItemResponse>>> getInventory(Jwt jwt, UUID gameSaveId) {
    validateUser(jwt);
    Set<Item> items = inventoryService.getInventoryItems(gameSaveId);
    Set<ItemResponse> itemResponses =
        items.stream().map(itemResponseMapper::map).collect(Collectors.toSet());
    return generateResponse(HttpStatus.OK, itemResponses);
  }

  @Override
  public ResponseEntity<ApiResponse<ItemResponse>> createItemInInventory(
      Jwt jwt, UUID gameSaveId, ItemRequest itemRequest) {
    validateUser(jwt);
    Item item = inventoryService.createItemInInventory(gameSaveId, itemRequest);
    ItemResponse itemResponse = itemResponseMapper.map(item);
    return generateResponse(HttpStatus.OK, itemResponse);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> deleteItemFromInventory(
      Jwt jwt, UUID gameSaveId, String itemClientId) {
    validateUser(jwt);
    inventoryService.deleteItemFromInventory(gameSaveId, itemClientId);
    return generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<ItemResponse>> updateItemInInventory(
      Jwt jwt, UUID gameSaveId, String itemClientId, ItemRequest itemRequest) {
    validateUser(jwt);
    Item item = inventoryService.updateItemInInventory(gameSaveId, itemClientId, itemRequest);
    ItemResponse itemResponse = itemResponseMapper.map(item);
    return generateResponse(HttpStatus.OK, itemResponse);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> clearInventoryItems(Jwt jwt, UUID gameSaveId) {
    validateUser(jwt);
    inventoryService.clearInventory(gameSaveId);
    return generateResponse(HttpStatus.OK);
  }
}
