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
package com.lsadf.admin.bdd.then;

import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.bdd.BddLoader;
import com.lsadf.admin.bdd.BddUtils;
import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.core.exceptions.http.ForbiddenException;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.models.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import jakarta.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the then steps in the BDD scenarios */
@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {

  @Then("^I should return the following game saves$")
  public void then_i_should_return_the_following_game_saves(DataTable dataTable)
      throws NotFoundException {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<GameSave> actual = gameSaveListStack.peek();

    for (Map<String, String> row : rows) {
      GameSave expectedGameSave = BddUtils.mapToGameSave(row);
      GameSave actualGameSave =
          actual.stream()
              .filter(g -> g.getId().equals(expectedGameSave.getId()))
              .findFirst()
              .orElseThrow();

      assertThat(actualGameSave)
          .usingRecursiveComparison()
          .ignoringFields("id", "createdAt", "updatedAt")
          .ignoringExpectedNullFields()
          .isEqualTo(expectedGameSave);
    }
  }

  @Then("^I should return the following game save entities$")
  public void then_i_should_return_the_following_game_save_entities(DataTable dataTable)
      throws NotFoundException {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<GameSaveEntity> actual = gameSaveEntityListStack.peek();

    for (Map<String, String> row : rows) {
      GameSaveEntity expected = BddUtils.mapToGameSaveEntity(row);
      GameSaveEntity gameSave =
          actual.stream()
              .filter(
                  g -> {
                    if (expected.getId() == null) {
                      return g.getUserEmail().equals(expected.getUserEmail());
                    }
                    return g.getId().equals(expected.getId());
                  })
              .findFirst()
              .orElseThrow();

      long gold = gameSave.getCurrencyEntity().getGoldAmount();
      long expectedGold = expected.getCurrencyEntity().getGoldAmount();
      assertThat(gold).isEqualTo(expectedGold);
    }
  }

  @Then("^I should return true$")
  public void then_i_should_return_true() {
    boolean actual = booleanStack.peek();
    assertThat(actual).isTrue();
  }

  @Then("^the number of game saves should be (.*)$")
  @Transactional(readOnly = true)
  public void then_the_number_of_game_saves_should_be(int expected) {
    long actual = gameSaveService.getGameSaves().count();
    assertThat(actual).isEqualTo(expected);
  }

  @Then("^I should return false$")
  public void then_i_should_return_false() {
    boolean actual = booleanStack.peek();
    assertThat(actual).isFalse();
  }

  @Then("^the response should have the following Boolean (.*)$")
  public void then_the_response_should_have_the_following_boolean(boolean expected) {
    boolean actual = (boolean) responseStack.peek().getData();
    assertThat(actual).isEqualTo(expected);
  }

  @Then("^the response status code should be (.*)$")
  public void then_the_response_status_code_should_be(int statusCode) {
    int actual = responseStack.peek().getStatus();
    assertThat(actual).isEqualTo(statusCode);
  }

  @Then("^the number of users should be (.*)$")
  public void then_the_number_of_users_should_be(int expected) {
    long actual = userService.getUsers().count();
    assertThat(actual).isEqualTo(expected);
  }

  @Then("^the response should have the following UserInfo$")
  public void then_the_response_should_have_the_following_user_info(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    UserInfo actual = (UserInfo) responseStack.peek().getData();
    UserInfo expected = BddUtils.mapToUserInfo(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdAt", "updatedAt", "roles")
        .isEqualTo(expected);

    assertThat(actual.getRoles()).containsAll(expected.getRoles());
  }

  @Then("^I should have no game save entries in DB$")
  public void then_i_should_have_no_game_save_entries_in_db() {
    assertThat(gameSaveRepository.count()).isZero();
  }

  @Then("^I should have no characteristics entries in DB$")
  public void then_i_should_have_no_characteristics_entries_in_db() {
    assertThat(characteristicsRepository.count()).isZero();
  }

  @Then("^I should have no currency entries in DB$")
  public void then_i_should_have_no_currency_entries_in_db() {
    assertThat(currencyRepository.count()).isZero();
  }

  @Then("^I should throw a NotFoundException$")
  public void then_i_should_throw_a_not_found_exception() {
    Exception exception = exceptionStack.peek();
    assertThat(exception).isInstanceOf(NotFoundException.class);
  }

  @Then("^I should throw a IllegalArgumentException$")
  public void then_i_should_throw_a_illegal_argument_exception() {
    Exception exception = exceptionStack.peek();
    assertThat(exception).isInstanceOf(IllegalArgumentException.class);
  }

  @Then("^the characteristics should be the following$")
  public void then_the_characteristics_amount_should_be(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    Characteristics expected = BddUtils.mapToCharacteristics(row);

    Characteristics actual = characteristicsStack.peek();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^the currency should be the following$")
  public void then_the_currency_amount_should_be(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    Currency expected = BddUtils.mapToCurrency(row);

    Currency actual = currencyStack.peek();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^I should throw a ForbiddenException$")
  public void then_i_should_throw_a_forbidden_exception() {
    Exception exception = exceptionStack.peek();
    assertThat(exception).isInstanceOf(ForbiddenException.class);
  }

  @Then("^the response should have the following Stage$")
  public void then_the_response_should_have_the_following_stage(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    Stage expected = BddUtils.mapToStage(row);
    Stage actual = (Stage) responseStack.peek().getData();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^the response should have the following Characteristics$")
  public void then_the_response_should_have_the_following_characteristics(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    Characteristics expected = BddUtils.mapToCharacteristics(row);
    Characteristics actual = (Characteristics) responseStack.peek().getData();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^the response should have the following Currency$")
  public void then_the_response_should_have_the_following_currency(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    Currency expected = BddUtils.mapToCurrency(row);
    Currency actual = (Currency) responseStack.peek().getData();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^I should return the following GlobalInfo$")
  public void then_i_should_return_the_following_global_info(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    GlobalInfo actual = globalInfoStack.peek();
    GlobalInfo expected = BddUtils.mapToGlobalInfo(row);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("^I should throw no Exception$")
  public void then_i_should_throw_no_exception() {
    assertThat(exceptionStack).isEmpty();
  }

  @Then("^the token from the response should not be null$")
  public void then_the_token_from_the_response_should_not_be_null() {
    JwtAuthentication jwtAuthentication = (JwtAuthentication) responseStack.peek().getData();
    assertThat(jwtAuthentication.getAccessToken()).isNotNull();
  }

  @Then("^the refresh token from the response should not be null$")
  public void then_the_refresh_token_from_the_response_should_not_be_null() {
    JwtAuthentication jwtAuthentication = (JwtAuthentication) responseStack.peek().getData();
    assertThat(jwtAuthentication.getRefreshToken()).isNotNull();
  }

  @Then("^the response should have the following GameSave$")
  public void then_the_response_should_have_the_following_game_save(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    GameSave actual = (GameSave) responseStack.peek().getData();
    GameSave expected = BddUtils.mapToGameSave(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdAt", "updatedAt")
        .ignoringExpectedNullFields()
        .isEqualTo(expected);
  }

  @Then("the response should have the following GlobalInfo")
  public void then_the_response_should_have_the_tollowing_global_info(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    GlobalInfo actual = (GlobalInfo) responseStack.peek().getData();
    GlobalInfo expected = BddUtils.mapToGlobalInfo(row);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Then("the response should have the following GameSaves")
  public void then_the_response_should_have_the_following_game_saves(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      GameSave actual =
          gameSaveListStack.peek().stream()
              .filter(g -> g.getId().equals(row.get("id")))
              .findFirst()
              .orElseThrow();
      GameSave expected = BddUtils.mapToGameSave(row);

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id", "createdAt", "updatedAt")
          .isEqualTo(expected);
    }
  }

  @Then("^the response should have the following Users in exact order$")
  public void then_the_response_should_have_the_following_users_in_exact_order(
      DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    List<User> actual = userListStack.peek();
    List<User> expected = new ArrayList<>();

    for (Map<String, String> row : rows) {
      User expectedEntity = BddUtils.mapToUser(row);
      expected.add(expectedEntity);
    }

    assertThat(actual).hasSameSizeAs(expected);

    for (int i = 0; i < actual.size(); i++) {
      User actualUser = actual.get(i);
      User expectedUser = expected.get(i);
      assertThat(actualUser)
          .usingRecursiveComparison()
          .ignoringExpectedNullFields()
          .ignoringFields("userRoles", "createdTimestamp")
          .isEqualTo(expectedUser);
    }
  }

  @Then("^the response should have the following User$")
  public void then_the_response_should_have_the_following_user(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);

    User actual = (User) responseStack.peek().getData();
    User expected = BddUtils.mapToUser(row);

    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("userRoles", "createdTimestamp")
        .ignoringExpectedNullFields()
        .isEqualTo(expected);

    expected.getUserRoles().forEach(role -> assertThat(actual.getUserRoles()).contains(role));
  }

  @Then("^the response should have the following Users$")
  public void then_the_response_should_have_the_following_users(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<User> actual = userListStack.peek();

    for (Map<String, String> row : rows) {
      User expectedUser = BddUtils.mapToUser(row);
      User actualUser =
          actual.stream()
              .filter(u -> u.getUsername().equals(expectedUser.getUsername()))
              .findFirst()
              .orElseThrow();

      assertThat(actualUser)
          .usingRecursiveComparison()
          .ignoringExpectedNullFields()
          .ignoringFields("userRoles", "createdTimestamp")
          .isEqualTo(expectedUser);

      expectedUser
          .getUserRoles()
          .forEach(role -> assertThat(actualUser.getUserRoles()).contains(role));
    }
  }

  @Then("^the response should have the following GameSaves in exact order$")
  public void then_the_response_should_have_the_following_game_saves_in_exact_order(
      DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<GameSave> actual = gameSaveListStack.peek();

    List<GameSave> expected = new ArrayList<>();

    for (Map<String, String> row : rows) {
      GameSave expectedEntity = BddUtils.mapToGameSave(row);
      expected.add(expectedEntity);
    }

    assertThat(actual).hasSameSizeAs(expected);

    for (int i = 0; i < actual.size(); i++) {
      GameSave actualGameSave = actual.get(i);
      GameSave expectedGameSave = expected.get(i);
      assertThat(actualGameSave)
          .usingRecursiveComparison()
          .ignoringFields("id", "createdAt", "updatedAt")
          .isEqualTo(expectedGameSave);
    }
  }

  @Then("^an email should have been sent to (.*)$")
  public void then_en_email_should_have_been_sent_to(String email) throws MessagingException {
    var mime = mimeMessageStack.peek();
    assertThat(mime).isNotNull();

    String actual = mime.getAllRecipients()[0].toString();
    assertThat(actual).isEqualTo(email);
  }

  @Then("^the response should have the following items in inventory$")
  public void then_the_response_should_have_the_following_items_in_inventory(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    Inventory inventory = inventoryStack.peek();
    assertThat(inventory).isNotNull();
    assertThat(inventory.getItems()).isNotNull();

    for (Map<String, String> row : rows) {
      Item expected = BddUtils.mapToItem(row);

      Item actual =
          inventory.getItems().stream()
              .filter(item -> item.getClientId().equals(expected.getClientId()))
              .findFirst()
              .orElseThrow(
                  () ->
                      new AssertionError(
                          "Item with clientId "
                              + expected.getClientId()
                              + " not found in inventory"));

      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id", "createdAt", "updatedAt")
          .isEqualTo(expected);
    }
  }

  @Then("^the response should have the following item$")
  public void then_the_response_should_have_the_following_item(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    Item expected = BddUtils.mapToItem(row);

    Item actual = (Item) responseStack.peek().getData();
    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdAt", "updatedAt")
        .isEqualTo(expected);
  }
}
