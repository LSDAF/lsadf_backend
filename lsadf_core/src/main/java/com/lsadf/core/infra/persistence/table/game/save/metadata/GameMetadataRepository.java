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

import static com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.*;

import java.util.Optional;
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
      "update t_game_metadata_tgme set tgme_nickname=coalesce(:tgme_nickname, tgme_nickname) where tgme_id=:tgme_id returning *")
  GameMetadataEntity updateGameSaveEntityNickname(UUID id, String nickname);

  @Query(
      """
              insert into t_game_metadata_tgme (tgme_user_email, tgme_nickname) values (:tgme_ser_email, :tgme_nickname) returning *
              """)
  GameMetadataEntity createNewGameSaveEntity(
      @Param(GAME_METADATA_USER_EMAIL) String userEmail,
      @Param(GAME_METADATA_NICKNAME) String nickname);

  @Query(
      """
              insert into t_game_metadata_tgme (tgme_user_email) values (:tgme_user_email) returning *
              """)
  GameMetadataEntity createNewGameSaveEntity(@Param(GAME_METADATA_USER_EMAIL) String userEmail);

  @Query(
      """
          insert into t_game_metadata_tgme (tgme_id, tgme_user_email, tgme_nickname) values (:tgme_id, :tgme_user_email, :tgme_nickname) returning *
          """)
  GameMetadataEntity createNewGameSaveEntity(
      @Param(GAME_METADATA_ID) UUID id,
      @Param(GAME_METADATA_USER_EMAIL) String userEmail,
      @Param(GAME_METADATA_NICKNAME) String nickname);

  @Modifying
  @Query("delete from t_game_metadata_tgme where tgme_id=:tgme_id")
  void deleteGameSaveEntityById(@Param(GAME_METADATA_ID) UUID id);

  @Query("select exists(select 1 from t_game_metadata_tgme where tgme_nickname=:tgme_nickname)")
  boolean existsByNickname(@Param(GAME_METADATA_NICKNAME) String nickname);

  @Modifying
  @Query("delete from t_game_metadata_tgme")
  void deleteAllGameSaveEntities();

  @Query("select exists(select 1 from t_game_metadata_tgme where tgme_id = :tgme_id)")
  boolean existsById(@Param(GAME_METADATA_ID) UUID id);

  @Query("select count(tgme_id) from t_game_metadata_tgme")
  Long count();

  @Query("select * from t_game_metadata_tgme where tgme_id=:tgme_id")
  GameMetadataEntity findGameSaveEntityById(@Param(GAME_METADATA_ID) UUID id);

  @Query("select tgme_user_email from t_game_metadata_tgme where tgme_id=:tgme_id")
  Optional<String> findUserEmailById(@Param(GAME_METADATA_ID) UUID id);
}
