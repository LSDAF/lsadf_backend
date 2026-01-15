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
package com.lsadf.core.infra.valkey.stream.consumer.handler.impl;

import static com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveEventType.STAGE_UPDATED;

import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.command.UpdateCacheStageCommand;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class StageUpdateEventHandler implements EventHandler {

  private final StageCommandService stageService;
  private final ObjectMapper objectMapper;

  public StageUpdateEventHandler(StageCommandService stageService, ObjectMapper objectMapper) {
    this.stageService = stageService;
    this.objectMapper = objectMapper;
  }

  @Override
  public EventType getEventType() {
    return STAGE_UPDATED;
  }

  @Override
  public void handleEvent(Event event) throws JacksonException {
    ValkeyGameSaveUpdatedEvent valkeyGameSaveUpdatedEvent = (ValkeyGameSaveUpdatedEvent) event;
    Stage stage = objectMapper.convertValue(valkeyGameSaveUpdatedEvent.getPayload(), Stage.class);
    UpdateCacheStageCommand command =
        UpdateCacheStageCommand.fromStage(valkeyGameSaveUpdatedEvent.getGameSaveId(), stage);
    stageService.updateCacheStage(command);
  }
}
