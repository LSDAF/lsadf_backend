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
import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.game.save.game_save.GameSaveController;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.request.game.save.update.GameSaveNicknameUpdateRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.GameSaveResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[GAME SAVE WHEN STEP DEFINITIONS]")
public class BddWhenGameSaveStepDefinitions extends BddLoader {

  @When("^the user requests the endpoint to generate a GameSave$")
  public void whenUserRequestsEndpointToGenerateGameSave() {
    String fullPath = ApiPathConstants.GAME_SAVE + GameSaveController.Constants.ApiPaths.GENERATE;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(jwtAuthenticationResponse.accessToken());
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GameSaveResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
      var body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the endpoint to update a GameSave with id (.*) with the following GameSaveNicknameUpdateRequest$")
  public void whenUserRequestsEndpointToUpdateGameSave(String gameSaveId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    // it should have only one line
    if (rows.size() > 1) {
      throw new IllegalArgumentException("Expected only one row in the DataTable");
    }

    Map<String, String> row = rows.get(0);
    GameSaveNicknameUpdateRequest updateRequest = BddUtils.mapToGameSaveUpdateUserRequest(row);

    String fullPath = ApiPathConstants.GAME_SAVE + "/" + gameSaveId + "/nickname";

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<GameSaveNicknameUpdateRequest> request = new HttpEntity<>(updateRequest, headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to get his GameSaves$")
  public void whenUserRequestsEndpointToGetGameSaves() {
    String fullPath = ApiPathConstants.GAME_SAVE + GameSaveController.Constants.ApiPaths.ME;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {

      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<List<GameSaveResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
      ApiResponse<List<GameSaveResponse>> body = result.getBody();
      gameSaveResponseListStack.push(body.data());
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
