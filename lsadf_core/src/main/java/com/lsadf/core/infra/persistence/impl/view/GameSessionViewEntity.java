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

package com.lsadf.core.infra.persistence.impl.view;

import static com.lsadf.core.infra.persistence.impl.view.GameSessionViewEntity.GameSessionViewAttributes.*;

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
@Table(GAME_SESSION_VIEW_ENTITY)
public class GameSessionViewEntity implements Identifiable, Dateable {

  @Id
  @Column(GAME_SESSION_ID)
  private UUID id;

  @Column(GAME_SESSION_GAME_METADATA_ID)
  private UUID gameSaveId;

  @Column(GAME_SESSION_USER_EMAIL)
  private String userEmail;

  @Column(GAME_SESSION_END_TIME)
  private Instant endTime;

  @Column(GAME_SESSION_CANCELLED)
  private boolean cancelled;

  @Column(GAME_SESSION_CREATED_AT)
  private Date createdAt;

  @Column(GAME_SESSION_UPDATED_AT)
  private Date updatedAt;

  @Column(GAME_SESSION_VERSION)
  private Integer version;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSessionViewAttributes {
    public static final String GAME_SESSION_VIEW_ENTITY = "v_game_session_vgse";
    public static final String GAME_SESSION_ID = "vgse_id";
    public static final String GAME_SESSION_GAME_METADATA_ID = "vgse_game_save_id";
    public static final String GAME_SESSION_USER_EMAIL = "vgse_user_email";
    public static final String GAME_SESSION_END_TIME = "vgse_end_time";
    public static final String GAME_SESSION_CANCELLED = "vgse_cancelled";
    public static final String GAME_SESSION_VERSION = "vgse_version";
    public static final String GAME_SESSION_CREATED_AT = "vgse_created_at";
    public static final String GAME_SESSION_UPDATED_AT = "vgse_updated_at";
  }
}
