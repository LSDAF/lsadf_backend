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
package com.lsadf.core.unit.infra.websocket.handler.game;

import static com.lsadf.core.infra.web.JsonAttributes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.StageEventPublisherPort;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.infra.websocket.event.EventRequestValidator;
import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventFactory;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.handler.game.StageWebSocketEventHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
class StageWebSocketEventHandlerTests {

  @Mock private StageCommandService stageCommandService;

  private final ObjectMapper objectMapper =
      JsonMapper.builder()
          .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy())
          .build();

  @Mock private GameSessionQueryService gameSessionQueryService;

  @Mock private WebSocketSession session;

  @Mock private CacheManager cacheManager;

  @Spy private WebSocketEventFactory eventFactory;

  @Mock private StageEventPublisherPort stageEventPublisherPort;

  @Mock private EventRequestValidator requestValidator;

  private StageWebSocketEventHandler handler;

  private Map<String, Object> sessionAttributes;

  @BeforeEach
  void setUp() {
    handler =
        new StageWebSocketEventHandler(
            stageCommandService,
            objectMapper,
            eventFactory,
            cacheManager,
            stageEventPublisherPort,
            requestValidator);
    sessionAttributes = new HashMap<>();
    sessionAttributes.put(GAME_SESSION_ID, UUID.randomUUID());
    sessionAttributes.put(GAME_SAVE_ID, UUID.randomUUID());
    sessionAttributes.put(USER_EMAIL, "toto@test.com");
  }

  @Test
  void shouldReturnCorrectEventType() {
    assertEquals(WebSocketEventType.STAGE_UPDATE, handler.getEventType());
  }

  @Test
  void shouldHandleStageUpdateEvent() throws Exception {
    StageRequest request = new StageRequest(10L, 20L, 3L);
    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event =
        new WebSocketEvent(WebSocketEventType.STAGE_UPDATE, UUID.randomUUID(), node);
    when(session.getAttributes()).thenReturn(sessionAttributes);
    when(cacheManager.isEnabled()).thenReturn(true);

    handler.handleEvent(session, event);

    ArgumentCaptor<Stage> commandCaptor = ArgumentCaptor.forClass(Stage.class);
    verify(stageEventPublisherPort)
        .publishStageUpdatedEvent(
            eq(sessionAttributes.get(USER_EMAIL).toString()),
            eq((UUID) sessionAttributes.get(GAME_SAVE_ID)),
            commandCaptor.capture(),
            eq((UUID) sessionAttributes.get(GAME_SESSION_ID)));

    Stage stage = commandCaptor.getValue();
    assertEquals(10L, stage.currentStage());
    assertEquals(20L, stage.maxStage());
    assertEquals(3L, stage.wave());

    verify(session).sendMessage(any(TextMessage.class));
  }

  @Test
  void shouldSendAckAfterSuccessfulUpdate() throws Exception {
    StageRequest request = new StageRequest(10L, 20L, 3L);
    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event =
        new WebSocketEvent(WebSocketEventType.STAGE_UPDATE, UUID.randomUUID(), node);
    when(session.getAttributes()).thenReturn(sessionAttributes);
    handler.handleEvent(session, event);

    ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
    verify(session).sendMessage(messageCaptor.capture());

    TextMessage sentMessage = messageCaptor.getValue();
    JsonNode sentNode = objectMapper.readTree(sentMessage.getPayload());
    assertEquals("ACK", sentNode.get(EVENT_TYPE).asString());
  }

  @Test
  void shouldThrowExceptionForInvalidRequest() throws Exception {
    StageRequest request = new StageRequest(-10L, 20L, 3L);
    JsonNode node = objectMapper.valueToTree(request);
    WebSocketEvent event =
        new WebSocketEvent(WebSocketEventType.STAGE_UPDATE, UUID.randomUUID(), node);
    when(session.getAttributes()).thenReturn(sessionAttributes);
    doThrow(new IllegalArgumentException("Validation failed"))
        .when(requestValidator)
        .validate(request);

    Exception exception = assertThrows(Exception.class, () -> handler.handleEvent(session, event));
    assertTrue(exception.getMessage().contains("Validation failed"));
  }

  @Test
  void shouldThrowErrorWhenEmptyRequest() throws Exception {
    JsonNode node = objectMapper.readTree("{}");
    WebSocketEvent event =
        new WebSocketEvent(WebSocketEventType.STAGE_UPDATE, UUID.randomUUID(), node);
    when(session.getAttributes()).thenReturn(sessionAttributes);

    Exception exception = assertThrows(Exception.class, () -> handler.handleEvent(session, event));
    assertTrue(exception.getMessage().contains("Missing required stage fields"));
  }
}
