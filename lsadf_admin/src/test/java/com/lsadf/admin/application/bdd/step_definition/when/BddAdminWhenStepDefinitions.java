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
package com.lsadf.admin.application.bdd.step_definition.when;

import static com.lsadf.admin.application.cache.AdminCacheController.Constants.ApiPaths.*;
import static com.lsadf.admin.application.search.AdminSearchController.Constants.ApiPaths.SEARCH_GAME_SAVES;
import static com.lsadf.admin.application.search.AdminSearchController.Constants.ApiPaths.SEARCH_USERS;
import static com.lsadf.admin.application.user.AdminUserController.Constants.ApiPaths.USER_ID;
import static com.lsadf.core.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.game.AdminGameSaveController;
import com.lsadf.admin.application.user.AdminUserController;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.infra.web.request.common.Filter;
import com.lsadf.core.infra.web.request.game.save.creation.AdminGameSaveCreationRequest;
import com.lsadf.core.infra.web.request.game.save.update.AdminGameSaveUpdateRequest;
import com.lsadf.core.infra.web.request.search.SearchRequest;
import com.lsadf.core.infra.web.request.user.creation.AdminUserCreationRequest;
import com.lsadf.core.infra.web.request.user.update.AdminUserUpdateRequest;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.response.jwt.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.response.user.UserResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
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

  @When("^the user requests the admin endpoint to get the global info$")
  public void whenTheUserRequestsTheAdminEndpointForGlobalInfo() {
    String fullPath = AdminApiPathConstants.ADMIN_GLOBAL_INFO;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GlobalInfoResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGlobalInfoDtoResponse());
      ApiResponse<GlobalInfoResponse> body = result.getBody();
      globalInfoResponseStack.push(body.data());
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get all the users ordered by (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToGetAllTheUsersOrderedBy(String orderBy) {
    String fullPath = AdminApiPathConstants.ADMIN_USER + "?order_by=" + orderBy;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<List<UserResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedUserResponseList());
      ApiResponse<List<UserResponse>> body = result.getBody();
      userResponseListStack.push(body.data());
      responseStack.push(body);

      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get all the game saves ordered by (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToGetAllSaveGamesOrderedBy(String orderBy) {
    String fullPath = AdminApiPathConstants.ADMIN_GAME_SAVE + "?order_by=" + orderBy;
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

  @When("^the user requests the admin endpoint to delete the user with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToDeleteTheUserWithId(String userId) {
    String fullPath =
        AdminApiPathConstants.ADMIN_USER
            + AdminUserController.Constants.ApiPaths.USER_ID.replace("{user_id}", userId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to delete the game save with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToDeleteTheGameSaveWithId(String gameSaveId) {
    String fullPath =
        AdminApiPathConstants.ADMIN_GAME_SAVE
            + AdminGameSaveController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get the user with the following id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToGetTheUserWithTheFollowingId(String userId) {
    String fullPath = AdminApiPathConstants.ADMIN_USER + USER_ID.replace("{user_id}", userId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<UserResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParamaterizedUserDtoResponse());
      ApiResponse<UserResponse> body = result.getBody();
      userResponseListStack.push(Collections.singletonList(body.data()));
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When("^the user requests the admin endpoint to get a game save with the following id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToGetTheGameSaveWithTheFollowingId(
      String gameSaveId) {
    String fullPath =
        AdminApiPathConstants.ADMIN_GAME_SAVE
            + AdminGameSaveController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GameSaveResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameSaveResponse());
      ApiResponse<GameSaveResponse> body = result.getBody();
      gameSaveResponseListStack.push(Collections.singletonList(body.data()));
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When("^the user requests the admin endpoint to get the user with the following username (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToGetTheUserWithTheFollowingUsername(
      String username) {
    String fullPath =
        AdminApiPathConstants.ADMIN_USER
            + AdminUserController.Constants.ApiPaths.USERNAME.replace("{username}", username);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<UserResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParamaterizedUserDtoResponse());
      ApiResponse<UserResponse> body = result.getBody();
      userResponseListStack.push(Collections.singletonList(body.data()));
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
      whenTheUserRequestsTheAdminEndpointToSearchUsersOrderedByWithTheFollowingSearchRequest(
          String orderBy, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath = AdminApiPathConstants.ADMIN_SEARCH + SEARCH_USERS + "?order_by=" + orderBy;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<Filter> filters = rows.stream().map(BddUtils::mapToFilter).toList();

    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<SearchRequest> request = new HttpEntity<>(new SearchRequest(filters), headers);
      ResponseEntity<ApiResponse<List<UserResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedUserResponseList());
      ApiResponse<List<UserResponse>> body = result.getBody();
      userResponseListStack.push(body.data());
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
      whenTheUserRequestsTheAdminEndpointToSearchGameSavesOrderedByWithTheFollowingSearchRequest(
          String orderBy, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath =
        AdminApiPathConstants.ADMIN_SEARCH + SEARCH_GAME_SAVES + "?order_by=" + orderBy;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<Filter> filters = rows.stream().map(BddUtils::mapToFilter).toList();

    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<SearchRequest> request = new HttpEntity<>(new SearchRequest(filters), headers);
      ResponseEntity<ApiResponse<List<GameSaveResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveListResponse());
      ApiResponse<List<GameSaveResponse>> body = result.getBody();
      gameSaveResponseListStack.push(body.data());
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update the user with id (.*) with the following AdminUserUpdateRequest$")
  public void whenTheUserRequestsTheAdminEndpointToUpdateTheUserWithId(
      String id, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    AdminUserUpdateRequest adminUserUpdateRequest =
        BddUtils.mapToAdminUserUpdateRequest(rows.get(0));

    String fullPath =
        AdminApiPathConstants.ADMIN_USER
            + AdminUserController.Constants.ApiPaths.USER_ID.replace("{user_id}", id);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminUserUpdateRequest> request =
          new HttpEntity<>(adminUserUpdateRequest, headers);
      ResponseEntity<ApiResponse<UserResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParamaterizedUserDtoResponse());
      ApiResponse<UserResponse> body = result.getBody();
      userResponseListStack.push(Collections.singletonList(body.data()));
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
      whenTheUserRequestsTheAdminEndpointToCreateANewUserWithTheFollowingAdminUserCreationRequest(
          DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath = AdminApiPathConstants.ADMIN_USER;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<AdminUserCreationRequest> requests =
        rows.stream().map(BddUtils::mapToAdminUserCreationRequest).toList();

    assertThat(requests).hasSize(1);

    AdminUserCreationRequest adminRequest = requests.get(0);

    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminUserCreationRequest> request = new HttpEntity<>(adminRequest, headers);
      ResponseEntity<ApiResponse<UserResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParamaterizedUserDtoResponse());
      ApiResponse<UserResponse> body = result.getBody();
      userResponseListStack.push(Collections.singletonList(body.data()));
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
      log.error("Error: {}", e);
    }
  }

  @When("^the user requests the admin endpoint to toggle the cache status$")
  public void whenTheUserRequestsTheAdminEndpointToToggleTheCacheStatus() {
    String fullPath = AdminApiPathConstants.ADMIN_CACHE + TOGGLE;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Boolean>> result =
          testRestTemplate.exchange(
              url, HttpMethod.PUT, request, buildParameterizedBooleanResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get the cache status$")
  public void whenTheUserRequestsTheAdminEndpointToGetTheCacheStatus() {
    String fullPath = AdminApiPathConstants.ADMIN_CACHE + ENABLED;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Boolean>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedBooleanResponse());
      responseStack.push(result.getBody());
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to flush and clear the cache$")
  public void whenTheUserRequestsTheAdminEndpointToClearTheCache() {
    String fullPath = AdminApiPathConstants.ADMIN_CACHE + FLUSH;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Void>> result =
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
      whenTheUserRequestsTheAdminEndpointToCreateANewGameSaveWithTheFollowingGameSaveCreationRequest(
          DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    String fullPath = AdminApiPathConstants.ADMIN_GAME_SAVE;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);

    List<AdminGameSaveCreationRequest> requests =
        rows.stream().map(BddUtils::mapToAdminGameSaveCreationRequest).toList();

    assertThat(requests).hasSize(1);

    AdminGameSaveCreationRequest adminRequest = requests.get(0);

    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminGameSaveCreationRequest> request = new HttpEntity<>(adminRequest, headers);
      ResponseEntity<ApiResponse<GameSaveResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      ApiResponse<GameSaveResponse> body = result.getBody();
      gameSaveResponseListStack.push(Collections.singletonList(body.data()));
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to get all the game saves of the user with the following username (.*)$")
  public void
      whenTheUserRequestsTheAdminEndpointToGetAllTheGameSavesOfTheUserWithTheFollowingUsername(
          String username) {
    String fullPath =
        AdminApiPathConstants.ADMIN_GAME_SAVE
            + AdminGameSaveController.Constants.ApiPaths.USER_GAME_SAVES.replace(
                "{username}", username);
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

  @When(
      "^the user requests the admin endpoint to update the game save with id (.*) with the following GameSaveUpdateRequest$")
  public void whenTheUserRequestsTheAdminEndpointToUpdateTheGameSaveWithId(
      String saveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);

    AdminGameSaveUpdateRequest updateRequest = BddUtils.mapToAdminGameSaveUpdateRequest(row);

    String fullPath =
        AdminApiPathConstants.ADMIN_GAME_SAVE
            + AdminGameSaveController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", saveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<AdminGameSaveUpdateRequest> request = new HttpEntity<>(updateRequest, headers);
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
}
