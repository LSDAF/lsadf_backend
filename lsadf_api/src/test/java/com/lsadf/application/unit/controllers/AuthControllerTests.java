/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.application.unit.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.application.controllers.AuthController;
import com.lsadf.application.controllers.impl.AuthControllerImpl;
import com.lsadf.core.infra.web.controllers.advices.GlobalExceptionHandler;
import com.lsadf.core.infra.web.requests.user.UserLoginRequest;
import com.lsadf.core.infra.web.requests.user.UserRefreshLoginRequest;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {AuthControllerImpl.class, AuthController.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class AuthControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void should_return_400_when_login_request_contains_null_username() {
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
  void should_return_400_when_login_request_contains_null_password() {
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
  void should_return_400_when_login_request_contains_not_email_username() {
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
  void should_return_400_when_login_request_is_null() {
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
  void should_return_400_when_refresh_request_contains_null_refresh_token() {
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
