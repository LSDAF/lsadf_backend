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
package com.lsadf.bdd.step_definition.when;

import static com.lsadf.bdd.util.ParameterizedTypeReferenceUtils.buildParameterizedJwtAuthenticationResponse;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.dto.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.datatable.DataTable;
import java.util.Map;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
@Component
public class BddWhenStepDefinitions {

  private static final String AUTH_LOGIN_PATH = "/api/v1/auth/login";
  private static final String AUTH_REFRESH_PATH = "/api/v1/auth/refresh";

  @Autowired protected TestRestTemplate testRestTemplate;

  @Autowired protected Stack<ApiResponse<?>> responseStack;

  @Autowired protected Stack<JwtAuthenticationResponse> jwtAuthenticationResponseStack;

  @Autowired protected Stack<Exception> exceptionStack;

  public void whenTheUserLogsInWithTheFollowingCredentials(DataTable dataTable, int serverPort) {
    try {
      var rows = dataTable.asMaps(String.class, String.class);

      // it should have only one line
      if (rows.size() > 1) {
        throw new IllegalArgumentException("Expected only one row in the DataTable");
      }

      Map<String, String> row = rows.get(0);
      String fullPath = AUTH_LOGIN_PATH;

      String url = BddUtils.buildUrl(serverPort, fullPath);
      UserLoginRequest userLoginRequest = BddUtils.mapToUserLoginRequest(row);

      HttpEntity<UserLoginRequest> request = BddUtils.buildHttpEntity(userLoginRequest);
      ResponseEntity<ApiResponse<JwtAuthenticationResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedJwtAuthenticationResponse());
      var response = result.getBody();
      assertThat(response).isNotNull();
      responseStack.push(response);
      var jwtAuthentication = response.data();
      if (jwtAuthentication != null) {
        jwtAuthenticationResponseStack.push(jwtAuthentication);
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  public void whenUserLogsInWithRefreshToken(String refreshToken, int serverPort) {
    try {
      String fullPath = AUTH_REFRESH_PATH;

      String url = BddUtils.buildUrl(serverPort, fullPath);
      UserRefreshLoginRequest userRefreshLoginRequest = new UserRefreshLoginRequest(refreshToken);

      HttpEntity<UserRefreshLoginRequest> request =
          BddUtils.buildHttpEntity(userRefreshLoginRequest);
      ResponseEntity<ApiResponse<JwtAuthenticationResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedJwtAuthenticationResponse());
      var response = result.getBody();
      assertThat(response).isNotNull();
      responseStack.push(response);
      var jwtAuthentication = response.data();
      if (jwtAuthentication != null) {
        jwtAuthenticationResponseStack.push(jwtAuthentication);
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  public void whenUserUsesGeneratedRefreshTokenToLogIn(int serverPort) {
    JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
    String refreshToken = jwtAuthenticationResponse.refreshToken();
    whenUserLogsInWithRefreshToken(refreshToken, serverPort);
  }
}
