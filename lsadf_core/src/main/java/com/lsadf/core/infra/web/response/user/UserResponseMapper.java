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

package com.lsadf.core.infra.web.response.user;

import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.response.ModelResponseMapper;

/**
 * A class responsible for mapping instances of the {@link User} domain model to instances of the
 * {@link UserResponse} response data transfer object (DTO).
 *
 * <p>This implementation of the {@link ModelResponseMapper} interface provides the mapping logic
 * necessary for converting domain-specific {@link User} entities into corresponding externally
 * exposed representations of {@link UserResponse}.
 *
 * <p>The mapping process involves extracting relevant fields from the {@link User} instance (e.g.,
 * user ID, first name, last name, username, roles, etc.) and populating the equivalent fields in a
 * {@link UserResponse} instance using the builder pattern.
 */
public class UserResponseMapper implements ModelResponseMapper<User, UserResponse> {
  /**
   * Maps a User domain model to a UserResponse DTO.
   *
   * @param model the User instance to be mapped
   * @return a UserResponse instance containing the mapped data
   */
  @Override
  public UserResponse map(User model) {
    return UserResponse.builder()
        .id(model.getId())
        .firstName(model.getFirstName())
        .lastName(model.getLastName())
        .username(model.getUsername())
        .userRoles(model.getUserRoles())
        .emailVerified(model.getEmailVerified())
        .enabled(model.getEnabled())
        .createdTimestamp(model.getCreatedTimestamp())
        .build();
  }
}
