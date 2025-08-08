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

package com.lsadf.core.unit.config;

import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.stage.Stage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

@TestConfiguration
public class UnitCacheConfiguration {
  @MockBean public RedisTemplate<String, Currency> currencyRedisTemplate;

  @MockBean public RedisTemplate<String, Characteristics> characteristicsRedisTemplate;

  @MockBean public RedisTemplate<String, String> stringRedisTemplate;

  @MockBean public RedisTemplate<String, Long> longRedisTemplate;

  @MockBean public RedisTemplate<String, Boolean> booleanRedisTemplate;

  @MockBean public RedisTemplate<String, Stage> stageRedisTemplate;
}
