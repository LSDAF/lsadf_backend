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

package com.lsadf.core.application.game.mail;

import com.lsadf.core.infra.persistence.impl.game.mail.GameMailEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameMailRepositoryPort {
  /**
   * Find mail by id with attachments
   *
   * @param mailId the mail id
   * @return the mail with attachments
   */
  Optional<GameMailEntity> findGameMailEntityById(UUID mailId);

  /**
   * Find all mails for a specific game save
   *
   * @param gameSaveId the game save id
   * @return list of mails for the game save
   */
  List<GameMailEntity> findGameMailsByGameSaveId(UUID gameSaveId);

  /**
   * Save a new game mail
   *
   * @param id the mail id
   * @param gameSaveId the game save id
   * @param subject the subject of the mail
   * @param body the body of the mail
   * @param isRead true if the mail is read, false otherwise
   * @param expiresAt the expiration time
   */
  void createNewGameEmail(
      UUID id, UUID gameSaveId, String subject, String body, boolean isRead, Instant expiresAt);

  /**
   * Mark a game email as read
   *
   * @param mailId the mail id
   */
  void readGameEmail(UUID mailId);

  /**
   * Delete game emails by their ids
   *
   * @param mailIds list of mail ids to delete
   */
  void deleteGameEmails(List<UUID> mailIds);

  /**
   * Mark game mail attachments as claimed
   *
   * @param mailId the mail id
   */
  void claimGameMailAttachments(UUID mailId);

  /**
   * Delete expired game mails in batch
   *
   * @param currentTime the current timestamp
   */
  void deleteExpiredGameMails(Instant currentTime);
}
