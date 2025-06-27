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
package com.lsadf.core.infra.config;

import com.lsadf.core.infra.cache.flush.CacheFlushService;
import com.lsadf.core.infra.logging.ConfigurationLogger;
import com.lsadf.core.infra.logging.properties.ConfigurationDisplayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/** Configuration class for the application listeners. */
@Configuration
public class ApplicationListenerConfiguration {

  @Bean
  public ShutdownListener shutdownListener(
      CacheFlushService cacheFlushService, ShutdownProperties shutdownProperties) {
    return new ShutdownListener(cacheFlushService, shutdownProperties);
  }

  @Bean
  public ConfigurationLogger configurationLogger(
      ConfigurableEnvironment environment,
      ConfigurationDisplayProperties configurationDisplayProperties) {
    return new ConfigurationLogger(environment, configurationDisplayProperties);
  }
}
