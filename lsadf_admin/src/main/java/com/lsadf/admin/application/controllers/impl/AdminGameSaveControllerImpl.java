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

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.admin.application.controllers.AdminGameSaveController;
import com.lsadf.core.common.exceptions.http.NotFoundException;
import com.lsadf.core.common.utils.StreamUtils;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.persistence.config.mappers.Mapper;
import com.lsadf.core.infra.persistence.game.GameSaveEntity;
import com.lsadf.core.infra.web.config.controllers.BaseController;
import com.lsadf.core.infra.web.config.controllers.JsonViews;
import com.lsadf.core.infra.web.responses.GenericResponse;
import com.lsadf.core.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.core.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.requests.currency.CurrencyRequest;
import com.lsadf.core.requests.game_save.GameSaveSortingParameter;
import com.lsadf.core.requests.stage.StageRequest;
import com.lsadf.core.services.*;
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

/** The implementation of the AdminGameSaveController */
@RestController
@Slf4j
public class AdminGameSaveControllerImpl extends BaseController implements AdminGameSaveController {

  private final CurrencyService currencyService;
  private final StageService stageService;
  private final GameSaveService gameSaveService;
  private final InventoryService inventoryService;
  private final CacheService cacheService;
  private final Mapper mapper;
  private final CharacteristicsService characteristicsService;

  @Autowired
  public AdminGameSaveControllerImpl(
      CurrencyService currencyService,
      StageService stageService,
      GameSaveService gameSaveService,
      InventoryService inventoryService,
      Mapper mapper,
      CacheService cacheService,
      CharacteristicsService characteristicsService) {
    this.currencyService = currencyService;
    this.stageService = stageService;
    this.gameSaveService = gameSaveService;
    this.inventoryService = inventoryService;
    this.cacheService = cacheService;
    this.mapper = mapper;
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
    try (Stream<GameSaveEntity> stream = gameSaveService.getGameSaves()) {
      Stream<GameSave> gameSaveStream = stream.map(mapper::mapGameSaveEntityToGameSave);
      Stream<GameSave> orderedStream = StreamUtils.sortGameSaves(gameSaveStream, gameSaveOrderBy);
      List<GameSave> gameSaves = orderedStream.toList();
      return generateResponse(HttpStatus.OK, gameSaves);
    }
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(
      Jwt jwt, String username) {
    validateUser(jwt);
    try (Stream<GameSaveEntity> stream = gameSaveService.getGameSavesByUsername(username)) {
      List<GameSave> gameSaves = stream.map(mapper::mapGameSaveEntityToGameSave).toList();
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
    GameSaveEntity entity = gameSaveService.getGameSave(gameSaveId);
    GameSave gameSave = mapper.mapGameSaveEntityToGameSave(entity);
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
    GameSaveEntity gameSaveEntity =
        gameSaveService.updateNickname(gameSaveId, adminGameSaveUpdateRequest);
    GameSave gameSave = mapper.mapGameSaveEntityToGameSave(gameSaveEntity);
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
    GameSaveEntity gameSaveEntity = gameSaveService.createGameSave(creationRequest);
    GameSave gameSave = mapper.mapGameSaveEntityToGameSave(gameSaveEntity);
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

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> updateGameSaveCharacteristics(
      Jwt jwt, String gameSaveId, CharacteristicsRequest characteristicsRequest) {
    validateUser(jwt);
    Characteristics characteristics =
        mapper.mapCharacteristicsRequestToCharacteristics(characteristicsRequest);
    if (!gameSaveService.existsById(gameSaveId)) {
      throw new NotFoundException("Game save not found");
    }
    characteristicsService.saveCharacteristics(
        gameSaveId, characteristics, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> updateGameSaveCurrencies(
      Jwt jwt, String gameSaveId, CurrencyRequest currencyRequest) {

    validateUser(jwt);
    Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);
    if (!gameSaveService.existsById(gameSaveId)) {
      throw new NotFoundException("Game save not found");
    }
    currencyService.saveCurrency(gameSaveId, currency, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> updateGameSaveStages(
      Jwt jwt, String gameSaveId, StageRequest stageRequest) {

    validateUser(jwt);
    Stage stage = mapper.mapStageRequestToStage(stageRequest);
    if (!gameSaveService.existsById(gameSaveId)) {
      throw new NotFoundException("Game save not found");
    }
    stageService.saveStage(gameSaveId, stage, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }
}
