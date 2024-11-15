package com.lsadf.core.unit.services;

import com.lsadf.core.cache.Cache;
import com.lsadf.core.entities.StageEntity;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.mappers.impl.MapperImpl;
import com.lsadf.core.models.Stage;
import com.lsadf.core.repositories.StageRepository;
import com.lsadf.core.services.StageService;
import com.lsadf.core.services.impl.StageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.MethodName.class)
class StageServiceTests {

    private StageService stageService;

    @Mock
    private StageRepository stageRepository;

    @Mock
    private Cache<Stage> stageCache;

    @BeforeEach
    public void init() {
        // Create all mocks and inject them into the service
        MockitoAnnotations.openMocks(this);

        Mapper mapper = new MapperImpl();

        stageService = new StageServiceImpl(stageRepository, stageCache, mapper);
    }

    @Test
    void get_stage_on_non_existing_gamesave_id() {
        // Arrange
        when(stageRepository.findById(anyString())).thenReturn(Optional.empty());
        when(stageCache.isEnabled()).thenReturn(true);

        // Assert
        assertThrows(NotFoundException.class, () -> stageService.getStage("1"));
    }

    @Test
    void get_stage_on_existing_gamesave_id_when_cached() {
        // Arrange
        StageEntity stageEntity = StageEntity.builder()
                .userEmail("test@test.com")
                .currentStage(1L)
                .maxStage(2L)
                .build();

        Stage stage = Stage.builder()
                .currentStage(1L)
                .maxStage(2L)
                .build();

        when(stageRepository.findById(anyString())).thenReturn(Optional.of(stageEntity));
        when(stageCache.isEnabled()).thenReturn(true);
        when(stageCache.get(anyString())).thenReturn(Optional.of(stage));

        // Act
        Stage result = stageService.getStage("1");

        // Assert
        assertThat(result).isEqualTo(stage);
    }

    @Test
    void get_stage_on_existing_gamesave_id_when_not_cached() {
        // Arrange
        StageEntity stageEntity = StageEntity.builder()
                .userEmail("test@test.com")
                .currentStage(1L)
                .maxStage(2L)
                .build();

        Stage stage = Stage.builder()
                .currentStage(1L)
                .maxStage(2L)
                .build();

        when(stageRepository.findById(anyString())).thenReturn(Optional.of(stageEntity));
        when(stageCache.isEnabled()).thenReturn(false);
        when(stageCache.get(anyString())).thenReturn(Optional.empty());

        // Act
        Stage result = stageService.getStage("1");

        // Assert
        assertThat(result).isEqualTo(stage);
    }

    @Test
    void get_stage_on_existing_gamesave_id_when_partially_cached() {
        // Arrange
        StageEntity stageEntity = StageEntity.builder()
                .userEmail("test@test.com")
                .currentStage(1L)
                .maxStage(2L)
                .build();

        Stage stageCached = Stage.builder()
                .maxStage(200L)
                .build();

        Stage stage = Stage.builder()
                .currentStage(1L)
                .maxStage(200L)
                .build();

        when(stageRepository.findById(anyString())).thenReturn(Optional.of(stageEntity));
        when(stageCache.isEnabled()).thenReturn(true);
        when(stageCache.get(anyString())).thenReturn(Optional.of(stageCached));

        // Act
        Stage result = stageService.getStage("1");

        // Assert
        assertThat(result).isEqualTo(stage);
    }

    @Test
    void get_stage_on_null_gamesave_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stageService.getStage(null));
    }

    @Test
    void save_stage_on_null_game_save_id_with_to_cache_to_true() {
        // Arrange
        Stage stage = new Stage(1L, 2L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(null, stage, true));
    }

    @Test
    void save_stage_on_null_game_save_id_with_to_cache_to_false() {
        // Arrange
        Stage stage = new Stage(1L, 2L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stageService.saveStage(null, stage, false));
    }

    @Test
    void save_stage_on_null_stage_with_to_cache_to_false() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stageService.saveStage("1", null, false));
    }

    @Test
    void save_stage_on_null_stage_with_to_cache_to_true() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stageService.saveStage("1", null, true));
    }

    @Test
    void save_stage_where_all_properties_are_null_with_cache_to_true() {
        // Arrange
        Stage stage = new Stage(null, null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stageService.saveStage("1", stage, true));
    }

    @Test
    void save_stage_where_all_properties_are_null_with_cache_to_false() {
        // Arrange
        Stage stage = new Stage(null, null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stageService.saveStage("1", stage, false));
    }

    @Test
    void save_stage_on_existing_gamesave_with_all_valid_currencies_value() {
        // Arrange
        Stage stage = new Stage(1L, 2L);

        // Act + Assert
        assertDoesNotThrow(() -> stageService.saveStage("1", stage, true));
    }
}
