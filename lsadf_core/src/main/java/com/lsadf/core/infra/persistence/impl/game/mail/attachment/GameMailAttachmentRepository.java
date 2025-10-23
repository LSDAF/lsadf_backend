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

package com.lsadf.core.infra.persistence.impl.game.mail.attachment;

import static com.lsadf.core.infra.persistence.impl.game.mail.attachment.GameMailAttachmentEntity.GameMailAttachmentEntityAttributes.*;

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMailAttachmentRepository extends JdbcRepository<GameMailAttachmentEntity> {

  @Query(
      """
              INSERT INTO t_game_mail_attachment_tgma
              (tgma_id, tgml_id, tgml_attachment_type, tgma_object)
              VALUES
              (:tgma_id, :tgml_id, :tgml_attachment_type, :tgma_object)
              """)
  void createNewGameMailAttachment(
      @Param(GAME_MAIL_ATTACHMENT_ID) String attachmentId,
      @Param(GAME_MAIL_ATTACHMENT_MAIL_ID) String mailId,
      @Param(GAME_MAIL_ATTACHMENT_TYPE) String attachmentType,
      @Param(GAME_MAIL_ATTACHMENT_OBJECT) String attachmentObject);

  @Query("SELECT * FROM t_game_mail_attachment_tgma WHERE tgml_id = :mailId")
  List<GameMailAttachmentEntity> findByMailId(@Param(GAME_MAIL_ATTACHMENT_MAIL_ID) String mailId);
}
