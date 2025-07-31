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
package com.lsadf.admin.application.user;

import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.util.StreamUtils;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.request.user.UserSortingParameter;
import com.lsadf.core.infra.web.request.user.creation.AdminUserCreationRequest;
import com.lsadf.core.infra.web.request.user.update.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.user.UserResponse;
import com.lsadf.core.infra.web.response.user.UserResponseMapper;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** The implementation of the AdminUserController */
@RestController
@Slf4j
public class AdminUserControllerImpl extends BaseController implements AdminUserController {

  private final UserService userService;

  private static final UserResponseMapper userResponseMapper = UserResponseMapper.INSTANCE;

  @Autowired
  public AdminUserControllerImpl(UserService userService) {
    this.userService = userService;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers(Jwt jwt, List<String> orderBy) {
    List<UserSortingParameter> sortingParameterList =
        Collections.singletonList(UserSortingParameter.NONE);
    if (orderBy != null && !orderBy.isEmpty()) {
      sortingParameterList = orderBy.stream().map(UserSortingParameter::fromString).toList();
    }
    Stream<User> users = StreamUtils.sortUsers(userService.getUsers(), sortingParameterList);
    List<UserResponse> userList = users.map(userResponseMapper::map).toList();
    return generateResponse(HttpStatus.OK, userList);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(Jwt jwt, UUID userId) {
    validateUser(jwt);
    User user = userService.getUserById(userId);
    UserResponse response = userResponseMapper.map(user);
    return generateResponse(HttpStatus.OK, response);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(Jwt jwt, String username) {
    validateUser(jwt);
    User user = userService.getUserByUsername(username);
    UserResponse response = userResponseMapper.map(user);
    return generateResponse(HttpStatus.OK, response);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<UserResponse>> updateUser(
      Jwt jwt, UUID userId, AdminUserUpdateRequest user) {
    validateUser(jwt);
    User updatedUser = userService.updateUser(userId, user);
    UserResponse response = userResponseMapper.map(updatedUser);
    return generateResponse(HttpStatus.OK, response);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<Void>> deleteUser(Jwt jwt, UUID userId) {
    validateUser(jwt);
    userService.deleteUser(userId);
    return generateResponse(HttpStatus.OK);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public ResponseEntity<ApiResponse<UserResponse>> createUser(
      Jwt jwt, AdminUserCreationRequest adminUserCreationRequest) {
    validateUser(jwt);
    User user = userService.createUser(adminUserCreationRequest);
    UserResponse userResponse = userResponseMapper.map(user);
    return generateResponse(HttpStatus.OK, userResponse);
  }
}
