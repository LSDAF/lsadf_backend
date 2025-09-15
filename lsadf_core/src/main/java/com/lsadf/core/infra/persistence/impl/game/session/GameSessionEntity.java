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

package com.lsadf.core.infra.persistence.impl.game.session;

import static com.lsadf.core.infra.persistence.impl.game.session.GameSessionEntity.GameSessionEntityAttributes.*;

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
@Table(GAME_SESSION_ENTITY)
@Getter
@Setter
public class GameSessionEntity implements Identifiable, Dateable {
  @Id
  @Column(GAME_SESSION_ID)
  private final UUID id;

  @Column(GAME_SESSION_GAME_METADATA_ID)
  private final UUID gameSaveId;

  @Column(GAME_SESSION_END_TIME)
  private final Instant endTime;

  @Column(GAME_SESSION_CANCELLED)
  private final boolean cancelled;

  @Column(GAME_SESSION_CREATED_AT)
  private final Date createdAt;

  @Column(GAME_SESSION_UPDATED_AT)
  private final Date updatedAt;

  @Column(GAME_SESSION_VERSION)
  private final Integer version;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSessionEntityAttributes {
    public static final String GAME_SESSION_ENTITY = "t_game_session_tgse";
    public static final String GAME_SESSION_ID = "tgse_id";
    public static final String GAME_SESSION_GAME_METADATA_ID = "tgme_id";
    public static final String GAME_SESSION_END_TIME = "tgse_end_time";
    public static final String GAME_SESSION_CANCELLED = "tgse_cancelled";
    public static final String GAME_SESSION_CREATED_AT = "tgse_created_at";
    public static final String GAME_SESSION_UPDATED_AT = "tgse_updated_at";
    public static final String GAME_SESSION_VERSION = "tgse_version";
  }
}
