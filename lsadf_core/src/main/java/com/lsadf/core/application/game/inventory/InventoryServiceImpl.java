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
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.infra.exception.AlreadyExistingItemClientIdException;
import com.lsadf.core.infra.exception.http.ForbiddenException;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.table.game.inventory.*;
import com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataRepository;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.transaction.annotation.Transactional;

public class InventoryServiceImpl implements InventoryService {

  private final ItemRepository itemRepository;
  private final AdditionalItemStatsRepository additionalItemStatsRepository;
  private final GameMetadataRepository gameMetadataRepository;
  private static final ItemEntityMapper itemEntityMapper = ItemEntityMapper.INSTANCE;

  public InventoryServiceImpl(
      ItemRepository itemRepository,
      GameMetadataRepository gameMetadataRepository,
      AdditionalItemStatsRepository additionalItemStatsRepository) {
    this.itemRepository = itemRepository;
    this.gameMetadataRepository = gameMetadataRepository;
    this.additionalItemStatsRepository = additionalItemStatsRepository;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Set<Item> getInventoryItems(UUID gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }

    if (!gameMetadataRepository.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    var itemEntities =
        itemRepository.findAllItemsByGameSaveId(gameSaveId).stream()
            .map(ItemEntityMapper.INSTANCE::map)
            .collect(Collectors.toMap(Item::getId, item -> item));
    List<UUID> ids = new ArrayList<>(itemEntities.keySet());
    if (ids.isEmpty()) {
      return Collections.emptySet();
    }
    try (Stream<AdditionalItemStatEntity> additionalItemStatEntityStream =
        additionalItemStatsRepository.findAllAdditionalItemStatsByGameSaveIds(ids)) {
      additionalItemStatEntityStream.forEach(
          additionalItemStatEntity -> {
            var item = itemEntities.get(additionalItemStatEntity.getItemId());
            if (item != null) {
              item.getAdditionalStats()
                  .add(AdditionalItemStatEntityMapper.INSTANCE.map(additionalItemStatEntity));
            }
          });
      return new HashSet<>(itemEntities.values());
    }
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public Item createItemInInventory(UUID gameSaveId, ItemRequest itemRequest)
      throws NotFoundException, AlreadyExistingItemClientIdException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }

    if (!gameMetadataRepository.existsById(gameSaveId)) {
      throw new NotFoundException("Game save not found for id " + gameSaveId);
    }

    if (itemRepository.findItemByClientId(itemRequest.clientId()).isPresent()) {
      throw new AlreadyExistingItemClientIdException(
          "Game save with id " + itemRequest.clientId() + " already exists");
    }

    var existingItem = itemRepository.findItemByClientId(itemRequest.clientId());

    if (existingItem.isPresent()) {
      throw new AlreadyExistingItemClientIdException(
          "Game save with id " + itemRequest.clientId() + " already exists");
    }

    ItemType itemType = ItemType.fromString(itemRequest.itemType());
    ItemRarity itemRarity = ItemRarity.fromString(itemRequest.itemRarity());

    ItemEntity newItem =
        itemRepository.createNewItemEntity(
            gameSaveId,
            itemRequest.clientId(),
            itemRequest.blueprintId(),
            itemType,
            itemRarity,
            itemRequest.isEquipped(),
            itemRequest.level(),
            itemRequest.mainStat().getStatistic(),
            itemRequest.mainStat().getBaseValue());

    Set<ItemStat> additionalItems =
        itemRequest.additionalStats().stream()
            .map(
                itemStat ->
                    additionalItemStatsRepository.createNewAdditionalItemStatEntity(
                        newItem.getId(), itemStat.getStatistic(), itemStat.getBaseValue()))
            .map(AdditionalItemStatEntityMapper.INSTANCE::map)
            .collect(Collectors.toSet());

    var item = itemEntityMapper.map(newItem);
    item.getAdditionalStats().addAll(additionalItems);
    return item;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void deleteItemFromInventory(UUID gameSaveId, String itemClientId)
      throws NotFoundException, ForbiddenException {
    if (gameSaveId == null || itemClientId == null) {
      throw new IllegalArgumentException("Game save id and item client id cannot be null");
    }

    if (!gameMetadataRepository.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    Optional<ItemEntity> optionalItemEntity = itemRepository.findItemByClientId(itemClientId);

    if (optionalItemEntity.isEmpty()) {
      throw new NotFoundException("Item not found for item client id " + itemClientId);
    }

    itemRepository.deleteAllItemsByGameSaveId(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public Item updateItemInInventory(UUID gameSaveId, String itemClientId, ItemRequest itemRequest)
      throws NotFoundException, ForbiddenException {
    if (gameSaveId == null || itemClientId == null || itemRequest == null) {
      throw new IllegalArgumentException(
          "Game save id, item client id and item request cannot be null");
    }

    if (!gameMetadataRepository.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    Optional<ItemEntity> optionalItem = itemRepository.findItemByClientId(itemClientId);
    if (optionalItem.isEmpty()) {
      throw new NotFoundException("Item not found for item client id " + itemClientId);
    }
    ItemEntity toUpdate = optionalItem.get();

    additionalItemStatsRepository.deleteAllAdditionalItemStatsByItemId(toUpdate.getId());

    List<ItemStat> additionalItems = new ArrayList<>();
    for (var additionalItem : itemRequest.additionalStats()) {
      var additionalItemStatEntity =
          additionalItemStatsRepository.createNewAdditionalItemStatEntity(
              toUpdate.getId(), additionalItem.getStatistic(), additionalItem.getBaseValue());
      additionalItems.add(AdditionalItemStatEntityMapper.INSTANCE.map(additionalItemStatEntity));
    }

    toUpdate.setLevel(itemRequest.level());
    toUpdate.setMainStatistic(itemRequest.mainStat().getStatistic());
    toUpdate.setMainBaseValue(itemRequest.mainStat().getBaseValue());
    toUpdate.setIsEquipped(itemRequest.isEquipped());
    toUpdate.setItemType(ItemType.fromString(itemRequest.itemType()));
    toUpdate.setBlueprintId(itemRequest.blueprintId());
    toUpdate.setItemRarity(ItemRarity.fromString(itemRequest.itemRarity()));

    var updated = itemRepository.save(toUpdate);
    var updatedItem = itemEntityMapper.map(updated);
    updatedItem.getAdditionalStats().addAll(additionalItems);
    return updatedItem;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void clearInventory(UUID gameSaveId) throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }

    if (!gameMetadataRepository.existsById(gameSaveId)) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    itemRepository.deleteAllItemsByGameSaveId(gameSaveId);
  }
}
