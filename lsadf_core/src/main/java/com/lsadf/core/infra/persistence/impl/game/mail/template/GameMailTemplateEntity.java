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

import com.lsadf.core.infra.persistence.Dateable;
import com.lsadf.core.infra.persistence.Identifiable;
import java.util.Date;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(GAME_MAIL_TEMPLATE_ENTITY)
public class GameMailTemplateEntity implements Dateable, Identifiable {

  @Id
  @Column(GAME_MAIL_TEMPLATE_ID)
  private UUID id;

  @Column(GAME_MAIL_TEMPLATE_NAME)
  private String name;

  @Column(GAME_MAIL_TEMPLATE_SUBJECT)
  private String subject;

  @Column(GAME_MAIL_TEMPLATE_BODY)
  private String body;

  @Column(GAME_MAIL_TEMPLATE_EXPIRATION_DAYS)
  private Integer expirationDays;

  @Column(GAME_MAIL_TEMPLATE_CREATED_AT)
  private java.time.Instant createdAt;

  @Column(GAME_MAIL_TEMPLATE_UPDATED_AT)
  private java.time.Instant updatedAt;

  @Override
  public Date getCreatedAt() {
    return Date.from(createdAt);
  }

  @Override
  public Date getUpdatedAt() {
    return Date.from(updatedAt);
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameMailTemplateEntityAttributes {
    public static final String GAME_MAIL_TEMPLATE_ENTITY = "t_game_mail_template_tgmt";
    public static final String GAME_MAIL_TEMPLATE_ID = "tgmt_id";
    public static final String GAME_MAIL_TEMPLATE_NAME = "tgmt_name";
    public static final String GAME_MAIL_TEMPLATE_SUBJECT = "tgmt_subject";
    public static final String GAME_MAIL_TEMPLATE_BODY = "tgmt_body";
    public static final String GAME_MAIL_TEMPLATE_EXPIRATION_DAYS = "tgmt_expiration_days";
    public static final String GAME_MAIL_TEMPLATE_CREATED_AT = "tgmt_created_at";
    public static final String GAME_MAIL_TEMPLATE_UPDATED_AT = "tgmt_updated_at";
  }
}
