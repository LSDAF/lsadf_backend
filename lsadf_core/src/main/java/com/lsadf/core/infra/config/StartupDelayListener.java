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

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j(topic = "[INIT]")
public class StartupDelayListener
    implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
  @Override
  public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    ConfigurableEnvironment env = event.getEnvironment();
    boolean enabled = Boolean.TRUE.equals(env.getProperty("sleep.enabled", Boolean.class));
    int seconds = env.getProperty("sleep.seconds", Integer.class);
    if (enabled) {
      log.info("SLEEP_ENABLED: true");
      log.info("Sleeping {} seconds...", seconds);
      try {
        Thread.sleep(seconds * 1000L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      log.info("Running application...");
    } else {
      log.info("SLEEP_ENABLED: false");
      log.info("Running application...");
    }
  }
}
