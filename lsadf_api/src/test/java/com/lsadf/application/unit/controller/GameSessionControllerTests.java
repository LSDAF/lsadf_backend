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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.application.controller.game.session.GameSessionController;
import com.lsadf.application.controller.game.session.GameSessionControllerImpl;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest({
  GameSessionController.class,
  GameSessionControllerImpl.class,
  GlobalExceptionHandler.class
})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class GameSessionControllerTests {

  private static final String GAME_SAVE_ID = "game_save_id";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private GameSessionQueryService gameSessionQueryService;

  private AutoCloseable openMocksCloseable;

  private static final GameSession GAME_SESSION =
      new GameSession(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "test@test.com",
          Instant.now().plus(10, ChronoUnit.HOURS),
          false,
          Instant.now());

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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_openNewGameSession_returns400_when_nonUuidGameSaveId() {
    // when
    mockMvc
        .perform(
            post("/api/v1/game_session")
                .queryParam(GAME_SAVE_ID, "totototototototo")
                .contentType(APPLICATION_JSON))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_openNewGameSession_returns200_when_validUuidAndAuthenticatedUser() {
    // when

    mockMvc
        .perform(
            post("/api/v1/game_session")
                .queryParam(GAME_SAVE_ID, "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON))
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
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_refreshGameSession_returns400_when_nonUuidGameSessionId() {
    // when
    mockMvc
        .perform(patch("/api/v1/game_session/{gameSessionId}", "totototototototo"))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_refreshGameSession_returns200_when_validUuidAndAuthenticatedUser() {
    // when
    Mockito.when(gameSessionQueryService.findGameSessionById(any())).thenReturn(GAME_SESSION);

    mockMvc
        .perform(
            patch("/api/v1/game_session/{gameSessionId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(status().isOk());
  }
}
