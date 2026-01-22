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

@Getter
@ToString(callSuper = true)
public abstract class AWebSocketEvent extends AEvent {
  protected final UUID sessionId;
  protected final UUID messageId;
  protected final UUID userId;

  protected AWebSocketEvent(EventType eventType, UUID sessionId, UUID messageId, UUID userId) {
    super(eventType);
    this.sessionId = sessionId;
    this.messageId = messageId;
    this.userId = userId;
  }

  protected AWebSocketEvent(
      EventType eventType, Long timestamp, UUID sessionId, UUID messageId, UUID userId) {
    super(eventType, timestamp);
    this.sessionId = sessionId;
    this.messageId = messageId;
    this.userId = userId;
  }
}
