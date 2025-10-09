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
import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.exception.InvalidGameSessionException;
import com.lsadf.core.exception.http.NotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSessionQueryServiceImpl implements GameSessionQueryService {

  private final GameSessionRepositoryPort gameSessionRepositoryPort;

  private final GameSessionCachePort gameSessionCachePort;

  private final CacheManager cacheManager;

  private final ClockService clockService;

  @Override
  public GameSession findGameSessionById(UUID id) {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      var optional = gameSessionCachePort.get(id.toString());
      if (optional.isPresent()) {
        return optional.get();
      }
      var gameSession = getGameSessionFromDatabase(id);
      gameSessionCachePort.set(id.toString(), gameSession);
      return gameSession;
    }
    return getGameSessionFromDatabase(id);
  }

  @Override
  public List<GameSession> findGameSessionByGameSaveId(UUID gameSaveId) {
    return gameSessionRepositoryPort.getGameSessionsByGameSaveId(gameSaveId);
  }

  private GameSession getGameSessionFromDatabase(UUID id) {
    return gameSessionRepositoryPort
        .getGameSessionById(id)
        .orElseThrow(() -> new NotFoundException("Didn't find any game session with id " + id));
  }

  @Override
  public void checkGameSessionValidity(UUID sessionId, UUID gameSaveId, Instant now) {
    GameSession gameSession = findGameSessionById(sessionId);
    if (!gameSession.getGameSaveId().equals(gameSaveId)) {
      throw new InvalidGameSessionException("Game session is not associated with this game save");
    }
    if (gameSession.isCancelled()) {
      throw new InvalidGameSessionException("Game session cancelled");
    }
    if (gameSession.getEndTime().isBefore(now)) {
      throw new InvalidGameSessionException("Game session expired");
    }
  }
}
