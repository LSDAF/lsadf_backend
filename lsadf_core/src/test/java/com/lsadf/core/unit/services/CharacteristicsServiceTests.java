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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.lsadf.core.common.exceptions.http.NotFoundException;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.entities.CharacteristicsEntity;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.mappers.impl.MapperImpl;
import com.lsadf.core.repositories.CharacteristicsRepository;
import com.lsadf.core.services.CharacteristicsService;
import com.lsadf.core.services.impl.CharacteristicsServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class CharacteristicsServiceTests {
  private CharacteristicsService characteristicsService;

  @Mock private CharacteristicsRepository characteristicsRepository;

  @Mock private Cache<Characteristics> characteristicsCache;

  private final Mapper mapper = new MapperImpl();

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);

    characteristicsService =
        new CharacteristicsServiceImpl(characteristicsRepository, characteristicsCache, mapper);
  }

  @Test
  void get_characteristics_on_non_existing_gamesave_id() {
    // Arrange
    when(characteristicsRepository.findById(anyString())).thenReturn(Optional.empty());
    when(characteristicsCache.isEnabled()).thenReturn(true);

    // Assert
    assertThrows(NotFoundException.class, () -> characteristicsService.getCharacteristics("1"));
  }

  @Test
  void get_characteristics_on_existing_gamesave_id_when_cached() {
    // Arrange
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    Characteristics characteristics =
        Characteristics.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    when(characteristicsRepository.findById(anyString()))
        .thenReturn(Optional.of(characteristicsEntity));
    when(characteristicsCache.isEnabled()).thenReturn(true);
    when(characteristicsCache.get(anyString())).thenReturn(Optional.of(characteristics));

    // Act
    Characteristics result = characteristicsService.getCharacteristics("1");

    // Assert
    assertThat(result).isEqualTo(characteristics);
  }

  @Test
  void get_characteristics_on_existing_gamesave_id_when_not_cached() {
    // Arrange
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    Characteristics characteristics =
        Characteristics.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    when(characteristicsRepository.findById(anyString()))
        .thenReturn(Optional.of(characteristicsEntity));
    when(characteristicsCache.isEnabled()).thenReturn(false);
    when(characteristicsCache.get(anyString())).thenReturn(Optional.empty());

    // Act
    Characteristics result = characteristicsService.getCharacteristics("1");

    // Assert
    assertThat(result).isEqualTo(characteristics);
  }

  @Test
  void get_characteristics_on_existing_gamesave_id_when_partially_cached() {
    // Arrange
    CharacteristicsEntity characteristicsEntity =
        CharacteristicsEntity.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    Characteristics characteristicsCached = Characteristics.builder().attack(1L).build();

    Characteristics characteristics =
        Characteristics.builder()
            .attack(1L)
            .critChance(2L)
            .critDamage(3L)
            .health(4L)
            .resistance(5L)
            .build();

    when(characteristicsRepository.findById(anyString()))
        .thenReturn(Optional.of(characteristicsEntity));
    when(characteristicsCache.isEnabled()).thenReturn(true);
    when(characteristicsCache.get(anyString())).thenReturn(Optional.of(characteristicsCached));

    // Act
    Characteristics result = characteristicsService.getCharacteristics("1");

    // Assert
    assertThat(result).isEqualTo(characteristics);
  }

  @Test
  void get_characteristics_on_null_gamesave_id() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> characteristicsService.getCharacteristics(null));
  }

  @Test
  void save_characteristics_on_null_game_save_id_with_to_cache_to_true() {
    // Arrange
    Characteristics characteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(null, characteristics, true));
  }

  @Test
  void save_characteristics_on_null_game_save_id_with_to_cache_to_false() {
    // Arrange
    Characteristics characteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics(null, characteristics, false));
  }

  @Test
  void save_characteristics_on_null_characteristics_with_to_cache_to_false() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics("1", null, false));
  }

  @Test
  void save_characteristics_on_null_characteristics_with_to_cache_to_true() {
    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics("1", null, true));
  }

  @Test
  void save_characteristics_where_all_properties_are_null_with_cache_to_true() {
    // Arrange
    Characteristics characteristics = new Characteristics(null, null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics("1", characteristics, true));
  }

  @Test
  void save_characteristics_where_all_properties_are_null_with_cache_to_false() {
    // Arrange
    Characteristics characteristics = new Characteristics(null, null, null, null, null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> characteristicsService.saveCharacteristics("1", characteristics, false));
  }

  @Test
  void save_characteristics_on_existing_gamesave_with_all_valid_characteristics_value() {
    // Arrange
    Characteristics characteristics = new Characteristics(1L, 2L, 3L, 4L, 5L);

    // Act + Assert
    assertDoesNotThrow(
        () -> characteristicsService.saveCharacteristics("1", characteristics, true));
  }
}
