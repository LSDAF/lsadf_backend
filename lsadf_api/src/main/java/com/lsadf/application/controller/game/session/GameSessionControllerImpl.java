/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.application.controller.game.session;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.session.GameSessionCommandService;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.application.game.session.command.InitializeSessionCommand;
import com.lsadf.core.application.game.session.command.UpdateSessionEndTimeCommand;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.exception.http.ForbiddenException;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.session.GameSessionResponse;
import com.lsadf.core.infra.web.dto.response.game.session.GameSessionResponseMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the GameSessionController interface. */
@RestController
@ConditionalOnProperty(prefix = "api", name = "enabled", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class GameSessionControllerImpl extends BaseController implements GameSessionController {

  private final GameSessionCommandService gameSessionCommandService;
  private final ClockService clockService;
  private final GameSaveService gameSaveService;
  private static final int SESSION_EXPIRATION_SECONDS = 3600;

  private static final GameSessionResponseMapper gameSessionResponseMapper =
      GameSessionResponseMapper.INSTANCE;
  private final GameSessionQueryService gameSessionQueryService;

  @Override
  public ResponseEntity<ApiResponse<GameSessionResponse>> openNewGameSession(
      Jwt jwt, UUID gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Instant endTime = computeEndTime(clockService.nowInstant(), SESSION_EXPIRATION_SECONDS);
    InitializeSessionCommand initializeSessionCommand =
        new InitializeSessionCommand(gameSaveId, endTime);
    GameSession newGameSession =
        gameSessionCommandService.initializeGameSession(initializeSessionCommand);
    GameSessionResponse gameSessionResponse = gameSessionResponseMapper.map(newGameSession);
    return generateResponse(HttpStatus.OK, gameSessionResponse);
  }

  @Override
  public ResponseEntity<ApiResponse<GameSessionResponse>> refreshGameSession(
      Jwt jwt, UUID gameSessionId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    Instant now = clockService.nowInstant();
    GameSession gameSession = gameSessionQueryService.findGameSessionById(gameSessionId);
    gameSaveService.checkGameSaveOwnership(gameSession.getGameSaveId(), userEmail);
    if (gameSession.isCancelled()) {
      throw new ForbiddenException("Game session has already been cancelled");
    }
    if (now.isAfter(gameSession.getEndTime())) {
      throw new ForbiddenException("Game session has expired");
    }
    Instant endTime = computeEndTime(now, SESSION_EXPIRATION_SECONDS);
    UpdateSessionEndTimeCommand updateSessionEndTimeCommand =
        new UpdateSessionEndTimeCommand(gameSessionId, endTime);
    GameSession updatedGameSession =
        gameSessionCommandService.updateGameSessionEndTime(updateSessionEndTimeCommand);
    GameSessionResponse gameSessionResponse = gameSessionResponseMapper.map(updatedGameSession);
    return generateResponse(HttpStatus.OK, gameSessionResponse);
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  private static Instant computeEndTime(Instant instant, int sessionExpirationSeconds) {
    return instant.plus(sessionExpirationSeconds, ChronoUnit.SECONDS);
  }
}
