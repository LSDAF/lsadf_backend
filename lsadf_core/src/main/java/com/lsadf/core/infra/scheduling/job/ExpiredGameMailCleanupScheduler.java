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

package com.lsadf.core.infra.scheduling.job;

import com.lsadf.core.infra.persistence.impl.game.mail.GameMailRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduler responsible for cleaning up expired game mails from the system. It runs according to a
 * configurable cron schedule.
 */
@Slf4j
@RequiredArgsConstructor
public class ExpiredGameMailCleanupScheduler {

  private final GameMailRepository gameMailRepository;

  /**
   * Scheduled method that performs the cleanup of expired game mails. Runs according to the cron
   * expression defined in the configuration properties.
   */
  @Scheduled(cron = "${scheduling.game-mail-cleanup.cron:-}")
  @Transactional
  public void cleanupExpiredGameMails() {
    doCleanup();
  }

  /**
   * Manual cleanup method that can be called programmatically for testing or administrative
   * purposes.
   */
  private void doCleanup() {
    log.debug("Starting expired game mail cleanup job");
    Instant currentTime = Instant.now();
    int deleted = gameMailRepository.deleteExpiredGameMails(currentTime);
    if (deleted != 0) {
      log.info("Expired game mail cleanup job completed, deleted {} expired mails", deleted);
    }
  }
}
