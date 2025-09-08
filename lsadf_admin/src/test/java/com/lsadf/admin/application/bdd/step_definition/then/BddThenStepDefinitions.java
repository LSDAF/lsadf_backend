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
package com.lsadf.admin.application.bdd.step_definition.then;

import static com.lsadf.core.bdd.BddFieldConstants.Item.CLIENT_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.infra.valkey.cache.flush.FlushStatus;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.response.game.inventory.ItemResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.dto.response.info.GlobalInfoResponse;
import com.lsadf.core.infra.web.dto.response.user.UserResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the then steps in the BDD scenarios */
@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {

  @Then("^the zset flush pending cache should be empty$")
  public void thenTheZsetFlushPendingCacheShouldBeEmpty() {
    log.info("Checking if flush pending zset is empty...");
    var results =
        stringRedisTemplate.opsForZSet().rangeByScore(FlushStatus.PENDING.getKey(), 0, -1);
    assertThat(results).isEmpty();
  }

  @Then("^the set flush processing cache should be empty$")
  public void thenTheSetFlushProcessingCacheShouldBeEmpty() {
    log.info("Checking if flush processing set is empty...");
    var results = stringRedisTemplate.opsForSet().members(FlushStatus.PROCESSING.getKey());
    assertThat(results).isEmpty();
  }

  @Then("^the number of game saves should be (.*)$")
  @Transactional(readOnly = true)
  public void thenTheNumberOfGameSavesShouldBe(int expected) {
    var list = gameSaveService.getGameSaves();
    assertThat(list).hasSize(expected);
  }

  @Then("^the response should have the following Boolean (.*)$")
  public void thenTheResponseShouldHaveTheFollowingBoolean(boolean expected) {
    boolean actual = (boolean) responseStack.peek().data();
    assertThat(actual).isEqualTo(expected);
  }

  @Then("^the response status code should be (.*)$")
  public void thenTheResponseStatusCodeShouldBe(int statusCode) {
    int actual = responseStack.peek().status();
    assertThat(actual).isEqualTo(statusCode);
  }

  @Then("^the number of users should be (.*)$")
  public void thenTheNumberOfUsersShouldBe(int expected) {
    long actual = userService.getUsers().count();
    assertThat(actual).isEqualTo(expected);
  }

  @Then("^the response should have the following itemResponses$")
  public void thenTheResponseShouldHaveTheFollowingItemsInTheInventory(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    Set<ItemResponse> inventory = itemResponseSetStack.peek();
    for (Map<String, String> row : rows) {
      ItemResponse actual =
          inventory.stream()
              .filter(g -> g.clientId().equals(row.get(CLIENT_ID)))
              .findFirst()
              .orElseThrow();

      ItemResponse expected = BddUtils.mapToItemResponse(row);

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id", "additionalStats")
          .isEqualTo(expected);

      assertThat(actual.additionalStats())
          .contains(expected.additionalStats().toArray(new ItemStatDto[0]));
    }
  }

  @Then("^the response should have the following GameSaveResponse$")
  public void thenTheResponseShouldHaveTheFollowingGameSaveResponse(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    GameSaveResponse actual = (GameSaveResponse) responseStack.peek().data();
    GameSaveResponse expected = BddUtils.mapToGameSaveResponse(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdAt", "updatedAt")
        .ignoringExpectedNullFields()
        .isEqualTo(expected);
  }

  @Then("the response should have the following GlobalInfo")
  public void thenTheResponseShouldHaveTheTollowingGlobalInfo(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    GlobalInfoResponse actual = globalInfoResponseStack.peek();
    GlobalInfoResponse expected = BddUtils.mapToGlobalInfoResponse(row);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^the response should have the following GameSaveResponses$")
  public void thenTheResponseShouldHaveTheFollowingGameSaves(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      var list = gameSaveResponseListStack.peek();
      String id = row.get("id");
      UUID uuid = UUID.fromString(id);
      var actual =
          list.stream().filter(g -> g.metadata().id().equals(uuid)).findFirst().orElseThrow();
      GameSaveResponse expected = BddUtils.mapToGameSaveResponse(row);

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id", "metadata.createdAt", "metadata.updatedAt")
          .isEqualTo(expected);
    }
  }

  @Then("^the response should have the following UserResponses in exact order$")
  public void thenTheResponseShouldHaveTheFollowingUserResponsesInExactOrder(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    List<UserResponse> actual = userResponseListStack.peek();
    List<UserResponse> expected = new ArrayList<>();

    for (Map<String, String> row : rows) {
      UserResponse expectedEntity = BddUtils.mapToUserResponse(row);
      expected.add(expectedEntity);
    }

    assertThat(actual).hasSameSizeAs(expected);

    for (int i = 0; i < actual.size(); i++) {
      UserResponse actualUser = actual.get(i);
      UserResponse expectedUser = expected.get(i);
      assertThat(actualUser)
          .usingRecursiveComparison()
          .ignoringExpectedNullFields()
          .ignoringFields("userRoles", "createdTimestamp")
          .isEqualTo(expectedUser);
    }
  }

  @Then("^the response should have the following UserResponse$")
  public void thenTheResponseShouldHaveTheFollowingUserResponse(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    UserResponse actual = (UserResponse) responseStack.peek().data();
    UserResponse expected = BddUtils.mapToUserResponse(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("userRoles", "createdTimestamp")
        .ignoringExpectedNullFields()
        .isEqualTo(expected);

    expected.userRoles().forEach(role -> assertThat(actual.userRoles()).contains(role));
  }

  @Then("^the response should have the following UserResponses$")
  public void thenTheResponseShouldHaveTheFollowingUsers(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<UserResponse> actual = userResponseListStack.peek();

    for (Map<String, String> row : rows) {
      UserResponse expectedUser = BddUtils.mapToUserResponse(row);
      UserResponse actualUser =
          actual.stream()
              .filter(u -> u.username().equals(expectedUser.username()))
              .findFirst()
              .orElseThrow();

      assertThat(actualUser)
          .usingRecursiveComparison()
          .ignoringExpectedNullFields()
          .ignoringFields("userRoles", "createdTimestamp")
          .isEqualTo(expectedUser);

      expectedUser.userRoles().forEach(role -> assertThat(actualUser.userRoles()).contains(role));
    }
  }

  @Then("^the response should have the following GameSaveResponses in exact order$")
  public void thenTheResponseShouldHaveTheFollowingGameSavesInExactOrder(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<GameSaveResponse> actual = gameSaveResponseListStack.peek();

    List<GameSaveResponse> expected = new ArrayList<>();

    for (Map<String, String> row : rows) {
      GameSaveResponse response = BddUtils.mapToGameSaveResponse(row);
      expected.add(response);
    }

    assertThat(actual).hasSameSizeAs(expected);

    for (int i = 0; i < actual.size(); i++) {
      GameSaveResponse actualGameSaveResponse = actual.get(i);
      GameSaveResponse expectedGameSaveResponse = expected.get(i);
      assertThat(actualGameSaveResponse)
          .usingRecursiveComparison()
          .ignoringFields("metadata.id", "metadata.createdAt", "metadata.updatedAt")
          .isEqualTo(expectedGameSaveResponse);
    }
  }

  @Then("^the inventory of the game save with id (.*) should be empty$")
  public void thenTheInventoryOfTheGameSaveWithIdShouldBeEmpty(String gameSaveId) {
    UUID uuid = UUID.fromString(gameSaveId);
    var results = inventoryService.getInventoryItems(uuid);
    assertThat(results).isEmpty();
  }
}
