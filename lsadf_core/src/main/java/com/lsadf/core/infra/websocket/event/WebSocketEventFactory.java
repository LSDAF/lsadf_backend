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

package com.lsadf.core.infra.websocket.event;

import com.lsadf.core.infra.websocket.event.system.AckWebSocketEvent;
import com.lsadf.core.infra.websocket.event.system.ErrorWebSocketEvent;
import java.util.UUID;

public class WebSocketEventFactory {
  public AckWebSocketEvent createAckEvent(WebSocketEvent event, java.util.UUID messageId) {
    return new AckWebSocketEvent(messageId, event.getMessageId());
  }

  public AckWebSocketEvent createAckEvent(WebSocketEvent event) {
    return createAckEvent(event, UUID.randomUUID());
  }

  public ErrorWebSocketEvent createErrorEvent(WebSocketEvent event, String errorMessage) {
    return createErrorEvent(UUID.randomUUID(), errorMessage, event.getMessageId());
  }

  public ErrorWebSocketEvent createErrorEvent(String errorMessage) {
    return new ErrorWebSocketEvent(UUID.randomUUID(), errorMessage, null);
  }

  public ErrorWebSocketEvent createErrorEvent(
      UUID messageId, String errorMessage, UUID originalMessageId) {
    return new ErrorWebSocketEvent(messageId, errorMessage, originalMessageId);
  }
}
