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

package com.lsadf.core.infra.websocket.handler.game.save;

import static com.lsadf.core.infra.web.JsonAttributes.*;
import static com.lsadf.core.infra.websocket.event.WebSocketEventType.CHARACTERISTICS_UPDATE;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.websocket.event.EventRequestValidator;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.event.system.AckWebSocketEvent;
import com.lsadf.core.infra.websocket.event.system.ErrorWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.EventType;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class CharacteristicsWebSocketEventHandler implements WebSocketEventHandler {

  private final CharacteristicsCommandService characteristicsCommandService;
  private final ObjectMapper objectMapper;
  private final WebSocketEventFactory eventFactory;
  private final CacheManager cacheManager;
  private final CharacteristicsEventPublisherPort characteristicsEventPublisherPort;
  private final EventRequestValidator requestValidator;

  private static final CharacteristicsRequestMapper requestModelMapper =
      CharacteristicsRequestMapper.INSTANCE;

  @Override
  public void handleEvent(WebSocketSession session, WebSocketEvent event) throws Exception {
    Map<String, Object> attributes = session.getAttributes();
    UUID gameSaveId = (UUID) attributes.get(GAME_SAVE_ID);
    UUID gameSessionID = (UUID) attributes.get(GAME_SESSION_ID);
    String userEmail = attributes.get(USER_EMAIL).toString();

    try {
      JsonNode data = event.getData();
      if (data == null || data.isEmpty()) {
        throw new IllegalArgumentException("Missing required characteristics fields");
      }
      CharacteristicsRequest payload = objectMapper.treeToValue(data, CharacteristicsRequest.class);
      requestValidator.validate(payload);
      Characteristics characteristics = requestModelMapper.map(payload);
      log.info("Handling characteristics update, payload: {}", payload);
      updateCharacteristics(userEmail, gameSaveId, gameSessionID, characteristics);

      sendAck(session, event);
    } catch (Exception e) {
      log.error("Error processing characteristics update", e);
      ErrorWebSocketEvent errorEvent =
          eventFactory.createErrorEvent(
              event, "Error while updating characteristics: " + e.getMessage());
      TextMessage textMessage = new TextMessage(objectMapper.writeValueAsBytes(errorEvent));
      session.sendMessage(textMessage);
      throw e;
    }
  }

  @Override
  public EventType getEventType() {
    return CHARACTERISTICS_UPDATE;
  }

  private void sendAck(WebSocketSession session, WebSocketEvent event) throws Exception {

    AckWebSocketEvent ackEvent = eventFactory.createAckEvent(event);

    String ackPayload = objectMapper.writeValueAsString(ackEvent);
    session.sendMessage(new TextMessage(ackPayload));

    log.info(
        "Sent ACK for eventType: {} with eventId: {}", event.getEventType(), event.getMessageId());
  }

  private void updateCharacteristics(
      String userEmail, UUID gameSaveId, UUID gameSessionId, Characteristics characteristics) {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      characteristicsEventPublisherPort.publishCharacteristicsUpdatedEvent(
          userEmail, gameSaveId, characteristics, gameSessionId);
    } else {
      var command = PersistCharacteristicsCommand.fromCharacteristics(gameSaveId, characteristics);
      characteristicsCommandService.persistCharacteristics(command);
    }
  }
}
