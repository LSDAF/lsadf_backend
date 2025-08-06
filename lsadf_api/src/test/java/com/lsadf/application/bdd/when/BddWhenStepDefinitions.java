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
package com.lsadf.application.bdd.when;

import static com.lsadf.application.controller.auth.AuthController.Constants.ApiPaths.*;
import static com.lsadf.core.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.*;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.game.game_save.GameSaveController;
import com.lsadf.application.controller.user.UserController;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.infra.web.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.response.jwt.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.response.user.UserInfoResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {


  @When("^the user logs in with the following refresh token (.*)$")
  public void whenUserLogsInWithRefreshToken(String refreshToken) {
    try {
      String fullPath = ApiPathConstants.AUTH + REFRESH;

      String url = BddUtils.buildUrl(this.serverPort, fullPath);
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

  @When("the user logs in with the following credentials")
  public void whenUserLogsInWithCredentials(DataTable dataTable) {
    try {
      var rows = dataTable.asMaps(String.class, String.class);

      // it should have only one line
      if (rows.size() > 1) {
        throw new IllegalArgumentException("Expected only one row in the DataTable");
      }

      Map<String, String> row = rows.get(0);
      String fullPath = ApiPathConstants.AUTH + LOGIN;

      String url = BddUtils.buildUrl(this.serverPort, fullPath);
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


  @When("^the user requests the endpoint to generate a GameSave$")
  public void whenUserRequestsEndpointToGenerateGameSave() {
    String fullPath = ApiPathConstants.GAME_SAVE + GameSaveController.Constants.ApiPaths.GENERATE;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(jwtAuthenticationResponse.accessToken());
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GameSaveResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      var body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }


  @When(
      "^the user requests the endpoint to update a GameSave with id (.*) with the following GameSaveNicknameUpdateRequest$")
  public void
      whenUserRequestsEndpointToUpdateGameSave(
          String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    // it should have only one line
    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);
    GameSaveNicknameUpdateRequest updateRequest = BddUtils.mapToGameSaveUpdateUserRequest(row);

    String fullPath = ApiPathConstants.GAME_SAVE + "/" + gameSaveId + "/nickname";

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<GameSaveNicknameUpdateRequest> request = new HttpEntity<>(updateRequest, headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }


  @When("^the user requests the endpoint to get his UserInfo$")
  public void whenUserRequestsEndpointToGetUserInfo() {
    String fullPath = ApiPathConstants.USER + UserController.Constants.ApiPaths.ME;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<UserInfoResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedUserInfoResponse());
      ApiResponse<UserInfoResponse> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to get his GameSaves$")
  public void whenUserRequestsEndpointToGetGameSaves() {
    String fullPath = ApiPathConstants.GAME_SAVE + GameSaveController.Constants.ApiPaths.ME;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<List<GameSaveResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
      ApiResponse<List<GameSaveResponse>> body = result.getBody();
      gameSaveResponseListStack.push(body.data());
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("the user uses the previously generated refresh token to log in")
  public void whenUserUsesGeneratedRefreshTokenToLogIn() {
    JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
    String refreshToken = jwtAuthenticationResponse.refreshToken();
    whenUserLogsInWithRefreshToken(refreshToken);
  }
}
