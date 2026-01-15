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
package com.lsadf.application.unit.controller;

import static com.lsadf.core.infra.web.controller.ParameterConstants.X_GAME_SESSION_ID;
import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lsadf.application.controller.game.save.currency.CurrencyController;
import com.lsadf.application.controller.game.save.currency.CurrencyControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.currency.*;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest({CurrencyControllerImpl.class, CurrencyController.class, GlobalExceptionHandler.class})
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@MockitoBean(
    types = {
      GameSaveService.class,
      GameSessionRepositoryPort.class,
      GameMetadataRepositoryPort.class,
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      CharacteristicsRepositoryPort.class,
      CurrencyRepositoryPort.class,
      StageRepositoryPort.class,
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      CurrencyEventPublisherPort.class,
      GameSessionQueryService.class,
    })
class CurrencyControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private CurrencyQueryService currencyQueryService;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private CurrencyCommandService currencyCommandService;

  @Autowired private ObjectMapper objectMapper;

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  @Test
  @SneakyThrows
  void test_getCurrency_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(get("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns401_when_userNotAuthenticated() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(1L, 1L, 1L, 1L);
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currencyRequest)))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_getCurrency_returns400_when_nonUuidGameSaveId() {
    // when
    mockMvc
        .perform(get("/api/v1/currency/{gameSaveId}", "testtesttest").with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_getCurrency_returns200_when_authenticatedUserAndValidUuid() {
    // when
    mockMvc
        .perform(
            get("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns400_when_noBody() {
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns400_when_bodyIsNull() {
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns400_when_gameSaveIdIsNonUuid() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(1L, 1L, 1L, 1L);
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "testtesttest")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currencyRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns400_when_oneCurrencyRequestFieldIsNegative() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(-1L, 1L, 1L, 1L);
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currencyRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns200_when_oneCurrencyRequestFieldIsNull() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(null, 1L, 1L, 1L);
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currencyRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns200_when_authenticatedUserValidBodyAndValidGameSaveId() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(1L, 1L, 1L, 1L);
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currencyRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString())
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_saveCurrency_returns400_when_noGameSessionIdHeader() {
    // given
    CurrencyRequest currencyRequest = new CurrencyRequest(1L, 1L, 1L, 1L);
    // when
    mockMvc
        .perform(
            post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currencyRequest))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }
}
