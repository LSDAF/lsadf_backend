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

import static com.lsadf.core.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.admin.application.game.inventory.AdminInventoryController;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.infra.web.dto.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** Step definitions for the admin inventory controller when steps in the BDD scenarios */
@Slf4j(topic = "[ADMIN INVENTORY WHEN STEP DEFINITIONS]")
public class BddAdminInventoryWhenStepDefinitions extends BddLoader {

  @When("^the user requests the admin endpoint to get the inventory for game save with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToGetTheInventoryForGameSaveWithId(
      String gameSaveId) {
    String fullPath =
        AdminApiPathConstants.ADMIN_INVENTORY
            + AdminInventoryController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Set<ItemResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedItemSetResponse());
      ApiResponse<Set<ItemResponse>> body = result.getBody();
      itemResponseSetStack.push(body.data());
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to create a new item in the inventory for game save with id (.*) with the following ItemRequest$")
  public void
      whenTheUserRequestsTheAdminEndpointToCreateANewItemInTheInventoryForGameSaveWithIdWithTheFollowingItemRequest(
          String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

    String fullPath =
        AdminApiPathConstants.ADMIN_INVENTORY
            + AdminInventoryController.Constants.ApiPaths.ITEMS.replace(
                "{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<ItemRequest> request = new HttpEntity<>(itemRequest, headers);
      ResponseEntity<ApiResponse<ItemResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedItemResponse());
      ApiResponse<ItemResponse> body = result.getBody();
      responseStack.push(body);
      if (body.data() != null) {
        var data = body.data();
        itemResponseSetStack.push(Set.of(data));
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update an item with clientId (.*) in the inventory for game save with id (.*) with the following ItemRequest$")
  public void
      whenTheUserRequestsTheAdminEndpointToUpdateAnItemWithClientIdInTheInventoryForGameSaveWithIdWithTheFollowingItemRequest(
          String itemClientId, String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

    String fullPath =
        AdminApiPathConstants.ADMIN_INVENTORY
            + AdminInventoryController.Constants.ApiPaths.CLIENT_ID
                .replace("{game_save_id}", gameSaveId)
                .replace("{client_id}", itemClientId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<ItemRequest> request = new HttpEntity<>(itemRequest, headers);
      ResponseEntity<ApiResponse<ItemResponse>> result =
          testRestTemplate.exchange(url, HttpMethod.PUT, request, buildParameterizedItemResponse());
      ApiResponse<ItemResponse> body = result.getBody();
      responseStack.push(body);
      if (body.data() != null) {
        ItemResponse data = body.data();
        itemResponseSetStack.push(Set.of(data));
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to clear the inventory for game save with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToClearTheInventoryForGameSaveWithId(
      String gameSaveId) {
    String fullPath =
        AdminApiPathConstants.ADMIN_INVENTORY
            + AdminInventoryController.Constants.ApiPaths.GAME_SAVE_ID.replace(
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
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
