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
import static com.lsadf.application.controller.game.inventory.InventoryController.Constants.ApiPaths.CLIENT_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.bdd.BddUtils;
import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.game.inventory.InventoryController;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.inventory.item.ItemEntity;
import com.lsadf.core.infra.web.request.game.inventory.ItemRequest;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.response.jwt.JwtAuthenticationResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[INVENTORY WHEN STEP DEFINITIONS]")
public class BddInventoryWhenStepDefinitions extends BddLoader {
  @Given("^the following items to the inventory of the game save with id (.*)$")
  @Transactional
  public void given_the_following_items(String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    InventoryEntity inventoryEntity;

    Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);
    if (optionalInventoryEntity.isEmpty()) {
      throw new NotFoundException("Inventory with id: " + gameSaveId + " not found.");
    } else {
      inventoryEntity = optionalInventoryEntity.get();
    }

    log.info("Creating items...");
    rows.forEach(
        row -> {
          ItemEntity itemEntity = BddUtils.mapToItemEntity(row);
          inventoryEntity.getItems().add(itemEntity);
        });

    inventoryRepository.save(inventoryEntity);

    log.info("Items created");
  }

  @When("^the user requests the endpoint to get the inventory of the game save with id (.*)$")
  public void when_the_user_requests_the_endpoint_to_get_the_inventory_of_the_game_save_with_id(
      String gameSaveId) {
    String fullPath =
        ApiPathConstants.INVENTORY
            + InventoryController.Constants.ApiPaths.GAME_SAVE_ID.replace(
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
      "the user requests the endpoint to create an item in the inventory of the game save with id (.*) with the following ItemCreationRequest$")
  public void
      when_the_user_requests_the_endpoint_to_create_an_item_in_the_inventory_of_the_game_save_with_id_with_the_following_item_creation_request(
          String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

    String fullPath =
        ApiPathConstants.INVENTORY
            + InventoryController.Constants.ApiPaths.ITEMS.replace("{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<ItemRequest> request = new HttpEntity<>(itemRequest, headers);
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
      "the user requests the endpoint to delete an item with client id (.*) in the inventory of the game save with id (.*)$")
  public void
      when_the_user_requests_the_endpoint_to_delete_an_item_with_id_in_the_inventory_of_the_game_save_with_id(
          String clientId, String gameSaveId) {
    String fullPath =
        ApiPathConstants.INVENTORY
            + CLIENT_ID.replace("{game_save_id}", gameSaveId).replace("{client_id}", clientId);
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

  @When(
      "the user requests the endpoint to update an item with client id (.*) in the inventory of the game save with id (.*) with the following ItemUpdateRequest$")
  public void
      when_the_user_requests_the_endpoint_to_update_an_item_in_the_inventory_of_the_game_save_with_id_with_the_following_item_update_request(
          String clientId, String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    ItemRequest itemRequest = BddUtils.mapToItemRequest(row);

    String fullPath =
        ApiPathConstants.INVENTORY
            + InventoryController.Constants.ApiPaths.CLIENT_ID
                .replace("{game_save_id}", gameSaveId)
                .replace("{client_id}", clientId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<ItemRequest> request = new HttpEntity<>(itemRequest, headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(url, HttpMethod.PUT, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @Then("^the inventory of the game save with id (.*) should be empty$")
  public void when_the_inventory_of_the_game_save_with_id_should_be_empty(String gameSaveId) {
    try {
      Set<Item> inventory = inventoryService.getInventoryItems(gameSaveId);

      assertThat(inventory).isNotNull();
      assertThat(inventory).isEmpty();
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @Then("^the response should have the following itemResponses$")
  public void then_the_response_should_have_the_following_items_in_the_inventory(
      DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    Set<ItemResponse> inventory = itemResponseSetStack.peek();
    for (Map<String, String> row : rows) {
      ItemResponse actual =
          inventory.stream().filter(g -> g.id().equals(row.get("id"))).findFirst().orElseThrow();

      Item expected = BddUtils.mapToItem(row);

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id", "createdAt", "updatedAt")
          .isEqualTo(expected);
    }
  }
}
