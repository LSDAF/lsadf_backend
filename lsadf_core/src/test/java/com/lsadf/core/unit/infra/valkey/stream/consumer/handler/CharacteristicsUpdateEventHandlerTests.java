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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.valkey.stream.consumer.handler.impl.CharacteristicsUpdateEventHandler;
import com.lsadf.core.infra.valkey.stream.event.EventType;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEventType;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CharacteristicsUpdateEventHandlerTests {

  @Mock(strictness = LENIENT)
  private CharacteristicsCommandService characteristicsService;

  @Mock(strictness = LENIENT)
  private ObjectMapper objectMapper;

  private CharacteristicsUpdateEventHandler handler;
  private UUID gameSaveId;
  private Map<String, String> payload;
  private GameSaveEvent event;
  private Characteristics characteristics;

  private static final Long ATTACK = 25L;
  private static final Long HEALTH = 10L;
  private static final Long RESISTANCE = 8L;
  private static final Long CRIT_DAMAGE = 12L;
  private static final Long CRIT_CHANCE = 10L;

  @BeforeEach
  void setUp() {
    handler = new CharacteristicsUpdateEventHandler(characteristicsService, objectMapper);
    gameSaveId = UUID.randomUUID();
    payload =
        Map.of(
            "attack",
            "25",
            "health",
            "10",
            "resistance",
            "8",
            "critDamage",
            "12",
            "critChance",
            "10");
    characteristics =
        Characteristics.builder()
            .attack(ATTACK)
            .health(HEALTH)
            .resistance(RESISTANCE)
            .critDamage(CRIT_DAMAGE)
            .critChance(CRIT_CHANCE)
            .build();

    event =
        GameSaveEvent.builder()
            .gameSaveId(gameSaveId)
            .userId("user123")
            .eventType(GameSaveEventType.CHARACTERISTICS_UPDATE)
            .timestamp(System.currentTimeMillis())
            .payload(payload)
            .build();
    when(objectMapper.convertValue(payload, Characteristics.class)).thenReturn(characteristics);
  }

  @Test
  void getEventTypeReturnsCharacteristicsUpdate() {
    EventType eventType = handler.getEventType();
    assertEquals(GameSaveEventType.CHARACTERISTICS_UPDATE, eventType);
  }

  @Test
  void handleEventCallsCharacteristicsServiceWithCorrectParameters() throws Exception {
    handler.handleEvent(event);

    verify(objectMapper).convertValue(payload, Characteristics.class);
    var command =
        UpdateCacheCharacteristicsCommand.fromCharacteristics(gameSaveId, characteristics);
    verify(characteristicsService).updateCacheCharacteristics(command);
  }
}
