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

import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.save.characteristics.impl.CharacteristicsServiceImpl;
import com.lsadf.core.application.shared.CachePort;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.valkey.cache.service.CacheService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class CharacteristicsServiceTests {
  private CharacteristicsService characteristicsService;

  @Mock private CharacteristicsRepositoryPort characteristicsRepositoryPort;
  @Mock private CacheService cacheService;
  @Mock private CachePort<Characteristics> characteristicsCache;

  private static final UUID UUID = java.util.UUID.randomUUID();

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);

    characteristicsService =
        new CharacteristicsServiceImpl(
            cacheService, characteristicsRepositoryPort, characteristicsCache);
  }

  @Test
  void test_getCharacteristics_throwsNotFoundException_when_nonExistingGameSaveId() {
    // Arrange
    when(characteristicsRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());
    when(cacheService.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(NotFoundException.class, () -> characteristicsService.getCharacteristics(UUID));
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
    when(cacheService.isEnabled()).thenReturn(true);
    when(characteristicsCache.get(anyString())).thenReturn(Optional.of(cachedCharacteristics));

    // Act
    Characteristics result = characteristicsService.getCharacteristics(UUID);

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
    when(cacheService.isEnabled()).thenReturn(false);
    when(characteristicsCache.get(anyString())).thenReturn(Optional.empty());

    // Act
    Characteristics result = characteristicsService.getCharacteristics(UUID);

    // Assert
    assertThat(result).isEqualTo(characteristics);
  }

  @Test
  void test_getCharacteristics_throwsIllegalArgumentException_when_nullGameSaveId() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> characteristicsService.getCharacteristics(null));
  }

  @Test
  void test_saveCharacteristics_throwsIllegalArgumentException_when_nullGameSaveIdAndToCacheTrue() {
    // Arrange
    Characteristics characteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(null, characteristics, true));
  }

  @Test
  void
      test_saveCharacteristics_throwsIllegalArgumentException_when_nullGameSaveIdAndToCacheFalse() {
    // Arrange
    Characteristics characteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(null, characteristics, false));
  }

  @Test
  void
      test_saveCharacteristics_throwsIllegalArgumentException_when_nullCharacteristicsAndToCacheFalse() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(UUID, null, false));
  }

  @Test
  void
      test_saveCharacteristics_throwsIllegalArgumentException_when_nullCharacteristicsAndToCacheTrue() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(UUID, null, true));
  }

  @Test
  void
      test_saveCharacteristics_throwsIllegalArgumentException_when_allPropertiesNullAndCacheTrue() {
    // Arrange
    Characteristics characteristics = new Characteristics(null, null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(UUID, characteristics, true));
  }

  @Test
  void
      test_saveCharacteristics_throwsIllegalArgumentException_when_allPropertiesNullAndCacheFalse() {
    // Arrange
    Characteristics characteristics = new Characteristics(null, null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(UUID, characteristics, false));
  }

  @Test
  void test_saveCharacteristics_savesSuccessfully_when_existingGameSaveAndValidCharacteristics() {
    // Arrange
    Characteristics characteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);

    // Act + Assert
    assertDoesNotThrow(
        () -> characteristicsService.saveCharacteristics(UUID, characteristics, true));
  }
}
