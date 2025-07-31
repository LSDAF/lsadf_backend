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

package com.lsadf.core.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.GameSaveServiceImpl;
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
import com.lsadf.core.infra.exception.http.ForbiddenException;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.request.game.save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.save.creation.SimpleGameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.stage.StageRequest;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class GameSaveServiceTests {

  @Mock private CacheService cacheService;
  @Mock private UserService userService;
  @Mock private GameMetadataService gameMetadataService;
  @Mock private StageService stageService;
  @Mock private CharacteristicsService characteristicsService;
  @Mock private CurrencyService currencyService;
  @Mock private HistoCache<Currency> currencyCache;
  @Mock private HistoCache<Characteristics> characteristicsCache;
  @Mock private HistoCache<Stage> stageCache;
  @Mock private Cache<String> gameSaveOwnershipCache;
  @Mock private GameSaveRepositoryPort gameSaveRepositoryPort;

  private GameSaveService gameSaveService;

  private static final UUID UUID = java.util.UUID.randomUUID();
  private static final String USER_EMAIL = "toto@test.com";
  private static final String NICKNAME = "ANewGame";

  private static final Characteristics DB_CHARACERISTICS =
      Characteristics.builder()
          .attack(1L)
          .critChance(2L)
          .critDamage(3L)
          .health(4L)
          .resistance(5L)
          .build();

  private static final Characteristics CACHED_CHARACERISTICS =
      Characteristics.builder()
          .attack(2L)
          .critChance(4L)
          .critDamage(6L)
          .health(8L)
          .resistance(10L)
          .build();

  private static final Currency DB_CURRENCY =
      Currency.builder().gold(1L).diamond(2L).emerald(3L).amethyst(4L).build();

  private static final Currency CACHED_CURRENCY =
      Currency.builder().gold(2L).diamond(4L).emerald(6L).amethyst(8L).build();

  private static final Stage DB_STAGE = Stage.builder().currentStage(10L).maxStage(20L).build();

  private static final Stage CACHED_STAGE = Stage.builder().currentStage(20L).maxStage(40L).build();

  private static final GameMetadata DB_METADATA =
      GameMetadata.builder()
          .id(UUID)
          .userEmail(USER_EMAIL)
          .nickname("aNewGame")
          .createdAt(new Date())
          .updatedAt(new Date())
          .build();

  private GameSave gameSave;

  private GameSave cachedGameSave;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    gameSave = new GameSave(DB_METADATA, DB_CHARACERISTICS, DB_CURRENCY, DB_STAGE);
    cachedGameSave =
        new GameSave(DB_METADATA, CACHED_CHARACERISTICS, CACHED_CURRENCY, CACHED_STAGE);
    Mockito.reset(
        cacheService,
        userService,
        gameMetadataService,
        stageService,
        characteristicsService,
        currencyService,
        gameSaveRepositoryPort,
        gameSaveOwnershipCache,
        stageCache,
        currencyCache,
        characteristicsCache);
    gameSaveService =
        new GameSaveServiceImpl(
            gameMetadataService,
            characteristicsService,
            stageService,
            currencyService,
            userService,
            gameSaveRepositoryPort,
            cacheService,
            gameSaveOwnershipCache,
            stageCache,
            currencyCache,
            characteristicsCache);
  }

  @Test
  void test01_getGameSave_on_non_existing_gameSaveId() {
    when(gameSaveRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> gameSaveService.getGameSave(UUID));
  }

  @Test
  void test02_getGameSave_on_existing_gameSaveId_when_cached() {
    when(gameSaveRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(gameSave));
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CHARACERISTICS));
    when(stageCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_STAGE));
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CURRENCY));
    var actual = gameSaveService.getGameSave(UUID);
    assertThat(actual).isEqualTo(cachedGameSave);
  }

  @Test
  void test03_getGameSave_on_existing_gameSaveId_when_not_cached() {
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(stageCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(gameSaveRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(gameSave));
    var actual = gameSaveService.getGameSave(UUID);
    assertThat(actual).isEqualTo(gameSave);
  }

  @Test
  void test04_getGameSave_on_null_gameSaveId() {
    assertThrows(IllegalArgumentException.class, () -> gameSaveService.getGameSave(null));
  }

  @Test
  void test05_createGameSave_on_already_existing_id() {
    GameMetadataRequest metadataRequest = new GameMetadataRequest(UUID, USER_EMAIL, NICKNAME);
    CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 2L, 3L, 4L, 5L);
    CurrencyRequest currencyRequest = new CurrencyRequest(1L, 2L, 3L, 4L);
    StageRequest stageRequest = new StageRequest(10L, 20L);
    GameSaveCreationRequest request =
        new AdminGameSaveCreationRequest(
            metadataRequest, characteristicsRequest, currencyRequest, stageRequest);
    when(gameMetadataService.existsById(UUID)).thenReturn(true);
    assertThrows(
        AlreadyExistingGameSaveException.class, () -> gameSaveService.createGameSave(request));
  }

  @Test
  void test06_createGameSave_on_invalid_request_nickname() {
    GameMetadataRequest metadataRequest = new GameMetadataRequest(UUID, USER_EMAIL, NICKNAME);
    CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 2L, 3L, 4L, 5L);
    CurrencyRequest currencyRequest = new CurrencyRequest(1L, 2L, 3L, 4L);
    StageRequest stageRequest = new StageRequest(10L, 20L);
    GameSaveCreationRequest request =
        new AdminGameSaveCreationRequest(
            metadataRequest, characteristicsRequest, currencyRequest, stageRequest);
    when(gameMetadataService.existsById(UUID)).thenReturn(false);
    when(gameMetadataService.existsByNickname(NICKNAME)).thenReturn(true);
    assertThrows(
        AlreadyExistingGameSaveException.class, () -> gameSaveService.createGameSave(request));
  }

  @Test
  void test07_createGameSave_on_invalid_request_userEmail() {
    GameMetadataRequest metadataRequest = new GameMetadataRequest(UUID, USER_EMAIL, NICKNAME);
    CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 2L, 3L, 4L, 5L);
    CurrencyRequest currencyRequest = new CurrencyRequest(1L, 2L, 3L, 4L);
    StageRequest stageRequest = new StageRequest(10L, 20L);
    GameSaveCreationRequest request =
        new AdminGameSaveCreationRequest(
            metadataRequest, characteristicsRequest, currencyRequest, stageRequest);
    when(gameMetadataService.existsById(UUID)).thenReturn(false);
    when(gameMetadataService.existsByNickname(NICKNAME)).thenReturn(false);
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(false);
    assertThrows(NotFoundException.class, () -> gameSaveService.createGameSave(request));
  }

  @Test
  void test07_createGameSave_on_invalid_simple_request_userEmail() {
    GameSaveCreationRequest request = new SimpleGameSaveCreationRequest(USER_EMAIL);
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(false);
    assertThrows(NotFoundException.class, () -> gameSaveService.createGameSave(request));
  }

  @Test
  void test08_createGameSave_with_valid_simple_request() {
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(true);
    when(gameMetadataService.createNewGameMetadata(
            nullable(UUID.class), any(String.class), nullable(String.class)))
        .thenReturn(DB_METADATA);
    when(characteristicsService.createNewCharacteristics(UUID)).thenReturn(DB_CHARACERISTICS);

    when(stageService.createNewStage(UUID)).thenReturn(DB_STAGE);
    when(currencyService.createNewCurrency(UUID)).thenReturn(DB_CURRENCY);
    GameSaveCreationRequest gameSaveCreationRequest = new SimpleGameSaveCreationRequest(USER_EMAIL);
    GameSave actual = gameSaveService.createGameSave(gameSaveCreationRequest);
    assertThat(actual).isEqualTo(gameSave);
  }

  @Test
  void test09_createGameSave_with_valid_admin_request() {
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(true);
    when(gameMetadataService.createNewGameMetadata(
            any(UUID.class), any(String.class), any(String.class)))
        .thenReturn(DB_METADATA);
    when(characteristicsService.createNewCharacteristics(
            any(UUID.class),
            any(Long.class),
            any(Long.class),
            any(Long.class),
            any(Long.class),
            any(Long.class)))
        .thenReturn(DB_CHARACERISTICS);
    when(stageService.createNewStage(any(UUID.class), any(Long.class), any(Long.class)))
        .thenReturn(DB_STAGE);
    when(currencyService.createNewCurrency(
            any(UUID.class), any(Long.class), any(Long.class), any(Long.class), any(Long.class)))
        .thenReturn(DB_CURRENCY);
    GameMetadataRequest gameMetadataRequest =
        new GameMetadataRequest(DB_METADATA.id(), DB_METADATA.userEmail(), DB_METADATA.nickname());
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(
            DB_CHARACERISTICS.attack(),
            DB_CHARACERISTICS.critChance(),
            DB_CHARACERISTICS.critDamage(),
            DB_CHARACERISTICS.health(),
            DB_CHARACERISTICS.resistance());
    CurrencyRequest currencyRequest =
        new CurrencyRequest(
            DB_CURRENCY.gold(),
            DB_CURRENCY.diamond(),
            DB_CURRENCY.emerald(),
            DB_CURRENCY.amethyst());
    StageRequest stageRequest = new StageRequest(DB_STAGE.currentStage(), DB_STAGE.maxStage());
    GameSaveCreationRequest gameSaveCreationRequest =
        new AdminGameSaveCreationRequest(
            gameMetadataRequest, characteristicsRequest, currencyRequest, stageRequest);
    var actual = gameSaveService.createGameSave(gameSaveCreationRequest);
    assertThat(actual).isEqualTo(gameSave);
  }

  @Test
  void test09_updateGameSave_on_invalid_request_id() {
    when(gameMetadataService.existsByNickname(DB_METADATA.nickname())).thenReturn(true);
    assertThrows(IllegalArgumentException.class, () -> gameSaveService.updateGameSave(null, null));
  }

  @Test
  void test10_existsById_on_valid_id() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(true);
    assertThat(gameSaveService.existsById(UUID)).isTrue();
  }

  @Test
  void test11_existsById_on_non_existing_id() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(false);
    assertThat(gameSaveService.existsById(UUID)).isFalse();
  }

  @Test
  void test12_deleteById_on_valid_id() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(true);
    gameSaveService.deleteGameSave(UUID);
  }

  @Test
  void test13_deleteById_on_non_existing_id() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(false);
    assertThrows(NotFoundException.class, () -> gameSaveService.deleteGameSave(UUID));
  }

  @Test
  void test14_countGameSaves() {
    when(gameMetadataService.count()).thenReturn(1L);

    var count = gameSaveService.countGameSaves();

    assertThat(count).isEqualTo(1L);
  }

  @Test
  void test15_checkGameSaveOwnership_on_valid_id_and_user_email() {
    when(gameSaveOwnershipCache.isEnabled()).thenReturn(false);
    when(gameMetadataService.findOwnerEmailById(UUID)).thenReturn(USER_EMAIL);
    gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL);
  }

  @Test
  void test15_checkGameSaveOwnership_on_valid_id_and_user_email_with_cache() {
    when(gameSaveOwnershipCache.isEnabled()).thenReturn(true);
    when(gameSaveOwnershipCache.get(UUID.toString())).thenReturn(Optional.of(USER_EMAIL));
    gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL);
  }

  @Test
  void test15_checkGameSaveOwnership_on_valid_id_and_user_email_with_empty_cache() {
    when(gameSaveOwnershipCache.isEnabled()).thenReturn(true);
    when(gameSaveOwnershipCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(gameMetadataService.findOwnerEmailById(UUID)).thenReturn(USER_EMAIL);
    gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL);
  }

  @Test
  void test16_checkGameSaveOwnership_on_non_existing_id() {
    when(gameSaveOwnershipCache.isEnabled()).thenReturn(false);
    when(gameMetadataService.findOwnerEmailById(UUID)).thenThrow(NotFoundException.class);
    assertThrows(
        NotFoundException.class, () -> gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL));
  }

  @Test
  void test17_checkGameSaveOwnership_on_invalid_user_email() {
    when(gameSaveOwnershipCache.isEnabled()).thenReturn(false);
    when(gameMetadataService.findOwnerEmailById(UUID)).thenReturn(USER_EMAIL);
    assertThrows(
        ForbiddenException.class,
        () -> gameSaveService.checkGameSaveOwnership(UUID, "anotherOne@gmail.com"));
  }

  @Test
  void test18_getGameSavesByUsername_with_no_cache() {
    Stream<GameSave> gameSaves = Stream.of(gameSave);
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(true);
    when(gameSaveOwnershipCache.isEnabled()).thenReturn(false);
    when(gameSaveRepositoryPort.findByUserEmail(USER_EMAIL)).thenReturn(gameSaves);
    var actual = gameSaveService.getGameSavesByUsername(USER_EMAIL);
    assertThat(actual).isEqualTo(gameSaves);
  }

  @Test
  void test19_getGameSavesByUsername_with_cache() {
    Stream<GameSave> gameSaves = Stream.of(gameSave);
    when(gameSaveOwnershipCache.isEnabled()).thenReturn(true);
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(true);
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CHARACERISTICS));
    when(stageCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_STAGE));
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CURRENCY));
    when(gameSaveRepositoryPort.findByUserEmail(USER_EMAIL)).thenReturn(gameSaves);
    var actual = gameSaveService.getGameSavesByUsername(USER_EMAIL);
    var list = actual.toList();
    assertThat(list).hasSize(1);
    assertThat(list.get(0)).isEqualTo(cachedGameSave);
  }

  @Test
  void test20_getGameSaves_with_no_cache() {
    Stream<GameSave> gameSaves = Stream.of(gameSave);
    when(cacheService.isEnabled()).thenReturn(false);
    when(gameSaveRepositoryPort.findAll()).thenReturn(gameSaves);
    var actual = gameSaveService.getGameSaves();
    assertThat(actual).isEqualTo(gameSaves);
  }

  @Test
  void test21_getGameSaves_with_cache() {
    Stream<GameSave> gameSaves = Stream.of(gameSave);
    when(cacheService.isEnabled()).thenReturn(true);
    when(gameSaveRepositoryPort.findAll()).thenReturn(gameSaves);
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CHARACERISTICS));
    when(stageCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_STAGE));
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CURRENCY));
    var actual = gameSaveService.getGameSaves();
    var list = actual.toList();
    assertThat(list).hasSize(1);
    var actualCachedGameSave = list.get(0);
    assertThat(actualCachedGameSave).isEqualTo(cachedGameSave);
  }
}
