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

package com.lsadf.core.infra.persistence.impl.game.mail.template;

import static com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateAttachmentEntity.GameMailTemplateAttachmentEntityAttributes.*;

import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMailTemplateAttachmentRepository
    extends JdbcRepository<GameMailTemplateAttachmentEntity> {
  @Query(
      """
                      INSERT INTO t_game_mail_template_attachment_tmta
                      (tmta_id, tgmt_id, tmta_type, tmta_object)
                      VALUES
                      (:tmta_id, :tgmt_id, :tmta_type, :tmta_object)
                      RETURNING *
              """)
  GameMailTemplateAttachmentEntity createNewGameMailAttachment(
      @Param(GAME_MAIL_TEMPLATE_ATTACHMENT_ID) UUID attachmentId,
      @Param(GAME_MAIL_TEMPLATE_ATTACHMENT_MAIL_TEMPLATE_ID) UUID mailTemplateId,
      @Param(GAME_MAIL_TEMPLATE_ATTACHMENT_TYPE) GameMailAttachmentType attachmentType,
      @Param(GAME_MAIL_TEMPLATE_ATTACHMENT_OBJECT) String attachmentObject);

  @Query("SELECT * FROM t_game_mail_template_attachment_tmta WHERE tgmt_id = :tgmt_id")
  List<GameMailTemplateAttachmentEntity> findByMailTemplateId(
      @Param(GAME_MAIL_TEMPLATE_ATTACHMENT_MAIL_TEMPLATE_ID) UUID mailTemplateId);

  @Query("SELECT COUNT(*) FROM t_game_mail_template_attachment_tmta")
  Long count();
}
