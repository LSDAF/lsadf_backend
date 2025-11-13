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

package com.lsadf.core.infra.event.listener.game.inventory;

import com.lsadf.core.application.game.inventory.InventoryEventListenerPort;
import com.lsadf.core.application.game.inventory.InventoryService;
import com.lsadf.core.domain.game.inventory.event.InventoryItemDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class InventoryEventListener implements InventoryEventListenerPort {

  private final InventoryService inventoryService;

  @EventListener
  @Override
  public void onItemDeleted(InventoryItemDeletedEvent event) {
    log.debug("{}: {}", event.getEventType(), event);
    inventoryService.deleteItemFromInventory(event.getGameSaveId(), event.getItemId());
  }
}
