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

import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.web.response.ModelResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * This interface defines a mapper to transform {@link UserInfo} model instances into {@link
 * UserInfoResponse} objects.
 *
 * <p>It extends {@link ModelResponseMapper} to inherit the general mapping contract between domain
 * model and response types, specifically specializing in mapping {@link UserInfo} to {@link
 * UserInfoResponse}.
 *
 * <p>The implementation relies on the MapStruct library to generate the mapping logic at compile
 * time. The instance {@code INSTANCE} is provided for convenient access to the generated
 * implementation.
 *
 * <p>Key functionality includes translating the attributes of {@link UserInfo}, such as the user's
 * name, email, verification status, and roles, into the corresponding fields of {@link
 * UserInfoResponse}.
 *
 * <p>Example usage scenarios where this is helpful: - Converting backend domain objects into
 * user-facing API responses. - Ensuring data consistency and transformation logic is centralized
 * and reusable.
 *
 * <p>This mapper supports immutability by working with immutable data transfer objects ({@code
 * record} types) for both the input model and the output response.
 */
@Mapper
public interface UserInfoResponseMapper extends ModelResponseMapper<UserInfo, UserInfoResponse> {

  UserInfoResponseMapper INSTANCE = Mappers.getMapper(UserInfoResponseMapper.class);

  /**
   * Maps a given UserInfo model object to a corresponding GlobalInfoResponse object.
   *
   * @param model the UserInfo model object to be mapped
   * @return the GlobalInfoResponse object representing the mapped data
   */
  @Override
  @Mapping(target = "username", source = "email")
  UserInfoResponse map(UserInfo model);
}
