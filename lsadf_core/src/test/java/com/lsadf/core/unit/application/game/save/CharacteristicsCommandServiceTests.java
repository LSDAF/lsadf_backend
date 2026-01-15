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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsQueryService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.command.UpdateCacheCharacteristicsCommand;
import com.lsadf.core.application.game.save.characteristics.impl.CharacteristicsCommandServiceImpl;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class CharacteristicsCommandServiceTests {
  private CharacteristicsCommandService characteristicsService;

  @Mock private CharacteristicsRepositoryPort characteristicsRepositoryPort;
  @Mock private CacheManager cacheManager;
  @Mock private CharacteristicsCachePort characteristicsCache;
  @Mock private CharacteristicsQueryService characteristicsQueryService;

  private static final UUID UUID = java.util.UUID.randomUUID();

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    openMocks = MockitoAnnotations.openMocks(this);
    Mockito.reset(
        characteristicsRepositoryPort,
        cacheManager,
        characteristicsCache,
        characteristicsQueryService);

    characteristicsService =
        new CharacteristicsCommandServiceImpl(
            cacheManager,
            characteristicsRepositoryPort,
            characteristicsCache,
            characteristicsQueryService);
  }

  @Test
  void
      test_saveCharacteristics_throwsIllegalArgumentException_when_allPropertiesNullAndCacheTrue() {
    // Arrange
    var command = new UpdateCacheCharacteristicsCommand(UUID, null, null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.updateCacheCharacteristics(command));
  }

  @Test
  void test_saveCharacteristics_savesSuccessfully_when_partialCharacteristics() {
    // Arrange
    Characteristics characteristics = new Characteristics(10L, 25L, null, null, null);
    Characteristics cachedCharacteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);
    when(characteristicsCache.get(UUID.toString())).thenReturn(Optional.of(cachedCharacteristics));
    var command = UpdateCacheCharacteristicsCommand.fromCharacteristics(UUID, characteristics);
    // Act & Assert
    assertDoesNotThrow(() -> characteristicsService.updateCacheCharacteristics(command));
  }

  @Test
  void test_saveCharacteristics_savesSuccessfully_when_existingGameSaveAndValidCharacteristics() {
    // Arrange
    Characteristics characteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);

    // Act + Assert
    var command = UpdateCacheCharacteristicsCommand.fromCharacteristics(UUID, characteristics);
    assertDoesNotThrow(() -> characteristicsService.updateCacheCharacteristics(command));
  }
}
