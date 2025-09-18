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
import com.lsadf.core.application.game.session.GameSessionCommandService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.application.game.session.command.InitializeSessionCommand;
import com.lsadf.core.application.game.session.command.UpdateSessionEndTimeCommand;
import com.lsadf.core.domain.game.session.GameSession;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameSessionCommandServiceImpl implements GameSessionCommandService {

  private final GameSessionRepositoryPort gameSessionRepositoryPort;
  private final CacheManager cacheManager;
  private final GameSessionCachePort gameSessionCachePort;
  private final ClockService clockService;

  @Override
  public GameSession initializeGameSession(InitializeSessionCommand command) {
    Instant instant = clockService.nowInstant();
    Instant added = instant.plus(15, ChronoUnit.MINUTES);
    GameSession gameSession =
        gameSessionRepositoryPort.createNewGameSession(command.gameSaveId(), added);
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      gameSessionCachePort.set(gameSession.getId().toString(), gameSession);
    }
    return gameSession;
  }

  @Override
  public GameSession updateGameSessionEndTime(UpdateSessionEndTimeCommand command) {
    Instant instant = clockService.getClock().instant();
    Instant added = instant.plus(15, ChronoUnit.MINUTES);
    GameSession updated =
        gameSessionRepositoryPort.updateGameSessionEndTime(command.gameSessionId(), added);
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      gameSessionCachePort.set(updated.getId().toString(), updated);
    }
    return updated;
  }
}
