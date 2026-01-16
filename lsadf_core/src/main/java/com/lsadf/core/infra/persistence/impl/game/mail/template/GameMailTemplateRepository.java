/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.persistence.impl.game.mail.template;

import static com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateEntity.GameMailTemplateEntityAttributes.*;

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface GameMailTemplateRepository extends JdbcRepository<GameMailTemplateEntity> {
  @Query(
      """
        INSERT INTO t_game_mail_template_tgmt
        (tgmt_id, tgmt_name, tgmt_subject, tgmt_body, tgmt_expiration_days)
        VALUES
        (:tgmt_id, :tgmt_name, :tgmt_subject, :tgmt_body, :tgmt_expiration_days)
        RETURNING *""")
  GameMailTemplateEntity createNewMailTemplate(
      @Param(GAME_MAIL_TEMPLATE_ID) UUID id,
      @Param(GAME_MAIL_TEMPLATE_NAME) String name,
      @Param(GAME_MAIL_TEMPLATE_SUBJECT) String subject,
      @Param(GAME_MAIL_TEMPLATE_BODY) String body,
      @Param(GAME_MAIL_TEMPLATE_EXPIRATION_DAYS) int expirationDays);

  @Query("DELETE FROM t_game_mail_template_tgmt WHERE tgmt_id = :tgmt_id")
  @Modifying
  void deleteGameMailTemplateById(@Param(GAME_MAIL_TEMPLATE_ID) UUID id);

  @Modifying
  @Query("DELETE FROM t_game_mail_template_tgmt")
  void deleteAllMailTemplates();

  @Query("SELECT * FROM t_game_mail_template_tgmt WHERE tgmt_id = :tgmt_id")
  Optional<GameMailTemplateEntity> findGameMailTemplateById(@Param(GAME_MAIL_TEMPLATE_ID) UUID id);

  @Query("SELECT * FROM t_game_mail_template_tgmt")
  List<GameMailTemplateEntity> findAllGameMailTemplates();

  @Query("SELECT COUNT(*) FROM t_game_mail_template_tgmt")
  Long count();

  boolean existsById(UUID id);
}
