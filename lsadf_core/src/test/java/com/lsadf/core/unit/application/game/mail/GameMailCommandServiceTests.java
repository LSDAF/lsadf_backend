/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.unit.application.game.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailQueryService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.command.DeleteGameMailsCommand;
import com.lsadf.core.application.game.mail.impl.GameMailCommandServiceImpl;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.exception.http.NotFoundException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class GameMailCommandServiceTests {
  private GameMailCommandService gameMailCommandService;
  @Mock private GameMailRepositoryPort gameMailRepositoryPort;
  @Mock private GameSaveService gameSaveService;
  @Mock private GameMailQueryService gameMailQueryService;

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    openMocks = MockitoAnnotations.openMocks(this);
    Mockito.reset(gameMailRepositoryPort, gameSaveService, gameMailQueryService);

    gameMailCommandService =
        new GameMailCommandServiceImpl(
            gameMailRepositoryPort, gameSaveService, gameMailQueryService);
  }

  @Test
  void test_readGameMailById_throwsNotFoundException_whenMailDoesNotExist() {
    // Given
    UUID mailId = UUID.randomUUID();
    when(gameMailQueryService.existsById(mailId)).thenReturn(false);

    // When & Then
    assertThrows(NotFoundException.class, () -> gameMailCommandService.readGameMailById(mailId));

    // Verify
    verify(gameMailQueryService).existsById(mailId);
    verify(gameMailRepositoryPort, never()).readGameEmail(any());
  }

  @Test
  void test_deleteAllReadGameMailsByGameSaveId_throwsNotFoundException_whenGameSaveDoesNotExist() {
    // Given
    UUID gameSaveId = UUID.randomUUID();
    when(gameSaveService.existsById(gameSaveId)).thenReturn(false);

    // When & Then
    assertThrows(
        NotFoundException.class,
        () -> gameMailCommandService.deleteAllReadGameMailsByGameSaveId(gameSaveId));

    // Verify
    verify(gameSaveService).existsById(gameSaveId);
    verify(gameMailRepositoryPort, never()).deleteReadGameEmailsByGameSaveId(any());
  }

  @Test
  void test_claimGameMailAttachments_throwsNotFoundException_whenMailDoesNotExist() {
    // Given
    UUID mailId = UUID.randomUUID();
    when(gameMailQueryService.existsById(mailId)).thenReturn(false);

    // When & Then
    assertThrows(
        NotFoundException.class, () -> gameMailCommandService.claimGameMailAttachments(mailId));

    // Verify
    verify(gameMailQueryService).existsById(mailId);
    verify(gameMailRepositoryPort, never()).claimGameMailAttachments(any());
  }

  @Test
  void test_readGameMailById_success_whenMailExists() {
    // Given
    UUID mailId = UUID.randomUUID();
    when(gameMailQueryService.existsById(mailId)).thenReturn(true);

    // When
    gameMailCommandService.readGameMailById(mailId);

    // Then
    verify(gameMailQueryService).existsById(mailId);
    verify(gameMailRepositoryPort).readGameEmail(mailId);
  }

  @Test
  void test_deleteAllReadGameMailsByGameSaveId_success_whenGameSaveExists() {
    // Given
    UUID gameSaveId = UUID.randomUUID();
    when(gameSaveService.existsById(gameSaveId)).thenReturn(true);

    // When
    gameMailCommandService.deleteAllReadGameMailsByGameSaveId(gameSaveId);

    // Then
    verify(gameSaveService).existsById(gameSaveId);
    verify(gameMailRepositoryPort).deleteReadGameEmailsByGameSaveId(gameSaveId);
  }

  @Test
  void test_claimGameMailAttachments_success_whenMailExists() {
    // Given
    UUID mailId = UUID.randomUUID();
    when(gameMailQueryService.existsById(mailId)).thenReturn(true);

    // When
    gameMailCommandService.claimGameMailAttachments(mailId);

    // Then
    verify(gameMailQueryService).existsById(mailId);
    verify(gameMailRepositoryPort).claimGameMailAttachments(mailId);
  }

  // Global service integration tests

  @Test
  void test_multipleOperations_workCorrectly_whenAllEntitiesExist() {
    // Given
    UUID mailId1 = UUID.randomUUID();
    UUID mailId2 = UUID.randomUUID();
    UUID gameSaveId = UUID.randomUUID();

    when(gameMailQueryService.existsById(mailId1)).thenReturn(true);
    when(gameMailQueryService.existsById(mailId2)).thenReturn(true);
    when(gameSaveService.existsById(gameSaveId)).thenReturn(true);

    // When - Perform multiple operations
    gameMailCommandService.readGameMailById(mailId1);
    gameMailCommandService.claimGameMailAttachments(mailId2);
    gameMailCommandService.deleteAllReadGameMailsByGameSaveId(gameSaveId);

    // Then - Verify all operations were executed correctly
    verify(gameMailQueryService).existsById(mailId1);
    verify(gameMailQueryService).existsById(mailId2);
    verify(gameSaveService).existsById(gameSaveId);

    verify(gameMailRepositoryPort).readGameEmail(mailId1);
    verify(gameMailRepositoryPort).claimGameMailAttachments(mailId2);
    verify(gameMailRepositoryPort).deleteReadGameEmailsByGameSaveId(gameSaveId);
  }

  @Test
  void test_serviceWithMixedExistenceScenarios() {
    // Given
    UUID existingMailId = UUID.randomUUID();
    UUID nonExistingMailId = UUID.randomUUID();
    UUID existingGameSaveId = UUID.randomUUID();
    UUID nonExistingGameSaveId = UUID.randomUUID();

    when(gameMailQueryService.existsById(existingMailId)).thenReturn(true);
    when(gameMailQueryService.existsById(nonExistingMailId)).thenReturn(false);
    when(gameSaveService.existsById(existingGameSaveId)).thenReturn(true);
    when(gameSaveService.existsById(nonExistingGameSaveId)).thenReturn(false);

    // When & Then - Test mixed scenarios
    // Success case
    gameMailCommandService.readGameMailById(existingMailId);
    verify(gameMailRepositoryPort).readGameEmail(existingMailId);

    // Failure case
    assertThrows(
        NotFoundException.class, () -> gameMailCommandService.readGameMailById(nonExistingMailId));

    // Success case
    gameMailCommandService.deleteAllReadGameMailsByGameSaveId(existingGameSaveId);
    verify(gameMailRepositoryPort).deleteReadGameEmailsByGameSaveId(existingGameSaveId);

    // Failure case
    assertThrows(
        NotFoundException.class,
        () -> gameMailCommandService.deleteAllReadGameMailsByGameSaveId(nonExistingGameSaveId));
  }

  @Test
  void test_serviceValidatesExistenceBeforeEveryOperation() {
    // Given
    UUID mailId = UUID.randomUUID();
    UUID gameSaveId = UUID.randomUUID();

    // Mock to return false (non-existing) for all checks
    when(gameMailQueryService.existsById(any())).thenReturn(false);
    when(gameSaveService.existsById(any())).thenReturn(false);

    // When & Then - Verify that existence is checked for every operation
    assertThrows(NotFoundException.class, () -> gameMailCommandService.readGameMailById(mailId));
    verify(gameMailQueryService).existsById(mailId);

    assertThrows(
        NotFoundException.class, () -> gameMailCommandService.claimGameMailAttachments(mailId));
    verify(gameMailQueryService, times(2)).existsById(mailId);

    assertThrows(
        NotFoundException.class,
        () -> gameMailCommandService.deleteAllReadGameMailsByGameSaveId(gameSaveId));
    verify(gameSaveService).existsById(gameSaveId);

    // Verify repository methods are never called when validation fails
    verify(gameMailRepositoryPort, never()).readGameEmail(any());
    verify(gameMailRepositoryPort, never()).claimGameMailAttachments(any());
    verify(gameMailRepositoryPort, never()).deleteReadGameEmailsByGameSaveId(any());
  }

  @Test
  void test_serviceHandlesRepositoryExceptions_gracefully() {
    // Given
    UUID mailId = UUID.randomUUID();
    UUID gameSaveId = UUID.randomUUID();

    when(gameMailQueryService.existsById(mailId)).thenReturn(true);
    when(gameSaveService.existsById(gameSaveId)).thenReturn(true);

    // Mock repository to throw exceptions
    RuntimeException repositoryException = new RuntimeException("Database error");
    doThrow(repositoryException).when(gameMailRepositoryPort).readGameEmail(mailId);
    doThrow(repositoryException).when(gameMailRepositoryPort).claimGameMailAttachments(mailId);
    doThrow(repositoryException)
        .when(gameMailRepositoryPort)
        .deleteReadGameEmailsByGameSaveId(gameSaveId);

    // When & Then - Verify exceptions are propagated correctly
    RuntimeException exception1 =
        assertThrows(RuntimeException.class, () -> gameMailCommandService.readGameMailById(mailId));
    assertEquals("Database error", exception1.getMessage());

    RuntimeException exception2 =
        assertThrows(
            RuntimeException.class, () -> gameMailCommandService.claimGameMailAttachments(mailId));
    assertEquals("Database error", exception2.getMessage());

    RuntimeException exception3 =
        assertThrows(
            RuntimeException.class,
            () -> gameMailCommandService.deleteAllReadGameMailsByGameSaveId(gameSaveId));
    assertEquals("Database error", exception3.getMessage());
  }

  @Test
  void test_serviceStateConsistency_afterMultipleOperations() {
    // Given
    UUID mailId1 = UUID.randomUUID();
    UUID mailId2 = UUID.randomUUID();
    UUID gameSaveId = UUID.randomUUID();

    when(gameMailQueryService.existsById(any())).thenReturn(true);
    when(gameSaveService.existsById(any())).thenReturn(true);

    // When - Perform operations in sequence
    gameMailCommandService.readGameMailById(mailId1);
    gameMailCommandService.readGameMailById(mailId2);
    gameMailCommandService.claimGameMailAttachments(mailId1);
    gameMailCommandService.claimGameMailAttachments(mailId2);
    gameMailCommandService.deleteAllReadGameMailsByGameSaveId(gameSaveId);

    // Then - Verify all operations were called with correct parameters
    verify(gameMailRepositoryPort).readGameEmail(mailId1);
    verify(gameMailRepositoryPort).readGameEmail(mailId2);
    verify(gameMailRepositoryPort).claimGameMailAttachments(mailId1);
    verify(gameMailRepositoryPort).claimGameMailAttachments(mailId2);
    verify(gameMailRepositoryPort).deleteReadGameEmailsByGameSaveId(gameSaveId);

    // Verify validation was called for each operation
    verify(gameMailQueryService, times(4)).existsById(any());
    verify(gameSaveService, times(1)).existsById(gameSaveId);
  }

  @Test
  void test_deleteExpiredGameMails_success_withValidTimestamp() {
    // Given
    Instant expiration = Instant.now().minusSeconds(86400); // 1 day ago

    // When
    gameMailCommandService.deleteExpiredGameMails(expiration);

    // Then
    verify(gameMailRepositoryPort).deleteExpiredGameMails(expiration);
  }

  @Test
  void test_deleteGameMail_success_withValidCommand() {
    // Given
    List<UUID> mailIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    DeleteGameMailsCommand command = new DeleteGameMailsCommand(mailIds);
    when(gameMailRepositoryPort.deleteGameEmails(mailIds)).thenReturn(2L);

    // When
    long result = gameMailCommandService.deleteGameMail(command);

    // Then
    assertEquals(2, result);
    verify(gameMailRepositoryPort).deleteGameEmails(mailIds);
  }

  @Test
  void test_deleteGameMail_returnsCorrectCount() {
    // Given
    List<UUID> mailIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    DeleteGameMailsCommand command = new DeleteGameMailsCommand(mailIds);
    when(gameMailRepositoryPort.deleteGameEmails(mailIds)).thenReturn(3L);

    // When
    long result = gameMailCommandService.deleteGameMail(command);

    // Then
    assertEquals(3, result);
    verify(gameMailRepositoryPort).deleteGameEmails(mailIds);
  }

  @Test
  void test_deleteGameMail_withEmptyList() {
    // Given
    List<UUID> mailIds = Collections.emptyList();
    DeleteGameMailsCommand command = new DeleteGameMailsCommand(mailIds);
    when(gameMailRepositoryPort.deleteGameEmails(mailIds)).thenReturn(0L);

    // When
    long result = gameMailCommandService.deleteGameMail(command);

    // Then
    assertEquals(0, result);
    verify(gameMailRepositoryPort).deleteGameEmails(mailIds);
  }

  @Test
  void test_deleteGameMail_returnsDifferentCountThanRequested() {
    // Given - Request to delete 3 mails but only 1 exists
    List<UUID> mailIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    DeleteGameMailsCommand command = new DeleteGameMailsCommand(mailIds);
    when(gameMailRepositoryPort.deleteGameEmails(mailIds)).thenReturn(1L);

    // When
    long result = gameMailCommandService.deleteGameMail(command);

    // Then
    assertEquals(1, result);
    verify(gameMailRepositoryPort).deleteGameEmails(mailIds);
  }
}
