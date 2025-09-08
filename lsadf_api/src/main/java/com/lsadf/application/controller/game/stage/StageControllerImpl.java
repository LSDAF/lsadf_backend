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
import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.stage.StageEventPublisherPort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequestMapper;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.stage.StageResponse;
import com.lsadf.core.infra.web.dto.response.game.save.stage.StageResponseMapper;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Stage Controller */
@RestController
@Slf4j
public class StageControllerImpl extends BaseController implements StageController {

  private final GameSaveService gameSaveService;
  private final CacheManager cacheManager;
  private final StageService stageService;
  private final StageEventPublisherPort stageEventPublisherPort;

  private static final StageRequestMapper stageRequestMapper = StageRequestMapper.INSTANCE;
  private static final StageResponseMapper stageResponseMapper = StageResponseMapper.INSTANCE;

  public StageControllerImpl(
      GameSaveService gameSaveService,
      CacheManager cacheManager,
      StageService stageService,
      StageEventPublisherPort stageEventPublisherPort) {
    this.gameSaveService = gameSaveService;
    this.cacheManager = cacheManager;
    this.stageService = stageService;
    this.stageEventPublisherPort = stageEventPublisherPort;
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> saveStage(
      Jwt jwt, UUID gameSaveId, StageRequest stageRequest) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, username);

    Stage stage = stageRequestMapper.map(stageRequest);
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      stageEventPublisherPort.publishStageUpdatedEvent(username, gameSaveId, stage);
    } else {
      stageService.saveStage(gameSaveId, stage, false);
    }

    return generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<StageResponse>> getStage(Jwt jwt, UUID gameSaveId) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, username);
    Stage stage = stageService.getStage(gameSaveId);
    StageResponse stageResponse = stageResponseMapper.map(stage);
    return generateResponse(HttpStatus.OK, stageResponse);
  }

  @Override
  public Logger getLogger() {
    return log;
  }
}
