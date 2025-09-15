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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsQueryService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.impl.CharacteristicsQueryServiceImpl;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CharacteristicsQueryServiceTests {

  private CharacteristicsQueryService characteristicsService;

  @Mock private CharacteristicsRepositoryPort characteristicsRepositoryPort;
  @Mock private CacheManager cacheManager;
  @Mock private CharacteristicsCachePort characteristicsCache;

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
    Mockito.reset(characteristicsRepositoryPort, cacheManager, characteristicsCache);

    characteristicsService =
        new CharacteristicsQueryServiceImpl(
            cacheManager, characteristicsRepositoryPort, characteristicsCache);
  }

  @Test
  void test_getCharacteristics_throwsNotFoundException_when_nonExistingGameSaveId() {
    // Arrange
    when(characteristicsRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());
    when(cacheManager.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(
        NotFoundException.class, () -> characteristicsService.retrieveCharacteristics(UUID));
  }

  @Test
  void test_getCharacteristics_returnsCharacteristics_when_existingGameSaveIdAndCached() {
    // Arrange

    Characteristics cachedCharacteristics =
        Characteristics.builder()
            .attack(2L)
            .critChance(3L)
            .critDamage(4L)
            .health(5L)
            .resistance(6L)
            .build();

    Characteristics characteristics =
        Characteristics.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    when(characteristicsRepositoryPort.findById(any(UUID.class)))
        .thenReturn(Optional.of(characteristics));
    when(cacheManager.isEnabled()).thenReturn(true);
    when(characteristicsCache.get(anyString())).thenReturn(Optional.of(cachedCharacteristics));

    // Act
    Characteristics result = characteristicsService.retrieveCharacteristics(UUID);

    // Assert
    assertThat(result).isEqualTo(cachedCharacteristics);
  }

  @Test
  void test_getCharacteristics_returnsCharacteristics_when_existingGameSaveIdAndNotCached() {
    // Arrange
    Characteristics characteristics =
        Characteristics.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    when(characteristicsRepositoryPort.findById(any(UUID.class)))
        .thenReturn(Optional.of(characteristics));
    when(cacheManager.isEnabled()).thenReturn(false);
    when(characteristicsCache.get(anyString())).thenReturn(Optional.empty());

    // Act
    Characteristics result = characteristicsService.retrieveCharacteristics(UUID);

    // Assert
    assertThat(result).isEqualTo(characteristics);
  }
}
