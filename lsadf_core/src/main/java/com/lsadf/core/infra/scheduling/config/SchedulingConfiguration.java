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

package com.lsadf.core.infra.scheduling.config;

import com.lsadf.core.infra.persistence.impl.game.mail.GameMailRepository;
import com.lsadf.core.infra.scheduling.job.ExpiredGameMailCleanupScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class SchedulingConfiguration implements SchedulingConfigurer {

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(10); // Adjust pool size as needed
    scheduler.setThreadNamePrefix("scheduled-virtual-");
    scheduler.setThreadFactory(Thread.ofVirtual().name("scheduled-virtual-", 0).factory());
    scheduler.initialize();
    taskRegistrar.setScheduler(scheduler);
  }

  @Bean
  public ExpiredGameMailCleanupScheduler expiredGameMailCleanupScheduler(
      GameMailRepository gameMailRepository) {
    return new ExpiredGameMailCleanupScheduler(gameMailRepository);
  }
}
