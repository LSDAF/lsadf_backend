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
package com.lsadf.core.infra.persistence.adapter.game.inventory;

import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.infra.persistence.impl.game.inventory.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InventoryRepositoryAdapter implements InventoryRepositoryPort {

  private final ItemRepository itemRepository;
  private final AdditionalItemStatsRepository additionalItemStatsRepository;
  private static final ItemEntityMapper itemEntityMapper = ItemEntityMapper.INSTANCE;

  public InventoryRepositoryAdapter(
      ItemRepository itemRepository, AdditionalItemStatsRepository additionalItemStatsRepository) {
    this.itemRepository = itemRepository;
    this.additionalItemStatsRepository = additionalItemStatsRepository;
  }

  @Override
  public Set<Item> findAllItemsByGameSaveId(UUID gameSaveId) {
    var itemEntities =
        itemRepository.findAllItemsByGameSaveId(gameSaveId).stream()
            .map(itemEntityMapper::map)
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

  @Override
  public Optional<Item> findItemByClientId(String clientId) {
    return itemRepository
        .findItemByClientId(clientId)
        .map(
            itemEntity -> {
              Item item = itemEntityMapper.map(itemEntity);
              // Load additional stats
              try (Stream<AdditionalItemStatEntity> additionalStats =
                  additionalItemStatsRepository.findAllAdditionalItemStatsByGameSaveIds(
                      Collections.singletonList(itemEntity.getId()))) {
                additionalStats.forEach(
                    stat ->
                        item.getAdditionalStats()
                            .add(AdditionalItemStatEntityMapper.INSTANCE.map(stat)));
              }
              return item;
            });
  }

  @Override
  public Item createItem(UUID gameSaveId, Item item) {
    ItemEntity newItemEntity =
        itemRepository.createNewItemEntity(
            item.getGameSaveId(),
            item.getClientId(),
            item.getBlueprintId(),
            item.getItemType(),
            item.getItemRarity(),
            item.getIsEquipped(),
            item.getLevel(),
            item.getMainStat().getStatistic(),
            item.getMainStat().getBaseValue());

    Set<ItemStat> additionalStats =
        item.getAdditionalStats().stream()
            .map(
                itemStat ->
                    additionalItemStatsRepository.createNewAdditionalItemStatEntity(
                        newItemEntity.getId(), itemStat.getStatistic(), itemStat.getBaseValue()))
            .map(AdditionalItemStatEntityMapper.INSTANCE::map)
            .collect(Collectors.toSet());

    Item createdItem = itemEntityMapper.map(newItemEntity);
    createdItem.getAdditionalStats().addAll(additionalStats);
    return createdItem;
  }

  @Override
  public Item updateItem(UUID gameSaveId, Item item) {
    Optional<ItemEntity> optionalItemEntity = itemRepository.findItemByClientId(item.getClientId());
    if (optionalItemEntity.isEmpty()) {
      throw new IllegalArgumentException("Item not found for client id: " + item.getClientId());
    }

    ItemEntity itemEntity = optionalItemEntity.get();

    // Delete existing additional stats
    additionalItemStatsRepository.deleteAllAdditionalItemStatsByItemId(itemEntity.getId());

    // Update item properties
    itemEntity.setLevel(item.getLevel());
    itemEntity.setMainStatistic(item.getMainStat().getStatistic());
    itemEntity.setMainBaseValue(item.getMainStat().getBaseValue());
    itemEntity.setIsEquipped(item.getIsEquipped());
    itemEntity.setItemType(item.getItemType());
    itemEntity.setBlueprintId(item.getBlueprintId());
    itemEntity.setItemRarity(item.getItemRarity());

    ItemEntity updatedEntity = itemRepository.save(itemEntity);

    // Create new additional stats
    List<ItemStat> additionalStats = new ArrayList<>();
    for (ItemStat additionalStat : item.getAdditionalStats()) {
      var additionalItemStatEntity =
          additionalItemStatsRepository.createNewAdditionalItemStatEntity(
              updatedEntity.getId(), additionalStat.getStatistic(), additionalStat.getBaseValue());
      additionalStats.add(AdditionalItemStatEntityMapper.INSTANCE.map(additionalItemStatEntity));
    }

    Item updatedItem = itemEntityMapper.map(updatedEntity);
    updatedItem.getAdditionalStats().addAll(additionalStats);
    return updatedItem;
  }

  @Override
  public void deleteItemByClientId(String clientId) {
    Optional<ItemEntity> optionalItemEntity = itemRepository.findItemByClientId(clientId);
    if (optionalItemEntity.isPresent()) {
      ItemEntity itemEntity = optionalItemEntity.get();
      additionalItemStatsRepository.deleteAllAdditionalItemStatsByItemId(itemEntity.getId());
      itemRepository.delete(itemEntity);
    }
  }

  @Override
  public void deleteAllItemsByGameSaveId(UUID gameSaveId) {
    itemRepository.deleteAllItemsByGameSaveId(gameSaveId);
  }

  @Override
  public boolean existsByClientId(String clientId) {
    return itemRepository.findItemByClientId(clientId).isPresent();
  }
}
