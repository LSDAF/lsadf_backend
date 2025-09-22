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

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JdbcRepository<GameSessionEntity> {

  @Modifying
  @Query(
      "insert into t_game_session_tgse (tgse_id, tgme_id, tgse_end_time) values (:tgse_id, :tgme_id, :tgse_end_time)")
  void createNewGameSession(
      @Param(GAME_SESSION_ID) UUID id,
      @Param(GAME_SESSION_GAME_METADATA_ID) UUID gameSaveId,
      @Param(GAME_SESSION_END_TIME) Instant endTime);

  @Modifying
  @Query(
      "insert into t_game_session_tgse (tgse_id, tgme_id, tgse_end_time, tgse_cancelled, tgse_version) values (:tgse_id, :tgme_id, :tgse_end_time, :tgse_cancelled, :tgse_version)")
  void createNewGameSession(
      @Param(GAME_SESSION_ID) UUID id,
      @Param(GAME_SESSION_GAME_METADATA_ID) UUID gameSaveId,
      @Param(GAME_SESSION_END_TIME) Instant endTime,
      @Param(GAME_SESSION_CANCELLED) boolean cancelled,
      @Param(GAME_SESSION_VERSION) Integer version);

  @Modifying
  @Query(
      """
              insert into t_game_session_tgse
              (
                  tgse_id,
                  tgme_id,
                  tgse_end_time,
                  tgse_version
              )
              select
                  :tgse_id,
                  tgme_id,
                  :tgse_end_time,
                  tgse_version + 1
              from t_game_session_tgse
                  where tgse_id=:tgse_id
              """)
  void refreshGameSession(
      @Param(GAME_SESSION_ID) UUID sessionId, @Param(GAME_SESSION_END_TIME) Instant endTime);

  @Modifying
  @Query("update t_game_session_tgse set tgse_cancelled=TRUE where tgse_id=:tgse_id")
  void cancelGameSession(@Param(GAME_SESSION_ID) UUID sessionId);

  @Modifying
  @Query("delete from t_game_session_tgse")
  void deleteAllGameSessions();

  @Query("select count(tgse_id) from t_game_session_tgse")
  long count();
}
