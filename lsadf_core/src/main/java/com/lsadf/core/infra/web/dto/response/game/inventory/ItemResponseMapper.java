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

package com.lsadf.core.infra.web.dto.response.game.inventory;

import com.lsadf.core.domain.game.inventory.Item;
import com.lsadf.core.domain.game.inventory.ItemStat;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.response.ModelResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * A mapper interface for transforming {@link Item} objects into their corresponding {@link
 * ItemResponse} representations. This interface extends the generic {@code ModelResponseMapper} to
 * provide specific mapping functionality between the domain model {@link Item} and its response
 * counterpart {@link ItemResponse}.
 *
 * <p>The intended use of this mapper is to encapsulate the logic required for converting the
 * internal representation of an {@link Item} into a format suitable for external APIs or other
 * external representations. By leveraging MapStruct annotations and features, the implementation
 * can be generated at compile time, ensuring efficiency and reducing manual mapping efforts.
 *
 * <p>The {@link ItemResponseMapper#INSTANCE} field serves as the singleton instance for accessing
 * the generated implementation of this interface.
 */
@Mapper
public interface ItemResponseMapper extends ModelResponseMapper<Item, ItemResponse> {
  ItemResponseMapper INSTANCE = Mappers.getMapper(ItemResponseMapper.class);

  /**
   * Maps an {@link Item} object to its corresponding {@link ItemResponse} representation.
   *
   * @param model the {@link Item} object to be mapped
   * @return the mapped {@link ItemResponse} containing the information from the provided {@link
   *     Item}
   */
  @Override
  ItemResponse map(Item model);

  default ItemStatDto mapItemStat(ItemStat itemStat) {
    return new ItemStatDto(itemStat.getStatistic(), itemStat.getBaseValue());
  }
}
