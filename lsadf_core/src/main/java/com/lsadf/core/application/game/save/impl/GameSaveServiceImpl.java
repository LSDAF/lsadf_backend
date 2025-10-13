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
package com.lsadf.core.application.game.save.impl;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.command.InitializeCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.InitializeDefaultCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.command.InitializeCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.InitializeDefaultCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.PersistCurrencyCommand;
import com.lsadf.core.application.game.save.currency.command.UpdateCacheCurrencyCommand;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.command.InitializeDefaultStageCommand;
import com.lsadf.core.application.game.save.stage.command.InitializeStageCommand;
import com.lsadf.core.application.game.save.stage.command.PersistStageCommand;
import com.lsadf.core.application.game.save.stage.command.UpdateCacheStageCommand;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.exception.AlreadyExistingGameSaveException;
import com.lsadf.core.exception.AlreadyTakenNicknameException;
import com.lsadf.core.exception.http.ForbiddenException;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.exception.http.UnauthorizedException;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.dto.request.game.save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveUpdateRequest;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of GameSaveService */
@Slf4j
public class GameSaveServiceImpl implements GameSaveService {
  private final UserService userService;
  private final GameMetadataService gameMetadataService;
  private final CharacteristicsCommandService characteristicsService;
  private final StageCommandService stageService;
  private final CurrencyCommandService currencyService;

  // Repository port to access game save data
  private final GameSaveRepositoryPort gameSaveRepositoryPort;

  private final CacheManager cacheManager;
  private final GameMetadataCachePort gameMetadataCache;
  private final StageCachePort stageCache;
  private final CurrencyCachePort currencyCache;
  private final CharacteristicsCachePort characteristicsCache;

  public GameSaveServiceImpl(
      GameMetadataService gameMetadataService,
      CharacteristicsCommandService characteristicsService,
      StageCommandService stageService,
      CurrencyCommandService currencyService,
      UserService userService,
      GameSaveRepositoryPort gameSaveRepositoryPort,
      CacheManager cacheManager,
      GameMetadataCachePort gameMetadataCache,
      StageCachePort stageCache,
      CurrencyCachePort currencyCache,
      CharacteristicsCachePort characteristicsCache) {
    this.userService = userService;
    this.characteristicsService = characteristicsService;
    this.stageService = stageService;
    this.currencyService = currencyService;
    this.gameMetadataService = gameMetadataService;
    this.gameSaveRepositoryPort = gameSaveRepositoryPort;
    this.cacheManager = cacheManager;
    this.gameMetadataCache = gameMetadataCache;
    this.stageCache = stageCache;
    this.currencyCache = currencyCache;
    this.characteristicsCache = characteristicsCache;
  }

  @Override
  @Transactional(readOnly = true)
  public GameSave getGameSave(UUID saveId) throws NotFoundException {
    GameSave gameSave =
        gameSaveRepositoryPort
            .findById(saveId)
            .orElseThrow(() -> new NotFoundException("Game save with id " + saveId + " not found"));
    return enrichGameSaveWithCachedData(gameSave);
  }

