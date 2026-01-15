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

package com.lsadf.core.unit.infra.valkey.cache.flush;

import static org.mockito.Mockito.when;

import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import com.lsadf.core.infra.valkey.cache.flush.impl.FlushRecoveryServiceImpl;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class FlushRecoveryServiceTests {

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private RedisTemplate<String, String> redisTemplate;

  @Mock private CacheFlushService cacheFlushService;

  private static final UUID UUID = java.util.UUID.randomUUID();

  private FlushRecoveryServiceImpl flushRecoveryService;

  private AutoCloseable openMocks;

  @BeforeEach
  void init() {
    openMocks = MockitoAnnotations.openMocks(this);
    flushRecoveryService = new FlushRecoveryServiceImpl(redisTemplate, cacheFlushService);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void test_recoverPendingFlush_doesNothing_when_noPendingFlush() {
    when(redisTemplate.opsForSet().members(FlushStatus.PROCESSING.getKey()))
        .thenReturn(new HashSet<>());
    flushRecoveryService.recoverPendingFlush();

    Mockito.verify(cacheFlushService, Mockito.never()).flushGameSave(UUID);
  }

  @Test
  void test_recoverPendingFlush_flushesPendingEntries() {
    when(redisTemplate.opsForSet().members(FlushStatus.PROCESSING.getKey()))
        .thenReturn(Set.of(UUID.toString()));
    flushRecoveryService.recoverPendingFlush();

    Mockito.verify(cacheFlushService).flushGameSave(UUID);
  }
}
