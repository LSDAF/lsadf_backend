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
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * UserToUserRepresentationMapper is a MapStruct-based mapper interface designed to handle the
 * transformation of {@link User} domain objects into {@link UserRepresentation} objects.
 *
 * <p>This interface extends the {@code Mapper} interface, providing type-safe and reusable mapping
 * functionality for converting between the {@code User} and {@code UserRepresentation} models.
 *
 * <p>The implementation of this mapper is generated at runtime by MapStruct, eliminating the need
 * for manual mapping code and ensuring consistency across the application. The generated instance
 * of this mapper can be accessed as a singleton through the static {@link #INSTANCE} field.
 *
 * <p>Key features: - Ignores the mapping of the {@code id} and {@code createdTimestamp} fields in
 * the generated output to prevent unintentional overwrites during the transformation process. -
 * Facilitates seamless conversion between domain and representation models in service or controller
 * layers.
 *
 * <p>Typical scenarios for using this mapper might include: - Preparing a {@code
 * UserRepresentation} object to be sent as a response in API layers. - Converting domain entities
 * to external representation formats in multi-service communication
 */
@Mapper
public interface UserToUserRepresentationMapper
    extends com.lsadf.core.shared.mapper.Mapper<User, UserRepresentation> {
  UserToUserRepresentationMapper INSTANCE = Mappers.getMapper(UserToUserRepresentationMapper.class);

  /** {@inheritDoc} */
  @Mapping(target = "createdTimestamp", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Override
  UserRepresentation map(User user);
}
