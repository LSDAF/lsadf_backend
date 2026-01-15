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
import static com.lsadf.core.unit.config.MockAuthenticationFactory.createMockJwt;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.admin.application.user.AdminUserController;
import com.lsadf.admin.application.user.AdminUserControllerImpl;
import com.lsadf.core.application.game.inventory.InventoryRepositoryPort;
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
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.user.UserSortingParameter;
import com.lsadf.core.infra.web.dto.request.user.creation.AdminUserCreationRequest;
import com.lsadf.core.infra.web.dto.request.user.update.AdminUserUpdateRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(
    value = {
      GlobalExceptionHandler.class,
      AdminUserController.class,
      AdminUserControllerImpl.class
    })
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
      CharacteristicsEventPublisherPort.class,
      GameSessionQueryService.class,
    })
class AdminUserControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private UserService userService;

  private static final User USER =
      new User(
          UUID.randomUUID(),
          "Paul",
          "ITESSE",
          "paul.itesse@test.com",
          true,
          true,
          List.of("USER"),
          new Date());

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_USER =
      createMockJwt("paul.ochon@test.com", List.of("USER"), "Paul OCHON");

  private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor MOCK_JWT_ADMIN =
      createMockJwt("paul.ochon@test.com", List.of("ADMIN", "USER"), "Paul OCHON");

  @Test
  @SneakyThrows
  void test_updateUser_returns401_when_userNotAuthenticated() {
    // given
    AdminUserUpdateRequest request =
        AdminUserUpdateRequest.builder()
            .firstName("Paul")
            .lastName("OCHON")
            .userRoles(List.of("ADMIN"))
            .enabled(true)
            .emailVerified(true)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_updateUser_returns403_when_userNotAdmin() {
    // given
    AdminUserUpdateRequest request =
        AdminUserUpdateRequest.builder()
            .firstName("Paul")
            .lastName("OCHON")
            .userRoles(List.of("ADMIN"))
            .enabled(true)
            .emailVerified(true)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_updateUser_returns400_when_idIsNotUuid() {
    // given
    AdminUserUpdateRequest request =
        AdminUserUpdateRequest.builder()
            .firstName("Paul")
            .lastName("OCHON")
            .userRoles(List.of("ADMIN"))
            .enabled(true)
            .emailVerified(true)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/user/id/{user_id}", "testtesttest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_updateUser_returns200_when_authenticatedUserIsAdmin() {
    // given
    AdminUserUpdateRequest request =
        AdminUserUpdateRequest.builder()
            .firstName("Paul")
            .lastName("OCHON")
            .userRoles(List.of("ADMIN"))
            .enabled(true)
            .emailVerified(true)
            .build();

    Mockito.when(userService.updateUser(any(UUID.class), any())).thenReturn(USER);
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  private static Stream<Arguments> updateUserArgumentsProvider() {
    return Stream.of(
        Arguments.of("", "OCHON", true, true), // invalid first name
        Arguments.of(null, "OCHON", true, true), // invalid first name 2
        Arguments.of("Paul", "", true, true), // invalid last name
        Arguments.of("Paul", null, true, true), // invalid last name 2
        Arguments.of("Paul", "OCHON", null, true), // invalid enabled
        Arguments.of("Paul", "OCHON", true, null) // invalid email verified
        );
  }

  @ParameterizedTest
  @SneakyThrows
  @MethodSource("updateUserArgumentsProvider")
  void test_updateUser_returns400_when_requestIsInvalid(
      String firstName, String lastName, Boolean enabled, Boolean emailVerified) {
    // given
    AdminUserUpdateRequest request =
        AdminUserUpdateRequest.builder()
            .firstName(firstName)
            .lastName(lastName)
            .userRoles(List.of("ADMIN"))
            .enabled(enabled)
            .emailVerified(emailVerified)
            .build();
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_createUser_returns401_when_userNotAuthenticated() {
    // given
    AdminUserCreationRequest request =
        AdminUserCreationRequest.builder()
            .firstName("Paul")
            .lastName("ITESSE")
            .username("paul.itesse@test.com")
            .enabled(true)
            .emailVerified(true)
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_createUser_returns403_when_userNotAdmin() {
    // given
    AdminUserCreationRequest request =
        AdminUserCreationRequest.builder()
            .firstName("Paul")
            .lastName("ITESSE")
            .username("paul.itesse@test.com")
            .enabled(true)
            .emailVerified(true)
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_createUser_returns200_when_authenticatedUserIsAdmin() {
    // given
    AdminUserCreationRequest request =
        AdminUserCreationRequest.builder()
            .firstName("Paul")
            .lastName("ITESSE")
            .username("paul.itesse@test.com")
            .enabled(true)
            .emailVerified(true)
            .build();

    Mockito.when(userService.createUser(request)).thenReturn(USER);

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  private static Stream<Arguments> createUserArgumentsProvider() {
    return Stream.of(
        Arguments.of("paul.itesse", "Paul", "ITESSE", true, true), // invalid username
        Arguments.of("paul.itesse@test.com", "", "ITESSE", true, true), // invalid first name
        Arguments.of("paul.itesse@test.com", null, "ITESSE", true, true), // invalid first name 2
        Arguments.of("paul.itesse@test.com", "Paul", "", true, true), // invalid last name
        Arguments.of("paul.itesse@test.com", "Paul", null, true, true), // invalid last name 2
        Arguments.of("paul.itesse@test.com", "Paul", "ITESSE", null, true), // invalid enabled
        Arguments.of("paul.itesse@test.com", "Paul", "ITESSE", true, null) // invalid email verified
        );
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource("createUserArgumentsProvider")
  void test_createUser_returns400_when_requestIsInvalid(
      String username, String firstName, String lastName, Boolean enabled, Boolean emailVerified) {
    // given
    AdminUserCreationRequest request =
        AdminUserCreationRequest.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .enabled(enabled)
            .emailVerified(emailVerified)
            .build();

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_deleteUser_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_deleteUser_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_deleteUser_returns400_when_idIsNotUuid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/v1/admin/user/id/{user_id}", "testtesttest")
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_deleteUser_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getUserByUsername_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/user/username/{username}", "paul.ochon@test.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_getUserByUsername_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/user/username/{username}", "paul.ochon@test.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_getUserByUsername_returns200_when_authenticatedUserIsAdmin() {
    // when
    Mockito.when(userService.getUserByUsername(any(String.class))).thenReturn(USER);
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user/username/{username}", "test@test.com")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getUserByUsername_returns400_when_usernameIsNotValid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user/username/{username}", "test")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_getUserById_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_getUserById_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_getUserById_returns200_when_authenticatedUserIsAdminAndValidUuid() {
    Mockito.when(userService.getUserById(any(UUID.class))).thenReturn(USER);
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                    "/api/v1/admin/user/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getUserById_returns400_when_idIsNotUuid() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user/id/{user_id}", "testtesttest")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_getUsers_returns401_when_userNotAuthenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void test_getUsers_returns403_when_userNotAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_USER))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  void test_getUsers_returns200_when_authenticatedUserIsAdmin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @SneakyThrows
  void test_getUsers_returns400_when_invalidOrderBy() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param(ORDER_BY, "INVALID_ORDER_BY")
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_getUsers_returns200_when_orderByIsSet() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param(ORDER_BY, UserSortingParameter.FIRST_NAME_DESC.name())
                .with(MOCK_JWT_ADMIN))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
