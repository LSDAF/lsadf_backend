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

import java.util.List;

/**
 * Contract for item creation and update operations in the inventory service. This interface
 * abstracts the item request data from infrastructure concerns while maintaining type safety and
 * extensibility.
 */
public interface ItemCommand {

  /**
   * @return the client-generated unique identifier for the item
   */
  String getClientId();

  /**
   * @return the item type as string (e.g., "boots", "weapon")
   */
  String getItemType();

  /**
   * @return the blueprint identifier for the item
   */
  String getBlueprintId();

  /**
   * @return the item rarity as string (e.g., "LEGENDARY", "COMMON")
   */
  String getItemRarity();

  /**
   * @return whether the item is currently equipped
   */
  Boolean getIsEquipped();

  /**
   * @return the item level
   */
  Integer getLevel();

  /**
   * @return the main stat of the item
   */
  ItemStatCommand getMainStat();

  /**
   * @return the list of additional stats for the item
   */
  List<ItemStatCommand> getAdditionalStats();
}
