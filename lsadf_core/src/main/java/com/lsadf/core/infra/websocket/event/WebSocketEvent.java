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

import com.lsadf.core.shared.event.AEvent;
import com.lsadf.core.shared.event.EventType;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

@Getter
@ToString(callSuper = true)
public class WebSocketEvent extends AEvent {
  private final UUID sessionId;
  private final UUID messageId;
  private final UUID userId;
  private final transient @Nullable JsonNode data;

  public WebSocketEvent(
      EventType eventType,
      Long timestamp,
      UUID sessionId,
      UUID messageId,
      UUID userId,
      @Nullable JsonNode data) {
    super(eventType, timestamp);
    this.sessionId = sessionId;
    this.messageId = messageId;
    this.userId = userId;
    this.data = data;
  }

  public WebSocketEvent(
      EventType eventType, UUID sessionId, UUID messageId, UUID userId, @Nullable JsonNode data) {
    super(eventType, System.currentTimeMillis());
    this.sessionId = sessionId;
    this.messageId = messageId;
    this.userId = userId;
    this.data = data;
  }
}
