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

import static com.lsadf.application.bdd.ParameterizedTypeReferenceUtils.buildParameterizedCurrencyResponse;
import static com.lsadf.application.bdd.ParameterizedTypeReferenceUtils.buildParameterizedVoidResponse;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.bdd.BddUtils;
import com.lsadf.application.controller.game.currency.CurrencyController;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.infra.web.client.keycloak.response.JwtAuthenticationResponse;
import com.lsadf.core.infra.web.controller.ControllerConstants;
import com.lsadf.core.infra.web.requests.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.currency.CurrencyResponse;
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
  @When("^we want to get the currencies for the game save with id (.*)$")
  public void when_we_want_to_get_the_currencies_for_the_game_save_with_id(String gameSaveId) {
    try {
      log.info("Getting currencies for game save with id: {}", gameSaveId);
      Currency currency = this.currencyService.getCurrency(gameSaveId);
      currencyStack.push(currency);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^we want to set the following currencies for the game save with id (.*) with toCache to (.*)$")
  public void when_we_want_to_set_the_currencies_for_the_game_save_with_id_to_with_cache(
      String gameSaveId, boolean toCache, DataTable dataTable) {
    var data = dataTable.asMaps(String.class, String.class);
    assertThat(data).hasSize(1);

    Currency currency = BddUtils.mapToCurrency(data.get(0));

    try {
      log.info("Setting {} for game save with id: {}", currency, gameSaveId);
      this.currencyService.saveCurrency(gameSaveId, currency, toCache);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to get the currencies of the game save with id (.*)$")
  public void when_the_user_requests_the_endpoint_to_get_the_currencies_of_the_game_save_with_id(
      String gameSaveId) {
    String fullPath =
        ControllerConstants.CURRENCY
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
      "^the user requests the endpoint to set the currencies with the following CurrencyRequest for the game save with id (.*)$")
  public void when_the_user_requests_the_endpoint_to_set_the_currencies_of_the_game_save_with_id_to(
      String gameSaveId, DataTable dataTable) {
    var data = dataTable.asMaps(String.class, String.class);
    assertThat(data).hasSize(1);

    CurrencyRequest request = BddUtils.mapToCurrencyRequest(data.get(0));

    String fullPath =
        ControllerConstants.CURRENCY
            + CurrencyController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);

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
