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
package com.lsadf.application.bdd.step_definition.when;

import static com.lsadf.core.bdd.ParameterizedTypeReferenceUtils.*;

import com.lsadf.application.bdd.BddLoader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

  @Autowired
  private com.lsadf.bdd.step_definition.when.BddWhenStepDefinitions bddCoreWhenStepDefinitions;

  @When("^the user logs in with the following refresh token (.*)$")
  public void whenUserLogsInWithRefreshToken(String refreshToken) {
    bddCoreWhenStepDefinitions.whenUserLogsInWithRefreshToken(refreshToken, serverPort);
  }

  @When("the user logs in with the following credentials")
  public void whenUserLogsInWithCredentials(DataTable dataTable) {
    bddCoreWhenStepDefinitions.whenTheUserLogsInWithTheFollowingCredentials(
        dataTable, this.serverPort);
  }

  @When("the user uses the previously generated refresh token to log in")
  public void whenUserUsesGeneratedRefreshTokenToLogIn() {
    bddCoreWhenStepDefinitions.whenUserUsesGeneratedRefreshTokenToLogIn(serverPort);
  }
}
