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

package com.lsadf.core.infra.valkey.stream.producer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.core.infra.valkey.stream.exception.EventHandlingException;
import com.lsadf.core.infra.valkey.stream.producer.StreamProducer;
import com.lsadf.core.infra.valkey.stream.serializer.ValkeyEventSerializer;
import com.lsadf.core.shared.event.Event;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class ValkeyStreamProducer<T extends Event> implements StreamProducer<T> {

  private final RedisTemplate<String, String> redisTemplate;
  private final ValkeyEventSerializer<T> valkeyEventSerializer;

  public ValkeyStreamProducer(
      RedisTemplate<String, String> redisTemplate, ValkeyEventSerializer<T> valkeyEventSerializer) {
    this.redisTemplate = redisTemplate;
    this.valkeyEventSerializer = valkeyEventSerializer;
  }

  @Override
  public RecordId publishEvent(String streamKey, T event) {
    try {
      Map<String, String> eventData = valkeyEventSerializer.serialize(event);
      MapRecord<String, String, String> mapRecord =
          StreamRecords.mapBacked(eventData).withStreamKey(streamKey);
      return redisTemplate.opsForStream().add(mapRecord);
    } catch (JsonProcessingException e) {
      log.error("JsonProcessingException error serializing event for stream {}", streamKey, e);
      throw new EventHandlingException(e);
    } catch (Exception e) {
      log.error("Error serializing event for stream {}", streamKey, e);
      throw e;
    }
  }
}
