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

package com.lsadf.core.unit.application.game.mail;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailSenderService;
import com.lsadf.core.application.game.mail.GameMailTemplateQueryService;
import com.lsadf.core.application.game.mail.command.SendEmailCommand;
import com.lsadf.core.application.game.mail.impl.GameMailSenderServiceImpl;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class GameMailSenderServiceTests {
  private GameMailSenderService gameMailSenderService;

  @Mock private GameSaveService gameSaveService;
  @Mock private GameMailTemplateQueryService gameMailTemplateQueryService;
  @Mock private GameMailRepositoryPort gameMailRepositoryPort;

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    openMocks = MockitoAnnotations.openMocks(this);
    Mockito.reset(gameSaveService, gameMailTemplateQueryService, gameMailRepositoryPort);

    gameMailSenderService =
        new GameMailSenderServiceImpl(
            gameSaveService, gameMailTemplateQueryService, gameMailRepositoryPort);
  }

  @Test
  void test_sendGameMailToGameSaveById_throwsNotFoundException_when_gameSaveDoesNotExist() {
    // Given
    UUID gameSaveId = UUID.randomUUID();
    UUID emailTemplateId = UUID.randomUUID();
    SendEmailCommand command = new SendEmailCommand(gameSaveId, emailTemplateId);

    when(gameSaveService.existsById(gameSaveId)).thenReturn(false);

    // When & Then
    assertThrows(
        NotFoundException.class, () -> gameMailSenderService.sendGameMailToGameSaveById(command));

    // Verify
    verify(gameSaveService).existsById(gameSaveId);
    verify(gameMailTemplateQueryService, never()).existsById(any());
    verify(gameMailRepositoryPort, never()).createNewGameEmail(any(), any(), any());
  }

  @Test
  void test_sendGameMailToGameSaveById_throwsNotFoundException_when_gameMailTemplateDoesNotExist() {
    // Given
    UUID gameSaveId = UUID.randomUUID();
    UUID emailTemplateId = UUID.randomUUID();
    SendEmailCommand command = new SendEmailCommand(gameSaveId, emailTemplateId);

    when(gameSaveService.existsById(gameSaveId)).thenReturn(true);
    when(gameMailTemplateQueryService.existsById(emailTemplateId)).thenReturn(false);

    // When & Then
    assertThrows(
        NotFoundException.class, () -> gameMailSenderService.sendGameMailToGameSaveById(command));

    // Verify
    verify(gameSaveService).existsById(gameSaveId);
    verify(gameMailTemplateQueryService).existsById(emailTemplateId);
    verify(gameMailRepositoryPort, never()).createNewGameEmail(any(), any(), any());
  }

  @Test
  void test_sendGameMailToGameSaveById_success_when_bothExist() {
    // Given
    UUID gameSaveId = UUID.randomUUID();
    UUID emailTemplateId = UUID.randomUUID();
    SendEmailCommand command = new SendEmailCommand(gameSaveId, emailTemplateId);

    when(gameSaveService.existsById(gameSaveId)).thenReturn(true);
    when(gameMailTemplateQueryService.existsById(emailTemplateId)).thenReturn(true);

    // When
    gameMailSenderService.sendGameMailToGameSaveById(command);

    // Then
    verify(gameSaveService).existsById(gameSaveId);
    verify(gameMailTemplateQueryService).existsById(emailTemplateId);
    verify(gameMailRepositoryPort)
        .createNewGameEmail(any(UUID.class), eq(gameSaveId), eq(emailTemplateId));
  }

  @Test
  void test_sendGameMailToAllGameSaves_success_when_noGameSaves() {
    // Given
    when(gameSaveService.getGameSaves()).thenReturn(java.util.List.of());

    UUID gameTemplateId = UUID.randomUUID();
    // When
    gameMailSenderService.sendGameMailToAllGameSaves(gameTemplateId);

    // Then
    verify(gameSaveService).getGameSaves();
    verifyNoMoreInteractions(gameSaveService, gameMailTemplateQueryService, gameMailRepositoryPort);
  }
}
