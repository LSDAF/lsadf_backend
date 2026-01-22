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
import com.lsadf.core.infra.websocket.event.game.CurrencyUpdateWebsSocketEvent;
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
public class CurrencyWebSocketEventHandler implements WebSocketEventHandler {

  private final CurrencyCommandService currencyCommandService;
  private final ObjectMapper objectMapper;

  @Override
  public void handleEvent(WebSocketSession session, Event event) throws Exception {
    CurrencyUpdateWebsSocketEvent currencyEvent = (CurrencyUpdateWebsSocketEvent) event;

    log.info("Handling currency update for gameSaveId: {}", currencyEvent.getGameSaveId());

    UpdateCacheCurrencyCommand command =
        new UpdateCacheCurrencyCommand(
            currencyEvent.getGameSaveId(),
            currencyEvent.getPayload().gold(),
            currencyEvent.getPayload().diamond(),
            currencyEvent.getPayload().emerald(),
            currencyEvent.getPayload().amethyst());

    currencyCommandService.updateCacheCurrency(command);

    sendAck(session, currencyEvent);
  }

  @Override
  public EventType getEventType() {
    return CURRENCY_UPDATE;
  }

  private void sendAck(WebSocketSession session, CurrencyUpdateWebsSocketEvent event)
      throws Exception {
    AckWebSocketEvent ackEvent =
        new AckWebSocketEvent(
            event.getSessionId(), UUID.randomUUID(), event.getUserId(), event.getMessageId());

    String ackPayload = objectMapper.writeValueAsString(ackEvent);
    session.sendMessage(new TextMessage(ackPayload));

    log.info(
        "Sent ACK for eventType: {} with eventId: {}", event.getEventType(), event.getMessageId());
  }
}
