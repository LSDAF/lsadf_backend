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

import static com.lsadf.core.infra.persistence.table.game.game_save.GameSaveEntity.GameSaveAttributes.GAME_SAVE_NICKNAME;
import static com.lsadf.core.infra.persistence.table.game.game_save.GameSaveEntity.GameSaveAttributes.GAME_SAVE_USER_EMAIL;

import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository class for GameSaveEntity */
@Repository
public interface GameSaveRepository
    extends org.springframework.data.repository.Repository<GameSaveEntity, UUID> {

  @Query(
      "update t_game_save_tgsa set nickname=coalesce(:nickname, nickname) where id=:id returning *")
  GameSaveEntity updateGameSaveEntityNickname(UUID id, String nickname);

  @Query(
      """
          insert into t_game_save_tgsa (user_email, nickname) values (:user_email, :nickname) returning *
          """)
  GameSaveEntity createNewGameSaveEntity(
      @Param(GAME_SAVE_USER_EMAIL) String userEmail, @Param(GAME_SAVE_NICKNAME) String nickname);

  @Query(
      """
              insert into t_game_save_tgsa (user_email) values (:user_email) returning *
              """)
  GameSaveEntity createNewGameSaveEntity(@Param(GAME_SAVE_USER_EMAIL) String userId);

  @Query(
      """
          insert into t_game_save_tgsa (id, user_email, nickname) values (:id, :user_email, :nickname) returning *
          """)
  GameSaveEntity createNewGameSaveEntity(
      @Param("id") UUID id,
      @Param(GAME_SAVE_USER_EMAIL) String userEmail,
      @Param(GAME_SAVE_NICKNAME) String nickname);

  @Modifying
  @Query("delete from t_game_save_tgsa where id=:id")
  void deleteGameSaveEntityById(@Param("id") UUID id);

  @Query("select exists(select 1 from t_game_save_tgsa where nickname=:nickname)")
  boolean existsByNickname(@Param(GAME_SAVE_NICKNAME) String nickname);

  @Modifying
  @Query("delete from t_game_save_tgsa")
  void deleteAllGameSaveEntities();

  @Query("select exists(select 1 from t_game_save_tgsa where id = :id)")
  boolean existsById(@Param("id") UUID id);

  @Query("select count(id) from t_game_save_tgsa")
  Long count();
}
