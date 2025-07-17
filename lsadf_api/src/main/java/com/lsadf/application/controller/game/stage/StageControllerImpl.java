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
package com.lsadf.application.controller.game.stage;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequestMapper;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.stage.StageResponse;
import com.lsadf.core.infra.web.response.game.stage.StageResponseMapper;
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
  private final StageRequestMapper stageRequestMapper;
  private static final StageResponseMapper stageResponseMapper = StageResponseMapper.INSTANCE;
  private final StageService stageService;

  @Autowired
  public StageControllerImpl(
      GameSaveService gameSaveService,
      CacheService cacheService,
      StageRequestMapper stageRequestMapper,
      StageService stageService) {
    this.gameSaveService = gameSaveService;
    this.cacheService = cacheService;
    this.stageRequestMapper = stageRequestMapper;
    this.stageService = stageService;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Void>> saveStage(
      Jwt jwt, String gameSaveId, StageRequest stageRequest) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, username);

    Stage stage = stageRequestMapper.map(stageRequest);
    stageService.saveStage(gameSaveId, stage, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<StageResponse>> getStage(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, username);
    Stage stage = stageService.getStage(gameSaveId);
    StageResponse stageResponse = stageResponseMapper.map(stage);
    return generateResponse(HttpStatus.OK, stageResponse);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
