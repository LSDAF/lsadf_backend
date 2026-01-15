/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.web.dto.response.user;

import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.dto.response.ModelResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Interface representing a mapper for converting a User domain model to its corresponding
 * UserResponse Data Transfer Object (DTO).
 *
 * <p>This interface extends the {@link ModelResponseMapper} interface, which provides a generic
 * definition for mapping domain models to their associated response objects. The UserResponseMapper
 * serves as a concrete mapper specifically designed for handling the mapping logic from {@code
 * User} to {@code UserResponse}.
 *
 * <p>The implementation of this mapper is automatically generated at runtime by the MapStruct
 * library. The generated implementation is responsible for mapping all relevant fields between the
 * User and UserResponse objects and is accessible as a static instance variable {@code INSTANCE}.
 *
 * <p>The map method defined by this interface ensures that a given User domain model is transformed
 * into a corresponding UserResponse DTO, which conforms to the structure required for API responses
 * or other external representations.
 */
@Mapper
public interface UserResponseMapper extends ModelResponseMapper<User, UserResponse> {
  UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

  /**
   * Maps a User domain model to a UserResponse DTO.
   *
   * @param model the User instance to be mapped
   * @return a UserResponse instance containing the mapped data
   */
  @Override
  UserResponse map(User model);
}
