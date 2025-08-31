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

package com.lsadf.core.infra.persistence.impl.game.inventory;

import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * A mapper interface for transforming {@link ItemEntity} objects into {@link Item} objects.
 *
 * <p>This interface extends {@link EntityModelMapper} with {@link ItemEntity} as the source type
 * and {@link Item} as the target type, utilizing MapStruct's mapping capabilities.
 *
 * <p>It defines the mapping logic for converting a persistence-layer representation of an item
 * ({@link ItemEntity}) into a domain-level representation ({@link Item}).
 *
 * <p>This interface utilizes a singleton instance {@code INSTANCE} provided by MapStruct's {@link
 * Mappers#getMapper(Class)} method.
 *
 * <p>Implementers are expected to ensure that all fields specified in {@link ItemEntity} are
 * appropriately translated to {@link Item} in the {@link #map(ItemEntity)} method.
 */
@Mapper
public interface ItemEntityMapper extends EntityModelMapper<ItemEntity, Item> {

  ItemEntityMapper INSTANCE = Mappers.getMapper(ItemEntityMapper.class);

  /**
   * Maps an instance of {@link ItemEntity} to an instance of {@link Item}.
   *
   * @param itemEntity the {@link ItemEntity} to map, must not be null
   * @return an {@link Item} object constructed based on the provided {@link ItemEntity}
   */
  @Override
  @Mapping(
      target = "mainStat",
      expression =
          "java(new com.lsadf.core.domain.game.inventory.item.ItemStat(itemEntity.getMainStatistic(), itemEntity.getMainBaseValue()))")
  @Mapping(target = "additionalStats", expression = "java(new java.util.ArrayList())")
  Item map(ItemEntity itemEntity);

  @Named("mapItemStatFromAdditionalItemStatEntity")
  default ItemStat map(AdditionalItemStatEntity additionalItemStatEntity) {
    return new ItemStat(
        additionalItemStatEntity.getStatistic(), additionalItemStatEntity.getBaseValue());
  }
}
