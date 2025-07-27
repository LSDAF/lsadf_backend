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
package com.lsadf.core.infra.persistence.table.game.game_save;

import static com.lsadf.core.infra.persistence.config.EntityAttributes.*;
import static com.lsadf.core.infra.persistence.table.game.game_save.GameSaveEntity.GameSaveAttributes.*;

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
@ToString(callSuper = true)
@Table(GAME_SAVE_ENTITY)
public class GameSaveEntity implements Dateable, Identifiable {

  @Serial private static final long serialVersionUID = 7786624859103259009L;

  @Id
  @Column(ID)
  private UUID id;

  @Column(CREATED_AT)
  private Date createdAt;

  @Column(UPDATED_AT)
  private Date updatedAt;

  @Column(GAME_SAVE_USER_EMAIL)
  private String userEmail;

  @Column(GAME_SAVE_NICKNAME)
  private String nickname;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSaveAttributes {
    public static final String GAME_SAVE_ENTITY = "t_game_save_tgsa";
    public static final String GAME_SAVE_USER_EMAIL = "user_email";
    public static final String GAME_SAVE_NICKNAME = "nickname";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    GameSaveEntity that = (GameSaveEntity) o;
    return Objects.equals(userEmail, that.userEmail) && Objects.equals(nickname, that.nickname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userEmail, nickname, createdAt, updatedAt);
  }
}
