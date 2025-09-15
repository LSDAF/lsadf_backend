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

package com.lsadf.core.application.game.session;

import com.lsadf.core.domain.game.session.GameSession;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameSessionRepositoryPort {
  Optional<GameSession> getGameSessionById(UUID id);

  List<GameSession> getGameSessionsByGameSaveId(UUID gameSaveId);

  GameSession createNewGameSession(UUID gameSaveId, Instant endTime);

  GameSession updateGameSessionEndTime(UUID sessionId, Instant endTime);
}
