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
package com.lsadf.application.controllers.impl;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.responses.ResponseUtils.generateResponse;

import com.lsadf.application.controllers.GameSaveController;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.mappers.Mapper;
import com.lsadf.core.infra.web.controllers.BaseController;
import com.lsadf.core.infra.web.requests.game.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.core.infra.web.responses.GenericResponse;
import java.util.List;
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
  private final Mapper mapper;

  public GameSaveControllerImpl(GameSaveService gameSaveService, Mapper mapper) {
    this.gameSaveService = gameSaveService;
    this.mapper = mapper;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<GameSave>> generateNewGameSave(Jwt jwt) {
    validateUser(jwt);

    String username = getUsernameFromJwt(jwt);

    GameSaveEntity newSave = gameSaveService.createGameSave(username);

    log.info("Successfully created new game for user with username {}", username);
    GameSave newGameSave = mapper.mapGameSaveEntityToGameSave(newSave);

    return generateResponse(HttpStatus.OK, newGameSave);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> updateNickname(
      Jwt jwt, String id, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(id, username);
    gameSaveService.updateNickname(id, gameSaveUpdateNicknameRequest);
    log.info("Successfully saved game with id {} for user with email {}", id, username);
    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(
      @AuthenticationPrincipal Jwt jwt) {
    validateUser(jwt);
    String username = getUsernameFromJwt(jwt);

    List<GameSave> gameSaveList =
        gameSaveService
            .getGameSavesByUsername(username)
            .map(mapper::mapGameSaveEntityToGameSave)
            .toList();

    return generateResponse(HttpStatus.OK, gameSaveList);
  }
}
