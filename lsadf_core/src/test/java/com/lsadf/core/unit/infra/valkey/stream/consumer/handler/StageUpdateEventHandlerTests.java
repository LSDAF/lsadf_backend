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

package com.lsadf.core.unit.infra.valkey.stream.consumer.handler;

import static com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveEventType.STAGE_UPDATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.command.UpdateCacheStageCommand;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.StageUpdateEventHandler;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.shared.event.EventType;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class StageUpdateEventHandlerTests {

  @Mock private StageCommandService stageService;

  @Mock private ObjectMapper objectMapper;

  private StageUpdateEventHandler handler;
  private UUID gameSaveId;
  private Map<String, String> payload;
  private ValkeyGameSaveUpdatedEvent event;
  private Stage stage;

  private Long currentStage = 5L;
  private Long maxStage = 75L;
  private Long wave = 8L;

  @BeforeEach
  void setUp() {
    handler = new StageUpdateEventHandler(stageService, objectMapper);
    gameSaveId = UUID.randomUUID();
    payload = Map.of("currentStage", "5", "maxStage", "75");
    stage = new Stage(currentStage, maxStage, wave);

    event = new ValkeyGameSaveUpdatedEvent(STAGE_UPDATED, gameSaveId, "user123", null, payload);
  }

  @Test
  void getEventTypeReturnsStageUpdate() {
    EventType eventType = handler.getEventType();
    assertEquals(STAGE_UPDATED, eventType);
  }

  @Test
  void handleEventCallsStageServiceWithCorrectParameters() throws Exception {
    when(objectMapper.convertValue(payload, Stage.class)).thenReturn(stage);
    handler.handleEvent(event);

    verify(objectMapper).convertValue(payload, Stage.class);
    UpdateCacheStageCommand command = UpdateCacheStageCommand.fromStage(gameSaveId, stage);
    verify(stageService).updateCacheStage(command);
  }
}
