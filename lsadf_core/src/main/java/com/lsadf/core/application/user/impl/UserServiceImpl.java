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
package com.lsadf.core.application.user.impl;

import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.exception.AlreadyExistingUserException;
import com.lsadf.core.exception.http.InternalServerErrorException;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.web.config.keycloak.mapper.UserRepresentationMapper;
import com.lsadf.core.infra.web.config.keycloak.mapper.UserToUserRepresentationMapper;
import com.lsadf.core.infra.web.config.keycloak.properties.KeycloakProperties;
import com.lsadf.core.infra.web.request.user.creation.UserCreationRequest;
import com.lsadf.core.infra.web.request.user.creation.UserCreationRequestMapper;
import com.lsadf.core.infra.web.request.user.update.UserUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;

@Slf4j
public class UserServiceImpl implements UserService {

  private final Keycloak keycloak;
  private final KeycloakProperties keycloakProperties;
  private final ClockService clockService;
  private static final UserCreationRequestMapper userCreationRequestMapper =
      UserCreationRequestMapper.INSTANCE;
  private static final UserToUserRepresentationMapper userToUserRepresentationMapper =
      UserToUserRepresentationMapper.INSTANCE;
  private static final UserRepresentationMapper userRepresentationMapper =
      UserRepresentationMapper.INSTANCE;

  private final String realm;

  public UserServiceImpl(
      Keycloak keycloak, KeycloakProperties keycloakProperties, ClockService clockService) {
    this.keycloakProperties = keycloakProperties;
    this.clockService = clockService;
    this.keycloak = keycloak;

    this.realm = keycloakProperties.getRealm();
  }

  @Override
  public Stream<User> getUsers() {
    // return keycloakAdminClient.getUsers(realm).stream();
    try {
      List<UserRepresentation> userlist = getUsersResource().list();
      return userlist.stream().map(this::enrichUserRoles).map(userRepresentationMapper::map);
    } catch (Exception e) {
      log.error("Failed to get users", e);
      throw new InternalServerErrorException("Failed to get users");
    }
  }

  @Override
  public Long countUsers() {
    return (long) getUsersResource().count();
  }

  @Override
  public List<String> getUserRoles() {
    List<RoleRepresentation> roles = getRolesResource().list();
    return roles.stream().map(RoleRepresentation::getName).toList();
  }

  @Override
  public Stream<User> getUsers(String search) {
    if (search == null || search.isEmpty()) {
      return getUsers();
    }

    // return keycloakAdminClient.getUsers(realm, Optional.ofNullable(search)).stream();
    try {
      return getUsersResource().search(search).stream()
          .map(this::enrichUserRoles)
          .map(userRepresentationMapper::map);
    } catch (Exception e) {
      log.error("Failed to get users with search: {}", search, e);
      throw new InternalServerErrorException("Failed to get users with search: " + search);
    }
  }

