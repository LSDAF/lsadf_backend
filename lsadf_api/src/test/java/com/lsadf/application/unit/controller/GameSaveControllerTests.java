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

import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lsadf.application.controller.game.save.game_save.GameSaveController;
import com.lsadf.application.controller.game.save.game_save.GameSaveControllerImpl;
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
import com.lsadf.core.application.game.session.GameSessionRepositoryPort;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.websocket.handler.game.CharacteristicsWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.CurrencyWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.StageWebSocketEventHandler;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(
    value = {GameSaveController.class, GameSaveControllerImpl.class, GlobalExceptionHandler.class})
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@MockitoBean(
    types = {
      GameSaveService.class,
      GameSessionRepositoryPort.class,
      GameMetadataRepositoryPort.class,
      CharacteristicsRepositoryPort.class,
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      CurrencyRepositoryPort.class,
      StageRepositoryPort.class,
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      CurrencyWebSocketEventHandler.class,
      CharacteristicsWebSocketEventHandler.class,
      StageWebSocketEventHandler.class
    })
class GameSaveControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private GameSaveService gameSaveService;

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

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
  void test_generateNewGameSave_returns200_when_userIsAuthenticated() {
    // when
    mockMvc
        .perform(post("/api/v1/game_save/generate").with(MOCK_JWT_USER))
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
  void test_updateNickname_returns400_when_gameSaveIdIsNotUuid() {
    // given
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest("test");
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "testtesttest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateNickname_returns400_when_nicknameValueIsNull() {
    // given
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest(null);
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateNickname_returns400_when_gameSaveNicknameUpdateRequestIsNull() {
    // given
    GameSaveNicknameUpdateRequest request = null;
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @SneakyThrows
  @ValueSource(strings = {"tr", "ertyuioqsdfghjklm", "test!", "test tu671"})
  void test_updateNickname_returns400_when_nicknameValueIsInvalid(String nickname) {
    // given
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest(nickname);
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateNickname_returns200_when_nicknameValueIsValidAndUserAuthenticated() {
    // given
    String nickname = "toto1234";
    GameSaveNicknameUpdateRequest request = new GameSaveNicknameUpdateRequest(nickname);
    // when
    mockMvc
        .perform(
            post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
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
  void test_getUserGameSaves_returns200_when_userIsAuthenticated() {
    // when
    mockMvc
        .perform(get("/api/v1/game_save/me").with(MOCK_JWT_USER))
        // then
        .andExpect(status().isOk());
  }
}
