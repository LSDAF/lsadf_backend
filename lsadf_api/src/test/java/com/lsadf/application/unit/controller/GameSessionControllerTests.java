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

import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.application.controller.game.session.GameSessionController;
import com.lsadf.application.controller.game.session.GameSessionControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCachePort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsRepositoryPort;
import com.lsadf.core.application.game.save.currency.CurrencyCachePort;
import com.lsadf.core.application.game.save.currency.CurrencyRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.application.game.session.GameSessionCommandService;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
  GameSessionController.class,
  GameSessionControllerImpl.class,
  GlobalExceptionHandler.class
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
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class
    })
class GameSessionControllerTests {

  private static final String GAME_SAVE_ID = "game_save_id";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private GameSessionQueryService gameSessionQueryService;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private GameSessionCommandService gameSessionCommandService;

  private AutoCloseable openMocksCloseable;

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  private static final GameSession GAME_SESSION =
      new GameSession(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "test@test.com",
          Instant.now().plus(10, ChronoUnit.HOURS),
          false,
          Instant.now(),
          1);

  @BeforeEach
  void setUp() {
    openMocksCloseable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    Mockito.reset(gameSessionQueryService);
    openMocksCloseable.close();
  }

  @Test
  @SneakyThrows
  void test_openNewGameSession_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            post("/api/v1/game_session")
                .queryParam(GAME_SAVE_ID, "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_openNewGameSession_returns400_when_nonUuidGameSaveId() {
    // when
    mockMvc
        .perform(
            post("/api/v1/game_session")
                .queryParam(GAME_SAVE_ID, "totototototototo")
                .contentType(APPLICATION_JSON)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_openNewGameSession_returns200_when_validUuidAndAuthenticatedUser() {
    // when

    mockMvc
        .perform(
            post("/api/v1/game_session")
                .queryParam(GAME_SAVE_ID, "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_refreshGameSession_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            patch("/api/v1/game_session/{gameSessionId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_refreshGameSession_returns400_when_nonUuidGameSessionId() {
    // when
    mockMvc
        .perform(
            patch("/api/v1/game_session/{gameSessionId}", "totototototototo").with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_refreshGameSession_returns200_when_validUuidAndAuthenticatedUser() {
    // when
    Mockito.when(gameSessionQueryService.findGameSessionById(any())).thenReturn(GAME_SESSION);

    mockMvc
        .perform(
            patch("/api/v1/game_session/{gameSessionId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isOk());
  }
}
