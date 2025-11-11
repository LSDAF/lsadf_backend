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

import com.lsadf.core.infra.persistence.Dateable;
import com.lsadf.core.infra.persistence.Identifiable;
import java.util.Date;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(GAME_MAIL_ENTITY)
public class GameMailEntity implements Dateable, Identifiable {

  @Id
  @Column(GAME_MAIL_ID)
  private UUID id;

  @Column(GAME_MAIL_MAIL_TEMPLATE_ID)
  private UUID mailTemplateId;

  @Column(GAME_MAIL_GAME_SAVE_ID)
  private UUID gameSaveId;

  @Column(GAME_MAIL_IS_READ)
  private boolean isRead;

  @Column(GAME_MAIL_IS_ATTACHMENT_CLAIMED)
  private boolean isAttachmentClaimed;

  @Column(GAME_MAIL_CREATED_AT)
  private java.time.Instant createdAt;

  @Column(GAME_MAIL_UPDATED_AT)
  private java.time.Instant updatedAt;

  @Column(GAME_MAIL_EXPIRES_AT)
  private java.time.Instant expiresAt;

  @Override
  public Date getCreatedAt() {
    return Date.from(createdAt);
  }

  @Override
  public Date getUpdatedAt() {
    return Date.from(updatedAt);
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameMailEntityAttributes {
    public static final String GAME_MAIL_ENTITY = "t_game_mail_instance_tgmi";
    public static final String GAME_MAIL_ID = "tgmi_id";
    public static final String GAME_MAIL_MAIL_TEMPLATE_ID = "tgmt_id";
    public static final String GAME_MAIL_GAME_SAVE_ID = "tgme_id";
    public static final String GAME_MAIL_IS_READ = "tgmi_read";
    public static final String GAME_MAIL_IS_ATTACHMENT_CLAIMED = "tgmi_attachment_claimed";
    public static final String GAME_MAIL_CREATED_AT = "tgmi_created_at";
    public static final String GAME_MAIL_UPDATED_AT = "tgmi_updated_at";
    public static final String GAME_MAIL_EXPIRES_AT = "tgmi_expires_at";
  }
}
