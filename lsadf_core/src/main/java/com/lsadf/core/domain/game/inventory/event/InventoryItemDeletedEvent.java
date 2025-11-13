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

package com.lsadf.core.domain.game.inventory.event;

import static com.lsadf.core.domain.game.inventory.event.InventoryEventType.INVENTORY_ITEM_DELETED;

import com.lsadf.core.shared.event.AEvent;
import com.lsadf.core.shared.event.Event;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class InventoryItemDeletedEvent extends AEvent implements Event {
  private final UUID gameSaveId;
  private final String userId;
  private final String itemId;

  public InventoryItemDeletedEvent(UUID gameSaveId, String userId, String itemId) {
    super(INVENTORY_ITEM_DELETED);
    this.gameSaveId = gameSaveId;
    this.userId = userId;
    this.itemId = itemId;
  }
}
