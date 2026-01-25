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

package com.lsadf.core.infra.websocket.interceptor;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.domain.game.session.GameSession;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@RequiredArgsConstructor
public class WebSocketGameSessionValidator implements HandshakeInterceptor {

  private final GameSessionQueryService gameSessionQueryService;

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes)
      throws Exception {
    String gameSessionId = extractGameSessionId(request);
    if (gameSessionId == null || gameSessionId.isEmpty()) {
      log.error("No gameSessionId found in WebSocket handshake");
      return false;
    }

    try {
      UUID gameSessionUuid = UUID.fromString(gameSessionId);
      GameSession gameSession = gameSessionQueryService.findGameSessionById(gameSessionUuid);

      String userEmail = attributes.get(USER_EMAIL).toString();
      if (!gameSession.getUserEmail().equals(userEmail)) {
        log.error("Game session {} does not belong to user {}", gameSessionId, userEmail);
        return false;
      }

      gameSessionQueryService.checkGameSessionValidity(
          gameSessionUuid, gameSession.getGameSaveId(), java.time.Instant.now());

      attributes.put(GAME_SESSION_ID, gameSessionUuid);
      attributes.put(GAME_SAVE_ID, gameSession.getGameSaveId());
      log.info("Websocket GameSession validation successful for session: {}", gameSessionId);
      return true;
    } catch (Exception e) {
      log.error("Error. Cannot validate WebSocket game session: {}", gameSessionId, e);
      return false;
    }
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      @Nullable Exception exception) {}

  private @Nullable String extractGameSessionId(ServerHttpRequest request) {
    var uri = request.getURI();
    var query = uri.getQuery();
    if (query == null || query.isEmpty()) {
      return null;
    }
    var params = query.split("&");
    for (var param : params) {
      var keyValue = param.split("=");
      if (keyValue.length == 2 && keyValue[0].equals(GAME_SESSION_ID)) {
        return keyValue[1];
      }
    }
    return null;
  }
}
