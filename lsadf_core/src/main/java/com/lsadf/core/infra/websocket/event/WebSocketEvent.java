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

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.lsadf.core.shared.event.AEvent;
import java.util.UUID;
import lombok.*;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.JsonNode;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WebSocketEvent extends AEvent {
  private final UUID messageId;
  private final @Nullable JsonNode data;

  public WebSocketEvent(
      WebSocketEventType eventType, UUID messageId, Long timestamp, @Nullable JsonNode data) {
    super(eventType, timestamp);
    this.messageId = messageId;
    this.data = data;
  }

  public WebSocketEvent(WebSocketEventType eventType, UUID messageId, @Nullable JsonNode data) {
    super(eventType, System.currentTimeMillis());
    this.messageId = messageId;
    this.data = data;
  }

  public WebSocketEvent(WebSocketEventType eventType, JsonNode data) {
    this(eventType, UUID.randomUUID(), data);
  }
}
