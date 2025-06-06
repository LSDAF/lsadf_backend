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

import static com.lsadf.application.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.bdd.BddUtils;
import com.lsadf.application.bdd.CacheEntryType;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.web.client.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.controller.ControllerConstants;
import com.lsadf.core.infra.web.request.game.game_save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.request.user.creation.SimpleUserCreationRequest;
import com.lsadf.core.infra.web.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.request.user.login.UserRefreshLoginRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.game_save.GameSaveResponse;
import com.lsadf.core.infra.web.responses.user.UserInfoResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.*;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

  @When("^a (.*) cache entry is expired$")
  public void when_a_cache_entry_is_expired(String cacheType) {
    CacheEntryType cacheEntryType = CacheEntryType.fromString(cacheType);
    int size =
        switch (cacheEntryType) {
          case CHARACTERISTICS, CHARACTERISTICS_HISTO -> characteristicsCache.getAllHisto().size();
          case CURRENCY, CURRENCY_HISTO -> currencyCache.getAllHisto().size();
          case STAGE, STAGE_HISTO -> stageCache.getAllHisto().size();
          case GAME_SAVE_OWNERSHIP -> gameSaveOwnershipCache.getAll().size();
        };
    log.info("Waiting for {} cache entry to expire...", cacheType);
    await()
        .atMost(1200, TimeUnit.SECONDS)
        .until(
            () -> {
              try {
                int newSize =
                    switch (cacheEntryType) {
                      case CHARACTERISTICS, CHARACTERISTICS_HISTO ->
                          characteristicsCache.getAllHisto().size();
                      case CURRENCY, CURRENCY_HISTO -> currencyCache.getAllHisto().size();
                      case STAGE, STAGE_HISTO -> stageCache.getAllHisto().size();
                      case GAME_SAVE_OWNERSHIP -> gameSaveOwnershipCache.getAll().size();
                    };
                return newSize < size;
              } catch (Exception e) {
                return false;
              }
            });
  }

  @When("^the cache is flushed$")
  public void when_the_cache_is_flushed() {
    log.info("Flushing cache...");
    this.cacheFlushService.flushCharacteristics();
    this.cacheFlushService.flushCurrencies();
    this.cacheFlushService.flushStages();
  }

  @When("^the user with email (.*) gets the game save with id (.*)$")
  public void when_the_user_with_email_gets_a_game_save_with_id(
      String userEmail, String gameSaveId) {
    try {
      GameSave gameSave = gameSaveService.getGameSave(gameSaveId);
      gameSaveListStack.push(Collections.singletonList(gameSave));
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user logs in with the following refresh token (.*)$")
  public void logInWithRefreshToken(String refreshToken) {
    try {
      String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.REFRESH;

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
      var jwtAuthentication = response.getData();
      if (jwtAuthentication != null) {
        jwtAuthenticationResponseStack.push(jwtAuthentication);
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("the user logs in with the following credentials")
  public void when_the_user_logs_in_with_the_following_credentials(DataTable dataTable) {
    try {
      var rows = dataTable.asMaps(String.class, String.class);

      // it should have only one line
      if (rows.size() > 1) {
        throw new IllegalArgumentException("Expected only one row in the DataTable");
      }

      Map<String, String> row = rows.get(0);
      String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.LOGIN;

      String url = BddUtils.buildUrl(this.serverPort, fullPath);
      UserLoginRequest userLoginRequest = BddUtils.mapToUserLoginRequest(row);

      HttpEntity<UserLoginRequest> request = BddUtils.buildHttpEntity(userLoginRequest);
      ResponseEntity<ApiResponse<JwtAuthenticationResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedJwtAuthenticationResponse());
      var response = result.getBody();
      assertThat(response).isNotNull();
      responseStack.push(response);
      var jwtAuthentication = response.getData();
      if (jwtAuthentication != null) {
        jwtAuthenticationResponseStack.push(jwtAuthentication);
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the endpoint to register a user with the following SimpleUserCreationRequest$")
  public void
      when_the_user_request_the_endpoint_to_register_a_user_with_the_following_UserCreationRequest(
          DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    // it should have only one line
    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);
    SimpleUserCreationRequest simpleUserCreationRequest = BddUtils.mapToUserCreationRequest(row);
    String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.REGISTER;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    HttpEntity<SimpleUserCreationRequest> request =
        BddUtils.buildHttpEntity(simpleUserCreationRequest);
    try {

      ResponseEntity<ApiResponse<UserInfoResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedUserInfoResponse());
      ApiResponse<UserInfoResponse> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^we want to delete the game save with id (.*)$")
  public void when_the_user_with_email_deletes_a_game_save(String saveId) {
    try {
      gameSaveService.deleteGameSave(saveId);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^we want to create a new game save for the user with email (.*)$")
  public void when_we_want_to_create_a_new_game_save_for_the_user_with_email(String userEmail) {
    try {
      GameSave gameSave = gameSaveService.createGameSave(userEmail);
      gameSaveListStack.push(Collections.singletonList(gameSave));
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^we want to get the game save with id (.*)$")
  public void when_we_want_to_get_the_game_save_with_id(String saveId) {
    try {
      GameSave gameSave = gameSaveService.getGameSave(saveId);
      gameSaveListStack.push(Collections.singletonList(gameSave));
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to generate a GameSave$")
  public void when_the_user_requests_the_endpoint_to_create_a_game_save() {
    String fullPath = ControllerConstants.GAME_SAVE + ControllerConstants.GameSave.GENERATE;

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

  @When("^the user requests the endpoint to generate a game save with no token$")
  public void when_the_user_requests_the_endpoint_to_create_a_game_save_with_no_token() {
    String fullPath = ControllerConstants.GAME_SAVE + ControllerConstants.GameSave.GENERATE;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
      ResponseEntity<ApiResponse<GameSaveResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      ApiResponse<GameSaveResponse> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to update a GameSave with no token$")
  public void when_the_user_requests_the_endpoint_to_update_a_game_save_with_no_token() {
    String fullPath = ControllerConstants.GAME_SAVE + "/1";

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<GameSaveNicknameUpdateRequest> request =
          new HttpEntity<>(new GameSaveNicknameUpdateRequest(), headers);
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

  @When(
      "^the user requests the endpoint to update a GameSave with id (.*) with the following GameSaveNicknameUpdateRequest$")
  public void
      when_the_user_requests_the_endpoint_to_update_a_game_save_with_id_with_the_following_game_save_update_nickname_request(
          String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    // it should have only one line
    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);
    GameSaveNicknameUpdateRequest updateRequest = BddUtils.mapToGameSaveUpdateUserRequest(row);

    String fullPath = ControllerConstants.GAME_SAVE + "/" + gameSaveId + "/nickname";

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

  @When("^the user requests the endpoint to get his UserInfo with no token$")
  public void when_the_user_requests_the_endpoint_to_get_his_user_info_with_no_token() {
    String fullPath = ControllerConstants.USER + ControllerConstants.User.ME;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
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

  @When("^the user requests the endpoint to get his UserInfo$")
  public void when_the_user_requests_the_endpoint_to_get_his_user_info() {
    String fullPath = ControllerConstants.USER + ControllerConstants.User.ME;

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
  public void when_the_user_requests_the_endpoint_to_get_his_game_saves() {
    String fullPath = ControllerConstants.GAME_SAVE + ControllerConstants.GameSave.ME;

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
      gameSaveResponseListStack.push(body.getData());
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to get his GameSaves with no token$")
  public void when_the_user_requests_the_endpoint_to_get_his_game_saves_with_no_token() {
    String fullPath = ControllerConstants.USER + ControllerConstants.User.USER_ME_GAME_SAVES;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
      ResponseEntity<ApiResponse<List<GameSaveResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
      ApiResponse<List<GameSaveResponse>> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("the user uses the previously generated refresh token to log in")
  public void when_the_user_uses_the_previously_generated_refresh_token_to_log_in() {
    JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
    String refreshToken = jwtAuthenticationResponse.refreshToken();
    logInWithRefreshToken(refreshToken);
  }
}
