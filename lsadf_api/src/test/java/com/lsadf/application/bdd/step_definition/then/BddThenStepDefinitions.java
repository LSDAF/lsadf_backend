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
package com.lsadf.application.bdd.step_definition.then;


import com.lsadf.application.bdd.BddLoader;
import com.lsadf.bdd.step_definition.then.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/** Step definitions for the then steps in the BDD scenarios */
@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {

  @Autowired
  private com.lsadf.bdd.step_definition.then.BddThenStepDefinitions bddThenCoreStepDefinitions;

  @Autowired private BddThenCacheStepDefinitions bddThenCacheStepDefinitions;
  @Autowired private BddThenCharacteristicsStepDefinitions bddThenCharacteristicsStepDefinitions;
  @Autowired private BddThenCurrencyStepDefinitions bddThenCurrencyStepDefinitions;
  @Autowired private BddThenGameSaveStepDefinitions bddThenGameSaveStepDefinitions;
  @Autowired private BddThenGameSessionStepDefinitions bddThenGameSessionStepDefinitions;
  @Autowired private BddThenItemStepDefinitions bddThenItemStepDefinitions;
  @Autowired private BddThenStageStepDefinitions bddThenStageStepDefinitions;
  @Autowired private BddThenUserInfoStepDefinitions bddThenUserInfoStepDefinitions;
  @Autowired private BddThenUserStepDefinitions bddThenUserStepDefinitions;

  @Then("^the response status code should be (.*)$")
  public void thenResponseStatusCodeShouldBe(int statusCode) {
    bddThenCoreStepDefinitions.thenResponseStatusCodeShouldBe(statusCode);
  }

  @Then("^the response should have the following UserInfo$")
  public void thenResponseShouldHaveFollowingUserInfo(DataTable dataTable) {
    bddThenUserInfoStepDefinitions.thenResponseShouldHaveFollowingUserInfo(dataTable);
  }

  @Then("^the response should have the following StageResponse$")
  public void thenResponseShouldHaveFollowingStageResponse(DataTable dataTable) {
    bddThenStageStepDefinitions.thenResponseShouldHaveFollowingStageResponse(dataTable);
  }

  @Then("^the response should have the following Characteristics$")
  public void thenResponseShouldHaveFollowingCharacteristics(DataTable dataTable) {
    bddThenCharacteristicsStepDefinitions.thenResponseShouldHaveFollowingCharacteristics(dataTable);
  }

  @Then("^the response should have the following CurrencyResponse$")
  public void thenResponseShouldHaveFollowingCurrency(DataTable dataTable) {
    bddThenCurrencyStepDefinitions.thenResponseShouldHaveFollowingCurrency(dataTable);
  }

  @Then("^the response should have the following GameSaveResponse$")
  public void thenResponseShouldHaveFollowingGameSaveResponse(DataTable dataTable) {
    bddThenGameSaveStepDefinitions.thenResponseShouldHaveFollowingGameSaveResponse(dataTable);
  }

  @Then("^the response should have the following GameSaveResponses$")
  public void thenResponseShouldHaveFollowingGameSaves(DataTable dataTable) {
    bddThenGameSaveStepDefinitions.thenResponseShouldHaveFollowingGameSaves(dataTable);
  }

  @Then("^the response should have the following GameSessionResponse$")
  public void thenResponseShouldHaveFollowingGameSessionResponse(DataTable dataTable) {
    bddThenGameSessionStepDefinitions.thenResponseShouldHaveFollowingGameSessionResponse(dataTable);
  }
}
