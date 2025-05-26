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
package com.lsadf.application.bdd.when;

import static com.lsadf.application.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.*;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.bdd.BddUtils;
import com.lsadf.core.common.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.core.common.exceptions.http.NotFoundException;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.info.GlobalInfo;
import com.lsadf.core.domain.user.User;
import com.lsadf.core.infra.persistence.game.GameSaveEntity;
import com.lsadf.core.infra.web.config.auth.JwtAuthentication;
import com.lsadf.core.infra.web.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.requests.admin.AdminUserCreationRequest;
import com.lsadf.core.infra.web.requests.admin.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.requests.common.Filter;
import com.lsadf.core.infra.web.requests.currency.CurrencyRequest;
import com.lsadf.core.infra.web.requests.game.stage.StageRequest;
import com.lsadf.core.infra.web.requests.search.SearchRequest;
import com.lsadf.core.infra.web.responses.GenericResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[ADMIN WHEN STEP DEFINITIONS]")
public class BddAdminWhenStepDefinitions extends BddLoader {

  @When("^we want to create a new game save with the following AdminGameSaveCreationRequest$")
  public void
      when_we_want_to_create_a_new_game_save_with_the_following_AdminGameSaveCreationRequest(
          DataTable dataTable) throws NotFoundException, AlreadyExistingGameSaveException {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    // it should have only one line
    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    List<GameSaveEntity> list = new ArrayList<>();
    for (Map<String, String> row : rows) {
      AdminGameSaveCreationRequest request = BddUtils.mapToAdminGameSaveCreationRequest(row);
      GameSaveEntity gameSave = gameSaveService.createGameSave(request);
      list.add(gameSave);
    }

    try {
      gameSaveEntityListStack.push(list);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("the user requests the admin endpoint to get the global info")
  public void when_the_user_requests_the_admin_endpoint_for_global_info() {
    String fullPath = ControllerConstants.ADMIN_GLOBAL_INFO;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<GlobalInfo>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGlobalInfoResponse());
      GenericResponse<GlobalInfo> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get all the users ordered by (.*)$")
  public void when_the_user_requests_the_admin_endpoint_to_get_all_the_users_ordered_by(
      String orderBy) {
    String fullPath = ControllerConstants.ADMIN_USERS + "?order_by=" + orderBy;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<List<User>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedUserListResponse());
      GenericResponse<List<User>> body = result.getBody();
      userListStack.push(body.getData());
      responseStack.push(body);

      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get all the game saves ordered by (.*)$")
  public void when_the_user_requests_the_admin_endpoint_to_get_all_save_games_ordered_by(
      String orderBy) {
    String fullPath = ControllerConstants.ADMIN_GAME_SAVES + "?order_by=" + orderBy;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<List<GameSave>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
      GenericResponse<List<GameSave>> body = result.getBody();
      gameSaveListStack.push(body.getData());
      responseStack.push(body);

      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @And("^the user requests the admin endpoint to delete the user with id (.*)$")
  public void when_the_user_requests_the_admin_endpoint_to_delete_the_user_with_id(String userId) {
    String fullPath =
        ControllerConstants.ADMIN_USERS
            + ControllerConstants.AdminUser.USER_ID.replace("{user_id}", userId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @And("^the user requests the admin endpoint to delete the game save with id (.*)$")
  public void when_the_user_requests_the_admin_endpoint_to_delete_the_game_save_with_id(
      String gameSaveId) {
    String fullPath =
        ControllerConstants.ADMIN_GAME_SAVES
            + ControllerConstants.AdminGameSave.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get the user with the following id (.*)$")
  public void when_the_user_requests_the_admin_endpoint_to_get_the_user_with_the_following_id(
      String userId) {
    String fullPath =
        ControllerConstants.ADMIN_USERS
            + ControllerConstants.AdminUser.USER_ID.replace("{user_id}", userId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<User>> result =
          testRestTemplate.exchange(url, HttpMethod.GET, request, buildParamaterizedUserResponse());
      GenericResponse<User> body = result.getBody();
      userListStack.push(Collections.singletonList(body.getData()));
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When("^the user requests the admin endpoint to get a game save with the following id (.*)$")
  public void when_the_user_requests_the_admin_endpoint_to_get_the_game_save_with_the_following_id(
      String gameSaveId) {
    String fullPath =
        ControllerConstants.ADMIN_GAME_SAVES
            + ControllerConstants.AdminGameSave.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<GameSave>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameSaveResponse());
      GenericResponse<GameSave> body = result.getBody();
      gameSaveListStack.push(Collections.singletonList(body.getData()));
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When("^the user requests the admin endpoint to get the user with the following username (.*)$")
  public void when_the_user_requests_the_admin_endpoint_to_get_the_user_with_the_following_username(
      String username) {
    String fullPath =
        ControllerConstants.ADMIN_USERS
            + ControllerConstants.AdminUser.USERNAME.replace("{username}", username);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<User>> result =
          testRestTemplate.exchange(url, HttpMethod.GET, request, buildParamaterizedUserResponse());
      GenericResponse<User> body = result.getBody();
      userListStack.push(Collections.singletonList(body.getData()));
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When(
      "^the user requests the admin endpoint to search users ordered by (.*) with the following SearchRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_search_users_ordered_by_with_the_following_search_request(
          String orderBy, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath =
        ControllerConstants.ADMIN_SEARCH
            + ControllerConstants.AdminSearch.SEARCH_USERS
            + "?order_by="
            + orderBy;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<Filter> filters = rows.stream().map(BddUtils::mapToFilter).toList();

    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<SearchRequest> request = new HttpEntity<>(new SearchRequest(filters), headers);
      ResponseEntity<GenericResponse<List<User>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedUserListResponse());
      GenericResponse<List<User>> body = result.getBody();
      userListStack.push(body.getData());
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When(
      "^the user requests the admin endpoint to search game saves ordered by (.*) with the following SearchRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_search_game_saves_ordered_by_with_the_following_search_request(
          String orderBy, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath =
        ControllerConstants.ADMIN_SEARCH
            + ControllerConstants.AdminSearch.SEARCH_GAME_SAVES
            + "?order_by="
            + orderBy;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<Filter> filters = rows.stream().map(BddUtils::mapToFilter).toList();

    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<SearchRequest> request = new HttpEntity<>(new SearchRequest(filters), headers);
      ResponseEntity<GenericResponse<List<GameSave>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveListResponse());
      GenericResponse<List<GameSave>> body = result.getBody();
      gameSaveListStack.push(body.getData());
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update the user with id (.*) with the following AdminUserUpdateRequest$")
  public void when_the_user_requests_the_admin_endpoint_to_update_the_user_with_id(
      String id, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    AdminUserUpdateRequest adminUserUpdateRequest =
        BddUtils.mapToAdminUserUpdateRequest(rows.get(0));

    String fullPath =
        ControllerConstants.ADMIN_USERS
            + ControllerConstants.AdminUser.USER_ID.replace("{user_id}", id);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminUserUpdateRequest> request =
          new HttpEntity<>(adminUserUpdateRequest, headers);
      ResponseEntity<GenericResponse<User>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParamaterizedUserResponse());
      GenericResponse<User> body = result.getBody();
      userListStack.push(Collections.singletonList(body.getData()));
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When(
      "^the user requests the admin endpoint to create a new user with the following AdminUserCreationRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_create_a_new_user_with_the_following_AdminUserCreationRequest(
          DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath = ControllerConstants.ADMIN_USERS;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<AdminUserCreationRequest> requests =
        rows.stream().map(BddUtils::mapToAdminUserCreationRequest).toList();

    assertThat(requests).hasSize(1);

    AdminUserCreationRequest adminRequest = requests.get(0);

    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminUserCreationRequest> request = new HttpEntity<>(adminRequest, headers);
      ResponseEntity<GenericResponse<User>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParamaterizedUserResponse());
      GenericResponse<User> body = result.getBody();
      userListStack.push(Collections.singletonList(body.getData()));
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When("^the user requests the admin endpoint to toggle the cache status$")
  public void when_the_user_requests_the_admin_endpoint_to_toggle_the_cache_status() {
    String fullPath = ControllerConstants.ADMIN_CACHE + ControllerConstants.AdminCache.TOGGLE;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<Boolean>> result =
          testRestTemplate.exchange(
              url, HttpMethod.PUT, request, buildParameterizedBooleanResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get the cache status$")
  public void when_the_user_requests_the_admin_endpoint_to_get_the_cache_status() {
    String fullPath = ControllerConstants.ADMIN_CACHE + ControllerConstants.AdminCache.ENABLED;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<Boolean>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedBooleanResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to flush and clear the cache$")
  public void when_the_user_requests_the_admin_endpoint_to_clear_the_cache() {
    String fullPath = ControllerConstants.ADMIN_CACHE + ControllerConstants.AdminCache.FLUSH;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<Void>> result =
          testRestTemplate.exchange(url, HttpMethod.PUT, request, buildParameterizedVoidResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_create_a_new_game_save_with_the_following_game_save_creation_request(
          DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath = ControllerConstants.ADMIN_GAME_SAVES;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<AdminGameSaveCreationRequest> requests =
        rows.stream().map(BddUtils::mapToAdminGameSaveCreationRequest).toList();

    assertThat(requests).hasSize(1);

    AdminGameSaveCreationRequest adminRequest = requests.get(0);

    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminGameSaveCreationRequest> request = new HttpEntity<>(adminRequest, headers);
      ResponseEntity<GenericResponse<GameSave>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      GenericResponse<GameSave> body = result.getBody();
      gameSaveListStack.push(Collections.singletonList(body.getData()));
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to get all the game saves of the user with the following username (.*)$")
  public void
      when_the_user_requests_the_admin_endpoint_to_get_all_the_game_saves_of_the_user_with_the_following_username(
          String username) {
    String fullPath =
        ControllerConstants.ADMIN_GAME_SAVES
            + ControllerConstants.AdminGameSave.USER_GAME_SAVES.replace("{username}", username);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<List<GameSave>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
      GenericResponse<List<GameSave>> body = result.getBody();
      gameSaveListStack.push(body.getData());
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update the characteristics of the game save with id (.*) with the following CharacteristicsRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_update_the_characteristics_of_the_game_save_with_id_with_the_following_characteristics_request(
          String saveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);

    CharacteristicsRequest characteristicsRequest = BddUtils.mapToCharacteristicsRequest(row);

    String fullPath =
        ControllerConstants.ADMIN_GAME_SAVES
            + ControllerConstants.AdminGameSave.UPDATE_GAME_SAVE_CHARACTERISTICS.replace(
                "{game_save_id}", saveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<CharacteristicsRequest> request =
          new HttpEntity<>(characteristicsRequest, headers);
      ResponseEntity<GenericResponse<GameSave>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      GenericResponse<GameSave> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update the currencies of the game save with id (.*) with the following CurrencyRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_update_the_currencies_of_the_game_save_with_id_with_the_following_currency_request(
          String saveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);

    CurrencyRequest currencyRequest = BddUtils.mapToCurrencyRequest(row);

    String fullPath =
        ControllerConstants.ADMIN_GAME_SAVES
            + ControllerConstants.AdminGameSave.UPDATE_GAME_SAVE_CURRENCIES.replace(
                "{game_save_id}", saveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<CurrencyRequest> request = new HttpEntity<>(currencyRequest, headers);
      ResponseEntity<GenericResponse<GameSave>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      GenericResponse<GameSave> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update the stages of the game save with id (.*) with the following StageRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_update_the_stages_of_the_game_save_with_id_with_the_following_stage_request(
          String saveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);

    StageRequest stageRequest = BddUtils.mapToStageRequest(row);

    String fullPath =
        ControllerConstants.ADMIN_GAME_SAVES
            + ControllerConstants.AdminGameSave.UPDATE_GAME_SAVE_STAGES.replace(
                "{game_save_id}", saveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<StageRequest> request = new HttpEntity<>(stageRequest, headers);
      ResponseEntity<GenericResponse<GameSave>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      GenericResponse<GameSave> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update the game save with id (.*) with the following AdminGameSaveUpdateRequest$")
  public void when_the_user_requests_the_admin_endpoint_to_update_the_game_save_with_id(
      String saveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);

    AdminGameSaveUpdateRequest updateRequest = BddUtils.mapToAdminGameSaveUpdateRequest(row);

    String fullPath =
        ControllerConstants.ADMIN_GAME_SAVES
            + ControllerConstants.AdminGameSave.GAME_SAVE_ID.replace("{game_save_id}", saveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminGameSaveUpdateRequest> request = new HttpEntity<>(updateRequest, headers);
      ResponseEntity<GenericResponse<GameSave>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      GenericResponse<GameSave> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
