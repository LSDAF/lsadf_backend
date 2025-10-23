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
import static com.lsadf.core.infra.persistence.impl.game.mail.attachment.GameMailAttachmentEntity.GameMailAttachmentEntityAttributes.GAME_MAIL_ATTACHMENT_ENTITY;

import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.Identifiable;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@Table(GAME_MAIL_ATTACHMENT_ENTITY)
@Getter
@Setter
public class GameMailAttachmentEntity implements Identifiable {

  @Id
  @Column(GAME_MAIL_ATTACHMENT_ID)
  private UUID id;

  @Column(GAME_MAIL_ATTACHMENT_MAIL_ID)
  private UUID mailId;

  @Column(GAME_MAIL_ATTACHMENT_TYPE)
  private GameMailAttachmentType attachmentType;

  @Column(GAME_MAIL_ATTACHMENT_OBJECT)
  private String attachmentObject;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameMailAttachmentEntityAttributes {
    public static final String GAME_MAIL_ATTACHMENT_ENTITY = "t_game_mail_attachment_tgma";
    public static final String GAME_MAIL_ATTACHMENT_ID = "tgma_id";
    public static final String GAME_MAIL_ATTACHMENT_MAIL_ID = "tgme_id";
    public static final String GAME_MAIL_ATTACHMENT_TYPE = "tgml_attachment_type";
    public static final String GAME_MAIL_ATTACHMENT_OBJECT = "tgma_object";
  }
}
