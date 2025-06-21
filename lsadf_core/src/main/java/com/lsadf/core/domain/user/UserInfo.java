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
package com.lsadf.core.domain.user;

import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;

/**
 * Immutable record representing user information.
 *
 * <p>This record encapsulates the essential details of a user, including their name, email,
 * verification status, and roles. It is designed to be lightweight and immutable while serving as a
 * data transfer object.
 *
 * <p>Key features: - Implements the {@code Model} interface for serialization purposes. - Overrides
 * {@code equals} and {@code hashCode} for logical equality and hash-based collections.
 */
@Builder
public record UserInfo(String name, String email, boolean verified, Set<String> roles)
    implements Model {

  @Serial private static final long serialVersionUID = -3162522781668155748L;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserInfo userInfo = (UserInfo) o;
    return verified == userInfo.verified
        && Objects.equals(name, userInfo.name)
        && Objects.equals(email, userInfo.email)
        && Objects.equals(roles, userInfo.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, email, verified, roles);
  }
}
