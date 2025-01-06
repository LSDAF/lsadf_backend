package com.lsadf.core.services.impl;

import com.lsadf.core.exceptions.AlreadyExistingUserException;
import com.lsadf.core.exceptions.http.InternalServerErrorException;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.User;
import com.lsadf.core.properties.KeycloakProperties;
import com.lsadf.core.requests.admin.AdminUserCreationRequest;
import com.lsadf.core.requests.admin.AdminUserUpdateRequest;
import com.lsadf.core.requests.user.UserCreationRequest;
import com.lsadf.core.requests.user.UserUpdateRequest;
import com.lsadf.core.services.ClockService;
import com.lsadf.core.services.UserService;
import java.util.ArrayList;
import java.util.List;
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
  private final Mapper mapper;

  private final String realm;

  public UserServiceImpl(
      Keycloak keycloak,
      KeycloakProperties keycloakProperties,
      ClockService clockService,
      Mapper mapper) {
    this.keycloakProperties = keycloakProperties;
    this.clockService = clockService;
    this.keycloak = keycloak;
    this.mapper = mapper;

    this.realm = keycloakProperties.getRealm();
  }

  /** {@inheritDoc} */
  @Override
  public Stream<User> getUsers() {
    // return keycloakAdminClient.getUsers(realm).stream();
    try {
      List<UserRepresentation> userlist = getUsersResource().list();
      return userlist.stream().map(this::enrichUserRoles).map(mapper::mapUserRepresentationToUser);
    } catch (Exception e) {
      log.error("Failed to get users", e);
      throw new InternalServerErrorException("Failed to get users");
    }
  }

  /** {@inheritDoc} */
  @Override
  public List<String> getUserRoles() {
    List<RoleRepresentation> roles = getRolesResource().list();
    return roles.stream().map(RoleRepresentation::getName).toList();
  }

  /** {@inheritDoc} */
  @Override
  public Stream<User> getUsers(String search) {
    if (search == null || search.isEmpty()) {
      return getUsers();
    }

    // return keycloakAdminClient.getUsers(realm, Optional.ofNullable(search)).stream();
    try {
      return getUsersResource().search(search).stream()
          .map(this::enrichUserRoles)
          .map(mapper::mapUserRepresentationToUser);
    } catch (Exception e) {
      log.error("Failed to get users with search: {}", search, e);
      throw new InternalServerErrorException("Failed to get users with search: " + search);
    }
  }

  /** {@inheritDoc} */
  @Override
  public User getUserById(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    // return keycloakAdminClient.getUserById(realm, id);
    try {
      UserRepresentation userResource = getUserRepresentation(id);
      UserRepresentation enrichedUser = enrichUserRoles(userResource);
      return mapper.mapUserRepresentationToUser(enrichedUser);
    } catch (jakarta.ws.rs.NotFoundException e) {
      log.error("User with id {} not found", id);
      throw new NotFoundException("User with id " + id + " not found");
    } catch (Exception e) {
      log.error("Failed to get user with id {}", id, e);
      throw new InternalServerErrorException("Failed to get user with id " + id);
    }
  }

  /** {@inheritDoc} */
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
    return mapper.mapUserRepresentationToUser(enrichedUser);
  }

  /** {@inheritDoc} */
  @Override
  public User updateUser(String id, AdminUserUpdateRequest adminUpdateRequest) {
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
        getUsersResource().get(id).roles().realmLevel().remove(roles);
        getUsersResource().get(id).roles().realmLevel().add(roles);
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
      getUsersResource().get(id).update(existingUser);
    }

    return getUserById(id);
  }

  /** {@inheritDoc} */
  @Override
  public void updateUser(String id, UserUpdateRequest updateRequest) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    UserRepresentation existingUser = getUserRepresentation(id);
    if (updateRequest.getFirstName() != null) {
      existingUser.setFirstName(updateRequest.getFirstName());
    }
    if (updateRequest.getLastName() != null) {
      existingUser.setLastName(updateRequest.getLastName());
    }
    getUsersResource().get(id).update(existingUser);
  }

  /** {@inheritDoc} */
  @Override
  public void resetUserPassword(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    String updatePasswordAction = "UPDATE_PASSWORD";
    List<String> actions = List.of(updatePasswordAction);
    // keycloakAdminClient.sendActionsEmail(realm, id, actions);
    getUsersResource().get(id).executeActionsEmail(actions);
  }

  /** {@inheritDoc} */
  @Override
  public void deleteUser(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    if (!checkIdExists(id)) {
      throw new NotFoundException("User with id " + id + " not found");
    }
    // keycloakAdminClient.deleteUser(realm, id);
    getUsersResource().get(id).remove();
  }

  /** {@inheritDoc} */
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
    UserRepresentation userRepresentation =
        mapper.mapUserCreationRequestToUserRepresentation(request);
    try (var response = getUsersResource().create(userRepresentation)) {
      if (response.getStatus() != HttpStatus.CREATED.value()) {
        throw new InternalServerErrorException("Failed to create user");
      }
      String responsePath = response.getLocation().getPath();
      int beginIndex = responsePath.lastIndexOf('/') + 1;
      String userId = responsePath.substring(beginIndex);

      // Add roles to user
      if (!roles.isEmpty()) {
        getUsersResource().get(userId).roles().realmLevel().add(roles);
      }

      return getUserById(userId);
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      log.error("Failed to create user", e);
      throw new InternalServerErrorException("Failed to create user: " + e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public User createUser(AdminUserCreationRequest adminUserCreationRequest) {
    UserCreationRequest userCreationRequest =
        mapper.mapAdminUserCreationRequestToUserCreationRequest(adminUserCreationRequest);
    return createUser(userCreationRequest);
  }

  /** {@inheritDoc} */
  @Override
  public boolean checkUsernameExists(String username) {
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null");
    }
    try {
      getUserByUsername(username);
      return true;
    } catch (NotFoundException e) {
      return false;
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean checkIdExists(String id) {
    // We assert that the null check is done in the calling method to avoid multiple checks
    // id can be null, then we return false
    if (id == null) {
      return false;
    }
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
  private UserRepresentation getUserRepresentation(String id) {
    return getUsersResource().get(id).toRepresentation();
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
