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

import static com.lsadf.bdd.util.ParameterizedTypeReferenceUtils.buildParameterizedCurrencyResponse;
import static com.lsadf.bdd.util.ParameterizedTypeReferenceUtils.buildParameterizedVoidResponse;
import static com.lsadf.core.infra.web.controller.ParameterConstants.X_GAME_SESSION_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.game.save.currency.CurrencyController;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.currency.CurrencyResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[CURRENCY WHEN STEP DEFINITIONS]")
public class BddCurrencyWhenStepDefinitions extends BddLoader {

  @When("^the user requests the endpoint to get the currencies of the game save with id (.*)$")
  public void whenUserRequestsEndpointToGetCurrencies(String gameSaveId) {
    String fullPath =
        ApiPathConstants.CURRENCY
            + CurrencyController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<CurrencyResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedCurrencyResponse());
      var body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the endpoint to set the currencies with the following CurrencyRequest for the game save with id (.*) and session id (.*)$")
  public void whenUserRequestsEndpointToSetCurrencies(
      String gameSaveId, String sessionId, DataTable dataTable) {
    var data = dataTable.asMaps(String.class, String.class);
    assertThat(data).hasSize(1);

    CurrencyRequest request = BddUtils.mapToCurrencyRequest(data.get(0));

    String fullPath =
        ApiPathConstants.CURRENCY
            + CurrencyController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      headers.set(X_GAME_SESSION_ID, sessionId);

      HttpEntity<CurrencyRequest> httpRequest = new HttpEntity<>(request, headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, httpRequest, buildParameterizedVoidResponse());
      var body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
