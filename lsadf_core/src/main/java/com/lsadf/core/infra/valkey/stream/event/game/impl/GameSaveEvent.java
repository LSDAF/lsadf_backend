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

package com.lsadf.core.infra.valkey.stream.event.game.impl;

import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEventType;
import com.lsadf.core.shared.model.Model;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public abstract class GameSaveEvent<T extends Model>
    implements com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent<T> {
  private final UUID id;
  private final UUID gameSaveId;
  private final String userId;
  private final GameSaveEventType eventType;
  private final Long timestamp;
  private final T payload;
}
