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

package com.lsadf.core.infra.valkey.stream.serializer.impl;

import static com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent.GameSaveEventAttributes.*;
import static com.lsadf.core.shared.event.AEvent.EventAttributes.EVENT_TYPE;
import static com.lsadf.core.shared.event.AEvent.EventAttributes.TIMESTAMP;

import com.lsadf.core.infra.util.ObjectUtils;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveEventType;
import com.lsadf.core.infra.valkey.stream.event.game.ValkeyGameSaveUpdatedEvent;
import com.lsadf.core.infra.valkey.stream.serializer.ValkeyEventSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class GameValkeyEventSerializer
    implements ValkeyEventSerializer<ValkeyGameSaveUpdatedEvent> {

  private final ObjectMapper objectMapper;

  @Override
  public Map<String, String> serialize(ValkeyGameSaveUpdatedEvent event) throws JacksonException {
    Map<String, String> map = HashMap.newHashMap(5);
    map.put(EVENT_TYPE, event.getEventType().getValue());
    map.put(GAME_SAVE_ID, event.getGameSaveId().toString());
    map.put(USER_ID, event.getUserId());
    map.put(TIMESTAMP, event.getTimestamp().toString());
    map.put(GAME_SESSION_ID, ObjectUtils.getOrDefault(event.getGameSessionId(), "").toString());
    String mapString = objectMapper.writeValueAsString(event.getPayload());
    map.put(PAYLOAD, mapString);

    return map;
  }

  @Override
  public ValkeyGameSaveUpdatedEvent deserialize(Map<String, String> map) throws JacksonException {
    UUID gameSaveId = UUID.fromString(map.get(GAME_SAVE_ID));
    String userId = map.get(USER_ID);
    ValkeyGameSaveEventType eventType = ValkeyGameSaveEventType.enumFromString(map.get(EVENT_TYPE));
    String payload = map.get(PAYLOAD);
    TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};
    Map<String, String> payloadMap = objectMapper.readValue(payload, typeRef);

    return new ValkeyGameSaveUpdatedEvent(eventType, gameSaveId, userId, null, payloadMap);
  }
}
