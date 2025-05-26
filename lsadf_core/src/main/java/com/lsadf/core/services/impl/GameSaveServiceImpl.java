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
package com.lsadf.core.services.impl;

import com.lsadf.core.common.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.core.common.exceptions.AlreadyTakenNicknameException;
import com.lsadf.core.common.exceptions.http.ForbiddenException;
import com.lsadf.core.common.exceptions.http.NotFoundException;
import com.lsadf.core.entities.*;
import com.lsadf.core.game.characteristics.Characteristics;
import com.lsadf.core.game.currency.Currency;
import com.lsadf.core.game.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.repositories.*;
import com.lsadf.core.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.core.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.requests.currency.CurrencyRequest;
import com.lsadf.core.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.core.requests.stage.StageRequest;
import com.lsadf.core.services.GameSaveService;
import com.lsadf.core.services.UserService;
import com.lsadf.core.user.User;
import java.util.*;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of GameSaveService */
@Slf4j
public class GameSaveServiceImpl implements GameSaveService {
  private final UserService userService;
  private final GameSaveRepository gameSaveRepository;
  private final InventoryRepository inventoryRepository;
  private final StageRepository stageRepository;
  private final CharacteristicsRepository characteristicsRepository;
  private final CurrencyRepository currencyRepository;

  private final Cache<String> gameSaveOwnershipCache;
  private final HistoCache<Stage> stageCache;
  private final HistoCache<Characteristics> characteristicsCache;
  private final HistoCache<Currency> currencyCache;

