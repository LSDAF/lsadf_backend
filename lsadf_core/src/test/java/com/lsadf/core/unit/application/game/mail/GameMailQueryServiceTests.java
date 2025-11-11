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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.core.application.game.mail.GameMailQueryService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.impl.GameMailQueryServiceImpl;
import com.lsadf.core.domain.game.mail.GameMail;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.exception.http.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class GameMailQueryServiceTests {
  private GameMailQueryService gameMailQueryService;

  @Mock private GameMailRepositoryPort gameMailRepositoryPort;

  private static final UUID UUID = java.util.UUID.randomUUID();

  private AutoCloseable openMocks;

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @BeforeEach
  void init() {
    openMocks = MockitoAnnotations.openMocks(this);
    Mockito.reset(gameMailRepositoryPort);

    gameMailQueryService = new GameMailQueryServiceImpl(gameMailRepositoryPort);
  }

  @Test
  void test_getMailsByGameSaveId_throwsNotFoundException_when_nonExistingGameSaveId() {
    when(gameMailRepositoryPort.findGameMailsByGameSaveId(UUID)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> gameMailQueryService.getMailsByGameSaveId(UUID));
  }

  @Test
  void test_getMailById_throwsNotFoundException_when_nonExistingGameSaveId()
      throws JsonProcessingException {
    when(gameMailRepositoryPort.findGameMailEntityById(UUID)).thenThrow(NotFoundException.class);

    assertThrows(NotFoundException.class, () -> gameMailQueryService.getMailById(UUID));
  }

  @Test
  void test_getMailsByGameSaveId_returnsEmptyList_when_noMailsFound() {
    // Given
    UUID gameSaveId = java.util.UUID.randomUUID();
    List<GameMail> emptyList = List.of();

    when(gameMailRepositoryPort.findGameMailsByGameSaveId(gameSaveId)).thenReturn(emptyList);

    // When
    List<GameMail> result = gameMailQueryService.getMailsByGameSaveId(gameSaveId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void test_getMailsByGameSaveId_returnsMailsList_when_mailsExist() {
    // Given
    UUID gameSaveId = java.util.UUID.randomUUID();

    GameMail mail1 = createSimpleGameMail(gameSaveId, "Welcome!", "Welcome to the game!");
    GameMail mail2 = createSimpleGameMail(gameSaveId, "Daily Reward", "Here's your daily reward!");
    GameMail mail3 = createSimpleGameMail(gameSaveId, "Quest Complete", "You completed a quest!");

    List<GameMail> expectedMails = List.of(mail1, mail2, mail3);

    when(gameMailRepositoryPort.findGameMailsByGameSaveId(gameSaveId)).thenReturn(expectedMails);

    // When
    List<GameMail> result = gameMailQueryService.getMailsByGameSaveId(gameSaveId);

    // Then
    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals(expectedMails, result);

    // Verify each mail has correct game save ID
    result.forEach(mail -> assertEquals(gameSaveId, mail.getGameSaveId()));
  }

  @Test
  void test_getMailById_returnsGameMail_when_mailExists() throws JsonProcessingException {
    // Given
    UUID mailId = java.util.UUID.randomUUID();
    UUID gameSaveId = java.util.UUID.randomUUID();

    GameMail expectedMail = createSimpleGameMail(gameSaveId, "Test Mail", "This is a test mail");

    when(gameMailRepositoryPort.findGameMailEntityById(mailId))
        .thenReturn(Optional.of(expectedMail));

    // When
    GameMail result = gameMailQueryService.getMailById(mailId);

    // Then
    assertNotNull(result);
    assertEquals(expectedMail, result);
    assertEquals("Test Mail", result.getSubject());
    assertEquals("This is a test mail", result.getBody());
    assertEquals(gameSaveId, result.getGameSaveId());
  }

  @Test
  void test_getMailById_returnsGameMailWithAttachments_when_mailExistsWithAttachments()
      throws JsonProcessingException {
    // Given
    UUID mailId = java.util.UUID.randomUUID();
    UUID gameSaveId = java.util.UUID.randomUUID();

    // Create currency attachment
    Currency currency =
        Currency.builder().gold(1000L).diamond(50L).emerald(25L).amethyst(10L).build();

    GameMailAttachment<Currency> currencyAttachment =
        GameMailAttachment.<Currency>builder()
            .type(GameMailAttachmentType.CURRENCY)
            .attachment(currency)
            .build();

    GameMail expectedMail =
        createSimpleGameMail(
            gameSaveId,
            "Reward Mail",
            "Congratulations! You've received rewards for your achievements.");

    expectedMail.addAttachment(currencyAttachment);

    when(gameMailRepositoryPort.findGameMailEntityById(mailId))
        .thenReturn(Optional.of(expectedMail));

    // When
    GameMail result = gameMailQueryService.getMailById(mailId);

    // Then
    assertNotNull(result);
    assertEquals(expectedMail, result);
    assertEquals("Reward Mail", result.getSubject());
    assertEquals(
        "Congratulations! You've received rewards for your achievements.", result.getBody());
    assertEquals(gameSaveId, result.getGameSaveId());
    assertFalse(result.isRead());
    assertFalse(result.isAttachmentsClaimed());

    // Verify attachments
    assertNotNull(result.getAttachments());
    assertEquals(1, result.getAttachments().size());

    // Verify currency attachment
    GameMailAttachment<?> firstAttachment = result.getAttachments().getFirst();
    assertEquals(GameMailAttachmentType.CURRENCY, firstAttachment.type());
    Currency attachedCurrency = assertInstanceOf(Currency.class, firstAttachment.attachment());
    assertEquals(1000L, attachedCurrency.gold());
    assertEquals(50L, attachedCurrency.diamond());
    assertEquals(25L, attachedCurrency.emerald());
    assertEquals(10L, attachedCurrency.amethyst());
  }

  @Test
  void test_getMailById_returnsEmpty_when_mailNotFound() throws JsonProcessingException {
    // Given
    UUID mailId = java.util.UUID.randomUUID();

    when(gameMailRepositoryPort.findGameMailEntityById(mailId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(NotFoundException.class, () -> gameMailQueryService.getMailById(mailId));
  }

  private GameMail createSimpleGameMail(UUID gameSaveId, String subject, String body) {
    GameMail mail =
        new GameMail(
            UUID,
            gameSaveId,
            subject,
            body,
            Instant.now(),
            Instant.now().plus(10, ChronoUnit.MINUTES),
            Instant.now().plus(10, ChronoUnit.HOURS),
            false);
    mail.setRead(false);
    return mail;
  }
}
