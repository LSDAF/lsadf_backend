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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.application.controller.game.save.game_save.GameSaveController;
import com.lsadf.application.controller.game.save.game_save.GameSaveControllerImpl;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest({GameSaveController.class, GameSaveControllerImpl.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class GameSaveControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void test_generateNewGameSave_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(post("/api/v1/game_save/generate"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_generateNewGameSave_returns200_when_userIsAuthenticated() {
    // when
    mockMvc
        .perform(post("/api/v1/game_save/generate"))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_updateNickname_returns401_when_userNotAuthenticated() {
    // given
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest("test");
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_updateNickname_returns400_when_gameSaveIdIsNotUuid() {
    // given
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest("test");
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "testtesttest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_updateNickname_returns400_when_nicknameValueIsNull() {
    // given
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest(null);
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_updateNickname_returns400_when_gameSaveNicknameUpdateRequestIsNull() {
    // given
    GameSaveNicknameUpdateRequest request = null;
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @SneakyThrows
  @ValueSource(strings = {"tr", "ertyuioqsdfghjklm", "test!", "test tu671"})
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_updateNickname_returns400_when_nicknameValueIsInvalid(String nickname) {
    // given
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest(nickname);
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_updateNickname_returns200_when_nicknameValueIsValidAndUserAuthenticated() {
    // given
    String nickname = "toto1234";
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest(nickname);
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getUserGameSaves_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(get("/api/v1/game_save/me"))
        // then
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_getUserGameSaves_returns200_when_userIsAuthenticated() {
    // when
    mockMvc
        .perform(get("/api/v1/game_save/me"))
        // then
        .andExpect(status().isOk());
  }
}
