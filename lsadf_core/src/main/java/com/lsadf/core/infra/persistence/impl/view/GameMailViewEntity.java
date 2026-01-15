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
package com.lsadf.core.infra.persistence.impl.view;

import static com.lsadf.core.infra.persistence.impl.view.GameMailViewEntity.GameMailViewEntityAttributes.*;

import com.lsadf.core.infra.persistence.Dateable;
import com.lsadf.core.infra.persistence.Identifiable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(GAME_MAIL_VIEW_ENTITY)
public class GameMailViewEntity implements Identifiable, Dateable {
  @Id
  @Column(GAME_MAIL_VIEW_ID)
  private UUID id;

  @Column(GAME_MAIL_VIEW_TEMPLATE_ID)
  private UUID templateId;

  @Column(GAME_MAIL_VIEW_GAME_SAVE_ID)
  private UUID gameSaveId;

  @Column(GAME_MAIL_VIEW_NAME)
  private String name;

  @Column(GAME_MAIL_VIEW_SUBJECT)
  private String subject;

  @Column(GAME_MAIL_VIEW_BODY)
  private String body;

  @Column(GAME_MAIL_VIEW_IS_READ)
  private boolean read;

  @Column(GAME_MAIL_VIEW_IS_ATTACHMENTS_CLAIMED)
  private boolean attachmentsClaimed;

  @Column(GAME_MAIL_VIEW_CREATED_AT)
  private Instant createdAt;

  @Column(GAME_MAIL_VIEW_UPDATED_AT)
  private Instant updatedAt;

  @Column(GAME_MAIL_VIEW_EXPIRES_AT)
  private Instant expiresAt;

  @Column(GAME_MAIL_VIEW_ATTACHMENT_COUNT)
  private Integer attachmentCount;

  @Override
  public Date getCreatedAt() {
    return Date.from(createdAt);
  }

  @Override
  public Date getUpdatedAt() {
    return Date.from(updatedAt);
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameMailViewEntityAttributes {
    public static final String GAME_MAIL_VIEW_ENTITY = "v_game_mail_instance_vgmi";
    public static final String GAME_MAIL_VIEW_ID = "tgmi_id";
    public static final String GAME_MAIL_VIEW_TEMPLATE_ID = "tgmt_id";
    public static final String GAME_MAIL_VIEW_GAME_SAVE_ID = "tgme_id";
    public static final String GAME_MAIL_VIEW_NAME = "tgmt_name";
    public static final String GAME_MAIL_VIEW_SUBJECT = "tgmt_subject";
    public static final String GAME_MAIL_VIEW_BODY = "tgmt_body";
    public static final String GAME_MAIL_VIEW_IS_READ = "tgmi_read";
    public static final String GAME_MAIL_VIEW_IS_ATTACHMENTS_CLAIMED = "tgmi_attachment_claimed";
    public static final String GAME_MAIL_VIEW_CREATED_AT = "tgmi_created_at";
    public static final String GAME_MAIL_VIEW_UPDATED_AT = "tgmi_updated_at";
    public static final String GAME_MAIL_VIEW_EXPIRES_AT = "tgmi_expires_at";
    public static final String GAME_MAIL_VIEW_ATTACHMENT_COUNT = "attachment_count";
  }
}
