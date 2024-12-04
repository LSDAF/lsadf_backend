package com.lsadf.admin.controllers.admin.impl;

import com.lsadf.admin.controllers.admin.AdminUserController;
import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.admin.AdminUserCreationRequest;
import com.lsadf.core.requests.admin.AdminUserUpdateRequest;
import com.lsadf.core.requests.user.UserSortingParameter;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.UserService;
import com.lsadf.core.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;

/**
 * The implementation of the AdminUserController
 */
@RestController
@Slf4j
public class AdminUserControllerImpl extends BaseController implements AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     */
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
    public ResponseEntity<GenericResponse<List<User>>> getUsers(Jwt jwt,
                                                                List<String> orderBy) {
        List<UserSortingParameter> sortingParameterList = Collections.singletonList(UserSortingParameter.NONE);
        if (orderBy != null && !orderBy.isEmpty()) {
            sortingParameterList = orderBy.stream()
                    .map(UserSortingParameter::fromString)
                    .toList();
        }
        Stream<User> users = StreamUtils.sortUsers(userService.getUsers(), sortingParameterList);
        List<User> userList = users.toList();
        return generateResponse(HttpStatus.OK, userList);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> getUserById(Jwt jwt,
                                                             String userId) {
        validateUser(jwt);
        User user = userService.getUserById(userId);
        return generateResponse(HttpStatus.OK, user);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> getUserByUsername(Jwt jwt,
                                                                   String username) {
        validateUser(jwt);
        User user = userService.getUserByUsername(username);
        return generateResponse(HttpStatus.OK, user);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> updateUser(Jwt jwt,
                                                            String userId,
                                                            AdminUserUpdateRequest user) {
        validateUser(jwt);
        User updatedUser = userService.updateUser(userId, user);
        return generateResponse(HttpStatus.OK, updatedUser);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteUser(Jwt jwt,
                                                            String userId) {
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
    public ResponseEntity<GenericResponse<User>> createUser(Jwt jwt,
                                                            AdminUserCreationRequest adminUserCreationRequest) {
        validateUser(jwt);
        User user = userService.createUser(adminUserCreationRequest);

        return generateResponse(HttpStatus.OK, user);
    }
}
