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

import static com.lsadf.core.infra.web.controller.ParameterConstants.ORDER_BY;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.admin.application.game.AdminGameSaveController;
import com.lsadf.admin.application.game.AdminGameSaveControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
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
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.request.game.metadata.GameMetadataRequest;
import com.lsadf.core.infra.web.dto.request.game.save.GameSaveSortingParameter;
import com.lsadf.core.infra.web.dto.request.game.save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.dto.request.game.save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.dto.request.game.stage.StageRequest;
import com.lsadf.core.unit.config.WithMockJwtUser;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
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
      AdminGameSaveController.class,
      AdminGameSaveControllerImpl.class
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
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      CharacteristicsEventPublisherPort.class,
      GameSessionQueryService.class,
    })
class AdminGameSaveControllerTests {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private GameSaveService gameSaveService;

  @BeforeEach
  void setUp() {

    Mockito.when(gameSaveService.existsById(any(UUID.class))).thenReturn(true);
  }

  @Test
  @SneakyThrows
  void test_deleteGameSave_returns401_when_userNotAuthenticated() {
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
  void test_deleteGameSave_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/game_save/id/{game_save_id}",
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
  void test_deleteGameSave_returns400_when_gameSaveIdIsNotUuid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/game_save/id/{game_save_id}", "testtesttest")
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
  void test_deleteGameSave_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/game_save/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_generateNewGameSave_returns401_when_userNotAuthenticated() {
    // given
    CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L);
    CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L);
    StageRequest stageRequest = new StageRequest(1L, 10L);
    GameMetadataRequest metadataRequest =
        new GameMetadataRequest(
            UUID.fromString("3ab69f45-de06-4fce-bded-21d989fdad73"), "test@test.com", "test");
    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .characteristics(characteristicsRequest)
            .currency(currencyRequest)
            .stage(stageRequest)
            .metadata(metadataRequest)
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_generateNewGameSave_returns403_when_userNotAdmin() {
    // given
    CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L);
    CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L);
    StageRequest stageRequest = new StageRequest(1L, 10L);
    GameMetadataRequest metadataRequest =
        new GameMetadataRequest(UUID.randomUUID(), "test@test.com", "test");
    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .characteristics(characteristicsRequest)
            .currency(currencyRequest)
            .stage(stageRequest)
            .metadata(metadataRequest)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_save")
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
  void test_generateNewGameSave_returns400_when_invalidRequest(
      String id,
      String userEmail,
      String nickname,
      CharacteristicsRequest characteristics,
      CurrencyRequest currency,
      StageRequest stage) {
    // given

    GameMetadataRequest metadataRequest =
        new GameMetadataRequest(UUID.fromString(id), userEmail, nickname);

    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .metadata(metadataRequest)
            .characteristics(characteristics)
            .currency(currency)
            .stage(stage)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_save")
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
  void test_generateNewGameSave_returns400_when_requestBodyIsNull() {
    // given
    AdminGameSaveCreationRequest request = null;
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_save")
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
  void test_generateNewGameSave_returns200_when_authenticatedUserIsAdminAndValidInputs(
      String id,
      String userEmail,
      String nickname,
      CharacteristicsRequest characteristics,
      CurrencyRequest currency,
      StageRequest stage) {
    // given
    UUID uuid = null;
    if (id != null) {
      uuid = UUID.fromString(id);
    }

    GameMetadataRequest metadataRequest = new GameMetadataRequest(uuid, userEmail, nickname);
    AdminGameSaveCreationRequest request =
        AdminGameSaveCreationRequest.builder()
            .metadata(metadataRequest)
            .characteristics(characteristics)
            .currency(currency)
            .stage(stage)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getGameSave_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_save/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_getGameSave_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_save/id/{game_save_id}",
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
  void test_getGameSave_returns400_when_gameSaveIdIsNotUuid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_save/id/{game_save_id}", "testtesttest")
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
  void test_getGameSave_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_save/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getGameSaves_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_getGameSaves_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_save")
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
  void test_getGameSaves_returns400_when_orderByIsInvalid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_save")
                .param(ORDER_BY, "INVALID_ORDER_BY")
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
  void test_getGameSaves_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_save")
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
  void test_getGameSaves_returns200_when_authenticatedUserIsAdminAndValidOrderBy() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param(ORDER_BY, GameSaveSortingParameter.NICKNAME.name())
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getUserGameSaves_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_save/user/{username}", "paul.ochon@test.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_getUserGameSaves_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_save/user/{username}", "paul.ochon@test.com")
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
  void test_getUserGameSaves_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/game_save/user/{username}", "paul.ochon@test.com")
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
  void test_getUserGameSaves_returns200_when_usernameIsNotEmail() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/game_save/user/{username}", "testtesttest")
                .content(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateGameSave_returns401_when_userNotAuthenticated() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_save/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void test_updateGameSave_returns403_when_userNotAdmin() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_save/{game_save_id}",
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
  void test_updateGameSave_returns400_when_gameSaveIdIsNotUuid() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/game_save/id/{game_save_id}", "testtesttest")
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
  void test_updateGameSave_returns400_when_invalidRequest(String nickname) {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname(nickname).build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_save/id/{game_save_id}",
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
  void test_updateGameSave_returns400_when_gameSaveUpdateRequestIsNull() {
    // given
    AdminGameSaveUpdateRequest request = null;
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_save/id/{game_save_id}",
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
  void test_updateGameSave_returns200_when_authenticatedUserIsAdmin() {
    // given
    AdminGameSaveUpdateRequest request =
        AdminGameSaveUpdateRequest.builder().nickname("test").build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/game_save/id/{game_save_id}",
                    "3ab69f45-de06-4fce-bded-21d989fdad73")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
