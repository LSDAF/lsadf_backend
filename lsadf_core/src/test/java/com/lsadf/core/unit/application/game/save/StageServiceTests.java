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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.game.save.stage.impl.StageServiceImpl;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class StageServiceTests {

  private StageService stageService;

  @Mock private StageRepositoryPort stageRepositoryPort;
  @Mock private CacheManager cacheManager;
  @Mock private StageCachePort stageCache;

  private static final UUID UUID = java.util.UUID.randomUUID();

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);

    stageService = new StageServiceImpl(cacheManager, stageRepositoryPort, stageCache);
  }

  @Test
  void test_getStage_throwsNotFoundException_when_gameSaveIdDoesNotExist() {
    // Arrange
    when(stageRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());
    when(cacheManager.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(NotFoundException.class, () -> stageService.getStage(UUID));
  }

  @Test
  void test_getStage_returnsCachedStage_when_gameSaveIdExistsAndCacheEnabled() {
    // Arrange
    Stage stage = Stage.builder().currentStage(1L).maxStage(2L).build();

    when(stageRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(stage));
    when(cacheManager.isEnabled()).thenReturn(true);
    when(stageCache.get(anyString())).thenReturn(Optional.of(stage));

    // Act
    Stage result = stageService.getStage(UUID);

    // Assert
    assertThat(result).isEqualTo(stage);
  }

  @Test
  void test_getStage_returnsStageFromRepository_when_gameSaveIdExistsAndCacheDisabled() {
    // Arrange
    Stage stage = Stage.builder().currentStage(1L).maxStage(2L).build();

    when(stageRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(stage));
    when(cacheManager.isEnabled()).thenReturn(false);
    when(stageCache.get(anyString())).thenReturn(Optional.empty());

    // Act
    Stage result = stageService.getStage(UUID);

    // Assert
    assertThat(result).isEqualTo(stage);
  }

  @Test
  void test_getStage_returnsCompleteStageFromRepository_when_cachedStageIsPartial() {
    // Arrange
    Stage stageCached = Stage.builder().maxStage(200L).build();

    Stage stage = Stage.builder().currentStage(1L).maxStage(200L).build();

    when(stageRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.of(stage));
    when(cacheManager.isEnabled()).thenReturn(true);
    when(stageCache.get(anyString())).thenReturn(Optional.of(stageCached));

    // Act
    Stage result = stageService.getStage(UUID);

    // Assert
    assertThat(result).isEqualTo(stage);
  }

  @Test
  void test_getStage_throwsIllegalArgumentException_when_gameSaveIdIsNull() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.getStage(null));
  }

  @Test
  void test_saveStage_throwsIllegalArgumentException_when_gameSaveIdIsNullAndCacheEnabled() {
    // Arrange
    Stage stage = new Stage(1L, 2L);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(null, stage, true));
  }

  @Test
  void test_saveStage_throwsIllegalArgumentException_when_gameSaveIdIsNullAndCacheDisabled() {
    // Arrange
    Stage stage = new Stage(1L, 2L);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(null, stage, false));
  }

  @Test
  void test_saveStage_throwsIllegalArgumentException_when_stageIsNullAndCacheDisabled() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(UUID, null, false));
  }

  @Test
  void test_saveStage_throwsIllegalArgumentException_when_stageIsNullAndCacheEnabled() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(UUID, null, true));
  }

  @Test
  void test_saveStage_throwsIllegalArgumentException_when_stagePropertiesAreNullAndCacheEnabled() {
    // Arrange
    Stage stage = new Stage(null, null);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(UUID, stage, true));
  }

  @Test
  void test_saveStage_throwsIllegalArgumentException_when_stagePropertiesAreNullAndCacheDisabled() {
    // Arrange
    Stage stage = new Stage(null, null);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(UUID, stage, false));
  }

  @Test
  void test_saveStage_succeeds_when_gameSaveExistsAndStageValuesAreValid() {
    // Arrange
    Stage stage = new Stage(1L, 2L);

    // Act + Assert
    assertDoesNotThrow(() -> stageService.saveStage(UUID, stage, true));
  }
}
