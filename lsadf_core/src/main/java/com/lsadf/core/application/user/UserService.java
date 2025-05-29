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
package com.lsadf.core.application.user;

import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.requests.user.creation.UserCreationRequest;
import com.lsadf.core.infra.web.requests.user.update.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.requests.user.update.UserUpdateRequest;
import java.util.List;
import java.util.stream.Stream;

/** Service for managing users */
public interface UserService {

  /**
   * @return List of all users
   */
  Stream<User> getUsers();

  /**
   * Counts the total number of users in the system.
   *
   * @return the total count of users as a Long value
   */
  Long countUsers();

  /**
   * Search users by name, or by username
   *
   * @param search search query
   * @return Stream of users
   */
  Stream<User> getUsers(String search);

  /**
   * Get user roles defined in keycloak
   *
   * @return list of user roles
   */
  List<String> getUserRoles();

  /**
   * Get user by id
   *
   * @param id user id
   * @return user
   */
  User getUserById(String id);

  /**
   * Get user by email
   *
   * @param email user email
   * @return user
   */
  User getUserByUsername(String email);

  /**
   * Update existing user
   *
   * @param user user to update
   */
  User updateUser(String id, AdminUserUpdateRequest user);

  /**
   * Update existing user
   *
   * @param id user id
   * @param user user to update
   */
  void updateUser(String id, UserUpdateRequest user);

  /**
   * Reset user password
   *
   * @param id user id
   */
  void resetUserPassword(String id);

  /**
   * Delete user by id
   *
   * @param id user id
   */
  void deleteUser(String id);

  /**
   * Creates a new user based on the provided user creation request.
   *
   * @param userCreationRequest the request containing details needed to create a new user
   * @return the newly created user
   */
  User createUser(UserCreationRequest userCreationRequest);

  /**
   * Check if user exists with its username
   *
   * @param username username
   * @return true if user exists
   */
  boolean checkUsernameExists(String username);

  /**
   * Check if user exists with its id
   *
   * @param id user id
   * @return true if user exists
   */
  boolean checkIdExists(String id);
}
