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
package com.lsadf.core.unit.infra.websocket.config;

import static com.lsadf.core.infra.web.JsonAttributes.USER_EMAIL;
import static com.lsadf.core.infra.web.JsonAttributes.USER_ROLES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lsadf.core.infra.web.config.auth.TokenUtils;
import com.lsadf.core.infra.websocket.interceptor.WebSocketAuthInterceptor;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.socket.WebSocketHandler;

@ExtendWith(MockitoExtension.class)
class WebSocketAuthInterceptorTests {

  @Mock private JwtDecoder jwtDecoder;

  @Mock private ServerHttpRequest request;

  @Mock private ServerHttpResponse response;

  @Mock private WebSocketHandler wsHandler;

  @Mock private Jwt jwt;

  private WebSocketAuthInterceptor interceptor;
  private Map<String, Object> attributes;

  private static final String USER_ID = "toto@test.com";
  private static final List<String> ROLES = List.of("ROLE_USER");
  private static final String VALID_TOKEN = "valid.jwt.token";

  @BeforeEach
  void setUp() {
    interceptor = new WebSocketAuthInterceptor(jwtDecoder);
    attributes = new HashMap<>();
  }

  @Test
  void shouldAuthenticateSuccessfullyWithValidToken() throws Exception {
    URI uri = new URI("ws://localhost:8080/ws?token=" + VALID_TOKEN);
    when(request.getURI()).thenReturn(uri);
    when(jwtDecoder.decode(VALID_TOKEN)).thenReturn(jwt);

    try (MockedStatic<TokenUtils> tokenUtilsMockedStatic = mockStatic(TokenUtils.class)) {
      tokenUtilsMockedStatic.when(() -> TokenUtils.getUsernameFromJwt(jwt)).thenReturn(USER_ID);

      tokenUtilsMockedStatic.when(() -> TokenUtils.getRolesFromJwt(jwt)).thenReturn(ROLES);

      boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

      assertTrue(result);
      assertEquals(USER_ID, attributes.get(USER_EMAIL));
      assertEquals(ROLES, attributes.get(USER_ROLES));
      verify(jwtDecoder).decode(VALID_TOKEN);
    }
  }

  @Test
  void shouldRejectHandshakeWhenNoTokenProvided() throws Exception {
    URI uri = new URI("ws://localhost:8080/ws");
    when(request.getURI()).thenReturn(uri);

    boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

    assertFalse(result);
    assertFalse(attributes.containsKey("jwt"));
    assertFalse(attributes.containsKey("userId"));
    verify(jwtDecoder, never()).decode(anyString());
  }

  @Test
  void shouldRejectHandshakeWhenTokenIsEmpty() throws Exception {
    URI uri = new URI("ws://localhost:8080/ws?token=");
    when(request.getURI()).thenReturn(uri);

    boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

    assertFalse(result);
    assertFalse(attributes.containsKey("jwt"));
    verify(jwtDecoder, never()).decode(anyString());
  }

  @Test
  void shouldRejectHandshakeWhenTokenIsInvalid() throws Exception {
    URI uri = new URI("ws://localhost:8080/ws?token=invalid.token");
    when(request.getURI()).thenReturn(uri);
    when(jwtDecoder.decode("invalid.token")).thenThrow(new JwtException("Invalid token"));

    boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

    assertFalse(result);
    assertFalse(attributes.containsKey("jwt"));
    verify(jwtDecoder).decode("invalid.token");
  }

  @Test
  void shouldExtractTokenFromQueryStringWithMultipleParams() throws Exception {
    URI uri =
        new URI("ws://localhost:8080/ws?param1=value1&token=" + VALID_TOKEN + "&param2=value2");
    when(request.getURI()).thenReturn(uri);
    when(jwtDecoder.decode(VALID_TOKEN)).thenReturn(jwt);

    boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

    assertTrue(result);
    verify(jwtDecoder).decode(VALID_TOKEN);
  }

  @Test
  void shouldHandleNullQueryString() throws Exception {
    URI uri = new URI("ws://localhost:8080/ws");
    when(request.getURI()).thenReturn(uri);

    boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

    assertFalse(result);
    verify(jwtDecoder, never()).decode(anyString());
  }

  @Test
  void afterHandshakeShouldDoNothing() {
    assertDoesNotThrow(() -> interceptor.afterHandshake(request, response, wsHandler, null));
  }
}
