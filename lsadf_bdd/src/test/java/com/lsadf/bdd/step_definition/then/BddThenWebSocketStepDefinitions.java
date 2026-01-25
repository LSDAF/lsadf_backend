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
package com.lsadf.bdd.step_definition.then;

import static com.lsadf.core.infra.web.JsonAttributes.EVENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Slf4j(topic = "[WEBSOCKET THEN STEP DEFINITIONS]")
@Component
public class BddThenWebSocketStepDefinitions {

  @Autowired protected List<JsonNode> webSocketReceivedMessages;
  @Autowired protected Stack<Exception> exceptionStack;

  public void thenWebSocketShouldReceiveAck() {
    await()
        .atMost(10, TimeUnit.SECONDS)
        .untilAsserted(
            () -> {
              assertThat(webSocketReceivedMessages)
                  .isNotEmpty()
                  .anySatisfy(
                      message -> {
                        String eventType = message.get(EVENT_TYPE).asString();
                        assertThat(eventType).isEqualTo(WebSocketEventType.ACK.name());
                      });
            });

    log.info("Verified ACK message received");
  }

  public void thenAnExceptionShouldBeThrown(String exceptionName) {
    var exception = exceptionStack.peek();
    assertThat(exception).isNotNull();
    assertThat(exception.getClass().getSimpleName()).isEqualTo(exceptionName);
  }

  public void thenWebSocketShouldReceiveError() {

    await()
        .atMost(10, TimeUnit.SECONDS)
        .untilAsserted(
            () -> {
              assertThat(webSocketReceivedMessages)
                  .isNotEmpty()
                  .anySatisfy(
                      message -> {
                        String eventType = message.get(EVENT_TYPE).asString();
                        assertThat(eventType).isEqualTo(WebSocketEventType.ERROR.name());
                      });
            });

    log.info("Verified ERROR message received");
  }

  public void thenWebSocketShouldHaveReceivedAckMessages(int expectedCount) {
    await()
        .atMost(10, TimeUnit.SECONDS)
        .untilAsserted(
            () -> {
              long ackCount =
                  webSocketReceivedMessages.stream()
                      .filter(
                          message -> {
                            String eventType = message.get(EVENT_TYPE).asString();
                            return WebSocketEventType.ACK.name().equals(eventType);
                          })
                      .count();

              assertThat(ackCount).isEqualTo(expectedCount);
            });

    log.info("Verified {} ACK messages received", expectedCount);
  }
}
