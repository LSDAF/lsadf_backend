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

import static com.lsadf.admin.application.auth.AdminAuthController.Constants.ApiPaths.*;
import static com.lsadf.core.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.infra.web.dto.request.user.login.UserLoginRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

  @When("the user logs in with the following credentials")
  public void whenTheUserLogsInWithTheFollowingCredentials(DataTable dataTable) {
    try {
      var rows = dataTable.asMaps(String.class, String.class);

      // it should have only one line
      if (rows.size() > 1) {
        throw new IllegalArgumentException("Expected only one row in the DataTable");
      }

      Map<String, String> row = rows.get(0);
      String fullPath = AdminApiPathConstants.AUTH + LOGIN;

      String url = BddUtils.buildUrl(this.serverPort, fullPath);
      UserLoginRequest userLoginRequest = BddUtils.mapToUserLoginRequest(row);

      HttpEntity<UserLoginRequest> request = BddUtils.buildHttpEntity(userLoginRequest);
      ResponseEntity<ApiResponse<JwtAuthenticationResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedJwtAuthenticationResponse());
      var response = result.getBody();
      assertThat(response).isNotNull();
      responseStack.push(response);
      var jwtAuthentication = response.data();
      if (jwtAuthentication != null) {
        jwtAuthenticationResponseStack.push(jwtAuthentication);
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
