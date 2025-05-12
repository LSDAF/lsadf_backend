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
package com.lsadf.core.constants;

import lombok.Getter;

@Getter
public enum UserRole {
  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");

  UserRole(String role) {
    this.role = role;
  }

  private final String role;

  public String getName() {
    return this.name();
  }

  public static UserRole fromRole(String role) {
    for (UserRole userRole : UserRole.values()) {
      if (userRole.getRole().equals(role)) {
        return userRole;
      }
    }
    return null;
  }

  public static UserRole fromName(String name) {
    for (UserRole userRole : UserRole.values()) {
      if (userRole.name().equals(name)) {
        return userRole;
      }
    }
    return null;
  }

  public static UserRole getDefaultRole() {
    return USER;
  }
}
