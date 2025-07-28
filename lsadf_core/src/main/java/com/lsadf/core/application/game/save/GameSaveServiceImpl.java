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
package com.lsadf.core.application.game.save;

import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.exception.AlreadyExistingGameSaveException;
import com.lsadf.core.infra.exception.AlreadyTakenNicknameException;
import com.lsadf.core.infra.exception.http.ForbiddenException;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.exception.http.UnauthorizedException;
import com.lsadf.core.infra.persistence.view.GameSaveViewEntity;
import com.lsadf.core.infra.persistence.view.GameSaveViewMapper;
import com.lsadf.core.infra.persistence.view.GameSaveViewRepository;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.request.game.save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.save.update.GameSaveUpdateRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import java.util.*;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of GameSaveService */
@Slf4j
public class GameSaveServiceImpl implements GameSaveService {
  private final UserService userService;
  private final GameMetadataService gameMetadataService;
  private final CharacteristicsService characteristicsService;
  private final StageService stageService;
  private final CurrencyService currencyService;

  // Repositories and services to access the database and the cache
  private final GameSaveViewRepository gameSaveViewRepository;

  private final CacheService cacheService;
  private final Cache<String> gameSaveOwnershipCache;
  private final HistoCache<Stage> stageCache;
  private final HistoCache<Currency> currencyCache;
  private final HistoCache<Characteristics> characteristicsCache;

  private static final GameSaveViewMapper gameSaveViewMapper = GameSaveViewMapper.INSTANCE;

