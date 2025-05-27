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

package com.lsadf.core.infra.web.requests.user.creation;

import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.requests.RequestModelMapper;
import java.util.Date;

/**
 * A mapper implementation responsible for converting {@link UserCreationRequest} objects into their
 * corresponding {@link User} model representations. This class implements the {@link
 * RequestModelMapper} interface to define the specific mapping logic required for creating a {@link
 * User} instance based on the incoming request data.
 *
 * <p>The mapping process includes transferring properties such as first name, last name, username,
 * user roles, and whether the email is verified or enabled. Additionally, it sets the user creation
 * timestamp to the current date.
 *
 * <p>The {@code mapToModel} method is the core function that performs the mapping operation,
 * ensuring all necessary attributes are appropriately populated in the {@link User} model.
 */
public class UserCreationRequestModelMapper
    implements RequestModelMapper<UserCreationRequest, User> {
  /** {@inheritDoc} */
  @Override
  public User mapToModel(UserCreationRequest userCreationRequestImpl) {
    return User.builder()
        .firstName(userCreationRequestImpl.getFirstName())
        .lastName(userCreationRequestImpl.getLastName())
        .username(userCreationRequestImpl.getUsername())
        .userRoles(userCreationRequestImpl.getUserRoles())
        .emailVerified(userCreationRequestImpl.getEmailVerified())
        .enabled(userCreationRequestImpl.getEnabled())
        .createdTimestamp(new Date())
        .build();
  }
}
