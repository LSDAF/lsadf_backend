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
import static com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateAttachmentEntity.GameMailTemplateAttachmentEntityAttributes.GAME_MAIL_TEMPLATE_ATTACHMENT_ID;
import static com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateAttachmentEntity.GameMailTemplateAttachmentEntityAttributes.GAME_MAIL_TEMPLATE_ATTACHMENT_OBJECT;

import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.Identifiable;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(GAME_MAIL_TEMPLATE_ATTACHMENT_ENTITY)
public class GameMailTemplateAttachmentEntity implements Identifiable {
  @Id
  @Column(GAME_MAIL_TEMPLATE_ATTACHMENT_ID)
  private UUID id;

  @Column(GAME_MAIL_TEMPLATE_ATTACHMENT_MAIL_TEMPLATE_ID)
  private UUID mailTemplateId;

  @Column(GAME_MAIL_TEMPLATE_ATTACHMENT_TYPE)
  private GameMailAttachmentType type;

  @Column(GAME_MAIL_TEMPLATE_ATTACHMENT_OBJECT)
  private String object;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameMailTemplateAttachmentEntityAttributes {
    public static final String GAME_MAIL_TEMPLATE_ATTACHMENT_ENTITY =
        "t_game_mail_template_attachment_tmta";
    public static final String GAME_MAIL_TEMPLATE_ATTACHMENT_ID = "tmta_id";
    public static final String GAME_MAIL_TEMPLATE_ATTACHMENT_MAIL_TEMPLATE_ID = "tgmt_id";
    public static final String GAME_MAIL_TEMPLATE_ATTACHMENT_TYPE = "tmta_type";
    public static final String GAME_MAIL_TEMPLATE_ATTACHMENT_OBJECT = "tmta_object";
  }
}
