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

import static com.lsadf.bdd.util.ParameterizedTypeReferenceUtils.*;
import static com.lsadf.core.infra.web.controller.ParameterConstants.X_GAME_SESSION_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.application.bdd.BddLoader;
import com.lsadf.application.controller.constant.ApiPathConstants;
import com.lsadf.application.controller.game.mail.GameMailController;
import com.lsadf.bdd.util.BddUtils;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailResponse;
import com.lsadf.core.infra.web.dto.response.jwt.JwtAuthenticationResponse;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[GAME MAIL WHEN STEP DEFINITIONS]")
public class BddWhenGameMailStepDefinitions extends BddLoader {

  @When("^the user gets the mails of his game save with id (.*)$")
  public void whenGetsAllMailsWithGameSaveId(String gameSaveIdStr) {
    String fullPath =
        ApiPathConstants.GAME_MAIL
            + GameMailController.Constants.ApiPaths.GAME_SAVE_ID.replace(
                "{game_save_id}", gameSaveIdStr);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<List<GameMailResponse>>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameMailResponseList());
      ApiResponse<List<GameMailResponse>> body = result.getBody();
      gameMailResponseListStack.push(body.data());
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user gets the content of the game mail with id (.*)$")
  public void whenGetsMailWithId(String mailIdStr) {
    String fullPath =
        ApiPathConstants.GAME_MAIL
            + GameMailController.Constants.ApiPaths.GAME_MAIL_ID.replace(
                "{game_mail_id}", mailIdStr);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<GameMailResponse>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedGameMailResponse());
      ApiResponse<GameMailResponse> body = result.getBody();
      responseStack.push(body);
      var data = body.data();
      if (data != null) {
        gameMailResponseStack.push(data);
        handleAttachments(data);
      }
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user claims the attachments of the game mail with id (.*) and session id (.*)$")
  public void whenClaimsAttachments(String mailIdStr, String sessionId) {
    String fullPath =
        ApiPathConstants.GAME_MAIL
            + GameMailController.Constants.ApiPaths.GAME_MAIL_ID.replace(
                "{game_mail_id}", mailIdStr);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      headers.set(X_GAME_SESSION_ID, sessionId);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.PATCH, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user deletes his game mails for the game save with id (.*) and session id (.*)$")
  public void whenDeletesReadGameMails(String gameSaveIdStr, String sessionId) {
    String fullPath =
        ApiPathConstants.GAME_MAIL
            + GameMailController.Constants.ApiPaths.GAME_MAIL_ID.replace(
                "{game_save_id}", gameSaveIdStr);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthenticationResponse jwtAuthenticationResponse = jwtAuthenticationResponseStack.peek();
      String token = jwtAuthenticationResponse.accessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      headers.set(X_GAME_SESSION_ID, sessionId);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<ApiResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
      ApiResponse<Void> body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  private void handleAttachments(GameMailResponse gameMailResponse) {
    var attachments = gameMailResponse.attachments();
    if (attachments == null) {
      gameMailAttachmentListStack.push(new ArrayList<>(0));
      return;
    }
    List<GameMailAttachment<?>> stringAttachments =
        attachments.stream()
            .map(
                gameMailAttachment -> {
                  try {
                    return new GameMailAttachment<>(
                        gameMailAttachment.type(),
                        objectMapper.writeValueAsString(gameMailAttachment.attachment()));
                  } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                  }
                })
            .collect(Collectors.toCollection(ArrayList::new));
    gameMailAttachmentListStack.push(stringAttachments);
  }
}
