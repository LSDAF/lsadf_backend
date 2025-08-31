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

package com.lsadf.core.infra.valkey.stream.event.game;

import com.lsadf.core.infra.valkey.stream.event.EventType;

public enum GameSaveEventType implements EventType {
  CHARACTERISTICS_UPDATE,
  STAGE_UPDATE,
  CURRENCY_UPDATE;

  public static GameSaveEventType enumFromString(String value) {
    for (var eventType : values()) {
      if (eventType.name().equals(value)) {
        return eventType;
      }
    }
    throw new IllegalArgumentException("event type value not found: " + value);
  }

  @Override
  public String getValue() {
    return name();
  }
}
