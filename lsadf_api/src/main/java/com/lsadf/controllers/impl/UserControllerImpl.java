package com.lsadf.controllers.impl;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;
import static com.lsadf.core.utils.TokenUtils.*;

import com.lsadf.controllers.UserController;
import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.models.UserInfo;
import com.lsadf.core.responses.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the User Controller */
@RestController
@Slf4j
public class UserControllerImpl extends BaseController implements UserController {
  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<UserInfo>> getUserInfo(Jwt jwt) {
    UserInfo userInfo = getUserInfoFromJwt(jwt);
    return generateResponse(HttpStatus.OK, userInfo);
  }
}
