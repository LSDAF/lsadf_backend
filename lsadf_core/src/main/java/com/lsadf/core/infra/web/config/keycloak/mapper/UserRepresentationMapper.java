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
import com.lsadf.core.infra.util.DateUtils;
import com.lsadf.core.shared.model.ModelMapper;
import java.util.Date;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * UserRepresentationMapper is an interface that defines the mapping behavior for converting {@link
 * UserRepresentation} objects to {@link User} objects.
 *
 * <p>This interface extends the {@link ModelMapper} interface, specializing its type parameters for
 * mapping from UserRepresentation to User. It provides a centralized and reusable mapping logic
 * between these specific types.
 *
 * <p>The implementation of this interface is generated at runtime by the {@link Mappers} utility,
 * ensuring type safety and reducing the need for boilerplate mapping code.
 *
 * <p>Usage of this interface typically involves injecting the generated instance and calling the
 * defined {@link #map(UserRepresentation)} method for direct model conversion.
 *
 * <p>Example scenarios where this mapper might be used include: - Converting a UserRepresentation
 * object fetched from an external system (e.g., Keycloak) into a User object used within the
 * application domain. - Ensuring consistency and standardization in mapping logic across the
 * application.
 *
 * <p>Note: The instance of this mapper is initialized as a singleton using {@link
 * Mappers#getMapper(Class)} for efficient memory use and centralized mapping logic.
 */
@Mapper(imports = DateUtils.class)
public interface UserRepresentationMapper extends ModelMapper<UserRepresentation, User> {
  UserRepresentationMapper INSTANCE = Mappers.getMapper(UserRepresentationMapper.class);

  @Override
  @Mapping(
      source = "createdTimestamp",
      target = "createdTimestamp",
      qualifiedByName = "mapTimestampToDate")
  @Mapping(source = "realmRoles", target = "userRoles")
  @Mapping(source = "id", target = "id", qualifiedByName = "stringToUUID")
  User map(UserRepresentation userRepresentation);

  @Named("mapTimestampToDate")
  default Date map(Long timestamp) {
    return DateUtils.dateFromTimestamp(timestamp);
  }

  @Named("stringToUUID")
  default UUID map(String id) {
    return UUID.fromString(id);
  }
}