  @Override
  public User getUserById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    // return keycloakAdminClient.getUserById(realm, id);
    try {
      UserRepresentation userResource = getUserRepresentation(id);
      UserRepresentation enrichedUser = enrichUserRoles(userResource);
      return userRepresentationMapper.map(enrichedUser);
    } catch (jakarta.ws.rs.NotFoundException e) {
      log.error("User with id {} not found", id);
      throw new NotFoundException("User with id " + id + " not found");
    } catch (Exception e) {
      log.error("Failed to get user with id {}", id, e);
      throw new InternalServerErrorException("Failed to get user with id " + id);
    }
  }

  @Override
  public User getUserByUsername(String username) {
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null");
    }
    // return keycloakAdminClient.getUserByUsername(realm, username);
    List<UserRepresentation> userRepresentationResults;
    try {
      userRepresentationResults = getUsersResource().searchByUsername(username, true);
    } catch (Exception e) {
      log.error("Failed to get user with username {}", username, e);
      throw new InternalServerErrorException("Failed to get user with username " + username);
    }
    if (userRepresentationResults == null || userRepresentationResults.isEmpty()) {
      throw new NotFoundException("User with username " + username + " not found");
    }
    var user = userRepresentationResults.get(0);
    var enrichedUser = enrichUserRoles(user);
    return userRepresentationMapper.map(enrichedUser);
  }

  @Override
  public User updateUser(UUID id, UserUpdateRequest adminUpdateRequest) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }

    if (!checkIdExists(id)) {
      throw new NotFoundException("User with id " + id + " not found");
    }

    UserRepresentation existingUser = getUserRepresentation(id);
    try {
      // Update roles
      List<String> userRoles = adminUpdateRequest.getUserRoles();
      if (userRoles != null && !userRoles.isEmpty()) {
        List<RoleRepresentation> roles = userRoles.stream().map(this::getRoleByName).toList();
        String idString = id.toString();
        getUsersResource().get(idString).roles().realmLevel().remove(roles);
        getUsersResource().get(idString).roles().realmLevel().add(roles);
      }
    } catch (NotFoundException e) {
      throw new IllegalArgumentException("Role not found", e);
    } catch (Exception e) {
      log.error("Failed to get roles before creating new user: ", e);
      throw new InternalServerErrorException("Failed to get roles", e);
    }

    boolean hasUpdates = false;
    if (adminUpdateRequest.getFirstName() != null) {
      existingUser.setFirstName(adminUpdateRequest.getFirstName());
      hasUpdates = true;
    }
    if (adminUpdateRequest.getLastName() != null) {
      existingUser.setLastName(adminUpdateRequest.getLastName());
      hasUpdates = true;
    }
    if (adminUpdateRequest.getEmailVerified() != null) {
      existingUser.setEmailVerified(adminUpdateRequest.getEmailVerified());
      hasUpdates = true;
    }
    if (adminUpdateRequest.getEnabled() != null) {
      existingUser.setEnabled(adminUpdateRequest.getEnabled());
      hasUpdates = true;
    }

    if (hasUpdates) {
      String idString = id.toString();
      getUsersResource().get(idString).update(existingUser);
    }

    return getUserById(id);
  }

  @Override
  public void resetUserPassword(UUID id) {
    String updatePasswordAction = "UPDATE_PASSWORD";
    List<String> actions = List.of(updatePasswordAction);
    String idString = id.toString();
    // keycloakAdminClient.sendActionsEmail(realm, id, actions);
    getUsersResource().get(idString).executeActionsEmail(actions);
  }

  @Override
  public void deleteUser(UUID id) {
    if (!checkIdExists(id)) {
      throw new NotFoundException("User with id " + id + " not found");
    }
    String idString = id.toString();
    // keycloakAdminClient.deleteUser(realm, id);
    getUsersResource().get(idString).remove();
  }

  @Override
  public User createUser(UserCreationRequest request) {
    // Check if roles exist
    List<RoleRepresentation> roles = new ArrayList<>();
    try {
      List<String> userRoles = request.getUserRoles();
      if (userRoles != null && !userRoles.isEmpty()) {
        roles = userRoles.stream().map(this::getRoleByName).toList();
      }
    } catch (NotFoundException e) {
      throw new IllegalArgumentException("Role not found", e);
    } catch (Exception e) {
      log.error("Failed to get roles before creating new user: ", e);
      throw new InternalServerErrorException("Failed to get roles", e);
    }

    // Check if username already exists
    if (checkUsernameExists(request.getUsername())) {
      throw new AlreadyExistingUserException(
          "User with username " + request.getUsername() + " already exists");
    }

    // Create user
    User user = userCreationRequestMapper.map(request);
    UserRepresentation userRepresentation = userToUserRepresentationMapper.map(user);
    try (var response = getUsersResource().create(userRepresentation)) {
      if (response.getStatus() != HttpStatus.CREATED.value()) {
        throw new InternalServerErrorException("Failed to create user");
      }
      String responsePath = response.getLocation().getPath();
      int beginIndex = responsePath.lastIndexOf('/') + 1;
      String userId = responsePath.substring(beginIndex);
      UUID uuid = UUID.fromString(userId);

      // Add roles to user
      if (!roles.isEmpty()) {
        getUsersResource().get(userId).roles().realmLevel().add(roles);
      }

      return getUserById(uuid);
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      log.error("Failed to create user", e);
      throw new InternalServerErrorException("Failed to create user: " + e);
    }
  }

  @Override
  public boolean checkUsernameExists(String username) {
    try {
      getUserByUsername(username);
      return true;
    } catch (NotFoundException e) {
      return false;
    }
  }

  @Override
  public boolean checkIdExists(UUID id) {
    // We assert that the null check is done in the calling method to avoid multiple checks
    // id can be null, then we return false
    try {
      getUserById(id);
      return true;
    } catch (NotFoundException e) {
      return false;
    }
  }

  /**
   * Get user representation by id
   *
   * @param id user id
   * @return UserRepresentation
   */
  private UserRepresentation getUserRepresentation(UUID id) {
    String userId = id.toString();
    return getUsersResource().get(userId).toRepresentation();
  }

  /**
   * Enrich user with roles
   *
   * @param userRepresentation UserRepresentation
   * @return UserRepresentation
   */
  private UserRepresentation enrichUserRoles(UserRepresentation userRepresentation) {
    // Add roles to POJO
    List<RoleRepresentation> roles =
        getUsersResource().get(userRepresentation.getId()).roles().realmLevel().listAll();
    List<String> roleNames = roles.stream().map(RoleRepresentation::getName).toList();
    userRepresentation.setRealmRoles(roleNames);
    return userRepresentation;
  }

  /**
   * Get keycloak UsersResource object
   *
   * @return UsersResource
   */
  private UsersResource getUsersResource() {
    return keycloak.realm(realm).users();
  }

  /**
   * Get keycloak RolesResource object
   *
   * @return RolesResource
   */
  private RolesResource getRolesResource() {
    return keycloak.realm(realm).roles();
  }

  /**
   * Get role by name
   *
   * @param roleName role name
   * @return RoleRepresentation
   */
  private RoleRepresentation getRoleByName(String roleName) {
    try {
      var resource = getRolesResource().get(roleName);
      return resource.toRepresentation();
    } catch (jakarta.ws.rs.NotFoundException e) {
      log.error("Role with name {} not found", roleName);
      throw new NotFoundException("Role with name " + roleName + " not found", e);
    } catch (Exception e) {
      log.error("Failed to get role with name {}", roleName, e);
      throw new InternalServerErrorException("Failed to get role with name " + roleName, e);
    }
  }
}
