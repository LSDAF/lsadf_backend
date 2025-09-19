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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEventType;
import com.lsadf.core.infra.valkey.stream.serializer.EventSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameEventSerializer implements EventSerializer<GameSaveEvent> {

  private final ObjectMapper objectMapper;

  @Override
  public Map<String, String> serialize(GameSaveEvent event) throws JsonProcessingException {
    Map<String, String> map = new HashMap<>(5);
    map.put(GameSaveEvent.GameSaveEventAttributes.EVENT_TYPE, event.getEventType().getValue());
    map.put(GameSaveEvent.GameSaveEventAttributes.GAME_SAVE_ID, event.gameSaveId().toString());
    map.put(GameSaveEvent.GameSaveEventAttributes.USER_ID, event.userId());
    map.put(GameSaveEvent.GameSaveEventAttributes.TIMESTAMP, event.timestamp().toString());
    map.put(
        GameSaveEvent.GameSaveEventAttributes.GAME_SESSION_ID, event.gameSessionId().toString());
    String mapString = objectMapper.writeValueAsString(event.payload());
    map.put(GameSaveEvent.GameSaveEventAttributes.PAYLOAD, mapString);

    return map;
  }

  @Override
  public GameSaveEvent deserialize(Map<String, String> map) throws JsonProcessingException {
    UUID gameSaveId = UUID.fromString(map.get(GameSaveEvent.GameSaveEventAttributes.GAME_SAVE_ID));
    String userId = map.get(GameSaveEvent.GameSaveEventAttributes.USER_ID);
    Long timestamp = Long.parseLong(map.get(GameSaveEvent.GameSaveEventAttributes.TIMESTAMP));
    GameSaveEventType eventType =
        GameSaveEventType.enumFromString(map.get(GameSaveEvent.GameSaveEventAttributes.EVENT_TYPE));
    String payload = map.get(GameSaveEvent.GameSaveEventAttributes.PAYLOAD);
    TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};
    Map<String, String> payloadMap = objectMapper.readValue(payload, typeRef);

    return GameSaveEvent.builder()
        .gameSaveId(gameSaveId)
        .userId(userId)
        .timestamp(timestamp)
        .payload(payloadMap)
        .eventType(eventType)
        .build();
  }
}
