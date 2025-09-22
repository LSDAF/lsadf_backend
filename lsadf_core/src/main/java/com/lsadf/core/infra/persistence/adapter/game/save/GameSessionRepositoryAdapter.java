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

package com.lsadf.core.infra.persistence.adapter.game.save;

import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.impl.game.session.GameSessionRepository;
import com.lsadf.core.infra.persistence.impl.view.GameSessionViewEntity;
import com.lsadf.core.infra.persistence.impl.view.GameSessionViewMapper;
import com.lsadf.core.infra.persistence.impl.view.GameSessionViewRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class GameSessionRepositoryAdapter implements GameSessionRepositoryPort {

  private final GameSessionRepository gameSessionRepository;
  private final GameSessionViewRepository gameSessionViewRepository;

  private static final GameSessionViewMapper gameSessionViewMapper = GameSessionViewMapper.INSTANCE;

  @Override
  @Transactional(readOnly = true)
  public Optional<GameSession> getGameSessionById(UUID id) {
    Optional<GameSessionViewEntity> gameSessionEntity =
        gameSessionViewRepository.findLatestBySessionId(id);
    if (gameSessionEntity.isPresent()) {
      GameSession result = gameSessionViewMapper.map(gameSessionEntity.get());
      return Optional.of(result);
    }
    return Optional.empty();
  }

  @Override
  @Transactional(readOnly = true)
  public List<GameSession> getGameSessionsByGameSaveId(UUID gameSaveId) {
    List<GameSessionViewEntity> databaseResults =
        gameSessionViewRepository.findByGameSaveId(gameSaveId);
    return databaseResults.stream().map(gameSessionViewMapper::map).toList();
  }

  @Override
  @Transactional
  public GameSession createNewGameSession(UUID id, UUID gameSaveId, Instant endTime) {
    Optional<GameSessionViewEntity> optional =
        gameSessionViewRepository.findLatestActiveGameSessionByGameSaveId(gameSaveId);
    optional.ifPresent(gameSession -> cancelGameSession(gameSession.getId()));
    gameSessionRepository.createNewGameSession(id, gameSaveId, endTime);
    return gameSessionViewRepository
        .findLatestActiveGameSessionByGameSaveId(gameSaveId)
        .map(gameSessionViewMapper::map)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Didn't find any active game session for gameSaveId " + gameSaveId));
  }

  @Override
  @Transactional
  public GameSession createNewGameSession(
      UUID id, UUID gameSaveId, Instant endTime, boolean cancelled) {
    Optional<GameSessionViewEntity> optional =
        gameSessionViewRepository.findLatestActiveGameSessionByGameSaveId(gameSaveId);
    optional.ifPresent(gameSession -> cancelGameSession(gameSession.getId()));
    gameSessionRepository.createNewGameSession(id, gameSaveId, endTime, cancelled);
    return gameSessionViewRepository
        .findLatestActiveGameSessionByGameSaveId(gameSaveId)
        .map(gameSessionViewMapper::map)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Didn't find any active game session for gameSaveId " + gameSaveId));
  }

  @Override
  @Transactional
  public GameSession updateGameSessionEndTime(UUID sessionId, Instant endTime) {
    gameSessionRepository.updateGameSession(sessionId, endTime);
    return gameSessionViewRepository
        .findLatestBySessionId(sessionId)
        .map(gameSessionViewMapper::map)
        .orElseThrow(
            () -> new NotFoundException("Didn't find any game session with id " + sessionId));
  }

  private void cancelGameSession(UUID sessionId) {
    gameSessionRepository.cancelGameSession(sessionId);
  }
}
