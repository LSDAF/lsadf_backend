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
package com.lsadf.core.infra.persistence.table.game.save.metadata;

import static com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_NICKNAME;
import static com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_USER_EMAIL;

import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository class for GameMetadataEntity */
@Repository
public interface GameMetadataRepository
    extends org.springframework.data.repository.Repository<GameMetadataEntity, UUID> {

  @Query(
      "update t_game_metadata_tgme set nickname=coalesce(:nickname, nickname) where id=:id returning *")
  GameMetadataEntity updateGameSaveEntityNickname(UUID id, String nickname);

  @Query(
      """
          insert into t_game_metadata_tgme (user_email, nickname) values (:user_email, :nickname) returning *
          """)
  GameMetadataEntity createNewGameSaveEntity(
      @Param(GAME_METADATA_USER_EMAIL) String userEmail,
      @Param(GAME_METADATA_NICKNAME) String nickname);

  @Query(
      """
              insert into t_game_metadata_tgme (user_email) values (:user_email) returning *
              """)
  GameMetadataEntity createNewGameSaveEntity(@Param(GAME_METADATA_USER_EMAIL) String userId);

  @Query(
      """
          insert into t_game_metadata_tgme (id, user_email, nickname) values (:id, :user_email, :nickname) returning *
          """)
  GameMetadataEntity createNewGameSaveEntity(
      @Param("id") UUID id,
      @Param(GAME_METADATA_USER_EMAIL) String userEmail,
      @Param(GAME_METADATA_NICKNAME) String nickname);

  @Modifying
  @Query("delete from t_game_metadata_tgme where id=:id")
  void deleteGameSaveEntityById(@Param("id") UUID id);

  @Query("select exists(select 1 from t_game_metadata_tgme where nickname=:nickname)")
  boolean existsByNickname(@Param(GAME_METADATA_NICKNAME) String nickname);

  @Modifying
  @Query("delete from t_game_metadata_tgme")
  void deleteAllGameSaveEntities();

  @Query("select exists(select 1 from t_game_metadata_tgme where id = :id)")
  boolean existsById(@Param("id") UUID id);

  @Query("select count(id) from t_game_metadata_tgme")
  Long count();

  @Query("select * from t_game_metadata_tgme where id=:id")
  GameMetadataEntity findGameSaveEntityById(@Param("id") UUID id);
}
