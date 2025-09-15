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

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JdbcRepository<GameSessionEntity> {
  @Query(
      "insert into t_game_session_tgse (tgme_id, tgse_end_time) values (:tgme_id, :tgse_end_time)")
  void createNewGameSession(UUID gameSaveId, Instant endTime);

  @Query("update t_game_session_tgse set tgse_end_time=:tgse_end_time where tgme_id=:tgme_id")
  void updateGameSessionEndTime(UUID sessionId, Instant endTime);

  @Query("update t_game_session_tgse set tgse_cancelled=TRUE where tgse_id=:tgse_id")
  void cancelGameSession(UUID sessionId);
}
