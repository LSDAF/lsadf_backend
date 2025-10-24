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
package com.lsadf.admin.application.bdd.step_definition.given;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.core.exception.http.NotFoundException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the given steps in the BDD scenarios */
@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
public class BddGivenStepDefinitions extends BddLoader {

  @Autowired
  private com.lsadf.bdd.step_definition.given.BddGivenStepDefinitions commonBddGivenStepDefinitions;

  @Given("^the following items to the inventory of the game save with id (.*)$")
  public void givenTheFollowingItemsInInventory(String gameSaveId, DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingItemsInInventory(gameSaveId, dataTable);
  }

  @Given("^the BDD engine is ready$")
  public void givenTheBddEngineIsReady() {
    commonBddGivenStepDefinitions.givenBddEngineIsReady();
  }

  @Given("^the time clock set to the present$")
  public void givenTheTimeClockSetToThePresent() {
    commonBddGivenStepDefinitions.givenTimeClockSetToPresent();
  }

  @Given("^the time clock set to the following value (.*)$")
  public void givenTheTimeClockSetToTheFollowingValue(String time) {
    commonBddGivenStepDefinitions.givenTheTimeClockSetToTheFollowingValue(time);
  }

  @Given("^the cache is enabled$")
  public void givenTheCacheIsEnabled() {
    commonBddGivenStepDefinitions.givenCacheIsEnabled();
  }

  @Given("^a clean database$")
  public void givenIHaveACleanDatabase() throws NotFoundException {
    commonBddGivenStepDefinitions.givenCleanDatabase();
  }

  @Given("^the following game saves$")
  public void givenIHaveTheFollowingGameSaves(DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingGameSaves(dataTable);
  }

  @Given("^the following (.*) entries in cache$")
  public void givenTheFollowingCacheEntriesInCache(String cacheType, DataTable dataTable) {
    commonBddGivenStepDefinitions.givenTheFollowingCacheEntriesInCache(cacheType, dataTable);
  }

  @Given("^the following game email templates$")
  @Transactional
  public void givenFollowingGameEmailTemplates(DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingGameEmailTemplates(dataTable);
  }

  @Given("^the following game email template attachments$")
  @Transactional
  public void givenFollowingGameEmailTemplateAttachments(DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingGameEmailTemplateAttachments(dataTable);
  }

  @Given("^the following game emails$")
  @Transactional
  public void givenFollowingGameEmails(DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingGameEmails(dataTable);
  }
}
