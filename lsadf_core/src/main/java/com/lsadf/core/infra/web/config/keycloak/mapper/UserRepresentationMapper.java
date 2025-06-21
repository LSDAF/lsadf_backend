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

package com.lsadf.core.infra.web.config.keycloak.mapper;

import com.lsadf.core.domain.user.User;
import com.lsadf.core.shared.model.ModelMapper;
import java.util.Date;
import org.keycloak.representations.idm.UserRepresentation;

/**
 * UserRepresentationMapper maps a {@link UserRepresentation} object to a {@link User} object.
 *
 * <p>This implementation of the {@link ModelMapper} interface processes and converts specific
 * properties of a {@link UserRepresentation}, including attributes such as username, first name,
 * last name, email verification status, account activation status, roles, and creation timestamp.
 * Additionally, it ensures proper handling of optional fields by verifying null values before
 * setting them to avoid potential runtime exceptions.
 */
public class UserRepresentationMapper implements ModelMapper<UserRepresentation, User> {

  /** {@inheritDoc} */
  @Override
  public User map(UserRepresentation userRepresentation) {
    Date createdTimestamp =
        (userRepresentation.getCreatedTimestamp() != null)
            ? new Date(userRepresentation.getCreatedTimestamp())
            : null;
    return User.builder()
        .username(userRepresentation.getUsername())
        .firstName(userRepresentation.getFirstName())
        .lastName(userRepresentation.getLastName())
        .id(userRepresentation.getId())
        .emailVerified(userRepresentation.isEmailVerified())
        .enabled(userRepresentation.isEnabled())
        .createdTimestamp(createdTimestamp)
        .userRoles(userRepresentation.getRealmRoles())
        .build();
  }
}