  @Override
  @Transactional
  public GameSave createGameSave(GameSaveCreationRequest creationRequest)
      throws NotFoundException, AlreadyExistingGameSaveException {
    GameMetadataRequest metadataRequest = creationRequest.getMetadataRequest();
    if (metadataRequest.id() != null && gameMetadataService.existsById(metadataRequest.id())) {
      throw new AlreadyExistingGameSaveException(
          "Game save with id " + metadataRequest.id() + " already exists");
    }
    if (metadataRequest.nickname() != null
        && gameMetadataService.existsByNickname(metadataRequest.nickname())) {
      throw new AlreadyExistingGameSaveException(
          "Game save with nickname " + metadataRequest.nickname() + " already exists");
    }
    if (!userService.checkUsernameExists(metadataRequest.userEmail())) {
      throw new NotFoundException("User with email " + metadataRequest.userEmail() + " not found");
    }

    GameSave.GameSaveBuilder gameSaveBuilder = GameSave.builder();

    GameMetadata newGameMetadata =
        gameMetadataService.createNewGameMetadata(
            metadataRequest.id(), metadataRequest.userEmail(), metadataRequest.nickname());
    gameSaveBuilder.metadata(newGameMetadata);

    CharacteristicsRequest characteristicsRequest = creationRequest.getCharacteristicsRequest();
    Characteristics newCharacteristics;
    if (characteristicsRequest == null) {
      var command = new InitializeDefaultCharacteristicsCommand(newGameMetadata.id());
      newCharacteristics = characteristicsService.initializeDefaultCharacteristics(command);
    } else {
      var command =
          new InitializeCharacteristicsCommand(
              newGameMetadata.id(),
              characteristicsRequest.attack(),
              characteristicsRequest.critChance(),
              characteristicsRequest.critDamage(),
              characteristicsRequest.health(),
              characteristicsRequest.resistance());
      newCharacteristics = characteristicsService.initializeCharacteristics(command);
    }

    gameSaveBuilder.characteristics(newCharacteristics);

    CurrencyRequest currencyRequest = creationRequest.getCurrencyRequest();
    Currency newCurrency;
    if (currencyRequest == null) {
      var command = new InitializeDefaultCurrencyCommand(newGameMetadata.id());
      newCurrency = currencyService.initializeDefaultCurrency(command);
    } else {
      var command =
          new InitializeCurrencyCommand(
              newGameMetadata.id(),
              currencyRequest.gold(),
              currencyRequest.diamond(),
              currencyRequest.emerald(),
              currencyRequest.amethyst());
      newCurrency = currencyService.initializeCurrency(command);
    }
    gameSaveBuilder.currency(newCurrency);

    StageRequest stageRequest = creationRequest.getStageRequest();

    Stage newStage;
    if (stageRequest == null) {
      var command = new InitializeDefaultStageCommand(newGameMetadata.id());
      newStage = stageService.initializeDefaultStage(command);
    } else {
      var command =
          new InitializeStageCommand(
              newGameMetadata.id(), stageRequest.currentStage(), stageRequest.maxStage());
      newStage = stageService.initializeStage(command);
    }
    gameSaveBuilder.stage(newStage);

    return gameSaveBuilder.build();
  }

