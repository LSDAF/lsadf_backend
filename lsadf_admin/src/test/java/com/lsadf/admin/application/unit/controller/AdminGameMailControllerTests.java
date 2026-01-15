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

import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.lsadf.admin.application.game.mail.AdminGameMailController;
import com.lsadf.admin.application.game.mail.AdminGameMailControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailSenderService;
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
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.mail.DeleteGameMailsRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.SendGameMailRequest;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

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
      GameMailSenderService.class,
      GameMailCommandService.class
    })
class AdminGameMailControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private GameMailSenderService gameMailSenderService;

  @Autowired private GameMailCommandService gameMailCommandService;

  private static final UUID GAME_SAVE_ID = UUID.randomUUID();
  private static final UUID GAME_MAIL_TEMPLATE_ID = UUID.randomUUID();

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_ADMIN =
      createMockJwt("paul.ochon@test.com", List.of("USER", "ADMIN"), "Paul OCHON");

  @Test
  @SneakyThrows
  void test_sendGameMailToGameSaves_returns_401_when_user_not_authenticated() {
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
  void test_sendGameMailToGameSaves_returns_403_when_user_not_admin() {
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
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_sendGameMailToGameSaves_returns_200_when_successful() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    doNothing().when(gameMailSenderService).sendGameMailToGameSaveById(any());

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(gameMailSenderService).sendGameMailToGameSaveById(any());
  }

  @Test
  @SneakyThrows
  void test_sendGameMailToGameSaves_returns_200_when_successful_with_no_specified_gameSaveId() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder().gameMailTemplateId(GAME_MAIL_TEMPLATE_ID).build();

    doNothing().when(gameMailSenderService).sendGameMailToAllGameSaves(any());

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(gameMailSenderService).sendGameMailToAllGameSaves(any());
  }

  @Test
  @SneakyThrows
  void test_sendGameMailToGameSaves_returns_404_when_template_not_found() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder()
            .gameSaveId(GAME_SAVE_ID)
            .gameMailTemplateId(GAME_MAIL_TEMPLATE_ID)
            .build();

    doThrow(new NotFoundException("Template not found"))
        .when(gameMailSenderService)
        .sendGameMailToGameSaveById(any());

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(gameMailSenderService).sendGameMailToGameSaveById(any());
  }

  @Test
  @SneakyThrows
  void
      test_sendGameMailToGameSaves_returns_404_when_template_not_found_with_no_specified_gameSaveId() {
    // given
    SendGameMailRequest request =
        SendGameMailRequest.builder().gameMailTemplateId(GAME_MAIL_TEMPLATE_ID).build();

    doThrow(new NotFoundException("Template not found"))
        .when(gameMailSenderService)
        .sendGameMailToAllGameSaves(any());

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    verify(gameMailSenderService).sendGameMailToAllGameSaves(any());
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource("provideInvalidRequests")
  void test_sendGameMailToGameSaves_returns_400_when_request_is_invalid(
      SendGameMailRequest request) {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Provides invalid SendGameMailRequest instances for parameterized tests.
   *
   * @return a stream of Arguments containing invalid SendGameMailRequest objects
   */
  private static java.util.stream.Stream<Arguments> provideInvalidRequests() {
    return java.util.stream.Stream.of(Arguments.of(new SendGameMailRequest(null, null)));
  }

  // DELETE endpoint tests

  @Test
  @SneakyThrows
  void test_deleteGameMails_returns_401_when_user_not_authenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/game_mail")
                .param("expired", "2025-12-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_deleteGameMails_returns_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/game_mail")
                .param("expired", "2025-12-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_deleteGameMails_returns_200_when_successful_with_expired_timestamp() {
    // given
    when(gameMailCommandService.deleteExpiredGameMails(any())).thenReturn(0L);

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/delete")
                .param("expired", "2025-12-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(gameMailCommandService).deleteExpiredGameMails(any());
  }

  @Test
  @SneakyThrows
  void test_deleteGameMails_returns_200_when_successful_with_mail_ids() {
    // given
    List<UUID> mailIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    DeleteGameMailsRequest request = DeleteGameMailsRequest.builder().mailIds(mailIds).build();
    when(gameMailCommandService.deleteExpiredGameMails(any())).thenReturn(4L);

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(gameMailCommandService).deleteGameMail(any());
  }

  @Test
  @SneakyThrows
  void test_deleteGameMails_returns_400_when_no_parameters_provided() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/delete")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_deleteGameMails_returns_400_with_invalid_timestamp() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_mail/delete")
                .param("expired", "invalid-timestamp")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
