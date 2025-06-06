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
package com.lsadf.application.controller.game.game_save;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.responses.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.web.controllers.BaseController;
import com.lsadf.core.infra.web.requests.game.game_save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponseMapper;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the GameSaveController. */
@RestController
@Slf4j
public class GameSaveControllerImpl extends BaseController implements GameSaveController {

  private final GameSaveService gameSaveService;

  private final GameSaveResponseMapper gameSaveResponseMapper;

  public GameSaveControllerImpl(
      GameSaveService gameSaveService, GameSaveResponseMapper gameSaveResponseMapper) {
    this.gameSaveService = gameSaveService;
    this.gameSaveResponseMapper = gameSaveResponseMapper;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<GameSaveResponse>> generateNewGameSave(Jwt jwt) {
    validateUser(jwt);

    String username = getUsernameFromJwt(jwt);

    GameSave newSave = gameSaveService.createGameSave(username);
    log.info("Successfully created new game for user with username {}", username);

    GameSaveResponse newSaveResponse = gameSaveResponseMapper.mapToResponse(newSave);
    return generateResponse(HttpStatus.OK, newSaveResponse);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Void>> updateNickname(
      Jwt jwt, String id, GameSaveNicknameUpdateRequest gameSaveNicknameUpdateRequest) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(id, username);
    gameSaveService.updateGameSave(id, gameSaveNicknameUpdateRequest);
    log.info("Successfully saved game with id {} for user with email {}", id, username);
    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<List<GameSaveResponse>>> getUserGameSaves(
      @AuthenticationPrincipal Jwt jwt) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);

    try (Stream<GameSave> gameSaveStream = gameSaveService.getGameSavesByUsername(username)) {
      List<GameSaveResponse> gameSaveList =
          gameSaveStream.map(gameSaveResponseMapper::mapToResponse).toList();
      log.info("Successfully retrieved game saves for user with email {}", username);
      return generateResponse(HttpStatus.OK, gameSaveList);
    }
  }
}
