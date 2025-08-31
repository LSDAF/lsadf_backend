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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.application.game.save.metadata.impl.GameMetadataServiceImpl;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class GameMetadataServiceTests {
  @Mock private UserService userService;
  @Mock private CacheManager cacheManager;
  @Mock private GameMetadataRepositoryPort gameMetadataRepositoryPort;
  @Mock private GameMetadataCachePort gameMetadataCachePort;

  private GameMetadataService gameMetadataService;

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    openMocks = MockitoAnnotations.openMocks(this);
    gameMetadataService =
        new GameMetadataServiceImpl(
            cacheManager, gameMetadataRepositoryPort, gameMetadataCachePort);
  }

  @Test
  void test_deleteById_deletesSuccessfully_when_validId() {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();

    // Act
    gameMetadataService.deleteById(gameSaveId);

    // Assert
    verify(gameMetadataRepositoryPort).deleteById(gameSaveId);
  }

  @Test
  void test_existsById_returnsTrue_when_gameMetadataExists() {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    when(gameMetadataRepositoryPort.existsById(gameSaveId)).thenReturn(true);

    // Act
    boolean result = gameMetadataService.existsById(gameSaveId);

    // Assert
    assertTrue(result);
    verify(gameMetadataRepositoryPort).existsById(gameSaveId);
  }

  @Test
  void test_existsByNickname_returnsTrue_when_nicknameExists() {
    // Arrange
    String nickname = "TestNickname";
    when(gameMetadataRepositoryPort.existsByNickname(nickname)).thenReturn(true);

    // Act
    boolean result = gameMetadataService.existsByNickname(nickname);

    // Assert
    assertTrue(result);
    verify(gameMetadataRepositoryPort).existsByNickname(nickname);
  }

  @Test
  void test_getGameMetadata_returnsMetadata_when_validId() {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    GameMetadata expectedMetadata =
        GameMetadata.builder()
            .id(gameSaveId)
            .userEmail("test@example.com")
            .nickname("TestNickname")
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

    when(gameMetadataRepositoryPort.findById(gameSaveId)).thenReturn(Optional.of(expectedMetadata));

    // Act
    GameMetadata result = gameMetadataService.getGameMetadata(gameSaveId);

    // Assert
    assertEquals(expectedMetadata, result);
    verify(gameMetadataRepositoryPort).findById(gameSaveId);
  }

  @Test
  void test_getGameMetadata_throwsException_when_metadataNotFound() {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    when(gameMetadataRepositoryPort.findById(gameSaveId)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              gameMetadataService.getGameMetadata(gameSaveId);
            });

    assertEquals("Game metadata not found for id: " + gameSaveId, exception.getMessage());
    verify(gameMetadataRepositoryPort).findById(gameSaveId);
  }

  @Test
  void test_count_returnsCount_when_called() {
    // Arrange
    Long expectedCount = 10L;
    when(gameMetadataRepositoryPort.count()).thenReturn(expectedCount);

    // Act
    Long result = gameMetadataService.count();

    // Assert
    assertEquals(expectedCount, result);
    verify(gameMetadataRepositoryPort).count();
  }

  @Test
  void test_updateNickname_updatesSuccessfully_when_validData() {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    String nickname = "UpdatedNickname";
    GameMetadata updatedMetadata =
        GameMetadata.builder()
            .id(gameSaveId)
            .userEmail("test@example.com")
            .nickname(nickname)
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

    when(gameMetadataRepositoryPort.updateNickname(gameSaveId, nickname))
        .thenReturn(updatedMetadata);

    // Act
    GameMetadata result = gameMetadataService.updateNickname(gameSaveId, nickname);

    // Assert
    assertEquals(updatedMetadata, result);
    assertEquals(nickname, result.nickname());
    verify(gameMetadataRepositoryPort).updateNickname(gameSaveId, nickname);
  }

  @Test
  void test_createNewGameMetadata_createsSuccessfully_when_allParameters() {
    // Arrange
    UUID gameSaveId = UUID.randomUUID();
    String username = "test@example.com";
    String nickname = "TestNickname";

    GameMetadata expectedMetadata =
        GameMetadata.builder()
            .id(gameSaveId)
            .userEmail(username)
            .nickname(nickname)
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

    when(gameMetadataRepositoryPort.create(gameSaveId, username, nickname))
        .thenReturn(expectedMetadata);

    // Act
    GameMetadata result = gameMetadataService.createNewGameMetadata(gameSaveId, username, nickname);

    // Assert
    assertEquals(expectedMetadata, result);
    verify(gameMetadataRepositoryPort).create(gameSaveId, username, nickname);
  }

  @Test
  void test_createNewGameMetadata_createsSuccessfully_when_usernameAndNickname() {
    // Arrange
    String username = "test@example.com";
    String nickname = "TestNickname";

    GameMetadata expectedMetadata =
        GameMetadata.builder()
            .userEmail(username)
            .nickname(nickname)
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

    when(gameMetadataRepositoryPort.create(nullable(UUID.class), eq(username), eq(nickname)))
        .thenReturn(expectedMetadata);

    // Act
    GameMetadata result = gameMetadataService.createNewGameMetadata(null, username, nickname);

    // Assert
    assertEquals(expectedMetadata, result);
    verify(gameMetadataRepositoryPort).create(nullable(UUID.class), eq(username), eq(nickname));
  }

  @Test
  void test_createNewGameMetadata_createsSuccessfully_when_usernameOnly() {
    // Arrange
    String username = "test@example.com";

    GameMetadata expectedMetadata =
        GameMetadata.builder()
            .id(UUID.randomUUID())
            .userEmail(username)
            .nickname(null)
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

    when(gameMetadataRepositoryPort.create(null, username, null)).thenReturn(expectedMetadata);

    // Act
    GameMetadata result = gameMetadataService.createNewGameMetadata(null, username, null);

    // Assert
    assertEquals(expectedMetadata, result);
    verify(gameMetadataRepositoryPort).create(nullable(UUID.class), eq(username), eq(null));
  }
}
