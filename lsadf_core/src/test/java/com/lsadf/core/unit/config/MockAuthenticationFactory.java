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

import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

@UtilityClass
public class MockAuthenticationFactory {
  public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor createMockJwt(
      String username, List<String> roles, String name) {
    return SecurityMockMvcRequestPostProcessors.jwt()
        .jwt(
            jwt -> {
              jwt.tokenValue("mock-token-value");
              jwt.claim("name", name);
              jwt.claim("scope", "openid profile");
              jwt.claim("preferred_username", username);
              jwt.claim("email_verified", true);
              jwt.claim("realm_access", java.util.Map.of("roles", roles));
            })
        .authorities(roles.stream().map(role -> (GrantedAuthority) () -> "ROLE_" + role).toList());
  }
}
