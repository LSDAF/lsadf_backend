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

package com.lsadf.core.application.game.session.impl;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.application.game.session.GameSessionCommandService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.application.game.session.command.InitializeSessionCommand;
import com.lsadf.core.application.game.session.command.UpdateSessionEndTimeCommand;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSessionCommandServiceImpl implements GameSessionCommandService {

  private final GameSessionRepositoryPort gameSessionRepositoryPort;
  private final CacheManager cacheManager;
  private final GameSessionCachePort gameSessionCachePort;
  private final GameSaveService gameSaveService;

  @Override
  public GameSession initializeGameSession(InitializeSessionCommand command) {
    UUID gameSaveId = command.gameSaveId();
    if (!gameSaveService.existsById(gameSaveId)) {
      throw new NotFoundException("Game save with id " + gameSaveId + " not found");
    }

    UUID uuid = UUID.randomUUID();
    GameSession gameSession =
        gameSessionRepositoryPort.createNewGameSession(
            uuid, command.gameSaveId(), command.endTime(), false);
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      gameSessionCachePort.set(gameSession.getId().toString(), gameSession);
    }
    return gameSession;
  }

  @Override
  public GameSession updateGameSessionEndTime(UpdateSessionEndTimeCommand command) {
    GameSession updated =
        gameSessionRepositoryPort.updateGameSessionEndTime(
            command.gameSessionId(), command.endTime());
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      gameSessionCachePort.set(updated.getId().toString(), updated);
    }
    return updated;
  }
}
