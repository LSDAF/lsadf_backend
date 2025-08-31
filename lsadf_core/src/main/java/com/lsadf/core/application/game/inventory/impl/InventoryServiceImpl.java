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
package com.lsadf.core.application.game.inventory.impl;

import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.exception.AlreadyExistingItemClientIdException;
import com.lsadf.core.exception.http.ForbiddenException;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepositoryPort inventoryRepositoryPort;
  private final GameMetadataService gameMetadataService;

  public InventoryServiceImpl(
      InventoryRepositoryPort inventoryRepositoryPort, GameMetadataService gameMetadataService) {
    this.inventoryRepositoryPort = inventoryRepositoryPort;
    this.gameMetadataService = gameMetadataService;
  }

  @Override
  @Transactional(readOnly = true)
  public Set<Item> getInventoryItems(UUID gameSaveId) throws NotFoundException {
    if (!gameMetadataService.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    return inventoryRepositoryPort.findAllItemsByGameSaveId(gameSaveId);
  }

  @Override
  @Transactional
  public Item createItemInInventory(UUID gameSaveId, ItemRequest itemRequest)
      throws NotFoundException, AlreadyExistingItemClientIdException {
    if (!gameMetadataService.existsById(gameSaveId)) {
      throw new NotFoundException("Game save not found for id " + gameSaveId);
    }

    if (inventoryRepositoryPort.existsByClientId(itemRequest.clientId())) {
      throw new AlreadyExistingItemClientIdException(
          "Item with client id " + itemRequest.clientId() + " already exists");
    }

    ItemType itemType = ItemType.fromString(itemRequest.itemType());
    ItemRarity itemRarity = ItemRarity.fromString(itemRequest.itemRarity());

    Item item =
        Item.builder()
            .gameSaveId(gameSaveId)
            .clientId(itemRequest.clientId())
            .blueprintId(itemRequest.blueprintId())
            .itemType(itemType)
            .itemRarity(itemRarity)
            .isEquipped(itemRequest.isEquipped())
            .level(itemRequest.level())
            .mainStat(itemRequest.mainStat())
            .additionalStats(itemRequest.additionalStats())
            .build();

    return inventoryRepositoryPort.createItem(gameSaveId, item);
  }

  @Override
  @Transactional
  public void deleteItemFromInventory(UUID gameSaveId, String itemClientId)
      throws NotFoundException, ForbiddenException {
    if (!gameMetadataService.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    Optional<Item> optionalItem = inventoryRepositoryPort.findItemByClientId(itemClientId);

    if (optionalItem.isEmpty()) {
      throw new NotFoundException("Item not found for item client id " + itemClientId);
    }

    inventoryRepositoryPort.deleteItemByClientId(itemClientId);
  }

  @Override
  @Transactional
  public Item updateItemInInventory(UUID gameSaveId, String itemClientId, ItemRequest itemRequest)
      throws NotFoundException, ForbiddenException {
    if (!gameMetadataService.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    Optional<Item> optionalItem = inventoryRepositoryPort.findItemByClientId(itemClientId);
    if (optionalItem.isEmpty()) {
      throw new NotFoundException("Item not found for item client id " + itemClientId);
    }

    Item existingItem = optionalItem.get();

    ItemType itemType = ItemType.fromString(itemRequest.itemType());
    ItemRarity itemRarity = ItemRarity.fromString(itemRequest.itemRarity());

    Item updatedItem =
        Item.builder()
            .id(existingItem.getId())
            .gameSaveId(gameSaveId)
            .clientId(itemClientId)
            .blueprintId(itemRequest.blueprintId())
            .itemType(itemType)
            .itemRarity(itemRarity)
            .isEquipped(itemRequest.isEquipped())
            .level(itemRequest.level())
            .mainStat(itemRequest.mainStat())
            .additionalStats(itemRequest.additionalStats())
            .build();

    return inventoryRepositoryPort.updateItem(gameSaveId, updatedItem);
  }

  @Override
  @Transactional
  public void clearInventory(UUID gameSaveId) throws NotFoundException {
    if (!gameMetadataService.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    inventoryRepositoryPort.deleteAllItemsByGameSaveId(gameSaveId);
  }
}
