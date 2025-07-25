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
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;
  private static final ItemEntityMapper itemEntityMapper = ItemEntityMapper.INSTANCE;

  public InventoryServiceImpl(
      InventoryRepository inventoryRepository, ItemRepository itemRepository) {
    this.inventoryRepository = inventoryRepository;
    this.itemRepository = itemRepository;
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
    return itemEntities.stream().map(itemEntityMapper::map).collect(Collectors.toSet());
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

    if (itemRepository.findItemEntityByClientId(itemRequest.clientId()).isPresent()) {
      throw new AlreadyExistingItemClientIdException(
          "Game save with id " + itemRequest.clientId() + " already exists");
    }

    InventoryEntity inventoryEntity = optionalInventoryEntity.get();

    Set<ItemEntity> items = inventoryEntity.getItems();

    if (items.stream().anyMatch(i -> i.getClientId().equals(itemRequest.clientId()))) {
      throw new AlreadyExistingItemClientIdException(
          "Game save with id " + itemRequest.clientId() + " already exists");
    }

    ItemEntity itemEntity =
        ItemEntity.builder()
            .inventoryEntity(inventoryEntity)
            .clientId(itemRequest.clientId())
            .itemType(ItemType.fromString(itemRequest.itemType()))
            .blueprintId(itemRequest.blueprintId())
            .itemRarity(ItemRarity.fromString(itemRequest.itemRarity()))
            .isEquipped(itemRequest.isEquipped())
            .level(itemRequest.level())
            .mainStat(itemRequest.mainStat())
            .additionalStats(itemRequest.additionalStats())
            .build();

    inventoryEntity.getItems().add(itemEntity);

    var updated = inventoryRepository.save(inventoryEntity);
    var item =
        updated.getItems().stream()
            .filter(i -> i.getClientId().equals(itemRequest.clientId()))
            .findFirst()
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "Item not found for item client id " + itemRequest.clientId()));
    return itemEntityMapper.map(item);
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

    toUpdate.setLevel(itemRequest.level());
    toUpdate.setMainStat(itemRequest.mainStat());
    toUpdate.setAdditionalStats(itemRequest.additionalStats());
    toUpdate.setIsEquipped(itemRequest.isEquipped());
    toUpdate.setItemType(ItemType.fromString(itemRequest.itemType()));
    toUpdate.setBlueprintId(itemRequest.blueprintId());
    toUpdate.setItemRarity(ItemRarity.fromString(itemRequest.itemRarity()));

    InventoryEntity updated = inventoryRepository.save(inventoryEntity);
    ItemEntity updatedItemEntity =
        updated.getItems().stream()
            .filter(item -> item.getClientId().equals(itemClientId))
            .findFirst()
            .orElseThrow(
                () -> new NotFoundException("Item not found for item client id " + itemClientId));
    return itemEntityMapper.map(updatedItemEntity);
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
