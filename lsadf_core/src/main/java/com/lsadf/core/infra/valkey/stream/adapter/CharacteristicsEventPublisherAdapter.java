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

package com.lsadf.core.infra.valkey.stream.adapter;

import static com.lsadf.core.infra.valkey.stream.event.game.GameSaveEventType.CHARACTERISTICS_UPDATE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.valkey.stream.event.game.GameSaveEvent;
import com.lsadf.core.infra.valkey.stream.producer.StreamProducer;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CharacteristicsEventPublisherAdapter implements CharacteristicsEventPublisherPort {

  private final StreamProducer<GameSaveEvent> streamProducer;
  private final String streamKey;
  private final ObjectMapper objectMapper;

  @Override
  public void publishCharacteristicsUpdatedEvent(
      String userEmail, UUID gameSaveId, Characteristics characteristics) {
    Long timestamp = System.currentTimeMillis();
    Map<String, String> characteristicsMap =
        objectMapper.convertValue(characteristics, new TypeReference<>() {});
    GameSaveEvent gameSaveEvent =
        new GameSaveEvent(
            gameSaveId, userEmail, CHARACTERISTICS_UPDATE, timestamp, characteristicsMap);

    streamProducer.publishEvent(streamKey, gameSaveEvent);
  }
}
