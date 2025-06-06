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
package com.lsadf.admin.application.game;

import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.util.StreamUtils;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.infra.web.request.game.game_save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.request.game.game_save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.game_save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.response.game.game_save.GameSaveResponseMapper;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller implementation for managing game save data by admins. This class provides
 * functionality to perform CRUD and update operations on game saves, including creating new saves,
 * updating game save properties like characteristics, currencies, and stages, and fetching game
 * save details. Inherits common functionality from the {@code BaseController}. Utilizes various
 * services and mappers to facilitate data transformation and service layer interactions.
 *
 * <p>This controller is intended for admin-level access and uses specific JSON views to manage
 * serialization of data.
 */
@RestController
@Slf4j
public class AdminGameSaveControllerImpl extends BaseController implements AdminGameSaveController {

  private final GameSaveService gameSaveService;

  private final GameSaveResponseMapper gameSaveResponseMapper;

  @Autowired
  public AdminGameSaveControllerImpl(
      GameSaveService gameSaveService, GameSaveResponseMapper gameSaveResponseMapper) {
    this.gameSaveService = gameSaveService;
    this.gameSaveResponseMapper = gameSaveResponseMapper;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<ApiResponse<List<GameSaveResponse>>> getSaveGames(
      Jwt jwt, List<String> orderBy) {
    List<GameSaveSortingParameter> gameSaveOrderBy =
        Collections.singletonList(GameSaveSortingParameter.NONE);
    if (orderBy != null && !orderBy.isEmpty()) {
      gameSaveOrderBy = orderBy.stream().map(GameSaveSortingParameter::fromString).toList();
    }
    validateUser(jwt);
    try (Stream<GameSave> stream = gameSaveService.getGameSaves()) {
      Stream<GameSave> orderedStream = StreamUtils.sortGameSaves(stream, gameSaveOrderBy);
      List<GameSave> gameSaves = orderedStream.toList();
      List<GameSaveResponse> responses =
          gameSaves.stream().map(gameSaveResponseMapper::mapToResponse).toList();
      return generateResponse(HttpStatus.OK, responses);
    }
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<List<GameSaveResponse>>> getUserGameSaves(
      Jwt jwt, String username) {
    validateUser(jwt);
    try (Stream<GameSave> stream = gameSaveService.getGameSavesByUsername(username)) {
      List<GameSaveResponse> gameSaves = stream.map(gameSaveResponseMapper::mapToResponse).toList();
      return generateResponse(HttpStatus.OK, gameSaves);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<ApiResponse<GameSaveResponse>> getGameSave(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    GameSave gameSave = gameSaveService.getGameSave(gameSaveId);
    GameSaveResponse response = gameSaveResponseMapper.mapToResponse(gameSave);
    return generateResponse(HttpStatus.OK, response);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<ApiResponse<GameSaveResponse>> updateGameSave(
      Jwt jwt, String gameSaveId, AdminGameSaveUpdateRequest adminGameSaveUpdateRequest) {

    validateUser(jwt);
    GameSave gameSave = gameSaveService.updateGameSave(gameSaveId, adminGameSaveUpdateRequest);
    GameSaveResponse response = gameSaveResponseMapper.mapToResponse(gameSave);
    return generateResponse(HttpStatus.OK, response);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<ApiResponse<GameSaveResponse>> generateNewSaveGame(
      Jwt jwt, AdminGameSaveCreationRequest creationRequest) {

    validateUser(jwt);
    GameSave gameSave = gameSaveService.createGameSave(creationRequest);
    GameSaveResponse response = gameSaveResponseMapper.mapToResponse(gameSave);
    return generateResponse(HttpStatus.OK, response);
  }

  /** {@inheritDoc} */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<ApiResponse<Void>> deleteGameSave(Jwt jwt, String gameSaveId) {

    validateUser(jwt);

    gameSaveService.deleteGameSave(gameSaveId);
    return generateResponse(HttpStatus.OK);
  }
}
