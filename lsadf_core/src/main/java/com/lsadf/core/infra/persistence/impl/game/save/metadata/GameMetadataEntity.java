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
package com.lsadf.core.infra.persistence.impl.game.save.metadata;

import static com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.*;

import com.lsadf.core.infra.persistence.Dateable;
import com.lsadf.core.infra.persistence.Identifiable;
import java.io.Serial;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/** Game Save Entity to persist data of a game save */
@Builder
@AllArgsConstructor
@Getter
@Setter
@Table(GAME_METADATA_ENTITY)
public class GameMetadataEntity implements Dateable, Identifiable {

  @Serial private static final long serialVersionUID = 7786624859103259009L;

  @Id
  @Column(GAME_METADATA_ID)
  private UUID id;

  @Column(GAME_METADATA_CREATED_AT)
  private Date createdAt;

  @Column(GAME_METADATA_UPDATED_AT)
  private Date updatedAt;

  @Column(GAME_METADATA_USER_EMAIL)
  private String userEmail;

  @Column(GAME_METADATA_NICKNAME)
  private String nickname;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSaveMetadataAttributes {
    public static final String GAME_METADATA_ENTITY = "t_game_metadata_tgme";
    public static final String GAME_METADATA_ID = "tgme_id";
    public static final String GAME_METADATA_CREATED_AT = "tgme_created_at";
    public static final String GAME_METADATA_UPDATED_AT = "tgme_updated_at";
    public static final String GAME_METADATA_USER_EMAIL = "tgme_user_email";
    public static final String GAME_METADATA_NICKNAME = "tgme_nickname";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    GameMetadataEntity that = (GameMetadataEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(createdAt, that.createdAt)
        && Objects.equals(updatedAt, that.updatedAt)
        && Objects.equals(userEmail, that.userEmail)
        && Objects.equals(nickname, that.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, updatedAt, userEmail, nickname);
  }
}
