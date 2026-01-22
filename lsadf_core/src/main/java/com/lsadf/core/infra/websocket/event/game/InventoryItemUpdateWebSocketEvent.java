/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.websocket.event.game;

import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.websocket.event.AWebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class InventoryItemUpdateWebSocketEvent extends AWebSocketEvent {
  private final UUID gameSaveId;
  private final String itemClientId;
  private final ItemRequest payload;

  public InventoryItemUpdateWebSocketEvent(
      UUID sessionId,
      UUID messageId,
      UUID userId,
      UUID gameSaveId,
      String itemClientId,
      ItemRequest payload) {
    super(WebSocketEventType.INVENTORY_ITEM_UPDATE, sessionId, messageId, userId);
    this.gameSaveId = gameSaveId;
    this.itemClientId = itemClientId;
    this.payload = payload;
  }
}
