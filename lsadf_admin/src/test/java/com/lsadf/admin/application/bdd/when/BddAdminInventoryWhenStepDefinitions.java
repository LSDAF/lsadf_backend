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
package com.lsadf.admin.application.bdd.when;

import static com.lsadf.admin.application.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.admin.application.bdd.BddUtils;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.web.config.auth.JwtAuthentication;
import com.lsadf.core.infra.web.controllers.ControllerConstants;
import com.lsadf.core.infra.web.requests.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.responses.GenericResponse;
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
  public void when_the_user_requests_the_admin_endpoint_to_get_the_inventory_for_game_save_with_id(
      String gameSaveId) {
    String fullPath =
        ControllerConstants.ADMIN_INVENTORIES
            + ControllerConstants.Inventory.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<Set<Item>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedItemSetResponse());
      GenericResponse<Set<Item>> body = result.getBody();
      itemSetStack.push(body.getData());
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to create a new item in the inventory for game save with id (.*) with the following ItemRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_create_a_new_item_in_the_inventory_for_game_save_with_id_with_the_following_item_request(
          String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

    String fullPath =
        ControllerConstants.ADMIN_INVENTORIES
            + ControllerConstants.Inventory.ITEMS.replace("{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<ItemRequest> request = new HttpEntity<>(itemRequest, headers);
      ResponseEntity<GenericResponse<Item>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedItemResponse());
      GenericResponse<Item> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to delete an item with clientId (.*) from the inventory for game save with id (.*)$")
  public void
      when_the_user_requests_the_admin_endpoint_to_delete_an_item_with_client_id_from_the_inventory_for_game_save_with_id(
          String itemClientId, String gameSaveId) {
    String fullPath =
        ControllerConstants.ADMIN_INVENTORIES
            + ControllerConstants.Inventory.CLIENT_ID
                .replace("{game_save_id}", gameSaveId)
                .replace("{client_id}", itemClientId);
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
      GenericResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to update an item with clientId (.*) in the inventory for game save with id (.*) with the following ItemRequest$")
  public void
      when_the_user_requests_the_admin_endpoint_to_update_an_item_with_client_id_in_the_inventory_for_game_save_with_id_with_the_following_item_request(
          String itemClientId, String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

    String fullPath =
        ControllerConstants.ADMIN_INVENTORIES
            + ControllerConstants.Inventory.CLIENT_ID
                .replace("{game_save_id}", gameSaveId)
                .replace("{client_id}", itemClientId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<ItemRequest> request = new HttpEntity<>(itemRequest, headers);
      ResponseEntity<GenericResponse<Item>> result =
          testRestTemplate.exchange(url, HttpMethod.PUT, request, buildParameterizedItemResponse());
      GenericResponse<Item> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to clear the inventory for game save with id (.*)$")
  public void
      when_the_user_requests_the_admin_endpoint_to_clear_the_inventory_for_game_save_with_id(
          String gameSaveId) {
    String fullPath =
        ControllerConstants.ADMIN_INVENTORIES
            + ControllerConstants.Inventory.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
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
      GenericResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
