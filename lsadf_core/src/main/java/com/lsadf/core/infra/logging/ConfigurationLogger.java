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
package com.lsadf.core.infra.logging;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class ConfigurationLogger implements ApplicationListener<ApplicationReadyEvent> {

  private final ConfigurableEnvironment environment;
  private final ConfigurationDisplayProperties configurationDisplayProperties;

  public ConfigurationLogger(
      ConfigurableEnvironment environment,
      ConfigurationDisplayProperties configurationDisplayProperties) {
    this.environment = environment;
    this.configurationDisplayProperties = configurationDisplayProperties;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    if (configurationDisplayProperties.isEnabled()) {
      log.info("Displaying configuration properties:");
      environment
          .getPropertySources()
          .forEach(
              source -> {
                if (source.getSource() instanceof Map) {
                  ((Map<?, ?>) source.getSource())
                      .forEach(
                          (key, value) -> {
                            String resolvedValue = environment.getProperty((String) key);
                            log.info("Property: {} Value: {}", key, resolvedValue);
                          });
                }
              });
    }
  }
}
