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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lsadf.application.controller.auth.AuthController;
import com.lsadf.application.controller.auth.AuthControllerImpl;
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
import com.lsadf.core.infra.web.client.keycloak.KeycloakRestClient;
import com.lsadf.core.infra.web.controller.advice.GlobalExceptionHandler;
import com.lsadf.core.infra.web.dto.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.dto.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.websocket.handler.game.CharacteristicsWebSocketEventHandler;
import com.lsadf.core.infra.websocket.handler.game.CurrencyWebSocketEventHandler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(value = {AuthControllerImpl.class, AuthController.class, GlobalExceptionHandler.class})
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
      GameMailTemplateRepositoryPort.class,
      GameMailRepositoryPort.class,
      GameSaveRepositoryPort.class,
      InventoryRepositoryPort.class,
      GameSessionCachePort.class,
      GameMetadataCachePort.class,
      CurrencyCachePort.class,
      StageCachePort.class,
      CharacteristicsCachePort.class,
      KeycloakRestClient.class,
      CurrencyWebSocketEventHandler.class,
      CharacteristicsWebSocketEventHandler.class
    })
class AuthControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void test_login_returns_400_when_username_is_null() {
    // given
    UserLoginRequest loginRequest = new UserLoginRequest(null, "password");
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_login_returns_400_when_password_is_null() {
    // given
    UserLoginRequest loginRequest = new UserLoginRequest("test@test.com", null);
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_login_returns_400_when_username_is_not_email_format() {
    // given
    UserLoginRequest loginRequest = new UserLoginRequest("username", "password");
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_login_returns_400_when_request_body_is_null() {
    // given
    UserLoginRequest loginRequest = null;
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void test_refresh_returns_400_when_refresh_token_is_null() {
    // given
    UserRefreshLoginRequest refreshRequest = new UserRefreshLoginRequest(null);
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(refreshRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }
}
