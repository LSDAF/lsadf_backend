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
package com.lsadf.application.bdd.step_definition.when;

import static com.lsadf.core.infra.web.JsonAttributes.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import tools.jackson.databind.JsonNode;

/** Step definitions for the when steps in WebSocket BDD scenarios */
@Slf4j(topic = "[CURRENCY WEBSOCKET WHEN STEP DEFINITIONS]")
public class BddCurrencyWebSocketWhenStepDefinitions extends BddLoader {

  @When("^the user connects to the WebSocket endpoint with session id (.*)$")
  public void whenUserConnectsToWebSocket(String sessionId) {
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();

      String wsUrl =
          String.format(
              "ws://localhost:%d/ws/game?token=%s&game_session_id=%s",
              this.serverPort, token, sessionId);
      URI uri = new URI(wsUrl);

      StandardWebSocketClient client = new StandardWebSocketClient();
      WebSocketSession session =
          client.execute(webSocketHandler, null, uri).get(5, TimeUnit.SECONDS);

      webSocketSessionStack.push(session);
      log.info("WebSocket connection established: {}", session.getId());
    } catch (Exception e) {
      log.error("Failed to connect to WebSocket", e);
      exceptionStack.push(e);
    }
  }

  @When("^the user sends a currency update event through WebSocket$")
  public void whenUserSendsCurrencyUpdateEventWithUserId(DataTable dataTable) {
    try {
      var data = dataTable.asMaps(String.class, String.class);
      assertThat(data).hasSize(1);

      var row = data.get(0);
      CurrencyRequest currencyRequest = BddUtils.mapToCurrencyRequest(row);

      WebSocketSession session = webSocketSessionStack.peek();

      JsonNode node = objectMapper.valueToTree(currencyRequest);
      WebSocketEvent event = new WebSocketEvent(WebSocketEventType.CURRENCY_UPDATE, node);

      String json = objectMapper.writeValueAsString(event);
      session.sendMessage(new TextMessage(json));

      log.info("Sent currency update event: {}", json);
    } catch (Exception e) {
      log.error("Failed to send currency update event", e);
      exceptionStack.push(e);
    }
  }
}
