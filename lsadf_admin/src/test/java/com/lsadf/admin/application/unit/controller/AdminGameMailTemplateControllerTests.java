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
package com.lsadf.admin.application.unit.controller;

import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lsadf.admin.application.game.mail.AdminGameMailTemplateController;
import com.lsadf.admin.application.game.mail.AdminGameMailTemplateControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailTemplateCommandService;
import com.lsadf.core.application.game.mail.GameMailTemplateQueryService;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailAttachmentRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailTemplateRequest;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(
    value = {
      GlobalExceptionHandler.class,
      AdminGameMailTemplateController.class,
      AdminGameMailTemplateControllerImpl.class
    })
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@MockitoBean(
    types = {
      GameSaveService.class,
      GameSessionRepositoryPort.class,
      GameMetadataRepositoryPort.class,
      CharacteristicsRepositoryPort.class,
      CurrencyRepositoryPort.class,
      StageRepositoryPort.class,
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      CharacteristicsEventPublisherPort.class,
      GameSessionQueryService.class,
    },
    answers = Answers.RETURNS_DEEP_STUBS)
class AdminGameMailTemplateControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private GameMailTemplateQueryService gameMailTemplateQueryService;

  @MockitoBean private GameMailTemplateCommandService gameMailTemplateCommandService;

  private static final UUID UUID = java.util.UUID.randomUUID();

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_ADMIN =
      createMockJwt("paul.ochon@test.com", List.of("ADMIN", "USER"), "Paul OCHON");

  @BeforeEach
  void setup() {
    Mockito.reset(gameMailTemplateQueryService, gameMailTemplateCommandService);
  }

  @Test
  @SneakyThrows
  void test_getAllTemplates_whenNotAdmin() {
    // when
    mockMvc
        .perform(get("/api/v1/admin/game_mail_template").with(MOCK_JWT_USER))
        // then
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_getAllTemplates_whenErrorOccurs() {
    // given
    Mockito.when(gameMailTemplateQueryService.getMailTemplates())
        .thenThrow(new RuntimeException("Error"));
    // when
    mockMvc
        .perform(get("/api/v1/admin/game_mail_template").with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isInternalServerError());
  }

  @Test
  @SneakyThrows
  void test_getTemplateById_whenNotAdmin() {
    // when
    mockMvc
        .perform(get("/api/v1/admin/game_mail_template/{id}", UUID.toString()).with(MOCK_JWT_USER))
        // then
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_getTemplateById_whenNotExistingId() {
    // given
    Mockito.when(gameMailTemplateQueryService.getMailTemplateById(UUID))
        .thenThrow(new NotFoundException("Error"));
    // when
    mockMvc
        .perform(get("/api/v1/admin/game_mail_template/{id}", UUID.toString()).with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void test_getTemplateById_whenInvalidIdFormat() {
    // when
    mockMvc
        .perform(get("/api/v1/admin/game_mail_template/{id}", "toto").with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_getTemplateById_whenErrorOccurs() {
    // given
    Mockito.when(gameMailTemplateQueryService.getMailTemplateById(UUID))
        .thenThrow(new RuntimeException("Error"));
    // when
    mockMvc
        .perform(get("/api/v1/admin/game_mail_template/{id}", UUID.toString()).with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isInternalServerError());
  }

  @Test
  @SneakyThrows
  void test_deleteMailTemplateById_whenNotAdmin() {
    // when
    mockMvc
        .perform(
            delete("/api/v1/admin/game_mail_template/{id}", UUID.toString()).with(MOCK_JWT_USER))
        // then
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_deleteMailTemplateById_whenNotExistingId() {
    // given
    doThrow(new NotFoundException("Error"))
        .when(gameMailTemplateCommandService)
        .deleteGameMailTemplateById(UUID);
    // when
    mockMvc
        .perform(
            delete("/api/v1/admin/game_mail_template/{id}", UUID.toString()).with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void test_deleteMailTemplateById_whenInvalidIdFormat() {
    // when
    mockMvc
        .perform(delete("/api/v1/admin/game_mail_template/{id}", "toto").with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_deleteMailTemplateById_whenErrorOccurs() {
    // given
    doThrow(new RuntimeException("Error"))
        .when(gameMailTemplateCommandService)
        .deleteGameMailTemplateById(UUID);
    // when
    mockMvc
        .perform(
            delete("/api/v1/admin/game_mail_template/{id}", UUID.toString()).with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isInternalServerError());
  }

  @Test
  @SneakyThrows
  void test_createNewMailTemplate_whenNotAdmin() {
    // given
    GameMailTemplateRequest request = new GameMailTemplateRequest("name", "subject", "body", 30);
    // when
    mockMvc
        .perform(
            post("/api/v1/admin/game_mail_template")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_createNewMailTemplate_whenErrorOccurs() {
    // given
    GameMailTemplateRequest request = new GameMailTemplateRequest("name", "subject", "body", 30);
    Mockito.when(gameMailTemplateCommandService.initializeGameMailTemplate(any()))
        .thenThrow(new RuntimeException("Error"));
    // when
    mockMvc
        .perform(
            post("/api/v1/admin/game_mail_template")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isInternalServerError());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidRequests")
  @SneakyThrows
  void test_createNewMailTemplate_whenInvalidRequest(GameMailTemplateRequest request) {
    // when
    mockMvc
        .perform(
            post("/api/v1/admin/game_mail_template")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isBadRequest());
  }

  /**
   * Provides invalid GameMailTemplateRequest instances for parameterized tests.
   *
   * @return a stream of Arguments containing invalid GameMailTemplateRequest objects
   */
  private static java.util.stream.Stream<Arguments> provideInvalidRequests() {
    return java.util.stream.Stream.of(
        Arguments.of(new GameMailTemplateRequest(null, "subject", "body", null)),
        Arguments.of(new GameMailTemplateRequest("name", "", "body", null)),
        Arguments.of(new GameMailTemplateRequest("name", "", "body", 10)),
        Arguments.of(new GameMailTemplateRequest("name", "", "body", 20)));
  }

  @Test
  @SneakyThrows
  void test_createNewTemplateAttachmentToTemplate_whenNotAdmin() {
    GameMailAttachmentRequest<Currency> attachmentRequest =
        new GameMailAttachmentRequest<>(
            GameMailAttachmentType.CURRENCY, new Currency(10L, 0L, 0L, 0L));

    List<GameMailAttachmentRequest<?>> attachments = List.of(attachmentRequest);

    // when
    mockMvc
        .perform(
            put("/api/v1/admin/game_mail_template/{id}", UUID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attachments))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_createNewTemplateAttachmentToTemplate_whenNotExistingTemplateId() {
    GameMailAttachmentRequest<Currency> attachmentRequest =
        new GameMailAttachmentRequest<>(
            GameMailAttachmentType.CURRENCY, new Currency(10L, 0L, 0L, 0L));

    List<GameMailAttachmentRequest<?>> attachments = List.of(attachmentRequest);
    doThrow(new NotFoundException("Not found"))
        .when(gameMailTemplateCommandService)
        .attachToGameMailTemplate(any());
    // when
    var body = objectMapper.writeValueAsString(attachments);
    mockMvc
        .perform(
            put("/api/v1/admin/game_mail_template/{id}", UUID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void test_createNewTemplateAttachmentToTemplate_whenInvalidTemplateIdFormat() {
    GameMailAttachmentRequest<Currency> attachmentRequest =
        new GameMailAttachmentRequest<>(
            GameMailAttachmentType.CURRENCY, new Currency(10L, 0L, 0L, 0L));

    List<GameMailAttachmentRequest<?>> attachments = List.of(attachmentRequest);
    // when
    var body = objectMapper.writeValueAsString(attachments);
    mockMvc
        .perform(
            put("/api/v1/admin/game_mail_template/{id}", "toto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_createNewTemplateAttachmentToTemplate_whenErrorOccurs() {
    GameMailAttachmentRequest<Currency> attachmentRequest =
        new GameMailAttachmentRequest<>(
            GameMailAttachmentType.CURRENCY, new Currency(10L, 0L, 0L, 0L));

    List<GameMailAttachmentRequest<?>> attachments = List.of(attachmentRequest);

    Mockito.doThrow(new RuntimeException("Error"))
        .when(gameMailTemplateCommandService)
        .attachToGameMailTemplate(any());

    // when
    var body = objectMapper.writeValueAsString(attachments);
    mockMvc
        .perform(
            put("/api/v1/admin/game_mail_template/{id}", UUID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(status().isInternalServerError());
  }
}
