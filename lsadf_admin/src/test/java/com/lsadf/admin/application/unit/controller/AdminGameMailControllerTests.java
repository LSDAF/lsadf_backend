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
package com.lsadf.admin.application.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.admin.application.game.mail.AdminGameMailController;
import com.lsadf.admin.application.game.mail.AdminGameMailControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailSenderService;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.mail.command.SendEmailCommand;
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
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.mail.SendGameMailRequest;
import com.lsadf.core.unit.config.WithMockJwtUser;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(
    value = {
      GlobalExceptionHandler.class,
      AdminGameMailController.class,
      AdminGameMailControllerImpl.class
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
      GameMailSenderService.class
    })
class AdminGameMailControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private GameMailSenderService gameMailSenderService;

  private static final UUID GAME_SAVE_ID = UUID.randomUUID();
  private static final UUID GAME_MAIL_TEMPLATE_ID = UUID.randomUUID();

  @Test
  @SneakyThrows
  void test_sendGameMailToAllGameSaves_returns_401_when_user_not_authenticated() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder().gameMailTemplateId(GAME_MAIL_TEMPLATE_ID).build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_sendGameMailToAllGameSaves_returns_403_when_user_not_admin() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      roles = {"ADMIN"},
      username = "paul.ochon@test.com",
      name = "Paul OCHON")
  void test_sendGameMailToAllGameSaves_returns_200_when_successful() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    doNothing().when(gameMailSenderService).sendGameMailToAllGameSaves();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(gameMailSenderService).sendGameMailToAllGameSaves();
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      roles = {"ADMIN"},
      username = "paul.ochon@test.com",
      name = "Paul OCHON")
  void test_sendGameMailToAllGameSaves_returns_404_when_template_not_found() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    doThrow(new NotFoundException("Template not found"))
        .when(gameMailSenderService)
        .sendGameMailToAllGameSaves();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(gameMailSenderService).sendGameMailToAllGameSaves();
  }

  @Test
  @SneakyThrows
  void test_sendGameMailToGameSaveById_returns_401_when_user_not_authenticated() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_sendGameMailToGameSaveById_returns_401_when_user_not_admin() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      roles = {"ADMIN"},
      username = "paul.ochon@test.com",
      name = "Paul OCHON")
  void test_sendGameMailToGameSaveById_returns_200_when_successful() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    doNothing().when(gameMailSenderService).sendGameMailToGameSaveById(any(SendEmailCommand.class));

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(gameMailSenderService).sendGameMailToGameSaveById(any(SendEmailCommand.class));
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      roles = {"ADMIN"},
      username = "paul.ochon@test.com",
      name = "Paul OCHON")
  void test_sendGameMailToGameSaveById_returns_404_when_game_save_not_found() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    doThrow(new NotFoundException("Game save not found"))
        .when(gameMailSenderService)
        .sendGameMailToGameSaveById(any(SendEmailCommand.class));

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(gameMailSenderService).sendGameMailToGameSaveById(any(SendEmailCommand.class));
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      roles = {"ADMIN"},
      username = "paul.ochon@test.com",
      name = "Paul OCHON")
  void test_sendGameMailToGameSaveById_returns_404_when_template_not_found() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    doThrow(new NotFoundException("Template not found"))
        .when(gameMailSenderService)
        .sendGameMailToGameSaveById(any(SendEmailCommand.class));

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send/" + GAME_SAVE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(gameMailSenderService).sendGameMailToGameSaveById(any(SendEmailCommand.class));
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource("provideInvalidRequests")
  @WithMockJwtUser(
      roles = {"ADMIN"},
      username = "paul.ochon@test.com",
      name = "Paul OCHON")
  void test_sendGameMailToAllGameSaves_returns_400_when_request_is_invalid(
      SendGameMailRequest request) {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Provides invalid SendGameMailRequest instances for parameterized tests.
   *
   * @return a stream of Arguments containing invalid SendGameMailRequest objects
   */
  private static java.util.stream.Stream<Arguments> provideInvalidRequests() {
    return java.util.stream.Stream.of(
        Arguments.of(new SendGameMailRequest(null, GAME_MAIL_TEMPLATE_ID)),
        Arguments.of(new SendGameMailRequest(GAME_SAVE_ID, null)));
  }
}
