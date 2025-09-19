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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.application.controller.game.save.stage.StageController;
import com.lsadf.application.controller.game.save.stage.StageControllerImpl;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest({StageControllerImpl.class, StageController.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class StageControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void test_getStage_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(get("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_getStage_returns400_when_nonUuidGameSaveId() {
    // when
    mockMvc
        .perform(get("/api/v1/stage/{gameSaveId}", "testtesttest"))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_getStage_returns200_when_authenticatedUserAndValidUuid() {
    // when
    mockMvc
        .perform(
            get("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns400_when_noBody() {
    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns400_when_bodyIsNull() {
    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString()))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns400_when_gameSaveIdIsNonUuid() {
    // given
    StageRequest stageRequest = new StageRequest(5L, 2L);
    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "testtesttest")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stageRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString()))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns400_when_maxStageSmallerThanCurrentStage() {
    // given
    StageRequest stageRequest = new StageRequest(5L, 2L);

    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stageRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString()))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns400_when_stagesNegative() {
    // given
    StageRequest stageRequest = new StageRequest(-5L, -2L);

    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stageRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString()))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_saveStage_returns401_when_userNotAuthenticated() {
    // given
    StageRequest stageRequest = new StageRequest(10L, 25L);
    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stageRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString()))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns200_when_validBodyValidGameSaveIdAndAuthenticatedUser() {
    // given
    StageRequest stageRequest = new StageRequest(10L, 25L);
    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stageRequest))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString()))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns400_when_oneStageRequestFieldIsNull() {
    // given
    StageRequest stageRequest1 = new StageRequest(125L, null);
    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stageRequest1))
                .header(X_GAME_SESSION_ID, UUID.randomUUID().toString()))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_saveStage_returns400_when_noGameSessionIdHeader() {
    // given
    StageRequest stageRequest1 = new StageRequest(125L, 200L);
    // when
    mockMvc
        .perform(
            post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stageRequest1)))
        // then
        .andExpect(status().isBadRequest());
  }
}
