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
package com.lsadf.core.application.game.inventory;

import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.infra.exception.AlreadyExistingItemClientIdException;
import com.lsadf.core.infra.exception.http.ForbiddenException;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemEntity;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemEntityMapper;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemRepository;
import com.lsadf.core.infra.web.requests.game.inventory.ItemRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;
  private final ItemEntityMapper itemEntityMapper;

  public InventoryServiceImpl(
      InventoryRepository inventoryRepository,
      ItemRepository itemRepository,
      ItemEntityMapper itemEntityMapper) {
    this.inventoryRepository = inventoryRepository;
    this.itemRepository = itemRepository;
    this.itemEntityMapper = itemEntityMapper;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Set<Item> getInventoryItems(String gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }

    InventoryEntity inventoryEntity = getInventoryEntity(gameSaveId);
    Set<ItemEntity> itemEntities = inventoryEntity.getItems();
    return itemEntities.stream()
        .parallel()
        .map(itemEntityMapper::mapToModel)
        .collect(Collectors.toSet());
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public Item createItemInInventory(String gameSaveId, ItemRequest itemRequest)
      throws NotFoundException, AlreadyExistingItemClientIdException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }

    Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);
    if (optionalInventoryEntity.isEmpty()) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    if (itemRepository.findItemEntityByClientId(itemRequest.getClientId()).isPresent()) {
      throw new AlreadyExistingItemClientIdException(
          "Game save with id " + itemRequest.getClientId() + " already exists");
    }

    InventoryEntity inventoryEntity = optionalInventoryEntity.get();

    Set<ItemEntity> items = inventoryEntity.getItems();

    if (items.stream().anyMatch(i -> i.getClientId().equals(itemRequest.getClientId()))) {
      throw new AlreadyExistingItemClientIdException(
          "Game save with id " + itemRequest.getClientId() + " already exists");
    }

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

    inventoryEntity.getItems().add(itemEntity);

    var updated = inventoryRepository.save(inventoryEntity);
    var item =
        updated.getItems().stream()
            .filter(i -> i.getClientId().equals(itemRequest.getClientId()))
            .findFirst()
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "Item not found for item client id " + itemRequest.getClientId()));
    return itemEntityMapper.mapToModel(item);
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
  public Item updateItemInInventory(String gameSaveId, String itemClientId, ItemRequest itemRequest)
      throws NotFoundException, ForbiddenException {
    if (gameSaveId == null || itemClientId == null || itemRequest == null) {
      throw new IllegalArgumentException(
          "Game save id, item client id and item request cannot be null");
    }

    Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

    if (optionalInventoryEntity.isEmpty()) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    InventoryEntity inventoryEntity = optionalInventoryEntity.get();

    Set<ItemEntity> items = inventoryEntity.getItems();
    ItemEntity toUpdate =
        items.stream()
            .filter(item -> item.getClientId().equals(itemClientId))
            .findFirst()
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "Item not found for game save id "
                            + gameSaveId
                            + " and item client id "
                            + itemClientId));

    toUpdate.setLevel(itemRequest.getLevel());
    toUpdate.setMainStat(itemRequest.getMainStat());
    toUpdate.setAdditionalStats(itemRequest.getAdditionalStats());
    toUpdate.setIsEquipped(itemRequest.getIsEquipped());
    toUpdate.setItemType(ItemType.fromString(itemRequest.getItemType()));
    toUpdate.setBlueprintId(itemRequest.getBlueprintId());
    toUpdate.setItemRarity(ItemRarity.fromString(itemRequest.getItemRarity()));

    InventoryEntity updated = inventoryRepository.save(inventoryEntity);
    ItemEntity updatedItemEntity =
        updated.getItems().stream()
            .filter(item -> item.getClientId().equals(itemClientId))
            .findFirst()
            .orElseThrow(
                () -> new NotFoundException("Item not found for item client id " + itemClientId));
    return itemEntityMapper.mapToModel(updatedItemEntity);
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
