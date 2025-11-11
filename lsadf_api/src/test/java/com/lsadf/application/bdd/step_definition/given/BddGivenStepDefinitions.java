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
package com.lsadf.application.bdd.step_definition.given;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.core.exception.http.NotFoundException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/** Step definitions for the given steps in the BDD scenarios */
@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
public class BddGivenStepDefinitions extends BddLoader {

  @Autowired
  com.lsadf.bdd.step_definition.given.BddGivenStepDefinitions commonBddGivenStepDefinitions;

  @Given("^the BDD engine is ready$")
  public void givenBddEngineIsReady() {
    commonBddGivenStepDefinitions.givenBddEngineIsReady();
  }

  @Given("^the time clock set to the present$")
  public void givenTimeClockSetToPresent() {
    commonBddGivenStepDefinitions.givenTimeClockSetToPresent();
  }

  @Given("^the cache is enabled$")
  public void givenCacheIsEnabled() {
    commonBddGivenStepDefinitions.givenCacheIsEnabled();
  }

  @Given("^the cache is disabled$")
  public void givenCacheIsDisabled() {
    commonBddGivenStepDefinitions.givenCacheIsDisabled();
  }

  @Given("^a clean database$")
  @Transactional
  public void givenCleanDatabase() throws NotFoundException {
    commonBddGivenStepDefinitions.givenCleanDatabase();
  }

  @Given("^the following game sessions$")
  @Transactional
  public void givenFollowingGameSessions(DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingGameSessions(dataTable);
  }

  @Given("^the following game saves$")
  @Transactional
  public void givenFollowingGameSaves(DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingGameSaves(dataTable);
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

  @Given("^the following (.*) entries in cache$")
  public void givenFollowingCacheEntries(String cacheType, DataTable dataTable) {
    commonBddGivenStepDefinitions.givenFollowingCacheEntries(cacheType, dataTable);
  }
}
