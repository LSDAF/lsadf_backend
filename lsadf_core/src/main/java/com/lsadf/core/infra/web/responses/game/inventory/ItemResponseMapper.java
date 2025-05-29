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

package com.lsadf.core.infra.web.responses.game.inventory;

import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.web.responses.ModelResponseMapper;

/**
 * This class is responsible for mapping {@link Item} domain objects to their corresponding {@link
 * ItemResponse} representations used in external interactions or API responses.
 *
 * <p>It implements the {@link ModelResponseMapper} interface, ensuring a contract for mapping
 * between a model and a response type.
 */
public class ItemResponseMapper implements ModelResponseMapper<Item, ItemResponse> {
  /**
   * Maps an {@link Item} object to its corresponding {@link ItemResponse} representation.
   *
   * @param model the {@link Item} object to be mapped
   * @return the mapped {@link ItemResponse} containing the information from the provided {@link
   *     Item}
   */
  @Override
  public ItemResponse mapToResponse(Item model) {
    return ItemResponse.builder()
        .id(model.getId())
        .additionalStats(model.getAdditionalStats())
        .level(model.getLevel())
        .itemType(model.getItemType())
        .itemRarity(model.getItemRarity())
        .blueprintId(model.getBlueprintId())
        .clientId(model.getClientId())
        .isEquipped(model.getIsEquipped())
        .mainStat(model.getMainStat())
        .build();
  }
}
