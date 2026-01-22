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
package com.lsadf.core.infra.websocket.handler.impl;

import com.lsadf.core.infra.websocket.handler.WebSocketEventHandler;
import com.lsadf.core.shared.event.Event;
import com.lsadf.core.shared.event.EventType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class WebSocketEventHandlerRegistry {
  private final Map<EventType, WebSocketEventHandler> handlers = new HashMap<>();

  public WebSocketEventHandlerRegistry(List<WebSocketEventHandler> handlerList) {
    for (WebSocketEventHandler handler : handlerList) {
      handlers.put(handler.getEventType(), handler);
      log.info("Registered WebSocket event handler for type: {}", handler.getEventType());
    }
  }

  public void handleEvent(WebSocketSession session, Event event) throws Exception {
    EventType eventType = event.getEventType();
    WebSocketEventHandler handler = handlers.get(eventType);

    if (handler == null) {
      throw new IllegalArgumentException("No handler registered for event type: " + eventType);
    }

    log.debug(
        "Handling event type {} with handler {}", eventType, handler.getClass().getSimpleName());
    handler.handleEvent(session, event);
  }

  public boolean hasHandler(EventType eventType) {
    return handlers.containsKey(eventType);
  }
}
