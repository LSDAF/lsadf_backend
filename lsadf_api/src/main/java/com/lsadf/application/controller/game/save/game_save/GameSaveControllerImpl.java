/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.application.controller.game.save.game_save;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.creation.SimpleGameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponseMapper;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the GameSaveController. */
@RestController
@ConditionalOnProperty(prefix = "api", name = "enabled", havingValue = "true")
@Slf4j
public class GameSaveControllerImpl extends BaseController implements GameSaveController {

  private final GameSaveService gameSaveService;

  private static final GameSaveResponseMapper gameSaveResponseMapper =
      GameSaveResponseMapper.INSTANCE;

  public GameSaveControllerImpl(GameSaveService gameSaveService) {
    this.gameSaveService = gameSaveService;
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public ResponseEntity<ApiResponse<GameSaveResponse>> generateNewGameSave(Jwt jwt) {
    validateUser(jwt);

    String username = getUsernameFromJwt(jwt);

    GameSaveCreationRequest creationRequest = new SimpleGameSaveCreationRequest(username);
    GameSave newSave = gameSaveService.createGameSave(creationRequest);
    log.info("Successfully created new game for user with username {}", username);

    GameSaveResponse newSaveResponse = gameSaveResponseMapper.map(newSave);
    return generateResponse(HttpStatus.OK, newSaveResponse);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> updateNickname(
      Jwt jwt, UUID id, GameSaveNicknameUpdateRequest gameSaveNicknameUpdateRequest) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(id, username);
    gameSaveService.updateGameSave(id, gameSaveNicknameUpdateRequest);
    log.info("Successfully saved game with id {} for user with email {}", id, username);
    return generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<List<GameSaveResponse>>> getUserGameSaves(
      @AuthenticationPrincipal Jwt jwt) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);

    List<GameSave> gameSaveList = gameSaveService.getGameSavesByUsername(username);
    var mapped = gameSaveList.stream().map(gameSaveResponseMapper::map).toList();
    log.info("Successfully retrieved game saves for user with email {}", username);
    return generateResponse(HttpStatus.OK, mapped);
  }
}
