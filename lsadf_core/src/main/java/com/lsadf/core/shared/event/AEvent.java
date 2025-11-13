/*
 * Copyright © 2024-2025 LSDAF
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

package com.lsadf.core.shared.event;

import lombok.*;

@Getter
@ToString
public abstract class AEvent implements Event {
  protected final EventType eventType;
  protected final Long timestamp;

  /**
   * Constructor accepting event type and timestamp.
   *
   * @param eventType the event type
   * @param timestamp the timestamp in epoch milliseconds
   */
  protected AEvent(EventType eventType, Long timestamp) {
    this.eventType = eventType;
    this.timestamp = timestamp;
  }

  /**
   * Constructor accepting event type only, using current system time for backward compatibility.
   *
   * @param eventType the event type
   */
  protected AEvent(EventType eventType) {
    this(eventType, System.currentTimeMillis());
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class EventAttributes {
    public static final String EVENT_TYPE = "eventType";
    public static final String TIMESTAMP = "timestamp";
  }
}
