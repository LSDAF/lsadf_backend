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

package com.lsadf.core.infra.persistence.mappers.game.inventory;

import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemEntity;
import com.lsadf.core.infra.persistence.mappers.EntityModelMapper;

public class ItemEntityModelMapper implements EntityModelMapper<ItemEntity, Item> {

  @Override
  public Item mapToModel(ItemEntity itemEntity) {
    return Item.builder()
        .id(itemEntity.getId())
        .clientId(itemEntity.getClientId())
        .blueprintId(itemEntity.getBlueprintId())
        .itemType(itemEntity.getItemType())
        .itemRarity(itemEntity.getItemRarity())
        .isEquipped(itemEntity.getIsEquipped())
        .level(itemEntity.getLevel())
        .mainStat(itemEntity.getMainStat())
        .additionalStats(itemEntity.getAdditionalStats())
        .build();
  }
}
