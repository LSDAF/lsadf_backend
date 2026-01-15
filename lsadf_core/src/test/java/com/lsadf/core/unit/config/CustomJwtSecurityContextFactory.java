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
package com.lsadf.core.unit.config;

import java.util.Collections;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class CustomJwtSecurityContextFactory
    implements WithSecurityContextFactory<WithMockJwtUser> {

  private final SecurityContextHolderStrategy securityContextHolderStrategy =
      SecurityContextHolder.getContextHolderStrategy();

  @Override
  public SecurityContext createSecurityContext(WithMockJwtUser mockUser) {
    SecurityContext context = securityContextHolderStrategy.createEmptyContext();

    // Create a mock Jwt object with the desired claims
    Jwt mockJwt = Mockito.mock(Jwt.class);
    String username = mockUser.username();
    String name = mockUser.name();
    boolean emailVerified = mockUser.emailVerified();
    boolean enabled = mockUser.enabled();

    Mockito.when(mockJwt.getClaimAsString("preferred_username")).thenReturn(username);
    Mockito.when(mockJwt.getClaimAsString("name")).thenReturn(name);
    Mockito.when(mockJwt.getClaimAsBoolean("email_verified")).thenReturn(emailVerified);
    Mockito.when(mockJwt.getClaimAsBoolean("enabled")).thenReturn(enabled);
    // Create the authentication token
    JwtAuthenticationToken authentication =
        new JwtAuthenticationToken(
            mockJwt,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + mockUser.roles()[0])));

    context.setAuthentication(authentication);
    return context;
  }
}
