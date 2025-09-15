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

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionViewRepository extends JdbcRepository<GameSessionViewEntity> {
  @Query("select * from v_game_session_vgse where vgse_id=:vgse_id")
  Optional<GameSessionViewEntity> findBySessionId(UUID sessionId);

  @Query("select * from v_game_session_vgse where vgse_game_save_id=:vgse_game_save_id")
  List<GameSessionViewEntity> findByGameSaveId(UUID gameSaveId);

  @Query(
      "SELECT * FROM v_game_session_vgse "
          + "WHERE vgse_end_time > NOW() "
          + "AND vgse_cancelled = FALSE "
          + "ORDER BY vgse_end_time DESC "
          + "LIMIT 1")
  Optional<GameSessionViewEntity> findLatestActiveGameSessionByGameSaveId(UUID gameSaveId);
}
