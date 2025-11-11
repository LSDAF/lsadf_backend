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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.bdd.step_definition.then.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the then steps in the BDD scenarios */
@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {

  @Autowired private BddThenCacheStepDefinitions bddThenCacheStepDefinitions;
  @Autowired private BddThenGameSaveStepDefinitions bddThenGameSaveStepDefinitions;
  @Autowired private BddThenGameMailStepDefinitions bddThenGameMailStepDefinitions;
  @Autowired private BddThenGameMailTemplateStepDefinitions bddThenGameMailTemplateStepDefinitions;

  @Autowired
  private com.lsadf.bdd.step_definition.then.BddThenStepDefinitions bddThenCoreStepDefinitions;

  @Autowired private BddThenUserStepDefinitions bddThenUserStepDefinitions;
  @Autowired private BddThenItemStepDefinitions bddThenItemStepDefinitions;
  @Autowired private BddThenGlobalInfoStepDefinitions bddThenGlobalInfoStepDefinitions;

  @Then("^the zset flush pending cache should be empty$")
  public void thenTheZsetFlushPendingCacheShouldBeEmpty() {
    bddThenCacheStepDefinitions.thenTheZsetFlushPendingCacheShouldBeEmpty();
  }

  @Then("^the set flush processing cache should be empty$")
  public void thenTheSetFlushProcessingCacheShouldBeEmpty() {
    bddThenCacheStepDefinitions.thenTheSetFlushProcessingCacheShouldBeEmpty();
  }

  @Then("^the number of game saves should be (.*)$")
  @Transactional(readOnly = true)
  public void thenTheNumberOfGameSavesShouldBe(int expected) {
    bddThenGameSaveStepDefinitions.thenTheNumberOfGameSavesShouldBe(expected);
  }

  @Then("^the response should have the following Boolean (.*)$")
  public void thenTheResponseShouldHaveTheFollowingBoolean(boolean expected) {
    bddThenCoreStepDefinitions.thenTheResponseShouldHaveTheFollowingBoolean(expected);
  }

  @Then("^the response status code should be (.*)$")
  public void thenTheResponseStatusCodeShouldBe(int statusCode) {
    bddThenCoreStepDefinitions.thenResponseStatusCodeShouldBe(statusCode);
  }

  @Then("^the number of users should be (.*)$")
  public void thenTheNumberOfUsersShouldBe(int expected) {
    bddThenUserStepDefinitions.thenTheNumberOfUsersShouldBe(expected);
  }

  @Then("^the response should have the following itemResponses$")
  public void thenTheResponseShouldHaveTheFollowingItemsInTheInventory(DataTable dataTable) {
    bddThenItemStepDefinitions.thenTheResponseShouldHaveTheFollowingItemsInTheInventory(dataTable);
  }

  @Then("^the response should have the following GameSaveResponse$")
  public void thenTheResponseShouldHaveTheFollowingGameSaveResponse(DataTable dataTable) {
    bddThenGameSaveStepDefinitions.thenResponseShouldHaveFollowingGameSaveResponse(dataTable);
  }

  @Then("the response should have the following GlobalInfo")
  public void thenTheResponseShouldHaveTheTollowingGlobalInfo(DataTable dataTable) {
    bddThenGlobalInfoStepDefinitions.thenTheResponseShouldHaveTheTollowingGlobalInfo(dataTable);
  }

  @Then("^the response should have the following GameSaveResponses$")
  public void thenTheResponseShouldHaveTheFollowingGameSaves(DataTable dataTable) {
    bddThenGameSaveStepDefinitions.thenResponseShouldHaveFollowingGameSaves(dataTable);
  }

  @Then("^the response should have the following UserResponses in exact order$")
  public void thenTheResponseShouldHaveTheFollowingUserResponsesInExactOrder(DataTable dataTable) {
    bddThenUserStepDefinitions.thenTheResponseShouldHaveTheFollowingUserResponsesInExactOrder(
        dataTable);
  }

  @Then("^the response should have the following UserResponse$")
  public void thenTheResponseShouldHaveTheFollowingUserResponse(DataTable dataTable) {
    bddThenUserStepDefinitions.thenTheResponseShouldHaveTheFollowingUserResponse(dataTable);
  }

  @Then("^the response should have the following UserResponses$")
  public void thenTheResponseShouldHaveTheFollowingUsers(DataTable dataTable) {
    bddThenUserStepDefinitions.thenTheResponseShouldHaveTheFollowingUsers(dataTable);
  }

  @Then("^the response should have the following GameSaveResponses in exact order$")
  public void thenTheResponseShouldHaveTheFollowingGameSavesInExactOrder(DataTable dataTable) {
    bddThenGameSaveStepDefinitions.thenTheResponseShouldHaveTheFollowingGameSavesInExactOrder(
        dataTable);
  }

  @Then("^the inventory of the game save with id (.*) should be empty$")
  public void thenTheInventoryOfTheGameSaveWithIdShouldBeEmpty(String gameSaveId) {
    bddThenItemStepDefinitions.thenTheInventoryOfTheGameSaveWithIdShouldBeEmpty(gameSaveId);
  }

  @Then("^the response should have the following GameMailAttachments$")
  public void thenTheResponseShouldHaveFollowingGameMailAttachments(DataTable dataTable)
      throws JsonProcessingException {
    bddThenGameMailStepDefinitions.thenResponseShouldHaveFollowingGameMailAttachments(dataTable);
  }

  @Then("^the response should have the following GameMailTemplateResponse$")
  public void thenTheResponseShouldHaveTheFollowingGameMailTemplateResponse(DataTable dataTable) {
    bddThenGameMailTemplateStepDefinitions.shouldHaveTheFollowingGameMailTemplateResponse(
        dataTable);
  }

  @Then("^the response should have the following GameMailTemplateResponses$")
  public void thenTheResponseShouldHaveTheFollowingGameMailTemplateResponses(DataTable dataTable) {
    bddThenGameMailTemplateStepDefinitions.shouldHaveTheFollowingGameMailTemplateResponses(
        dataTable);
  }

  @Then("^the database contains the following game mails$")
  @Transactional(readOnly = true)
  public void thenDbShouldContainGameMails(DataTable dataTable) {
    bddThenGameMailStepDefinitions.thenDbShouldContainGameMails(dataTable);
  }

  @Then("^the database contains the following game mails ignoring the ids$")
  public void thenDbShouldContainGameMailsIgnoringIds(DataTable dataTable) {
    bddThenGameMailStepDefinitions.thenDbShouldContainGameMailsIgnoringId(dataTable);
  }
}