  public GameSaveServiceImpl(
      UserService userService,
      GameSaveRepository gameSaveRepository,
      InventoryRepository inventoryRepository,
      StageRepository stageRepository,
      CharacteristicsRepository characteristicsRepository,
      CurrencyRepository currencyRepository,
      Cache<String> gameSaveOwnershipCache,
      HistoCache<Stage> stageCache,
      HistoCache<Characteristics> characteristicsCache,
      HistoCache<Currency> currencyCache) {
    this.userService = userService;
    this.gameSaveRepository = gameSaveRepository;
    this.inventoryRepository = inventoryRepository;
    this.characteristicsRepository = characteristicsRepository;
    this.currencyRepository = currencyRepository;
    this.stageRepository = stageRepository;
    this.gameSaveOwnershipCache = gameSaveOwnershipCache;
    this.stageCache = stageCache;
    this.characteristicsCache = characteristicsCache;
    this.currencyCache = currencyCache;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public GameSaveEntity createGameSave(String userEmail) throws NotFoundException {
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

    return gameSaveRepository.save(saved);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public GameSaveEntity getGameSave(String saveId) throws NotFoundException {
    GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);
    return enrichGameSaveWithCachedData(gameSaveEntity);
  }

  /** {@inheritDoc} */
  @Override
  public GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest)
      throws NotFoundException, AlreadyExistingGameSaveException {

    User user = userService.getUserByUsername(creationRequest.getUserEmail());

    GameSaveEntity entity = GameSaveEntity.builder().userEmail(user.getUsername()).build();

    if (creationRequest.getId() != null) {
      if (gameSaveRepository.existsById(creationRequest.getId())) {
        throw new AlreadyExistingGameSaveException(
            "Game save with id " + creationRequest.getId() + " already exists");
      }
      entity.setId(creationRequest.getId());
    }

    GameSaveEntity saved = gameSaveRepository.save(entity);

    String nickname =
        creationRequest.getNickname() != null ? creationRequest.getNickname() : saved.getId();

    saved.setNickname(nickname);

    CharacteristicsRequest characteristicsRequest = creationRequest.getCharacteristics();
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .gameSave(saved)
            .attack(characteristicsRequest.getAttack())
            .critChance(characteristicsRequest.getCritChance())
            .critDamage(characteristicsRequest.getCritDamage())
            .health(characteristicsRequest.getHealth())
            .resistance(characteristicsRequest.getResistance())
            .build();

    saved.setCharacteristicsEntity(characteristicsEntity);

    CurrencyRequest currencyRequest = creationRequest.getCurrency();
    CurrencyEntity currencyEntity =
        CurrencyEntity.builder()
            .userEmail(user.getUsername())
            .id(saved.getId())
            .goldAmount(currencyRequest.getGold())
            .diamondAmount(currencyRequest.getDiamond())
            .emeraldAmount(currencyRequest.getEmerald())
            .amethystAmount(currencyRequest.getAmethyst())
            .build();

    saved.setCurrencyEntity(currencyEntity);

    StageRequest stageRequest = creationRequest.getStage();
    StageEntity stageEntity =
        StageEntity.builder()
            .currentStage(stageRequest.getCurrentStage())
            .maxStage(stageRequest.getMaxStage())
            .userEmail(user.getUsername())
            .id(saved.getId())
            .build();

    saved.setStageEntity(stageEntity);

    return gameSaveRepository.save(saved);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public GameSaveEntity updateNickname(
      String saveId, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest)
      throws NotFoundException, AlreadyTakenNicknameException {
    GameSaveEntity currentGameSave = getGameSaveEntity(saveId);

    if (gameSaveRepository
        .findGameSaveEntityByNickname(gameSaveUpdateNicknameRequest.getNickname())
        .isPresent()) {
      throw new AlreadyTakenNicknameException(
          "Game save with nickname "
              + gameSaveUpdateNicknameRequest.getNickname()
              + " already exists");
    }

    return updateGameSaveEntityNickname(currentGameSave, gameSaveUpdateNicknameRequest);
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public GameSaveEntity updateNickname(String saveId, AdminGameSaveUpdateRequest updateRequest)
      throws NotFoundException, AlreadyTakenNicknameException {
    GameSaveEntity currentGameSave = getGameSaveEntity(saveId);

    if (gameSaveRepository.findGameSaveEntityByNickname(updateRequest.getNickname()).isPresent()) {
      throw new AlreadyTakenNicknameException(
          "Game save with nickname " + updateRequest.getNickname() + " already exists");
    }

    return updateGameSaveEntity(currentGameSave, updateRequest);
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
    inventoryRepository.deleteById(saveId);
    stageRepository.deleteById(saveId);

    gameSaveRepository.deleteById(saveId);
    if (characteristicsCache.isEnabled()) {
      characteristicsCache.unset(saveId);
    }
    if (currencyCache.isEnabled()) {
      currencyCache.unset(saveId);
    }
    if (stageCache.isEnabled()) {
      stageCache.unset(saveId);
    }
    if (gameSaveOwnershipCache.isEnabled()) {
      gameSaveOwnershipCache.unset(saveId);
    }
  }

  /** {@inheritDoc} */
  @Override
  @Transactional(readOnly = true)
  public Stream<GameSaveEntity> getGameSaves() {
    return gameSaveRepository.findAllGameSaves().map(this::enrichGameSaveWithCachedData);
  }

  /**
   * Update the game save entity with the new nickname
   *
   * @param gameSaveEntity the game save entity
   * @param gameSaveUpdateNicknameRequest the nickname update request
   * @return the updated game save entity
   */
  private GameSaveEntity updateGameSaveEntityNickname(
      GameSaveEntity gameSaveEntity, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest) {
    boolean hasUpdates = false;

    if (!Objects.equals(
        gameSaveEntity.getNickname(), gameSaveUpdateNicknameRequest.getNickname())) {
      gameSaveEntity.setNickname(gameSaveUpdateNicknameRequest.getNickname());
      hasUpdates = true;
    }

    if (hasUpdates) {
      return gameSaveRepository.save(gameSaveEntity);
    }

    return gameSaveEntity;
  }

  /**
   * Update the game save entity with the new data
   *
   * @param gameSaveEntity the game save entity
   * @param adminUpdateRequest the admin update request
   * @return the updated game save entity
   */
  private GameSaveEntity updateGameSaveEntity(
      GameSaveEntity gameSaveEntity, AdminGameSaveUpdateRequest adminUpdateRequest) {
    boolean hasUpdates = false;

    if (!Objects.equals(gameSaveEntity.getNickname(), adminUpdateRequest.getNickname())) {
      gameSaveEntity.setNickname(adminUpdateRequest.getNickname());
      hasUpdates = true;
    }

    if (hasUpdates) {
      return gameSaveRepository.save(gameSaveEntity);
    }

    return gameSaveEntity;
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
  public Stream<GameSaveEntity> getGameSavesByUsername(String username) {
    if (!userService.checkUsernameExists(username)) {
      throw new NotFoundException("User with username " + username + " not found");
    }
    return gameSaveRepository
        .findGameSaveEntitiesByUserEmail(username)
        .map(this::enrichGameSaveWithCachedData);
  }

  /**
   * Enrich the game save with cached data
   *
   * @param gameSave the game save
   * @return the game save with cached data
   */
  private GameSaveEntity enrichGameSaveWithCachedData(GameSaveEntity gameSave) {
    if (characteristicsCache.isEnabled()) {
      Optional<Characteristics> optionalCharacteristics =
          characteristicsCache.get(gameSave.getId());
      optionalCharacteristics.ifPresent(gameSave::setCharacteristicsEntity);
    }
    if (currencyCache.isEnabled()) {
      Optional<Currency> optionalCurrency = currencyCache.get(gameSave.getId());
      optionalCurrency.ifPresent(gameSave::setCurrencyEntity);
    }
    if (stageCache.isEnabled()) {
      Optional<Stage> optionalStage = stageCache.get(gameSave.getId());
      optionalStage.ifPresent(gameSave::setStageEntity);
    }

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
