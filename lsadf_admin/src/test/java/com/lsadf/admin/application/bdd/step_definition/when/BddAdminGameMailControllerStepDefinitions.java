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

import static com.lsadf.bdd.util.ParameterizedTypeReferenceUtils.buildParameterizedVoidResponse;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.infra.web.dto.request.game.mail.SendGameMailRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.java.en.When;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** BDD Step Definitions for Admin Game Mail Controller - When Steps */
@Slf4j(topic = "[ADMIN GAME MAIL CONTROLLER WHEN STEP DEFINITIONS]")
public class BddAdminGameMailControllerStepDefinitions extends BddLoader {

  @When(
      "^the user requests the admin endpoint to send game mail to all game saves with template with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToSendGameMailToAllGameSavesWithTemplate(
      String templateId) {
    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL + "/send";
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);

      SendGameMailRequest sendGameMailRequest =
          SendGameMailRequest.builder().gameMailTemplateId(UUID.fromString(templateId)).build();

      HttpEntity<SendGameMailRequest> request = new HttpEntity<>(sendGameMailRequest, headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to delete the expired game saves with timestamp set to tomorrow$")
  public void whenTheUserRequestsTheAdminEndpointToDeleteTheExpiredGameSaves() {
    long timestamp = clockService.getClock().instant().plus(1, ChronoUnit.DAYS).getEpochSecond();
    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL + "?expired=" + timestamp;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);

      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to send game mail to game save with id (.*) with template with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToSendGameMailToGameSaveWithTemplate(
      String gameSaveId, String templateId) {
    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL + "/send";
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);

      SendGameMailRequest sendGameMailRequest =
          SendGameMailRequest.builder()
              .gameSaveId(UUID.fromString(gameSaveId))
              .gameMailTemplateId(UUID.fromString(templateId))
              .build();

      HttpEntity<SendGameMailRequest> request = new HttpEntity<>(sendGameMailRequest, headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
