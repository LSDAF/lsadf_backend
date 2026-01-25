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

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;

/** Step definitions for the when steps in Characteristics WebSocket BDD scenarios */
@Slf4j(topic = "[CHARACTERISTICS WEBSOCKET WHEN STEP DEFINITIONS]")
public class BddCharacteristicsWebSocketWhenStepDefinitions extends BddLoader {

  @When("^the user sends a characteristics update event through WebSocket$")
  public void whenUserSendsCharacteristicsUpdateEvent(DataTable dataTable) {
    try {
      var data = dataTable.asMaps(String.class, String.class);
      assertThat(data).hasSize(1);

      var row = data.getFirst();
      CharacteristicsRequest characteristicsRequest = BddUtils.mapToCharacteristicsRequest(row);

      WebSocketSession session = webSocketSessionStack.peek();

      JsonNode node = objectMapper.valueToTree(characteristicsRequest);
      WebSocketEvent event = new WebSocketEvent(WebSocketEventType.CHARACTERISTICS_UPDATE, node);

      String json = objectMapper.writeValueAsString(event);
      session.sendMessage(new TextMessage(json));

      log.info("Sent characteristics update event: {}", json);
    } catch (Exception e) {
      log.error("Failed to send characteristics update event", e);
      exceptionStack.push(e);
    }
  }
}
