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
package com.lsadf.core.unit.infra.websocket.handler.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.infra.websocket.event.WebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.WebSocketEventHandlerRegistry;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
class WebSocketEventHandlerRegistryTests {

  @Mock private WebSocketEventHandler handler1;

  @Mock private WebSocketEventHandler handler2;

  @Mock private WebSocketEventHandler handler3;

  @Mock private WebSocketSession session;

  @Mock private WebSocketEvent event;

  private WebSocketEventHandlerRegistry registry;

  @BeforeEach
  void setUp() {
    when(handler1.getEventType()).thenReturn(WebSocketEventType.CHARACTERISTICS_UPDATE);
    when(handler2.getEventType()).thenReturn(WebSocketEventType.CURRENCY_UPDATE);
    when(handler3.getEventType()).thenReturn(WebSocketEventType.STAGE_UPDATE);

    registry = new WebSocketEventHandlerRegistry(List.of(handler1, handler2, handler3));
  }

  @Test
  void shouldRegisterAllHandlers() {
    assertTrue(registry.hasHandler(WebSocketEventType.CHARACTERISTICS_UPDATE));
    assertTrue(registry.hasHandler(WebSocketEventType.CURRENCY_UPDATE));
    assertTrue(registry.hasHandler(WebSocketEventType.STAGE_UPDATE));
  }

  @Test
  void shouldHandleEventWithCorrectHandler() throws Exception {
    when(event.getEventType()).thenReturn(WebSocketEventType.CHARACTERISTICS_UPDATE);

    registry.handleEvent(session, event);

    verify(handler1).handleEvent(session, event);
    verify(handler2, never()).handleEvent(any(), any());
    verify(handler3, never()).handleEvent(any(), any());
  }

  @Test
  void shouldThrowExceptionWhenNoHandlerFound() {
    when(event.getEventType()).thenReturn(WebSocketEventType.INVENTORY_ITEM_CREATE);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> registry.handleEvent(session, event));

    assertTrue(exception.getMessage().contains("No handler registered"));
  }

  @Test
  void shouldReturnTrueWhenHandlerExists() {
    assertTrue(registry.hasHandler(WebSocketEventType.CHARACTERISTICS_UPDATE));
  }

  @Test
  void shouldReturnFalseWhenHandlerDoesNotExist() {
    assertFalse(registry.hasHandler(WebSocketEventType.INVENTORY_ITEM_CREATE));
  }

  @Test
  void shouldHandleEmptyHandlerList() {
    WebSocketEventHandlerRegistry emptyRegistry = new WebSocketEventHandlerRegistry(List.of());

    assertFalse(emptyRegistry.hasHandler(WebSocketEventType.CHARACTERISTICS_UPDATE));
  }
}
