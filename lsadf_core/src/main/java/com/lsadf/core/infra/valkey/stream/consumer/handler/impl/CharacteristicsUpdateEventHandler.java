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

package com.lsadf.core.infra.valkey.stream.consumer.handler.impl;

import static com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveEventType.CHARACTERISTICS_UPDATED;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;

public class CharacteristicsUpdateEventHandler implements EventHandler {

  private final CharacteristicsCommandService characteristicsService;
  private final ObjectMapper objectMapper;

  public CharacteristicsUpdateEventHandler(
      CharacteristicsCommandService characteristicsService, ObjectMapper objectMapper) {
    this.characteristicsService = characteristicsService;
    this.objectMapper = objectMapper;
  }

  @Override
  public EventType getEventType() {
    return CHARACTERISTICS_UPDATED;
  }

  @Override
  public void handleEvent(Event event) throws JsonProcessingException {
    // string to map

    ValkeyGameSaveUpdatedEvent valkeyGameSaveUpdatedEvent = (ValkeyGameSaveUpdatedEvent) event;
    Characteristics characteristics =
        objectMapper.convertValue(valkeyGameSaveUpdatedEvent.getPayload(), Characteristics.class);
    var command =
        UpdateCacheCharacteristicsCommand.fromCharacteristics(
            valkeyGameSaveUpdatedEvent.getGameSaveId(), characteristics);
    characteristicsService.updateCacheCharacteristics(command);
  }
}
