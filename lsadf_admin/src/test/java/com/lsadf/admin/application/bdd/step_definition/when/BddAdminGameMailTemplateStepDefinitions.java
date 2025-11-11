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

import static com.lsadf.bdd.util.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.application.bdd.BddLoader;
import com.lsadf.admin.application.constant.AdminApiPathConstants;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailAttachmentRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailTemplateRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailTemplateResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** BDD Step Definitions for Admin Game Mail Template - When Steps */
@Slf4j(topic = "[ADMIN GAME MAIL TEMPLATE WHEN STEP DEFINITIONS]")
public class BddAdminGameMailTemplateStepDefinitions extends BddLoader {

  @Autowired private Stack<List<GameMailAttachment<?>>> gameMailAttachmentListStack;

  @When("^the user requests the admin endpoint to get all the game mail templates$")
  public void whenTheUserRequestsTheAdminEndpointToGetAllTheGameMailTemplates() {
    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL_TEMPLATE;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<List<GameMailTemplateResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameMailTemplateListResponse());
      ApiResponse<List<GameMailTemplateResponse>> body = result.getBody();
      gameMailTemplateResponseListStack.push(body.data());
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to get the game mail template with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToGetTheGameMailTemplateWithId(String templateId) {
    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL_TEMPLATE + "/" + templateId;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GameMailTemplateResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameMailTemplateResponse());
      ApiResponse<GameMailTemplateResponse> body = result.getBody();
      var data = body.data();
      gameMailTemplateResponseStack.push(data);
      if (body.data().attachments() != null) {
        gameMailAttachmentListStack.push(data.attachments());
      } else {
        gameMailAttachmentListStack.push(List.of());
      }
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to create a new game mail template with the following data$")
  public void whenTheUserRequestsCreationNewGameMailTemplate(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    assertThat(rows).hasSize(1);

    Map<String, String> row = rows.get(0);
    GameMailTemplateRequest gameMailTemplateRequest = BddUtils.mapToGameMailTemplateRequest(row);

    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL_TEMPLATE;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<GameMailTemplateRequest> request =
          new HttpEntity<>(gameMailTemplateRequest, headers);
      ResponseEntity<ApiResponse<GameMailTemplateResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, request, buildParameterizedGameMailTemplateResponse());
      ApiResponse<GameMailTemplateResponse> body = result.getBody();
      var data = body.data();
      gameMailTemplateResponseStack.push(data);
      gameMailAttachmentListStack.push(data.attachments() != null ? data.attachments() : List.of());
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the admin endpoint to delete the game mail template with id (.*)$")
  public void whenTheUserRequestsTheAdminEndpointToDeleteTheGameMailTemplateWithId(
      String templateId) {
    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL_TEMPLATE + "/" + templateId;
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
      var body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the admin endpoint to add a new attachment to the game mail template with id (.*) with the following data$")
  public void whenUserRequestsToAddNewAttachmentToGameMailTemplate(
      String templateId, DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    List<GameMailAttachmentRequest<?>> gameMailAttachmentRequests = new ArrayList<>();
    for (Map<String, String> row : rows) {
      GameMailAttachmentRequest<?> gameMailAttachmentRequest =
          BddUtils.mapToGameMailAttachmentRequest(row);
      gameMailAttachmentRequests.add(gameMailAttachmentRequest);
    }

    String fullPath = AdminApiPathConstants.ADMIN_GAME_MAIL_TEMPLATE + "/" + templateId;
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<List<GameMailAttachmentRequest<?>>> request =
          new HttpEntity<>(gameMailAttachmentRequests, headers);
      ResponseEntity<ApiResponse<GameMailTemplateResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.PUT, request, buildParameterizedGameMailTemplateResponse());
      var body = result.getBody();
      var data = body.data();
      gameMailTemplateResponseStack.push(data);
      gameMailAttachmentListStack.push(data.attachments() != null ? data.attachments() : List.of());
      responseStack.push(body);
      log.info("Response: {}", body);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
