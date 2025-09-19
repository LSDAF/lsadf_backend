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

import static com.lsadf.core.bdd.ParameterizedTypeReferenceUtils.buildParameterizedGameSessionResponse;

import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.game.session.GameSessionController;
import com.lsadf.core.bdd.BddUtils;
import com.lsadf.core.infra.web.controller.ParameterConstants;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.session.GameSessionResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Slf4j(topic = "[GAME SESSION WHEN STEP DEFINITIONS]")
public class BddGameSessionWhenStepDefinitions extends BddLoader {

  @When("^the user requests the endpoint to generate a new session for the game save with id (.*)$")
  public void whenUserRequestsEndpointToGenerateANewGameSession(String gameSaveId) {
    String fullPath =
        ApiPathConstants.GAME_SESSION + "?" + ParameterConstants.GAME_SAVE_ID + "=" + gameSaveId;

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GameSessionResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameSessionResponse());
      ApiResponse<GameSessionResponse> body = result.getBody();
      gameSessionResponseStack.push(body.data());
      responseStack.push(body);
      log.info("Response: " + body.data());
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to refresh his current game session with id (.*)$")
  public void whenUserRequestsEndpointToRefreshGameSessionEndTime(String gameSessionId) {
    String fullPath =
        ApiPathConstants.GAME_SESSION
            + GameSessionController.Constants.ApiPaths.GAME_SESSION_ID.replace(
                "{game_session_id}", gameSessionId);

    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GameSessionResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.PATCH, request, buildParameterizedGameSessionResponse());
      ApiResponse<GameSessionResponse> body = result.getBody();
      gameSessionResponseStack.push(body.data());
      responseStack.push(body);
      log.info("Response: " + body.data());
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
