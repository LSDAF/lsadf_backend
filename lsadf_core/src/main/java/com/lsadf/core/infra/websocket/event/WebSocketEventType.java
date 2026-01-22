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
package com.lsadf.core.infra.websocket.event;

import com.lsadf.core.shared.event.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebSocketEventType implements EventType {
  STAGE_UPDATE("websocket.stage.update"),
  CURRENCY_UPDATE("websocket.currency.update"),
  CHARACTERISTICS_UPDATE("websocket.characteristics.update"),
  INVENTORY_ITEM_CREATE("websocket.inventory.item.create"),
  INVENTORY_ITEM_UPDATE("websocket.inventory.item.update"),
  INVENTORY_ITEM_DELETE("websocket.inventory.item.delete"),
  ERROR("websocket.error"),
  ACK("websocket.ack");

  private final String value;
}
