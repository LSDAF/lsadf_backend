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
package com.lsadf.core.services.impl;

import com.lsadf.core.constants.item.ItemRarity;
import com.lsadf.core.constants.item.ItemType;
import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.entities.ItemEntity;
import com.lsadf.core.exceptions.AlreadyExistingItemClientIdException;
import com.lsadf.core.exceptions.http.ForbiddenException;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.repositories.InventoryRepository;
import com.lsadf.core.repositories.ItemRepository;
import com.lsadf.core.requests.item.ItemRequest;
import com.lsadf.core.services.InventoryService;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;

  public InventoryServiceImpl(
      InventoryRepository inventoryRepository, ItemRepository itemRepository) {
    this.inventoryRepository = inventoryRepository;
    this.itemRepository = itemRepository;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public InventoryEntity getInventory(String gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }

    return getInventoryEntity(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public ItemEntity createItemInInventory(String gameSaveId, ItemRequest itemRequest)
      throws NotFoundException, AlreadyExistingItemClientIdException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }
    if (itemRepository.findItemEntityByClientId(itemRequest.getClientId()).isPresent()) {
      throw new AlreadyExistingItemClientIdException(
          "Game save with id " + itemRequest.getClientId() + " already exists");
    }

    Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

    if (optionalInventoryEntity.isEmpty()) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    InventoryEntity inventoryEntity = optionalInventoryEntity.get();

    ItemEntity itemEntity =
        ItemEntity.builder()
            .inventoryEntity(inventoryEntity)
            .clientId(itemRequest.getClientId())
            .itemType(ItemType.fromString(itemRequest.getItemType()))
            .blueprintId(itemRequest.getBlueprintId())
            .itemRarity(ItemRarity.fromString(itemRequest.getItemRarity()))
            .isEquipped(itemRequest.getIsEquipped())
            .level(itemRequest.getLevel())
            .mainStat(itemRequest.getMainStat())
            .additionalStats(itemRequest.getAdditionalStats())
            .build();

    ItemEntity saved = itemRepository.save(itemEntity);

    inventoryEntity.getItems().add(saved);

    inventoryRepository.save(inventoryEntity);

    return saved;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void deleteItemFromInventory(String gameSaveId, String itemClientId)
      throws NotFoundException, ForbiddenException {
    if (gameSaveId == null || itemClientId == null) {
      throw new IllegalArgumentException("Game save id and item client id cannot be null");
    }

    Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

    if (optionalInventoryEntity.isEmpty()) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    InventoryEntity inventoryEntity = optionalInventoryEntity.get();

    Optional<ItemEntity> optionalItemEntity = itemRepository.findItemEntityByClientId(itemClientId);

    if (optionalItemEntity.isEmpty()) {
      throw new NotFoundException("Item not found for item client id " + itemClientId);
    }

    if (!optionalItemEntity.get().getInventoryEntity().getGameSave().getId().equals(gameSaveId)) {
      throw new ForbiddenException(
          "The given game save id is not the owner of item with the given client id");
    }

    ItemEntity itemEntity = optionalItemEntity.get();

    inventoryEntity.getItems().remove(itemEntity);

    inventoryRepository.save(inventoryEntity);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public ItemEntity updateItemInInventory(
      String gameSaveId, String itemClientId, ItemRequest itemRequest)
      throws NotFoundException, ForbiddenException {
    if (gameSaveId == null || itemClientId == null || itemRequest == null) {
      throw new IllegalArgumentException(
          "Game save id, item client id and item request cannot be null");
    }

    Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

    if (optionalInventoryEntity.isEmpty()) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    Optional<ItemEntity> optionalItemEntity = itemRepository.findItemEntityByClientId(itemClientId);

    if (optionalItemEntity.isEmpty()) {
      throw new NotFoundException("Item not found for item client id " + itemClientId);
    }

    if (!optionalItemEntity.get().getInventoryEntity().getGameSave().getId().equals(gameSaveId)) {
      throw new ForbiddenException(
          "The given game save id does not own the item with the given client id");
    }

    ItemEntity itemEntity = optionalItemEntity.get();

    itemEntity.setItemType(ItemType.fromString(itemRequest.getItemType()));
    itemEntity.setBlueprintId(itemRequest.getBlueprintId());
    itemEntity.setItemRarity(ItemRarity.fromString(itemRequest.getItemRarity()));
    itemEntity.setIsEquipped(itemRequest.getIsEquipped());
    itemEntity.setLevel(itemRequest.getLevel());
    itemEntity.setMainStat(itemRequest.getMainStat());
    itemEntity.setAdditionalStats(itemRequest.getAdditionalStats());

    itemRepository.save(itemEntity);

    return itemEntity;
  }

  /** {@inheritDoc} */
  private InventoryEntity getInventoryEntity(String gameSaveId) throws NotFoundException {
    return inventoryRepository
        .findById(gameSaveId)
        .orElseThrow(
            () -> new NotFoundException("Inventory not found for game save id " + gameSaveId));
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void clearInventory(String gameSaveId) throws NotFoundException {
    InventoryEntity inventoryEntity = getInventoryEntity(gameSaveId);
    inventoryEntity.getItems().clear();
    inventoryRepository.save(inventoryEntity);
  }
}
