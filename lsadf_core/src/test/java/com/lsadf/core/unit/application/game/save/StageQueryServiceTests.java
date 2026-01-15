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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageQueryService;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.impl.StageQueryServiceImpl;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StageQueryServiceTests {
  private StageQueryService stageQueryService;

  @Mock private StageRepositoryPort stageRepositoryPort;
  @Mock private StageCachePort stageCachePort;
  @Mock private CacheManager cacheManager;

  private static final UUID UUID = java.util.UUID.randomUUID();

  private AutoCloseable openMocks;

  private static final Stage STAGE = Stage.builder().currentStage(1L).maxStage(2L).build();

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    Mockito.reset(stageRepositoryPort, stageCachePort, cacheManager);

    stageQueryService =
        new StageQueryServiceImpl(cacheManager, stageRepositoryPort, stageCachePort);
  }

  @Test
  void test_getCharacteristics_throwsNotFoundException_when_nonExistingGameSaveId() {
    // Arrange
    when(stageRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());
    when(cacheManager.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(NotFoundException.class, () -> stageQueryService.retrieveStage(UUID));
  }

  @Test
  void test_getCharacteristics_returnsCharacteristics_when_existingGameSaveIdAndCached() {
    // Arrange
    when(cacheManager.isEnabled()).thenReturn(true);
    when(stageCachePort.get(UUID.toString())).thenReturn(Optional.of(STAGE));

    // Act
    Stage result = stageQueryService.retrieveStage(UUID);

    // Assert
    assertThat(result).isEqualTo(STAGE);
  }

  @Test
  void test_getCharacteristics_returnsCharacteristics_when_existingGameSaveIdAndNotCached() {
    // Arrange
    when(cacheManager.isEnabled()).thenReturn(true);
    when(stageCachePort.get(UUID.toString())).thenReturn(Optional.empty());
    when(stageRepositoryPort.findById(UUID)).thenReturn(Optional.of(STAGE));

    // Act
    Stage result = stageQueryService.retrieveStage(UUID);

    // Assert
    assertThat(result).isEqualTo(STAGE);
  }
}
