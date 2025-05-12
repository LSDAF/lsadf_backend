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
package com.lsadf.core.configurations;

import com.lsadf.core.configurations.interceptors.RequestLoggerInterceptor;
import com.lsadf.core.properties.HttpLogProperties;
import com.lsadf.core.services.ClockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {
  @Bean
  public RequestLoggerInterceptor requestLoggerInterceptor(
      HttpLogProperties httpLogProperties, ClockService clockService) {
    return new RequestLoggerInterceptor(httpLogProperties, clockService);
  }
}