  @Override
  @Transactional
  public GameSave updateGameSave(UUID saveId, GameSaveUpdateRequest gameSaveUpdateRequest)
      throws ForbiddenException,
          NotFoundException,
          UnauthorizedException,
          AlreadyTakenNicknameException {
    if (gameMetadataService.existsByNickname(gameSaveUpdateRequest.getNickname())) {
      throw new AlreadyTakenNicknameException(
          "Nickname " + gameSaveUpdateRequest.getNickname() + " is already taken");
    }

    if (gameSaveUpdateRequest.getCharacteristics() != null) {
      Characteristics characteristicsUpdate = gameSaveUpdateRequest.getCharacteristics();
      if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
        UpdateCacheCharacteristicsCommand command =
            UpdateCacheCharacteristicsCommand.fromCharacteristics(saveId, characteristicsUpdate);
        characteristicsService.updateCacheCharacteristics(command);
      } else {
        PersistCharacteristicsCommand command =
            PersistCharacteristicsCommand.fromCharacteristics(saveId, characteristicsUpdate);
        characteristicsService.persistCharacteristics(command);
      }
    }
    if (gameSaveUpdateRequest.getCurrency() != null) {
      Currency currencyUpdate = gameSaveUpdateRequest.getCurrency();
      if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
        UpdateCacheCurrencyCommand command =
            UpdateCacheCurrencyCommand.fromCurrency(saveId, currencyUpdate);
        currencyService.updateCacheCurrency(command);
      } else {
        PersistCurrencyCommand command =
            PersistCurrencyCommand.fromCurrency(saveId, currencyUpdate);
        currencyService.persistCurrency(command);
      }
    }
    if (gameSaveUpdateRequest.getStage() != null) {
      Stage stageUpdate = gameSaveUpdateRequest.getStage();
      if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
        UpdateCacheStageCommand command = UpdateCacheStageCommand.fromStage(saveId, stageUpdate);
        stageService.updateCacheStage(command);
      } else {
        PersistStageCommand command = PersistStageCommand.fromStage(saveId, stageUpdate);
        stageService.persistStage(command);
      }
    }

    gameMetadataService.updateNickname(saveId, gameSaveUpdateRequest.getNickname());

    GameSave gameSave =
        gameSaveRepositoryPort
            .findById(saveId)
            .orElseThrow(() -> new NotFoundException("Game save with id " + saveId + " not found"));
    return enrichGameSaveWithCachedData(gameSave);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(UUID gameSaveId) {
    return gameMetadataService.existsById(gameSaveId);
  }

  @Override
  @Transactional
  public void deleteGameSave(UUID saveId) {
    if (!gameMetadataService.existsById(saveId)) {
      log.error("Game save with id {} not found", saveId);
      throw new NotFoundException("Game save with id " + saveId + " not found");
    }
    // Delete entities from currency & stage before deleting the game save
    gameMetadataService.deleteById(saveId);
    String saveIdString = saveId.toString();
    cacheManager.clearGameSaveValues(saveIdString);
  }

  @Override
  @Transactional(readOnly = true)
  public List<GameSave> getGameSaves() {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      return gameSaveRepositoryPort.findAll().map(this::enrichGameSaveWithCachedData).toList();
    }
    return gameSaveRepositoryPort.findAll().toList();
  }

  @Transactional(readOnly = true)
  @Override
  public Long countGameSaves() {
    return gameMetadataService.count();
  }

  @Override
  @Transactional(readOnly = true)
  public void checkGameSaveOwnership(UUID saveId, String userEmail)
      throws ForbiddenException, NotFoundException {
    if (Boolean.FALSE.equals(cacheManager.isEnabled())) {
      GameMetadata gameMetadata = gameMetadataService.getGameMetadata(saveId);
      if (!Objects.equals(gameMetadata.userEmail(), userEmail)) {
        throw new ForbiddenException("The given user email is not the owner of the game save");
      }
      return;
    }
    String saveIdString = saveId.toString();
    Optional<GameMetadata> optionalGameMetadata = gameMetadataCache.get(saveIdString);
    if (optionalGameMetadata.isEmpty()) {
      GameMetadata gameMetadata = gameMetadataService.getGameMetadata(saveId);
      gameMetadataCache.set(saveIdString, gameMetadata);
      if (!Objects.equals(gameMetadata.userEmail(), userEmail)) {
        throw new ForbiddenException("The given user username is not the owner of the game save");
      }
      return;
    }

    if (!Objects.equals(optionalGameMetadata.get().userEmail(), userEmail)) {
      throw new ForbiddenException("The given username is not the owner of the game save");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<GameSave> getGameSavesByUsername(String username) {
    if (!userService.checkUsernameExists(username)) {
      throw new NotFoundException("User with username " + username + " not found");
    }
    if (Boolean.FALSE.equals(cacheManager.isEnabled())) {
      return gameSaveRepositoryPort.findByUserEmail(username).toList();
    }
    return gameSaveRepositoryPort
        .findByUserEmail(username)
        .map(this::enrichGameSaveWithCachedData)
        .toList();
  }

  /**
   * Enriches the given GameSave object with cached data, if available. Specifically, it attempts to
   * load and set the characteristics, currency, and stage data from their corresponding caches
   * based on the metadata ID of the provided GameSave.
   *
   * @param gameSave the GameSave object to be enriched with cached data
   * @return the enriched GameSave object
   */
  private GameSave enrichGameSaveWithCachedData(GameSave gameSave) {
    GameMetadata metadata = gameSave.getMetadata();
    var cacheCharacteristics = characteristicsCache.get(metadata.id().toString());
    cacheCharacteristics.ifPresent(gameSave::setCharacteristics);

    var cacheCurrency = currencyCache.get(metadata.id().toString());
    cacheCurrency.ifPresent(gameSave::setCurrency);

    var cacheStage = stageCache.get(metadata.id().toString());
    cacheStage.ifPresent(gameSave::setStage);
    return gameSave;
  }
}
