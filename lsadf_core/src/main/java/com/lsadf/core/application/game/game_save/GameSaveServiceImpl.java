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
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
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
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.game.currency.CurrencyRepository;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntityMapper;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveRepository;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryRepository;
import com.lsadf.core.infra.persistence.game.stage.StageEntity;
import com.lsadf.core.infra.persistence.game.stage.StageRepository;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.game_save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.game_save.update.GameSaveUpdateRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import java.util.*;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of GameSaveService */
@Slf4j
public class GameSaveServiceImpl implements GameSaveService {
  private final UserService userService;

  private final GameSaveRepository gameSaveRepository;
  private final CurrencyRepository currencyRepository;
  private final StageRepository stageRepository;
  private final InventoryRepository inventoryRepository;
  private final CharacteristicsRepository characteristicsRepository;

  private final CacheService cacheService;
  private final Cache<String> gameSaveOwnershipCache;
  private final HistoCache<Stage> stageCache;
  private final HistoCache<Currency> currencyCache;
  private final HistoCache<Characteristics> characteristicsCache;

  private static final GameSaveEntityMapper gameSaveEntityMapper = GameSaveEntityMapper.INSTANCE;

  public GameSaveServiceImpl(
      UserService userService,
      GameSaveRepository gameSaveRepository,
      CharacteristicsRepository characteristicsRepository,
      CurrencyRepository currencyRepository,
      StageRepository stageRepository,
      InventoryRepository inventoryRepository,
      CacheService cacheService,
      Cache<String> gameSaveOwnershipCache,
      HistoCache<Stage> stageCache,
      HistoCache<Currency> currencyCache,
      HistoCache<Characteristics> characteristicsCache) {
    this.userService = userService;
    this.gameSaveRepository = gameSaveRepository;
    this.cacheService = cacheService;
    this.gameSaveOwnershipCache = gameSaveOwnershipCache;
    this.stageCache = stageCache;
    this.currencyCache = currencyCache;
    this.characteristicsCache = characteristicsCache;
    this.characteristicsRepository = characteristicsRepository;
    this.currencyRepository = currencyRepository;
    this.stageRepository = stageRepository;
    this.inventoryRepository = inventoryRepository;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public GameSave createGameSave(String userEmail) throws NotFoundException {
    log.info("Creating new save for user {}", userEmail);

    User user = userService.getUserByUsername(userEmail);

    GameSaveEntity entity = GameSaveEntity.builder().userEmail(userEmail).build();

    GameSaveEntity saved = gameSaveRepository.save(entity);
    saved.setNickname(saved.getId());

    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder().id(saved.getId()).build();

    saved.setCharacteristicsEntity(characteristicsEntity);

    CurrencyEntity currencyEntity =
        CurrencyEntity.builder().userEmail(user.getUsername()).id(saved.getId()).build();

    saved.setCurrencyEntity(currencyEntity);

    InventoryEntity inventoryEntity = InventoryEntity.builder().items(new HashSet<>()).build();

    saved.setInventoryEntity(inventoryEntity);

    StageEntity stageEntity =
        StageEntity.builder().userEmail(user.getUsername()).id(saved.getId()).build();

    saved.setStageEntity(stageEntity);

    var results = gameSaveRepository.save(saved);
    return gameSaveEntityMapper.map(results);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public GameSave getGameSave(String saveId) throws NotFoundException {
    GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);
    GameSave gameSave = gameSaveEntityMapper.map(gameSaveEntity);
    return enrichGameSaveWithCachedData(gameSave);
  }

  /** {@inheritDoc} */
  @Override
  public GameSave createGameSave(AdminGameSaveCreationRequest creationRequest)
      throws NotFoundException, AlreadyExistingGameSaveException {

    User user = userService.getUserByUsername(creationRequest.userEmail());

    GameSaveEntity entity = GameSaveEntity.builder().userEmail(user.getUsername()).build();

    if (creationRequest.id() != null) {
      if (gameSaveRepository.existsById(creationRequest.id())) {
        throw new AlreadyExistingGameSaveException(
            "Game save with id " + creationRequest.id() + " already exists");
      }
      entity.setId(creationRequest.id());
    }

    GameSaveEntity saved = gameSaveRepository.save(entity);

    String nickname =
        creationRequest.nickname() != null ? creationRequest.nickname() : saved.getId();

    saved.setNickname(nickname);

    CharacteristicsRequest characteristicsRequest = creationRequest.characteristics();
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .gameSave(saved)
            .attack(characteristicsRequest.attack())
            .critChance(characteristicsRequest.critChance())
            .critDamage(characteristicsRequest.critDamage())
            .health(characteristicsRequest.health())
            .resistance(characteristicsRequest.resistance())
            .build();

    saved.setCharacteristicsEntity(characteristicsEntity);

    CurrencyRequest currencyRequest = creationRequest.currency();
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .userEmail(user.getUsername())
            .id(saved.getId())
            .goldAmount(currencyRequest.gold())
            .diamondAmount(currencyRequest.diamond())
            .emeraldAmount(currencyRequest.emerald())
            .amethystAmount(currencyRequest.amethyst())
            .build();

    saved.setCurrencyEntity(currencyEntity);

    StageRequest stageRequest = creationRequest.stage();
    StageEntity stageEntity =
        StageEntity.builder()
            .currentStage(stageRequest.currentStage())
            .maxStage(stageRequest.maxStage())
            .userEmail(user.getUsername())
            .id(saved.getId())
            .build();

    saved.setStageEntity(stageEntity);

    var result = gameSaveRepository.save(saved);
    return gameSaveEntityMapper.map(result);
  }

  /** {@inheritDoc} */
  @Override
  public GameSave updateGameSave(String saveId, GameSaveUpdateRequest gameSaveUpdateRequest)
      throws ForbiddenException,
          NotFoundException,
          UnauthorizedException,
          AlreadyTakenNicknameException {
    if (saveId == null) {
      throw new NotFoundException("Game save id is null");
    }

    GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);

    if (gameSaveRepository
        .findGameSaveEntityByNickname(gameSaveUpdateRequest.getNickname())
        .isPresent()) {
      throw new AlreadyTakenNicknameException(
          "Nickname " + gameSaveUpdateRequest.getNickname() + " is already taken");
    }

    if (!Objects.equals(gameSaveEntity.getNickname(), gameSaveUpdateRequest.getNickname())) {
      gameSaveEntity.setNickname(gameSaveUpdateRequest.getNickname());
    }

    if (gameSaveUpdateRequest.getCharacteristics() != null) {
      Characteristics characteristicsUpdate = gameSaveUpdateRequest.getCharacteristics();
      gameSaveEntity.setCharacteristicsEntity(characteristicsUpdate);
    }
    if (gameSaveUpdateRequest.getCurrency() != null) {
      Currency currencyUpdate = gameSaveUpdateRequest.getCurrency();
      gameSaveEntity.setCurrencyEntity(currencyUpdate);
    }
    if (gameSaveUpdateRequest.getStage() != null) {
      Stage stageUpdate = gameSaveUpdateRequest.getStage();
      gameSaveEntity.setStageEntity(stageUpdate);
    }

    GameSaveEntity updated = gameSaveRepository.save(gameSaveEntity);

    return gameSaveEntityMapper.map(updated);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public boolean existsById(String gameSaveId) {
    return gameSaveRepository.existsById(gameSaveId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public void deleteGameSave(String saveId) {
    if (saveId == null) {
      throw new NotFoundException("Game save id is null");
    }
    if (!gameSaveRepository.existsById(saveId)) {
      log.error("Game save with id {} not found", saveId);
      throw new NotFoundException("Game save with id " + saveId + " not found");
    }
    // Delete entities from currency & stage before deleting the game save
    characteristicsRepository.deleteById(saveId);
    currencyRepository.deleteById(saveId);
    stageRepository.deleteById(saveId);
    inventoryRepository.deleteById(saveId);
    gameSaveRepository.deleteById(saveId);
    cacheService.clearGameSaveValues(saveId);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Stream<GameSave> getGameSaves() {
    try (var resultStream = gameSaveRepository.findAllGameSaves()) {
      List<GameSave> results = resultStream.map(gameSaveEntityMapper::map).toList();
      return results.stream().map(this::enrichGameSaveWithCachedData);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public Long countGameSaves() {
    return gameSaveRepository.count();
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public void checkGameSaveOwnership(String saveId, String userEmail)
      throws ForbiddenException, NotFoundException {
    if (!gameSaveOwnershipCache.isEnabled()) {
      GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);

      if (!Objects.equals(gameSaveEntity.getUserEmail(), userEmail)) {
        throw new ForbiddenException("The given user email is not the owner of the game save");
      }

      return;
    }

    Optional<String> optionalOwnership = gameSaveOwnershipCache.get(saveId);
    if (optionalOwnership.isEmpty()) {
      GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);
      gameSaveOwnershipCache.set(saveId, userEmail);
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
    try (var resultStream = gameSaveRepository.findGameSaveEntitiesByUserEmail(username)) {
      List<GameSave> results = resultStream.map(gameSaveEntityMapper::map).toList();
      return results.stream().map(this::enrichGameSaveWithCachedData);
    }
  }

  private GameSave enrichGameSaveWithCachedData(GameSave gameSave) {
    var cacheCharacteristics = characteristicsCache.get(gameSave.getId());
    cacheCharacteristics.ifPresent(gameSave::setCharacteristics);

    var cacheCurrency = currencyCache.get(gameSave.getId());
    cacheCurrency.ifPresent(gameSave::setCurrency);

    var cacheStage = stageCache.get(gameSave.getId());
    cacheStage.ifPresent(gameSave::setStage);
    return gameSave;
  }

  /**
   * Get the game save entity in the database or throw an exception if not found
   *
   * @param saveId the save id
   * @return the game save entity
   */
  private GameSaveEntity getGameSaveEntity(String saveId) {
    return gameSaveRepository
        .findById(saveId)
        .orElseThrow(() -> new NotFoundException("Game save with id " + saveId + " not found"));
  }
}
