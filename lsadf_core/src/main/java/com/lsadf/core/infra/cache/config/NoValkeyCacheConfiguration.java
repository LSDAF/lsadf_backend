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
package com.lsadf.core.infra.cache.config;

import com.lsadf.core.infra.cache.flush.CacheFlushService;
import com.lsadf.core.infra.cache.flush.NoOpFlushServiceImpl;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.cache.service.NoOpCacheServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "false")
public class NoValkeyCacheConfiguration {

  @Bean
  public CacheService noOpCacheService() {
    return new NoOpCacheServiceImpl();
  }

  @Bean
  public CacheFlushService noOpCacheFlushService() {
    return new NoOpFlushServiceImpl();
  }
}
