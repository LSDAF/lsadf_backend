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
package com.lsadf.application.controller.user;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUserInfoFromJwt;
import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.domain.user.UserInfo;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.user.UserInfoResponse;
import com.lsadf.core.infra.web.response.user.UserInfoResponseMapper;
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

  private static final UserInfoResponseMapper userInfoResponseMapper =
      UserInfoResponseMapper.INSTANCE;

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(Jwt jwt) {
    UserInfo userInfo = getUserInfoFromJwt(jwt);
    var response = userInfoResponseMapper.map(userInfo);
    return generateResponse(HttpStatus.OK, response);
  }
}
