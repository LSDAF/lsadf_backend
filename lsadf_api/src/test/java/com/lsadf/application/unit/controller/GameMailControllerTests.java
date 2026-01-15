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
package com.lsadf.application.unit.controller;

import static com.lsadf.core.infra.web.controller.ParameterConstants.X_GAME_SESSION_ID;
import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lsadf.application.controller.game.mail.GameMailController;
import com.lsadf.application.controller.game.mail.GameMailControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailQueryService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
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
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest({GameMailController.class, GameMailControllerImpl.class, GlobalExceptionHandler.class})
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
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      CharacteristicsEventPublisherPort.class,
      GameSessionQueryService.class,
    })
class GameMailControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private GameMailQueryService gameMailQueryService;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private GameMailCommandService gameMailCommandService;

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  @BeforeEach
  void init() {
    Mockito.reset(gameMailCommandService, gameMailQueryService);
  }

  @Test
  void test_getAllGameMails_returns401_when_userNotAuthenticated() throws Exception {
    // when
    mockMvc
        .perform(
            get("/api/v1/game_mail/game_save/{gameSaveId}", "4dae1e72-bd47-4f84-96ec-48c96f7708ba"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  void test_getAllGameMails_returns400_when_notUUID() throws Exception {
    // when
    mockMvc
        .perform(
            get("/api/v1/game_mail/game_save/{gameSaveId}", "toto")
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))

        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  void test_getGameMailById_returns401_when_userNotAuthenticated() throws Exception {
    // when
    mockMvc
        .perform(get("/api/v1/game_mail/{gameMailId}", "4dae1e72-bd47-4f84-96ec-48c96f7708ba"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  void test_getGameMailById_returns400_when_notUUID() throws Exception {
    // when
    mockMvc
        .perform(
            get("/api/v1/game_mail/{gameMailId}", "toto")
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  void test_getGameMailById_returns404_when_mailNotFound() throws Exception {
    // given
    when(gameMailQueryService.getMailById(any(UUID.class))).thenThrow(new NotFoundException());
    // when
    mockMvc
        .perform(
            get("/api/v1/game_mail/{gameMailId}", "4dae1e72-bd47-4f84-96ec-48c96f7708ba")
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isNotFound());
  }

  @Test
  void test_deleteReadGameMails_returns401_when_userNotAuthenticated() throws Exception {
    // when
    mockMvc
        .perform(delete("/api/v1/game_save/{gameSaveId}", "4dae1e72-bd47-4f84-96ec-48c96f7708ba"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  void test_deleteReadGameMails_returns400_when_notUUID() throws Exception {
    // when
    mockMvc
        .perform(
            delete("/api/v1/game_mail/game_save/{gameMailId}", "toto")
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  void test_deleteReadGameMails_returns404_when_gameSaveNotFound() throws Exception {
    // given
    Mockito.doThrow(new NotFoundException())
        .when(gameMailCommandService)
        .deleteAllReadGameMailsByGameSaveId(any(UUID.class));
    // when
    mockMvc
        .perform(
            delete(
                    "/api/v1/game_mail/game_save/{gameSaveId}",
                    "4dae1e72-bd47-4f84-96ec-48c96f7708ba")
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isNotFound());
  }

  @Test
  void test_claimGameMailAttachments_returns401_when_userNotAuthenticated() throws Exception {
    // when
    mockMvc
        .perform(
            patch(
                "/api/v1/game_mail/game_save/{gameMailId}", "4dae1e72-bd47-4f84-96ec-48c96f7708ba"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  void test_claimGameMailAttachments_returns400_when_notUUID() throws Exception {
    // when
    mockMvc
        .perform(
            patch("/api/v1/game_mail/{gameMailId}", "toto")
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  void test_claimGameMailAttachments_returns404_when_mailNotFound() throws Exception {
    // given
    when(gameMailQueryService.getMailById(any())).thenThrow(NotFoundException.class);
    // when
    mockMvc
        .perform(
            patch("/api/v1/game_mail/{gameMailId}", "4dae1e72-bd47-4f84-96ec-48c96f7708ba")
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isNotFound());
  }
}
