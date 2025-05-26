/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.common.utils;

import static com.lsadf.core.constants.ClaimsConstants.*;

import com.lsadf.core.domain.user.UserInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
public class TokenUtils {
  private static final String REALM_ACCESS = "realm_access";
  private static final String ROLES = "roles";

  /**
   * Get roles from jwt object
   *
   * @param jwt the jwt
   * @return the roles
   */
  public static List<GrantedAuthority> getRolesFromJwt(Jwt jwt) {
    Map<String, Object> claims = jwt.getClaims();
    List<String> roles = new ArrayList<>();
    if (claims.containsKey(REALM_ACCESS)) {
      Map<String, Object> realmAccess = (Map<String, Object>) claims.get(REALM_ACCESS);
      roles = ((List<String>) realmAccess.get(ROLES));
    }
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());
  }

  /**
   * Get username from jwt
   *
   * @param jwt the jwt
   * @return the username email
   */
  public static String getUsernameFromJwt(Jwt jwt) {
    return jwt.getClaimAsString(PREFERRED_USERNAME);
  }

  /**
   * Get user info from jwt
   *
   * @param jwt the jwt
   * @return the user info
   */
  public static UserInfo getUserInfoFromJwt(Jwt jwt) {
    String username = getUsernameFromJwt(jwt);
    String name = getNameFromJwt(jwt);
    boolean verified = getEmailVerifiedFromJwt(jwt);
    List<GrantedAuthority> authorities = getRolesFromJwt(jwt);
    Set<String> roles =
        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    return new UserInfo(name, username, verified, roles);
  }

  /**
   * Get name from jwt
   *
   * @param jwt the jwt
   * @return the name
   */
  public static String getNameFromJwt(Jwt jwt) {
    return jwt.getClaimAsString(NAME);
  }

  /**
   * Get email verified from jwt
   *
   * @param jwt the jwt
   * @return the email verified
   */
  public static boolean getEmailVerifiedFromJwt(Jwt jwt) {
    return jwt.getClaimAsBoolean(EMAIL_VERIFIED);
  }
}
