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

import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.infra.websocket.interceptor.WebSocketAuthInterceptor;
import com.lsadf.core.infra.websocket.interceptor.WebSocketGameSessionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@Import(WebSocketHandlerConfiguration.class)
public class WebSocketConfiguration implements WebSocketConfigurer {

  private final GameWebSocketHandler gameWebSocketHandler;
  private final JwtDecoder jwtDecoder;
  private final GameSessionQueryService gameSessionQueryService;

  @Bean
  public WebSocketAuthInterceptor webSocketAuthInterceptor() {
    return new WebSocketAuthInterceptor(jwtDecoder);
  }

  @Bean
  public WebSocketGameSessionValidator webSocketGameSessionValidator(
      GameSessionQueryService gameSessionQueryService) {
    return new WebSocketGameSessionValidator(gameSessionQueryService);
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
        .addHandler(gameWebSocketHandler, "/ws/game")
        .addInterceptors(
            webSocketAuthInterceptor(), webSocketGameSessionValidator(gameSessionQueryService))
        .setAllowedOrigins("*");
  }
}
