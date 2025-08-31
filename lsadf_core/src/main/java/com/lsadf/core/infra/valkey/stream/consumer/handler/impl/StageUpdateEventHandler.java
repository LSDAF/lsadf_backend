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

import static com.lsadf.core.infra.valkey.stream.event.game.GameSaveEventType.STAGE_UPDATE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.stream.consumer.handler.EventHandler;
import com.lsadf.core.infra.valkey.stream.event.Event;
import com.lsadf.core.infra.valkey.stream.event.EventType;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;

public class StageUpdateEventHandler implements EventHandler {

  private final StageService stageService;
  private final ObjectMapper objectMapper;

  public StageUpdateEventHandler(StageService stageService, ObjectMapper objectMapper) {
    this.stageService = stageService;
    this.objectMapper = objectMapper;
  }

  @Override
  public EventType getEventType() {
    return STAGE_UPDATE;
  }

  @Override
  public void handleEvent(Event event) throws JsonProcessingException {
    GameSaveEvent gameSaveEvent = (GameSaveEvent) event;
    Stage stage = objectMapper.convertValue(gameSaveEvent.payload(), Stage.class);
    stageService.saveStage(gameSaveEvent.gameSaveId(), stage, true);
  }
}
