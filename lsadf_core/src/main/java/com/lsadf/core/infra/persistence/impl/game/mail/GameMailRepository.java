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

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMailRepository extends JdbcRepository<GameMailEntity> {

  /**
   * Find mail by id with attachments
   *
   * @param mailId the mail id
   * @return the mail with attachments
   */
  @Query("SELECT tgml.* FROM t_game_mail_tgml tgml WHERE tgml.tgml_id = :tgml_id")
  Optional<GameMailEntity> findGameMailEntityById(@Param(GAME_MAIL_ID) String mailId);

  /**
   * Find all mails for a specific game save
   *
   * @param gameSaveId the game save id
   * @return list of mails for the game save
   */
  @Query("SELECT * FROM t_game_mail_tgml WHERE tgme_id = :tgme_id")
  List<GameMailEntity> findGameMailsByGameSaveId(
      @Param(GAME_MAIL_GAME_METADATA_ID) String gameSaveId);

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
  @Modifying
  @Query(
      "INSERT INTO t_game_mail_tgml "
          + "(tgml_id, tgme_id, tgml_subject, tgml_body, tgml_read, tgml_deleted, tgml_expires_at) "
          + "VALUES (:tgml_id, :tgme_id, :tgml_subject, :tgml_body, :tgml_read, :tgml_deleted, :tgml_expires_at)")
  void createNewGameEmail(
      @Param(GAME_MAIL_ID) String id,
      @Param(GAME_MAIL_GAME_METADATA_ID) String gameSaveId,
      @Param(GAME_MAIL_SUBJECT) String subject,
      @Param(GAME_MAIL_BODY) String body,
      @Param(GAME_MAIL_READ) String isRead,
      @Param(GAME_MAIL_EXPIRES_AT) Instant expiresAt);

  /**
   * Mark a game email as read
   *
   * @param mailId the mail id
   */
  @Modifying
  @Query("UPDATE t_game_mail_tgml SET tgml_read = true WHERE tgml_id = :tgml_id")
  void readGameEmail(@Param(GAME_MAIL_ID) String mailId);

  /**
   * Delete game emails by their ids
   *
   * @param mailIds list of mail ids to delete
   */
  @Modifying
  @Query("DELETE FROM t_game_mail_tgml WHERE tgml_id IN (:tgml_id)")
  void deleteGameEmails(@Param(GAME_MAIL_ID) List<String> mailIds);

  /**
   * Mark game mail attachments as claimed
   *
   * @param mailId the mail id
   */
  @Modifying
  @Query("UPDATE t_game_mail_tgml SET tgml_attachment_claimed = true WHERE tgml_id = :tgml_id")
  void claimGameMailAttachments(@Param(GAME_MAIL_ID) String mailId);

  /**
   * Delete expired game mails in batch
   *
   * @param currentTime the current timestamp
   */
  @Modifying
  @Query(
      "DELETE FROM t_game_mail_tgml WHERE tgml_expires_at < :currentTime AND tgml_id IN (SELECT tgml_id FROM t_game_mail_tgml WHERE tgml_expires_at < :currentTime)")
  void deleteExpiredGameMails(@Param("currentTime") Instant currentTime);
}
