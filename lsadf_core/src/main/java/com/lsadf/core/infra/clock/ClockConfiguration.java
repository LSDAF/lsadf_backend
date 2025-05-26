/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.infra.clock;

import com.lsadf.core.properties.ClockProperties;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {

  @Bean
  public Clock clock(ClockProperties clockProperties) {
    if (clockProperties.getTimeZone() != null) {
      ZoneId zoneId = ZoneId.of(clockProperties.getTimeZone());
      return Clock.system(zoneId);
    }
    return Clock.systemDefaultZone();
  }
}
