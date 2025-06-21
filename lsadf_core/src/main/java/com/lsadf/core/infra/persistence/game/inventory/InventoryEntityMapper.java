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

package com.lsadf.core.infra.persistence.game.inventory;

import com.lsadf.core.domain.game.inventory.Inventory;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemEntity;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemEntityMapper;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for mapping between {@link InventoryEntity} and {@link Inventory}. It
 * implements the {@link EntityModelMapper} interface to provide a method for transforming an {@link
 * InventoryEntity} into its corresponding business model representation, {@link Inventory}.
 *
 * <p>The class uses an instance of {@link ItemEntityMapper} to handle the mapping of {@link
 * ItemEntity} instances contained within the {@link InventoryEntity} to their respective {@link
 * Item} objects.
 */
public class InventoryEntityMapper implements EntityModelMapper<InventoryEntity, Inventory> {

  public InventoryEntityMapper(ItemEntityMapper itemEntityMapper) {
    this.itemEntityMapper = itemEntityMapper;
  }

  private final ItemEntityMapper itemEntityMapper;

  /** {@inheritDoc} */
  @Override
  public Inventory map(InventoryEntity inventoryEntity) {
    Set<Item> items =
        inventoryEntity.getItems().parallelStream()
            .map(itemEntityMapper::map)
            .collect(Collectors.toSet());
    return new Inventory(items);
  }
}
