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

package com.lsadf.core.infra.persistence.impl.game.mail;

import static com.lsadf.core.infra.persistence.impl.game.mail.GameMailEntity.GameMailEntityAttributes.*;
import static com.lsadf.core.infra.persistence.impl.game.mail.GameMailEntity.GameMailEntityAttributes.GAME_MAIL_ID;

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMailRepository extends JdbcRepository<GameMailEntity> {
  @Query(
      """
            INSERT INTO t_game_mail_instance_tgmi
                (tgmi_id, tgme_id, tgmt_id, tgmi_read, tgmi_attachment_claimed)
            VALUES
                (:tgmi_id, :tgme_id, :tgmt_id, :tgmi_read, :tgmi_attachment_claimed)
            RETURNING *
        """)
  GameMailEntity createNewGameEmail(
      @Param(GAME_MAIL_ID) UUID id,
      @Param(GAME_MAIL_GAME_SAVE_ID) UUID gameSaveId,
      @Param(GAME_MAIL_MAIL_TEMPLATE_ID) UUID mailTemplateId,
      @Param(GAME_MAIL_IS_READ) boolean isRead,
      @Param(GAME_MAIL_IS_ATTACHMENT_CLAIMED) boolean isAttachmentClaimed);

  /**
   * Mark a game email as read
   *
   * @param mailId the mail id
   */
  @Modifying
  @Query(
      """
        UPDATE t_game_mail_instance_tgmi
        SET tgmi_read = TRUE
        WHERE tgmi_id = :tgmi_id
        """)
  void readGameEmail(@Param(GAME_MAIL_ID) UUID mailId);

  /**
   * Delete game emails by their ids
   *
   * @param mailIds list of mail ids to delete
   */
  @Modifying
  @Query(
      """
            DELETE FROM t_game_mail_instance_tgmi
            WHERE tgmi_id IN (:tgmi_id)
            """)
  void deleteGameEmails(@Param(GAME_MAIL_ID) List<UUID> mailIds);

  /**
   * Mark game mail attachments as claimed
   *
   * @param mailId the mail id
   */
  @Modifying
  @Query(
      """
            UPDATE t_game_mail_instance_tgmi
            SET tgmi_attachment_claimed = TRUE
            WHERE tgmi_id = :tgmi_id
            """)
  void claimGameMailAttachments(@Param(GAME_MAIL_ID) UUID mailId);

  /**
   * Delete expired game mails in batch
   *
   * @param currentTime the current timestamp
   */
  @Modifying
  @Query(
      """
        DELETE FROM t_game_mail_instance_tgmi
        WHERE tgmi_expires_at <= :currentTime
        """)
  void deleteExpiredGameMails(@Param("currentTime") Instant currentTime);

  /**
   * Delete all read game emails for a specific game save
   *
   * @param gameSaveId the game save id
   */
  @Query(
      """
        DELETE FROM t_game_mail_instance_tgmi
        WHERE tgmi_read = TRUE AND tgme_id = :tgme_id
        """)
  void deleteReadGameEmailsByGameSaveId(@Param(GAME_MAIL_GAME_SAVE_ID) UUID gameSaveId);

  @Query(
      """
        SELECT COUNT(*) FROM t_game_mail_instance_tgmi
        """)
  Long count();
}
