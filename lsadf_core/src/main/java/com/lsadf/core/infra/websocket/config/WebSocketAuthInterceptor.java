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
package com.lsadf.core.infra.websocket.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

  private final JwtDecoder jwtDecoder;

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes)
      throws Exception {
    try {
      String token = extractToken(request);
      if (token == null) {
        log.warn("No token found in WebSocket handshake");
        return false;
      }

      Jwt jwt = jwtDecoder.decode(token);
      attributes.put("jwt", jwt);
      attributes.put("userId", jwt.getSubject());

      log.info("WebSocket authentication successful for user: {}", jwt.getSubject());
      return true;

    } catch (Exception e) {
      log.error("WebSocket authentication failed", e);
      return false;
    }
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      @Nullable Exception exception) {}

  private @Nullable String extractToken(ServerHttpRequest request) {
    String query = request.getURI().getQuery();
    if (query != null && query.contains("token=")) {
      String[] params = query.split("&");
      for (String param : params) {
        if (param.startsWith("token=")) {
          return param.substring(6);
        }
      }
    }
    return null;
  }
}
