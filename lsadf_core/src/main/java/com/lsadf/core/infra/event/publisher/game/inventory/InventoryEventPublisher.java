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
package com.lsadf.core.infra.event.publisher.game.inventory;

import com.lsadf.core.application.game.inventory.InventoryEventPublisherPort;
import com.lsadf.core.domain.game.inventory.event.InventoryItemDeletedEvent;
import com.lsadf.core.infra.event.factory.game.inventory.InventoryEventFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class InventoryEventPublisher implements InventoryEventPublisherPort {
  private final ApplicationEventPublisher eventPublisher;
  private final InventoryEventFactory inventoryEventFactory;

  @Override
  public void publishInventoryItemDeletedEvent(String userId, UUID gameSaveId, String itemId) {
    InventoryItemDeletedEvent event =
        inventoryEventFactory.createInventoryItemDeletedEvent(userId, gameSaveId, itemId);
    eventPublisher.publishEvent(event);
  }
}
