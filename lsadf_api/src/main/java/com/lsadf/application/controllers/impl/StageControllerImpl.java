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
package com.lsadf.application.controllers.impl;

import static com.lsadf.core.common.utils.ResponseUtils.generateResponse;
import static com.lsadf.core.common.utils.TokenUtils.getUsernameFromJwt;

import com.lsadf.application.controllers.StageController;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.persistence.config.mappers.Mapper;
import com.lsadf.core.infra.web.config.controllers.BaseController;
import com.lsadf.core.infra.web.responses.GenericResponse;
import com.lsadf.core.requests.stage.StageRequest;
import com.lsadf.core.services.CacheService;
import com.lsadf.core.services.GameSaveService;
import com.lsadf.core.services.StageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Stage Controller */
@RestController
@Slf4j
public class StageControllerImpl extends BaseController implements StageController {

  private final GameSaveService gameSaveService;
  private final CacheService cacheService;
  private final Mapper mapper;
  private final StageService stageService;

  @Autowired
  public StageControllerImpl(
      GameSaveService gameSaveService,
      CacheService cacheService,
      Mapper mapper,
      StageService stageService) {
    this.gameSaveService = gameSaveService;
    this.cacheService = cacheService;
    this.mapper = mapper;
    this.stageService = stageService;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> saveStage(
      Jwt jwt, String gameSaveId, StageRequest stageRequest) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, username);

    Stage stage = mapper.mapStageRequestToStage(stageRequest);
    stageService.saveStage(gameSaveId, stage, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> getStage(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, username);
    Stage stage = stageService.getStage(gameSaveId);
    return generateResponse(HttpStatus.OK, stage);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
