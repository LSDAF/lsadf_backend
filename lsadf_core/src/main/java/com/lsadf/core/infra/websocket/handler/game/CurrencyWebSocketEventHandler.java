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

import static com.lsadf.core.infra.websocket.event.WebSocketEventType.CURRENCY_UPDATE;

import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.command.UpdateCacheCurrencyCommand;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.system.AckWebSocketEvent;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.EventType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class CurrencyWebSocketEventHandler implements WebSocketEventHandler {

  private final CurrencyCommandService currencyCommandService;
  private final ObjectMapper objectMapper;
  private final GameSessionQueryService gameSessionQueryService;

  @Override
  public void handleEvent(WebSocketSession session, WebSocketEvent event) throws Exception {
    var payload = event.getData();
    var gameSession = gameSessionQueryService.findGameSessionById(event.getSessionId());
    var gameSaveId = gameSession.getGameSaveId();

    var gold = payload.get("gold").asLong();
    var diamond = payload.get("diamond").asLong();
    var emerald = payload.get("emerald").asLong();
    var amethyst = payload.get("amethyst").asLong();

    UpdateCacheCurrencyCommand command =
        new UpdateCacheCurrencyCommand(gameSaveId, gold, diamond, emerald, amethyst);

    log.info("Handling currency update for gameSaveId: {}", gameSaveId);
    currencyCommandService.updateCacheCurrency(command);

    sendAck(session, event);
  }

  @Override
  public EventType getEventType() {
    return CURRENCY_UPDATE;
  }

  private void sendAck(WebSocketSession session, WebSocketEvent event) throws Exception {
    AckWebSocketEvent ackEvent =
        new AckWebSocketEvent(
            event.getSessionId(),
            UUID.randomUUID(),
            event.getUserId(),
            event.getMessageId(),
            objectMapper.valueToTree(event));

    String ackPayload = objectMapper.writeValueAsString(ackEvent);
    session.sendMessage(new TextMessage(ackPayload));

    log.info(
        "Sent ACK for eventType: {} with eventId: {}", event.getEventType(), event.getMessageId());
  }
}
