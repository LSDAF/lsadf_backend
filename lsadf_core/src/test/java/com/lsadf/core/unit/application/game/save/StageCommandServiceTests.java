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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.stage.StageCommandService;
import com.lsadf.core.application.game.save.stage.StageQueryService;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.command.InitializeDefaultStageCommand;
import com.lsadf.core.application.game.save.stage.command.InitializeStageCommand;
import com.lsadf.core.application.game.save.stage.command.PersistStageCommand;
import com.lsadf.core.application.game.save.stage.command.UpdateCacheStageCommand;
import com.lsadf.core.application.game.save.stage.impl.StageCommandServiceImpl;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.stage.Stage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class StageCommandServiceTests {
  private StageCommandService stageService;

  @Mock private StageRepositoryPort stageRepositoryPort;
  @Mock private CacheManager cacheManager;
  @Mock private CachePort<Stage> stageCache;
  @Mock private StageQueryService stageQueryService;

  private static final UUID UUID = java.util.UUID.randomUUID();
  private static final Stage DEFAULT_STAGE =
      Stage.builder().currentStage(1L).maxStage(1L).wave(3L).build();

  private static final Stage CACHED_STAGE =
      Stage.builder().currentStage(5L).maxStage(10L).wave(16L).build();

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    openMocks = MockitoAnnotations.openMocks(this);
    Mockito.reset(stageRepositoryPort, cacheManager, stageCache, stageQueryService);

    stageService =
        new StageCommandServiceImpl(
            cacheManager, stageRepositoryPort, stageCache, stageQueryService);
  }

  @Test
  void test_updateCacheStage_throwsIllegalArgumentException_when_allPropertiesNullAndCacheTrue() {
    // Arrange
    var command = new UpdateCacheStageCommand(UUID, null, null, null);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.updateCacheStage(command));
  }

  @Test
  void test_updateCacheStage_savesSuccessfully_when_partialStage() {
    // Arrange
    Stage stage = new Stage(8L, null, 3L);
    when(cacheManager.isEnabled()).thenReturn(true);
    when(stageCache.get(UUID.toString())).thenReturn(Optional.of(CACHED_STAGE));
    var command = UpdateCacheStageCommand.fromStage(UUID, stage);

    // Act
    stageService.updateCacheStage(command);

    // Assert
    Stage expectedMergedStage = new Stage(8L, CACHED_STAGE.maxStage(), 3L);
    verify(stageCache).set(UUID.toString(), expectedMergedStage);
  }

  @Test
  void test_updateCacheStage_savesSuccessfully_when_cacheDisabled() {
    // Arrange
    Stage stage = new Stage(8L, 12L, 16L);
    when(cacheManager.isEnabled()).thenReturn(false);
    var command = UpdateCacheStageCommand.fromStage(UUID, stage);

    // Act & Assert
    assertDoesNotThrow(() -> stageService.updateCacheStage(command));
    verify(stageCache, times(0)).set(anyString(), any(Stage.class));
  }

  @Test
  void test_updateCacheStage_savesSuccessfully_when_validStage() {
    // Arrange
    Stage stage = new Stage(8L, 12L, 16L);
    when(cacheManager.isEnabled()).thenReturn(true);
    var command = UpdateCacheStageCommand.fromStage(UUID, stage);

    // Act
    stageService.updateCacheStage(command);

    // Assert
    verify(stageCache).set(UUID.toString(), stage);
  }

  @Test
  void test_updateCacheStage_savesSuccessfully_when_cacheMissAndQueryServiceCalled() {
    // Arrange
    Stage stage = new Stage(8L, null, 16L);
    when(cacheManager.isEnabled()).thenReturn(true);
    when(stageCache.get(UUID.toString())).thenReturn(Optional.empty());
    when(stageQueryService.retrieveStage(UUID)).thenReturn(CACHED_STAGE);
    var command = UpdateCacheStageCommand.fromStage(UUID, stage);

    // Act
    stageService.updateCacheStage(command);

    // Assert
    Stage expectedMergedStage = new Stage(8L, CACHED_STAGE.maxStage(), CACHED_STAGE.wave());
    verify(stageCache).set(UUID.toString(), expectedMergedStage);
    verify(stageQueryService).retrieveStage(UUID);
  }

  @Test
  void test_initializeDefaultStage_returnsDefaultStage() {
    // Arrange
    InitializeDefaultStageCommand command = new InitializeDefaultStageCommand(UUID);
    when(stageRepositoryPort.create(UUID)).thenReturn(DEFAULT_STAGE);

    // Act
    Stage result = stageService.initializeDefaultStage(command);

    // Assert
    assertEquals(DEFAULT_STAGE, result);
    verify(stageRepositoryPort).create(UUID);
  }

  @Test
  void test_initializeStage_returnsCustomStage() {
    // Arrange
    Long currentStage = 3L;
    Long maxStage = 5L;
    Long wave = 2L;
    InitializeStageCommand command = new InitializeStageCommand(UUID, currentStage, maxStage, wave);
    Stage expectedStage = new Stage(currentStage, maxStage, wave);
    when(stageRepositoryPort.create(UUID, currentStage, maxStage, wave)).thenReturn(expectedStage);

    // Act
    Stage result = stageService.initializeStage(command);

    // Assert
    assertEquals(expectedStage, result);
    verify(stageRepositoryPort).create(UUID, currentStage, maxStage, wave);
  }

  @Test
  void test_persistStage_persistsSuccessfully() {
    // Arrange
    PersistStageCommand command = new PersistStageCommand(UUID, 7L, 10L, 12L);
    Stage expectedStage = new Stage(7L, 10L, 12L);

    // Act
    stageService.persistStage(command);

    // Assert
    verify(stageRepositoryPort).update(UUID, expectedStage);
  }
}
