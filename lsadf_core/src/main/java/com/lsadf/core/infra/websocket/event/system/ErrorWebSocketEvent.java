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
package com.lsadf.core.infra.websocket.event.system;

import com.lsadf.core.infra.websocket.event.AWebSocketEvent;
import com.lsadf.core.infra.websocket.event.WebSocketEventType;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ErrorWebSocketEvent extends AWebSocketEvent {
  private final String errorCode;
  private final String errorMessage;
  private final UUID originalMessageId;

  public ErrorWebSocketEvent(
      UUID sessionId,
      UUID messageId,
      UUID userId,
      String errorCode,
      String errorMessage,
      UUID originalMessageId) {
    super(WebSocketEventType.ERROR, sessionId, messageId, userId);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.originalMessageId = originalMessageId;
  }
}