  @Autowired
  public GameSaveServiceImpl(
      GameMetadataService gameMetadataService,
      CharacteristicsService characteristicsService,
      StageService stageService,
      CurrencyService currencyService,
      UserService userService,
      GameSaveViewRepository gameSaveViewRepository,
      CacheService cacheService,
      Cache<String> gameSaveOwnershipCache,
      HistoCache<Stage> stageCache,
      HistoCache<Currency> currencyCache,
      HistoCache<Characteristics> characteristicsCache) {
    this.userService = userService;
    this.characteristicsService = characteristicsService;
    this.stageService = stageService;
    this.currencyService = currencyService;
    this.gameMetadataService = gameMetadataService;
    this.gameSaveViewRepository = gameSaveViewRepository;
    this.cacheService = cacheService;
    this.gameSaveOwnershipCache = gameSaveOwnershipCache;
    this.stageCache = stageCache;
    this.currencyCache = currencyCache;
    this.characteristicsCache = characteristicsCache;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public GameSave getGameSave(UUID saveId) throws NotFoundException {
    GameSaveViewEntity gameSaveEntity = getGameSaveEntity(saveId);
    GameSave gameSave = gameSaveViewMapper.map(gameSaveEntity);
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
    Characteristics newCharacteristics =
        (characteristicsRequest != null)
            ? characteristicsService.createNewCharacteristics(
                newGameMetadata.id(),
                characteristicsRequest.attack(),
                characteristicsRequest.critChance(),
                characteristicsRequest.critDamage(),
                characteristicsRequest.health(),
                characteristicsRequest.resistance())
            : characteristicsService.createNewCharacteristics(newGameMetadata.id());

    gameSaveBuilder.characteristics(newCharacteristics);

    CurrencyRequest currencyRequest = creationRequest.getCurrencyRequest();
    Currency newCurrency =
        (currencyRequest != null)
            ? currencyService.createNewCurrency(
                newGameMetadata.id(),
                currencyRequest.gold(),
                currencyRequest.diamond(),
                currencyRequest.emerald(),
                currencyRequest.amethyst())
            : currencyService.createNewCurrency(newGameMetadata.id());
    gameSaveBuilder.currency(newCurrency);

    StageRequest stageRequest = creationRequest.getStageRequest();
    Stage newStage =
        (stageRequest != null)
            ? stageService.createNewStage(
                newGameMetadata.id(), stageRequest.currentStage(), stageRequest.maxStage())
            : stageService.createNewStage(newGameMetadata.id());
    gameSaveBuilder.stage(newStage);

    return gameSaveBuilder.build();
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public GameSave updateGameSave(UUID saveId, GameSaveUpdateRequest gameSaveUpdateRequest)
      throws ForbiddenException,
          NotFoundException,
          UnauthorizedException,
          AlreadyTakenNicknameException {
    if (saveId == null) {
      throw new NotFoundException("Game save id is null");
    }

    if (gameMetadataService.existsByNickname(gameSaveUpdateRequest.getNickname())) {
      throw new AlreadyTakenNicknameException(
          "Nickname " + gameSaveUpdateRequest.getNickname() + " is already taken");
    }

    if (gameSaveUpdateRequest.getCharacteristics() != null) {
      Characteristics characteristicsUpdate = gameSaveUpdateRequest.getCharacteristics();
      characteristicsService.saveCharacteristics(
          saveId, characteristicsUpdate, cacheService.isEnabled());
    }
    if (gameSaveUpdateRequest.getCurrency() != null) {
      Currency currencyUpdate = gameSaveUpdateRequest.getCurrency();
      currencyService.saveCurrency(saveId, currencyUpdate, cacheService.isEnabled());
    }
    if (gameSaveUpdateRequest.getStage() != null) {
      Stage stageUpdate = gameSaveUpdateRequest.getStage();
      stageService.saveStage(saveId, stageUpdate, cacheService.isEnabled());
    }

    gameMetadataService.updateNickname(saveId, gameSaveUpdateRequest.getNickname());

    GameSaveViewEntity updatedView = getGameSaveEntity(saveId);

    GameSave gameSave = gameSaveViewMapper.map(updatedView);
    return enrichGameSaveWithCachedData(gameSave);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public boolean existsById(UUID gameSaveId) {
    return gameMetadataService.existsById(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void deleteGameSave(UUID saveId) {
    if (saveId == null) {
      throw new NotFoundException("Game save id is null");
    }
    if (!gameMetadataService.existsById(saveId)) {
      log.error("Game save with id {} not found", saveId);
      throw new NotFoundException("Game save with id " + saveId + " not found");
    }
    // Delete entities from currency & stage before deleting the game save
    gameMetadataService.deleteById(saveId);
    String saveIdString = saveId.toString();
    cacheService.clearGameSaveValues(saveIdString);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Stream<GameSave> getGameSaves() {
    var resultStream = gameSaveViewRepository.findAllGameSaves();
    List<GameSave> results = resultStream.map(gameSaveViewMapper::map).toList();
    return results.stream().map(this::enrichGameSaveWithCachedData);
  }

  @Transactional(readOnly = true)
  @Override
  public Long countGameSaves() {
    return gameMetadataService.count();
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public void checkGameSaveOwnership(UUID saveId, String userEmail)
      throws ForbiddenException, NotFoundException {
    if (!gameSaveOwnershipCache.isEnabled()) {
      GameSaveViewEntity gameSaveEntity = getGameSaveEntity(saveId);

      if (!Objects.equals(gameSaveEntity.getUserEmail(), userEmail)) {
        throw new ForbiddenException("The given user email is not the owner of the game save");
      }

      return;
    }
    String saveIdString = saveId.toString();
    Optional<String> optionalOwnership = gameSaveOwnershipCache.get(saveIdString);
    if (optionalOwnership.isEmpty()) {
      GameSaveViewEntity gameSaveEntity = getGameSaveEntity(saveId);
      gameSaveOwnershipCache.set(saveIdString, userEmail);
      if (!Objects.equals(gameSaveEntity.getUserEmail(), userEmail)) {
        throw new ForbiddenException("The given user username is not the owner of the game save");
      }
      return;
    }

    if (!Objects.equals(optionalOwnership.get(), userEmail)) {
      throw new ForbiddenException("The given username is not the owner of the game save");
    }
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Stream<GameSave> getGameSavesByUsername(String username) {
    if (!userService.checkUsernameExists(username)) {
      throw new NotFoundException("User with username " + username + " not found");
    }
    if (!gameSaveOwnershipCache.isEnabled()) {
      return gameSaveViewRepository
          .findGameSaveEntitiesByUserEmail(username)
          .map(gameSaveViewMapper::map);
    }
    return gameSaveViewRepository
        .findGameSaveEntitiesByUserEmail(username)
        .map(gameSaveViewMapper::map)
        .map(this::enrichGameSaveWithCachedData);
  }

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

  /**
   * Get the game save entity in the database or throw an exception if not found
   *
   * @param saveId the save id
   * @return the game save entity
   */
  private GameSaveViewEntity getGameSaveEntity(UUID saveId) {
    return gameSaveViewRepository
        .findGameSaveEntityById(saveId)
        .orElseThrow(() -> new NotFoundException("Game save with id " + saveId + " not found"));
  }
}
