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
package com.lsadf.core.application.game.game_save;

import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.game_save.GameSave;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.exception.AlreadyExistingGameSaveException;
import com.lsadf.core.infra.exception.AlreadyTakenNicknameException;
import com.lsadf.core.infra.exception.http.ForbiddenException;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.exception.http.UnauthorizedException;
import com.lsadf.core.infra.persistence.table.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.table.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.table.game.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.table.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.table.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.table.game.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.table.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.table.game.game_save.GameSaveEntityMapper;
import com.lsadf.core.infra.persistence.table.game.game_save.GameSaveRepository;
import com.lsadf.core.infra.persistence.table.game.stage.StageEntity;
import com.lsadf.core.infra.persistence.table.game.stage.StageEntityMapper;
import com.lsadf.core.infra.persistence.table.game.stage.StageRepository;
import com.lsadf.core.infra.persistence.view.GameSaveViewEntity;
import com.lsadf.core.infra.persistence.view.GameSaveViewMapper;
import com.lsadf.core.infra.persistence.view.GameSaveViewRepository;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.game_save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.game_save.update.GameSaveUpdateRequest;
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

  // Repositories and services to access the database and the cache
  private final GameSaveViewRepository gameSaveViewRepository;
  private final GameSaveRepository gameSaveEntityRepository;
  private final CurrencyRepository currencyRepository;
  private final StageRepository stageRepository;
  private final CharacteristicsRepository characteristicsRepository;

  private final CacheService cacheService;
  private final Cache<String> gameSaveOwnershipCache;
  private final HistoCache<Stage> stageCache;
  private final HistoCache<Currency> currencyCache;
  private final HistoCache<Characteristics> characteristicsCache;

  private static final GameSaveEntityMapper gameSaveEntityMapper = GameSaveEntityMapper.INSTANCE;
  private static final GameSaveViewMapper gameSaveViewMapper = GameSaveViewMapper.INSTANCE;

  @Autowired
  public GameSaveServiceImpl(
      UserService userService,
      GameSaveRepository gameSaveEntityRepository,
      GameSaveViewRepository gameSaveViewRepository,
      CharacteristicsRepository characteristicsRepository,
      CurrencyRepository currencyRepository,
      StageRepository stageRepository,
      CacheService cacheService,
      Cache<String> gameSaveOwnershipCache,
      HistoCache<Stage> stageCache,
      HistoCache<Currency> currencyCache,
      HistoCache<Characteristics> characteristicsCache) {
    this.userService = userService;
    this.gameSaveEntityRepository = gameSaveEntityRepository;
    this.gameSaveViewRepository = gameSaveViewRepository;
    this.cacheService = cacheService;
    this.gameSaveOwnershipCache = gameSaveOwnershipCache;
    this.stageCache = stageCache;
    this.currencyCache = currencyCache;
    this.characteristicsCache = characteristicsCache;
    this.characteristicsRepository = characteristicsRepository;
    this.currencyRepository = currencyRepository;
    this.stageRepository = stageRepository;
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
    User user = userService.getUserByUsername(creationRequest.getUserEmail());
    GameSaveEntity newGameSaveEntity;
    if (creationRequest.getId() != null) {
      if (gameSaveEntityRepository.existsById(creationRequest.getId())) {
        throw new AlreadyExistingGameSaveException(
            "Game save with id " + creationRequest.getId() + " already exists");
      }
      newGameSaveEntity =
          gameSaveEntityRepository.createNewGameSaveEntity(
              creationRequest.getId(), user.getUsername(), creationRequest.getNickname());
    } else {
      newGameSaveEntity =
          gameSaveEntityRepository.createNewGameSaveEntity(
              user.getUsername(), creationRequest.getNickname());
    }

    GameSave newGameSave = gameSaveEntityMapper.map(newGameSaveEntity);

    CharacteristicsEntity newCharacteristicsEntity;
    CharacteristicsRequest characteristicsRequest = creationRequest.getCharacteristicsRequest();
    if (characteristicsRequest != null) {
      newCharacteristicsEntity =
          characteristicsRepository.createNewCharacteristicsEntity(
              newGameSave.getId(),
              characteristicsRequest.attack(),
              characteristicsRequest.critChance(),
              characteristicsRequest.critDamage(),
              characteristicsRequest.health(),
              characteristicsRequest.resistance());
    } else {
      newCharacteristicsEntity =
          characteristicsRepository.createNewCharacteristicsEntity(newGameSave.getId());
    }
    Characteristics newCharacteristics =
        CharacteristicsEntityMapper.INSTANCE.map(newCharacteristicsEntity);
    newGameSave.setCharacteristics(newCharacteristics);

    CurrencyEntity newCurrencyEntity;
    CurrencyRequest currencyRequest = creationRequest.getCurrencyRequest();
    if (currencyRequest != null) {
      newCurrencyEntity =
          currencyRepository.createNewCurrencyEntity(
              newGameSave.getId(),
              currencyRequest.gold(),
              currencyRequest.diamond(),
              currencyRequest.emerald(),
              currencyRequest.amethyst());
    } else {
      newCurrencyEntity = currencyRepository.createNewCurrencyEntity(newGameSave.getId());
    }
    Currency newCurrency = CurrencyEntityMapper.INSTANCE.map(newCurrencyEntity);
    newGameSave.setCurrency(newCurrency);

    StageRequest stageRequest = creationRequest.getStageRequest();
    StageEntity newStageEntity;
    if (stageRequest != null) {
      newStageEntity =
          stageRepository.createNewStageEntity(
              newGameSave.getId(), stageRequest.currentStage(), stageRequest.maxStage());
    } else {
      newStageEntity = stageRepository.createNewStageEntity(newGameSave.getId());
    }
    Stage newStage = StageEntityMapper.INSTANCE.map(newStageEntity);
    newGameSave.setStage(newStage);

    return newGameSave;
  }

  /** {@inheritDoc} */
  @Override
  public GameSave updateGameSave(UUID saveId, GameSaveUpdateRequest gameSaveUpdateRequest)
      throws ForbiddenException,
          NotFoundException,
          UnauthorizedException,
          AlreadyTakenNicknameException {
    if (saveId == null) {
      throw new NotFoundException("Game save id is null");
    }

    if (gameSaveEntityRepository.existsByNickname(gameSaveUpdateRequest.getNickname())) {
      throw new AlreadyTakenNicknameException(
          "Nickname " + gameSaveUpdateRequest.getNickname() + " is already taken");
    }

    if (gameSaveUpdateRequest.getCharacteristics() != null) {
      Characteristics characteristicsUpdate = gameSaveUpdateRequest.getCharacteristics();
      if (Boolean.TRUE.equals(cacheService.isEnabled())) {
        characteristicsCache.set(saveId.toString(), characteristicsUpdate);
      } else {
        characteristicsRepository.updateCharacteristics(
            saveId,
            characteristicsUpdate.getAttack(),
            characteristicsUpdate.getCritChance(),
            characteristicsUpdate.getCritDamage(),
            characteristicsUpdate.getHealth(),
            characteristicsUpdate.getResistance());
      }
    }
    if (gameSaveUpdateRequest.getCurrency() != null) {
      Currency currencyUpdate = gameSaveUpdateRequest.getCurrency();
      if (Boolean.TRUE.equals(cacheService.isEnabled())) {
        currencyCache.set(saveId.toString(), currencyUpdate);
      } else {
        currencyRepository.updateCurrency(
            saveId,
            currencyUpdate.getGold(),
            currencyUpdate.getDiamond(),
            currencyUpdate.getEmerald(),
            currencyUpdate.getAmethyst());
      }
    }
    if (gameSaveUpdateRequest.getStage() != null) {
      Stage stageUpdate = gameSaveUpdateRequest.getStage();
      if (Boolean.TRUE.equals(cacheService.isEnabled())) {
        stageCache.set(saveId.toString(), stageUpdate);
      } else {
        stageRepository.updateStage(
            saveId, stageUpdate.getCurrentStage(), stageUpdate.getMaxStage());
      }
    }

    gameSaveEntityRepository.updateGameSaveEntityNickname(
        saveId, gameSaveUpdateRequest.getNickname());

    Optional<GameSaveViewEntity> gameSaveViewEntityOptional =
        gameSaveViewRepository.findGameSaveEntityById(saveId);
    if (gameSaveViewEntityOptional.isEmpty()) {
      log.error("Game save with id {} not found", saveId);
      throw new NotFoundException("Game save with id " + saveId + " not found");
    }
    GameSave gameSave = gameSaveViewMapper.map(gameSaveViewEntityOptional.get());
    return enrichGameSaveWithCachedData(gameSave);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public boolean existsById(UUID gameSaveId) {
    return gameSaveEntityRepository.existsById(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void deleteGameSave(UUID saveId) {
    if (saveId == null) {
      throw new NotFoundException("Game save id is null");
    }
    if (!gameSaveEntityRepository.existsById(saveId)) {
      log.error("Game save with id {} not found", saveId);
      throw new NotFoundException("Game save with id " + saveId + " not found");
    }
    // Delete entities from currency & stage before deleting the game save
    gameSaveEntityRepository.deleteGameSaveEntityById(saveId);
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
    return gameSaveEntityRepository.count();
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
    var cacheCharacteristics = characteristicsCache.get(gameSave.getId().toString());
    cacheCharacteristics.ifPresent(gameSave::setCharacteristics);

    var cacheCurrency = currencyCache.get(gameSave.getId().toString());
    cacheCurrency.ifPresent(gameSave::setCurrency);

    var cacheStage = stageCache.get(gameSave.getId().toString());
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
