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

package com.lsadf.core.unit.application.game.save;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyService;
import com.lsadf.core.application.game.save.impl.GameSaveServiceImpl;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.exception.AlreadyExistingGameSaveException;
import com.lsadf.core.exception.http.ForbiddenException;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.dto.request.game.save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.creation.GameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.creation.SimpleGameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class GameSaveServiceTests {

  @Mock private CacheManager cacheManager;
  @Mock private UserService userService;
  @Mock private GameMetadataService gameMetadataService;
  @Mock private StageService stageService;
  @Mock private CharacteristicsService characteristicsService;
  @Mock private CurrencyService currencyService;
  @Mock private CharacteristicsCachePort characteristicsCache;
  @Mock private CurrencyCachePort currencyCache;
  @Mock private StageCachePort stageCache;
  @Mock private GameMetadataCachePort gameMetadataCache;
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

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    openMocks = MockitoAnnotations.openMocks(this);
    gameSave = new GameSave(DB_METADATA, DB_CHARACERISTICS, DB_CURRENCY, DB_STAGE);
    cachedGameSave =
        new GameSave(DB_METADATA, CACHED_CHARACERISTICS, CACHED_CURRENCY, CACHED_STAGE);
    Mockito.reset(
        cacheManager,
        userService,
        gameMetadataService,
        stageService,
        characteristicsService,
        currencyService,
        gameSaveRepositoryPort,
        gameMetadataCache,
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
            cacheManager,
            gameMetadataCache,
            stageCache,
            currencyCache,
            characteristicsCache);
  }

  @Test
  void test_getGameSave_throwsNotFoundException_when_nonExistingGameSaveId() {
    when(gameSaveRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> gameSaveService.getGameSave(UUID));
  }

  @Test
  void test_getGameSave_returnsGameSave_when_existingGameSaveIdAndCached() {
    when(gameSaveRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(gameSave));
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CHARACERISTICS));
    when(stageCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_STAGE));
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CURRENCY));
    var actual = gameSaveService.getGameSave(UUID);
    assertThat(actual).isEqualTo(cachedGameSave);
  }

  @Test
  void test_getGameSave_returnsGameSave_when_existingGameSaveIdAndNotCached() {
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(stageCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(gameSaveRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(gameSave));
    var actual = gameSaveService.getGameSave(UUID);
    assertThat(actual).isEqualTo(gameSave);
  }

  @Test
  void test_createGameSave_throwsAlreadyExistingGameSaveException_when_existingId() {
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
  void test_createGameSave_throwsIllegalArgumentException_when_invalidRequestNickname() {
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
  void test_createGameSave_throwsIllegalArgumentException_when_invalidRequestUserEmail() {
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
  void test_createGameSave_throwsIllegalArgumentException_when_invalidSimpleRequestUserEmail() {
    GameSaveCreationRequest request = new SimpleGameSaveCreationRequest(USER_EMAIL);
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(false);
    assertThrows(NotFoundException.class, () -> gameSaveService.createGameSave(request));
  }

  @Test
  void test_createGameSave_createsSuccessfully_when_validSimpleRequest() {
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
  void test_createGameSave_createsSuccessfully_when_validAdminRequest() {
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
  void test_existsById_returnsTrue_when_validId() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(true);
    assertThat(gameSaveService.existsById(UUID)).isTrue();
  }

  @Test
  void test_existsById_returnsFalse_when_nonExistingId() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(false);
    assertThat(gameSaveService.existsById(UUID)).isFalse();
  }

  @Test
  void test_deleteById_deletesSuccessfully_when_validId() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(true);
    assertDoesNotThrow(() -> gameSaveService.deleteGameSave(UUID));
  }

  @Test
  void test_deleteById_throwsNotFoundException_when_nonExistingId() {
    when(gameMetadataService.existsById(any(UUID.class))).thenReturn(false);
    assertThrows(NotFoundException.class, () -> gameSaveService.deleteGameSave(UUID));
  }

  @Test
  void test_countGameSaves_returnsCount_when_called() {
    when(gameMetadataService.count()).thenReturn(1L);

    var count = gameSaveService.countGameSaves();

    assertThat(count).isEqualTo(1L);
  }

  @Test
  void test_checkGameSaveOwnership_succeeds_when_validIdAndUserEmail() {
    when(cacheManager.isEnabled()).thenReturn(false);
    when(gameMetadataService.getGameMetadata(UUID)).thenReturn(DB_METADATA);
    gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL);
  }

  @Test
  void test_checkGameSaveOwnership_succeeds_when_validIdAndUserEmailWithCache() {
    when(cacheManager.isEnabled()).thenReturn(true);
    when(gameMetadataCache.get(UUID.toString())).thenReturn(Optional.of(DB_METADATA));
    gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL);
  }

  @Test
  void test_checkGameSaveOwnership_succeeds_when_validIdAndUserEmailWithEmptyCache() {
    when(cacheManager.isEnabled()).thenReturn(true);
    when(gameMetadataCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(gameMetadataService.getGameMetadata(UUID)).thenReturn(DB_METADATA);
    gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL);
  }

  @Test
  void test_checkGameSaveOwnership_throwsNotFoundException_when_nonExistingId() {
    when(cacheManager.isEnabled()).thenReturn(false);
    when(gameMetadataService.getGameMetadata(UUID)).thenThrow(NotFoundException.class);
    assertThrows(
        NotFoundException.class, () -> gameSaveService.checkGameSaveOwnership(UUID, USER_EMAIL));
  }

  @Test
  void test_checkGameSaveOwnership_throwsForbiddenException_when_invalidUserEmail() {
    when(cacheManager.isEnabled()).thenReturn(false);
    when(gameMetadataService.getGameMetadata(UUID)).thenReturn(DB_METADATA);
    assertThrows(
        ForbiddenException.class,
        () -> gameSaveService.checkGameSaveOwnership(UUID, "anotherOne@gmail.com"));
  }

  @Test
  void test_getGameSavesByUsername_returnsGameSaves_when_noCache() {
    List<GameSave> gameSaves = List.of(gameSave);
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(true);
    when(cacheManager.isEnabled()).thenReturn(false);
    when(gameSaveRepositoryPort.findByUserEmail(USER_EMAIL)).thenReturn(gameSaves.stream());
    var actual = gameSaveService.getGameSavesByUsername(USER_EMAIL);
    assertThat(actual).isEqualTo(gameSaves);
  }

  @Test
  void test_getGameSavesByUsername_returnsGameSaves_when_cached() {
    Stream<GameSave> gameSaves = Stream.of(gameSave);
    when(cacheManager.isEnabled()).thenReturn(true);
    when(userService.checkUsernameExists(USER_EMAIL)).thenReturn(true);
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CHARACERISTICS));
    when(stageCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_STAGE));
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CURRENCY));
    when(gameSaveRepositoryPort.findByUserEmail(USER_EMAIL)).thenReturn(gameSaves);
    var actual = gameSaveService.getGameSavesByUsername(USER_EMAIL);
    assertThat(actual).hasSize(1);
    assertThat(actual.get(0)).isEqualTo(cachedGameSave);
  }

  @Test
  void test_getGameSaves_returnsGameSaves_when_noCache() {
    List<GameSave> gameSaves = List.of(gameSave);
    when(cacheManager.isEnabled()).thenReturn(false);
    when(gameSaveRepositoryPort.findAll()).thenReturn(gameSaves.stream());
    var actual = gameSaveService.getGameSaves();
    assertThat(actual).isEqualTo(gameSaves);
  }

  @Test
  void test_getGameSaves_returnsGameSaves_when_cached() {
    Stream<GameSave> gameSaves = Stream.of(gameSave);
    when(cacheManager.isEnabled()).thenReturn(true);
    when(gameSaveRepositoryPort.findAll()).thenReturn(gameSaves);
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CHARACERISTICS));
    when(stageCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_STAGE));
    when(currencyCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_CURRENCY));
    var actual = gameSaveService.getGameSaves();
    assertThat(actual).hasSize(1);
    var actualCachedGameSave = actual.get(0);
    assertThat(actualCachedGameSave).isEqualTo(cachedGameSave);
  }
}
