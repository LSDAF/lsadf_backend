package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Implementation of the User Controller
 */
@RestController(value = ControllerConstants.USER)
@Slf4j
public class UserControllerImpl extends BaseController implements UserController {
    private final UserService userService;
    private final Mapper mapper;

    public UserControllerImpl(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ResponseEntity<GenericResponse<UserInfo>> getUserInfo(@CurrentUser LocalUser localUser) throws UnauthorizedException {
        try {
            validateUser(localUser);
            UserInfo userInfo = mapper.mapLocalUserToUserInfo(localUser);
            return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully retrieved user info", userInfo);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user info: ", e);
            return ResponseUtils.generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while getting user info", null);
        } catch (Exception e) {
            log.error("Exception {} while getting user info: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting user info", null);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            String email = localUser.getUsername();

            UserEntity user = userService.getUserByEmail(email);
            List<GameSave> gameSaves = user.getGameSaves().stream().map(mapper::mapToGameSave).toList();

            return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully retrieved user game saves from user", gameSaves);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user game saves: ", e);
            return ResponseUtils.generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while getting user game saves", null);
        } catch (Exception e) {
            log.error("Exception {} while getting user game saves: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting user game saves", null);
        }
    }
}
