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
import com.lsadf.core.infra.exception.AlreadyExistingItemClientIdException;
import com.lsadf.core.infra.exception.http.ForbiddenException;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import java.util.Set;

public interface InventoryService {

  /**
   * Retrieves the inventory for the specified game save.
   *
   * @param gameSaveId the ID of the game save for which the inventory is to be retrieved
   * @return a set of items representing the inventory of the specified game save
   */
  Set<Item> getInventoryItems(String gameSaveId);

  /**
   * Create an item in the inventory of a game save
   *
   * @param gameSaveId the game save id
   * @param itemRequest the item to add
   * @throws NotFoundException
   */
  Item createItemInInventory(String gameSaveId, ItemRequest itemRequest)
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
  Item updateItemInInventory(String gameSaveId, String itemClientId, ItemRequest itemRequest)
      throws NotFoundException, ForbiddenException;

  /**
   * Clears all items in the inventory for the specified game save.
   *
   * @param gameSaveId the ID of the game save whose inventory will be cleared
   */
  void clearInventory(String gameSaveId) throws NotFoundException;
}
