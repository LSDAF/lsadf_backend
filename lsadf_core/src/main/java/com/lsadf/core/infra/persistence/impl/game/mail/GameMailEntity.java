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
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@Table(GAME_MAIL_ENTITY)
@Getter
@Setter
public class GameMailEntity implements Identifiable, Dateable {

  @Id
  @Column(GAME_MAIL_ID)
  private UUID id;

  @Column(GAME_MAIL_CREATED_AT)
  private Instant createdAt;

  @Column(GAME_MAIL_UPDATED_AT)
  private Instant updatedAt;

  @Column(GAME_MAIL_GAME_METADATA_ID)
  private UUID gameSaveId;

  @Column(GAME_MAIL_EXPIRES_AT)
  private Instant expiresAt;

  @Column(GAME_MAIL_SUBJECT)
  private String subject;

  @Column(GAME_MAIL_BODY)
  private String body;

  @Column(GAME_MAIL_READ)
  private boolean read;

  @Column(GAME_MAIL_ATTACHMENT_CLAIMED)
  private boolean attachmentsClaimed;

  @Override
  public Date getCreatedAt() {
    return Date.from(createdAt);
  }

  @Override
  public Date getUpdatedAt() {
    return Date.from(updatedAt);
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameMailEntityAttributes {
    public static final String GAME_MAIL_ENTITY = "t_game_mail_tgml";
    public static final String GAME_MAIL_ID = "tgml_id";
    public static final String GAME_MAIL_GAME_METADATA_ID = "tgme_id";
    public static final String GAME_MAIL_SUBJECT = "tgml_subject";
    public static final String GAME_MAIL_BODY = "tgml_body";
    public static final String GAME_MAIL_READ = "tgml_read";
    public static final String GAME_MAIL_CREATED_AT = "tgml_created_at";
    public static final String GAME_MAIL_EXPIRES_AT = "tgml_expires_at";
    public static final String GAME_MAIL_ATTACHMENT_CLAIMED = "tgml_attachment_claimed";
    public static final String GAME_MAIL_UPDATED_AT = "tgml_updated_at";
  }
}
