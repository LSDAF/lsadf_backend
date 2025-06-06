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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.admin.application.game.AdminGameSaveController;
import com.lsadf.admin.application.game.AdminGameSaveControllerImpl;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.infra.web.controller.ControllerConstants;
import com.lsadf.core.infra.web.controller.advices.GlobalExceptionHandler;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.requests.game.game_save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.requests.game.game_save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.requests.game.game_save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.requests.game.stage.StageRequest;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(
    value = {
      GlobalExceptionHandler.class,
      AdminGameSaveController.class,
      AdminGameSaveControllerImpl.class
    })
@Import({UnitTestConfiguration.class, GlobalExceptionHandler.class})
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class AdminGameSaveControllerTests {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private GameSaveService gameSaveService;

  @BeforeEach
  void setUp() {
    Mockito.when(gameSaveService.existsById(Mockito.anyString())).thenReturn(true);
  }

  @Test
  @SneakyThrows
  void deleteGameSave_should_return_401_when_user_not_authenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void deleteGameSave_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void deleteGameSave_should_return_400_when_game_save_id_is_not_uuid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/game_saves/id/{game_save_id}", "testtesttest")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void deleteGameSave_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void generateNewGameSave_should_return_401_when_user_not_authenticated() {
    // given
    CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L);
    CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L);
    StageRequest stageRequest = new StageRequest(1L, 10L);
    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .characteristics(characteristicsRequest)
            .currency(currencyRequest)
            .stage(stageRequest)
            .nickname("test")
            .id("3ab69f45-de06-4fce-bded-21d989fdad73")
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void generateNewGameSave_should_return_403_when_user_not_admin() {
    // given
    CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L);
    CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L);
    StageRequest stageRequest = new StageRequest(1L, 10L);
    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .characteristics(characteristicsRequest)
            .currency(currencyRequest)
            .stage(stageRequest)
            .nickname("test")
            .id("3ab69f45-de06-4fce-bded-21d989fdad73")
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  private static Stream<Arguments> provideGenerateGameSaveInvalidArguments() {
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L); // valid characteristicsRequest
    CurrencyRequest currencyRequest =
        new CurrencyRequest(100L, 100L, 100L, 100L); // valid currencyRequest
    StageRequest stageRequest = new StageRequest(1L, 10L); // valid stageRequest

    CharacteristicsRequest invalidCharacteristicsRequest =
        new CharacteristicsRequest(100L, 100L, 100L, -100L, 100L); // invalid characteristicsRequest
    CurrencyRequest invalidCurrencyRequest =
        new CurrencyRequest(100L, 100L, 100L, -100L); // invalid currencyRequest
    StageRequest invalidStageRequest = new StageRequest(10L, 1L); // invalid stageRequest

    return Stream.of(
        Arguments.of(
            "testtesttest",
            null,
            "test",
            characteristicsRequest,
            currencyRequest,
            stageRequest), // invalid id
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            null,
            "test",
            characteristicsRequest,
            currencyRequest,
            stageRequest), // invalid email
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "test",
            "test",
            characteristicsRequest,
            currencyRequest,
            stageRequest), // invalid email 2
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "paul.ochon@test.com",
            "test",
            null,
            currencyRequest,
            stageRequest), // invalid characteristicsRequest (null)
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "paul.ochon@test.com",
            "test",
            invalidCharacteristicsRequest,
            currencyRequest,
            stageRequest), // invalid characteristicsRequest
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "paul.ochon@test.com",
            "test",
            characteristicsRequest,
            null,
            stageRequest), // invalid currencyRequest (null)
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "paul.ochon@test.com",
            "test",
            characteristicsRequest,
            invalidCurrencyRequest,
            stageRequest), // invalid currencyRequest
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "paul.ochon@test.com",
            "test",
            characteristicsRequest,
            currencyRequest,
            null), // invalid stageRequest (null)
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "paul.ochon@test.com",
            "test",
            characteristicsRequest,
            currencyRequest,
            invalidStageRequest) // invalid stageRequest
        );
  }

  @ParameterizedTest
  @MethodSource("provideGenerateGameSaveInvalidArguments")
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void generateNewGameSave_should_return_400_when_invalid_request(
      String id,
      String userEmail,
      String nickname,
      CharacteristicsRequest characteristics,
      CurrencyRequest currency,
      StageRequest stage) {
    // given
    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .id(id)
            .userEmail(userEmail)
            .nickname(nickname)
            .characteristics(characteristics)
            .currency(currency)
            .stage(stage)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void generateNewGameSave_should_return_400_when_request_body_is_null() {
    // given
    AdminGameSaveCreationRequest request = null;
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  private static Stream<Arguments> provideGenerateGameSaveValidArguments() {
    CharacteristicsRequest characteristicsRequest =
        new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L); // valid characteristicsRequest
    CurrencyRequest currencyRequest =
        new CurrencyRequest(100L, 100L, 100L, 100L); // valid currencyRequest
    StageRequest stageRequest = new StageRequest(1L, 10L); // valid stageRequest

    return Stream.of(
        Arguments.of(
            null,
            "paul.ochon@test.com",
            "test",
            characteristicsRequest,
            currencyRequest,
            stageRequest), // valid: null id
        Arguments.of(
            "3ab69f45-de06-4fce-bded-21d989fdad73",
            "paul.ochon@test.com",
            "test",
            characteristicsRequest,
            currencyRequest,
            stageRequest) // valid
        );
  }

  @ParameterizedTest
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  @SneakyThrows
  @MethodSource("provideGenerateGameSaveValidArguments")
  void generateNewGameSave_should_return_200_when_authenticated_user_is_admin_and_valid_inputs(
      String id,
      String userEmail,
      String nickname,
      CharacteristicsRequest characteristics,
      CurrencyRequest currency,
      StageRequest stage) {
    // given
    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .id(id)
            .userEmail(userEmail)
            .nickname(nickname)
            .characteristics(characteristics)
            .currency(currency)
            .stage(stage)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void getGameSave_should_return_401_when_user_not_authenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void getGameSave_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getGameSave_should_return_400_when_game_save_id_is_not_uuid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_saves/id/{game_save_id}", "testtesttest")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getGameSave_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void getGameSaves_should_return_401_when_user_not_authenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void getGameSaves_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getGameSaves_should_return_400_when_orderBy_is_invalid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_saves")
                .param(ControllerConstants.Params.ORDER_BY, "INVALID_ORDER_BY")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getGameSaves_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getGameSaves_should_return_200_when_authenticated_user_is_admin_and_valid_orderBy() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_saves")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param(
                    ControllerConstants.Params.ORDER_BY, GameSaveSortingParameter.NICKNAME.name())
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void getUserGameSaves_should_return_401_when_user_not_authenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_saves/user/{username}", "paul.ochon@test.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void getUserGameSaves_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_saves/user/{username}", "paul.ochon@test.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getUserGameSaves_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_saves/user/{username}", "paul.ochon@test.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getUserGameSaves_should_return_200_when_username_is_not_email() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_saves/user/{username}", "testtesttest")
                .content(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void updateGameSave_should_return_401_when_user_not_authenticated() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_saves/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void updateGameSave_should_return_403_when_user_not_admin() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_saves/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void updateGameSave_should_return_400_when_gameSaveId_is_not_uuid() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_saves/id/{game_save_id}", "testtesttest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  private static Stream<Arguments> provideUpdateGameSaveInvalidArguments() {
    return Stream.of(
        Arguments.of("zuoezzh!@#&") // invalid nickname
        );
  }

  @ParameterizedTest
  @SneakyThrows
  @MethodSource("provideUpdateGameSaveInvalidArguments")
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void updateGameSave_should_return_400_when_invalid_request(String nickname) {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname(nickname).build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void updateGameSave_should_return_400_when_GameSaveUpdateRequest_is_null() {
    // given
    AdminGameSaveUpdateRequest request = null;
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void updateGameSave_should_return_200_when_authenticated_user_is_admin() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_saves/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
