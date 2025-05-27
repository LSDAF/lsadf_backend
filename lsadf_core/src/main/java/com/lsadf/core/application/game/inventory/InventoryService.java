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

import com.lsadf.core.infra.exceptions.AlreadyExistingItemClientIdException;
import com.lsadf.core.infra.exceptions.http.ForbiddenException;
import com.lsadf.core.infra.exceptions.http.NotFoundException;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemEntity;
import com.lsadf.core.infra.web.requests.game.inventory.item.ItemRequest;

public interface InventoryService {

  /**
   * Get the inventory of a game save
   *
   * @param gameSaveId the id of the game save
   * @return the inventory entity
   */
  InventoryEntity getInventory(String gameSaveId);

  /**
   * Create an item in the inventory of a game save
   *
   * @param gameSaveId the game save id
   * @param itemRequest the item to add
   * @throws NotFoundException
   */
  ItemEntity createItemInInventory(String gameSaveId, ItemRequest itemRequest)
      throws NotFoundException, AlreadyExistingItemClientIdException;

  /**
   * Remove an item from the inventory of a game save
   *
   * @param gameSaveId the game save id
   * @param itemClientId the item to remove
   * @throws NotFoundException, ForbiddenException
   */
  void deleteItemFromInventory(String gameSaveId, String itemClientId)
      throws NotFoundException, ForbiddenException;

  /**
   * Update an item in the inventory of a game save
   *
   * @param gameSaveId the game save id
   * @param itemClientId the item to update
   * @param itemRequest the item to update
   * @throws NotFoundException, ForbiddenException
   */
  ItemEntity updateItemInInventory(String gameSaveId, String itemClientId, ItemRequest itemRequest)
      throws NotFoundException, ForbiddenException;

  /**
   * Clears all items in the inventory for the specified game save.
   *
   * @param gameSaveId the ID of the game save whose inventory will be cleared
   */
  void clearInventory(String gameSaveId) throws NotFoundException;
}
