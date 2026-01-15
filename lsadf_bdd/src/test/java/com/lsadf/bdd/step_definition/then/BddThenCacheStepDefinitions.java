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
package com.lsadf.bdd.step_definition.then;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[CACHE THEN STEP DEFINITIONS]")
@Component
public class BddThenCacheStepDefinitions {

  @Autowired protected StringRedisTemplate stringRedisTemplate;

  public void thenTheZsetFlushPendingCacheShouldBeEmpty() {
    log.info("Checking if flush pending zset is empty...");
    var results =
        stringRedisTemplate.opsForZSet().rangeByScore(FlushStatus.PENDING.getKey(), 0, -1);
    assertThat(results).isEmpty();
  }

  public void thenTheSetFlushProcessingCacheShouldBeEmpty() {
    log.info("Checking if flush processing set is empty...");
    var results = stringRedisTemplate.opsForSet().members(FlushStatus.PROCESSING.getKey());
    assertThat(results).isEmpty();
  }
}
