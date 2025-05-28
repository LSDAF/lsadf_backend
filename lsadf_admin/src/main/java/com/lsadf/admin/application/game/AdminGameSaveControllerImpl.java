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

import static com.lsadf.core.infra.web.responses.ResponseUtils.generateResponse;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.game.stage.StageService;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.cache.services.CacheService;
import com.lsadf.core.infra.utils.StreamUtils;
import com.lsadf.core.infra.web.controllers.BaseController;
import com.lsadf.core.infra.web.controllers.JsonViews;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequestMapper;
import com.lsadf.core.infra.web.requests.game.game_save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.requests.game.game_save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.requests.game.game_save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.requests.game.stage.StageRequestMapper;
import com.lsadf.core.infra.web.responses.GenericResponse;
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

  private final CurrencyService currencyService;
  private final StageService stageService;
  private final GameSaveService gameSaveService;
  private final CacheService cacheService;
  private final CharacteristicsRequestMapper characteristicsRequestMapper;
  private final CurrencyRequestMapper currencyRequestMapper;
  private final StageRequestMapper stageRequestMapper;
  private final CharacteristicsService characteristicsService;

  @Autowired
  public AdminGameSaveControllerImpl(
      CurrencyService currencyService,
      StageService stageService,
      GameSaveService gameSaveService,
      CharacteristicsRequestMapper characteristicsRequestMapper,
      CurrencyRequestMapper currencyRequestMapper,
      CacheService cacheService,
      CharacteristicsService characteristicsService,
      StageRequestMapper stageRequestMapper) {
    this.currencyService = currencyService;
    this.stageService = stageService;
    this.gameSaveService = gameSaveService;
    this.cacheService = cacheService;
    this.characteristicsRequestMapper = characteristicsRequestMapper;
    this.currencyRequestMapper = currencyRequestMapper;
    this.stageRequestMapper = stageRequestMapper;
    this.characteristicsService = characteristicsService;
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
  public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(
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
      return generateResponse(HttpStatus.OK, gameSaves);
    }
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(
      Jwt jwt, String username) {
    validateUser(jwt);
    try (Stream<GameSave> stream = gameSaveService.getGameSavesByUsername(username)) {
      List<GameSave> gameSaves = stream.toList();
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
  public ResponseEntity<GenericResponse<GameSave>> getGameSave(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    GameSave gameSave = gameSaveService.getGameSave(gameSaveId);
    return generateResponse(HttpStatus.OK, gameSave);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<GenericResponse<GameSave>> updateGameSave(
      Jwt jwt, String gameSaveId, AdminGameSaveUpdateRequest adminGameSaveUpdateRequest) {

    validateUser(jwt);
    GameSave gameSave = gameSaveService.updateGameSave(gameSaveId, adminGameSaveUpdateRequest);
    return generateResponse(HttpStatus.OK, gameSave);
  }

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(
      Jwt jwt, AdminGameSaveCreationRequest creationRequest) {

    validateUser(jwt);
    GameSave gameSave = gameSaveService.createGameSave(creationRequest);
    return generateResponse(HttpStatus.OK, gameSave);
  }

  /** {@inheritDoc} */
  @Override
  @JsonView(JsonViews.Admin.class)
  public ResponseEntity<GenericResponse<Void>> deleteGameSave(Jwt jwt, String gameSaveId) {

    validateUser(jwt);

    gameSaveService.deleteGameSave(gameSaveId);
    return generateResponse(HttpStatus.OK);
  }
}
