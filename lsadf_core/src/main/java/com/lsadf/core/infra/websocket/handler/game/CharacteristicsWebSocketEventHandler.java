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
package com.lsadf.core.infra.websocket.handler.game;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.event.game.CharacteristicsUpdateWebSocketEvent;
import com.lsadf.core.infra.websocket.event.system.AckWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class CharacteristicsWebSocketEventHandler implements WebSocketEventHandler {

  private final CharacteristicsCommandService characteristicsCommandService;
  private final ObjectMapper objectMapper;

  @Override
  public void handleEvent(WebSocketSession session, Event event) throws Exception {
    CharacteristicsUpdateWebSocketEvent charEvent = (CharacteristicsUpdateWebSocketEvent) event;

    log.info("Handling characteristics update for gameSaveId: {}", charEvent.getGameSaveId());

    UpdateCacheCharacteristicsCommand command =
        new UpdateCacheCharacteristicsCommand(
            charEvent.getGameSaveId(),
            charEvent.getPayload().attack(),
            charEvent.getPayload().critChance(),
            charEvent.getPayload().critDamage(),
            charEvent.getPayload().health(),
            charEvent.getPayload().resistance());

    characteristicsCommandService.updateCacheCharacteristics(command);

    sendAck(session, charEvent);
  }

  @Override
  public EventType getEventType() {
    return WebSocketEventType.CHARACTERISTICS_UPDATE;
  }

  private void sendAck(WebSocketSession session, CharacteristicsUpdateWebSocketEvent event)
      throws Exception {
    AckWebSocketEvent ack =
        new AckWebSocketEvent(
            event.getSessionId(), UUID.randomUUID(), event.getUserId(), event.getMessageId());

    String json = objectMapper.writeValueAsString(ack);
    session.sendMessage(new TextMessage(json));
  }
}
