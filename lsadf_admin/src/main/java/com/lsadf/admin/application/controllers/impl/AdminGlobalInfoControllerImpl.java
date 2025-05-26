/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.admin.application.controllers.impl;

import static com.lsadf.core.common.utils.ResponseUtils.generateResponse;

import com.lsadf.admin.application.controllers.AdminGlobalInfoController;
import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.infra.web.config.controllers.BaseController;
import com.lsadf.core.infra.web.responses.GenericResponse;
import com.lsadf.core.services.ClockService;
import com.lsadf.core.services.GameSaveService;
import com.lsadf.core.services.UserService;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** The implementation of the AdminGlobalInfoController */
@RestController
@Slf4j
public class AdminGlobalInfoControllerImpl extends BaseController
    implements AdminGlobalInfoController {

  private final UserService userService;
  private final GameSaveService gameSaveService;
  private final ClockService clockService;

  @Autowired
  public AdminGlobalInfoControllerImpl(
      UserService userService, GameSaveService gameSaveService, ClockService clockService) {
    this.userService = userService;
    this.gameSaveService = gameSaveService;
    this.clockService = clockService;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo(Jwt jwt) {
    validateUser(jwt);
    Long userCount = userService.getUsers().count();
    Long gameSaveCount = gameSaveService.getGameSaves().count();
    Instant now = clockService.nowInstant();

    GlobalInfo globalInfo =
        GlobalInfo.builder().userCounter(userCount).now(now).gameSaveCounter(gameSaveCount).build();
    return generateResponse(HttpStatus.OK, globalInfo);
  }
}
