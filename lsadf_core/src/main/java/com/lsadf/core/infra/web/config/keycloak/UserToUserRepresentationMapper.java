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

package com.lsadf.core.infra.web.config.keycloak;

import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.mappers.Mapper;
import java.util.Date;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;

/**
 * UserToUserRepresentationMapper maps a {@link User} object to a {@link UserRepresentation} object.
 *
 * <p>This implementation of the {@link Mapper} interface processes specific properties of the
 * {@link User} instance, including attributes like username, first name, last name, email
 * verification status, account activation status, and assigned roles. Additionally, it generates a
 * unique identifier and a creation timestamp for the resulting {@link UserRepresentation}.
 */
public class UserToUserRepresentationMapper implements Mapper<User, UserRepresentation> {
  /** {@inheritDoc} */
  @Override
  public UserRepresentation mapToModel(User user) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setCreatedTimestamp(new Date().getTime());
    userRepresentation.setUsername(user.getUsername());
    userRepresentation.setId(UUID.randomUUID().toString());
    userRepresentation.setFirstName(user.getFirstName());
    userRepresentation.setLastName(user.getLastName());
    userRepresentation.setEmailVerified(user.getEmailVerified());
    userRepresentation.setEnabled(user.getEnabled());
    userRepresentation.setRealmRoles(user.getUserRoles());

    return userRepresentation;
  }
}
