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

package com.lsadf.admin.application.game.mail;

import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;
import static org.springframework.http.HttpStatus.OK;

import com.lsadf.core.application.game.mail.GameMailTemplateCommandService;
import com.lsadf.core.application.game.mail.GameMailTemplateQueryService;
import com.lsadf.core.application.game.mail.command.CreateNewTemplateAttachmentCommand;
import com.lsadf.core.application.game.mail.command.InitializeDefaultGameMailTemplateCommand;
import com.lsadf.core.application.game.mail.command.InitializeGameMailTemplateCommand;
import com.lsadf.core.domain.game.mail.GameMailTemplate;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailAttachmentRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.GameMailTemplateRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailTemplateResponse;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailTemplateResponseMapper;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminGameMailTemplateControllerImpl extends BaseController
    implements AdminGameMailTemplateController {

  private final GameMailTemplateQueryService gameMailTemplateQueryService;
  private final GameMailTemplateCommandService gameMailTemplateCommandService;

  private static final GameMailTemplateResponseMapper gameMailTemplateResponseMapper =
      GameMailTemplateResponseMapper.INSTANCE;

  @Override
  public ResponseEntity<ApiResponse<List<GameMailTemplateResponse>>> getAllTemplates(Jwt jwt) {
    validateUser(jwt);

    var results = gameMailTemplateQueryService.getMailTemplates();
    return generateResponse(OK, results);
  }

  @Override
  public ResponseEntity<ApiResponse<GameMailTemplateResponse>> getTemplateById(Jwt jwt, UUID id)
      throws JacksonException {
    validateUser(jwt);

    var result = gameMailTemplateQueryService.getMailTemplateById(id);
    return generateResponse(OK, result);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> deleteMailTemplateById(Jwt jwt, UUID id) {
    validateUser(jwt);

    gameMailTemplateCommandService.deleteGameMailTemplateById(id);
    return generateResponse(OK);
  }

  @Override
  public ResponseEntity<ApiResponse<GameMailTemplateResponse>>
      createNewTemplateAttachmentToTemplate(
          Jwt jwt, UUID id, List<GameMailAttachmentRequest<?>> attachments)
          throws JacksonException {
    validateUser(jwt);
    for (GameMailAttachmentRequest<?> attachment : attachments) {
      CreateNewTemplateAttachmentCommand command =
          new CreateNewTemplateAttachmentCommand(id, attachment.type(), attachment.object());
      gameMailTemplateCommandService.attachToGameMailTemplate(command);
    }
    GameMailTemplate finalizedTemplate = gameMailTemplateQueryService.getMailTemplateById(id);
    GameMailTemplateResponse response = gameMailTemplateResponseMapper.map(finalizedTemplate);
    return generateResponse(OK, response);
  }

  @Override
  public ResponseEntity<ApiResponse<GameMailTemplateResponse>> createNewMailTemplate(
      Jwt jwt, GameMailTemplateRequest gameMailTemplateRequest) {
    validateUser(jwt);
    GameMailTemplate gameMailTemplate;
    if (gameMailTemplateRequest.expirationDays() == null) {
      InitializeDefaultGameMailTemplateCommand command =
          new InitializeDefaultGameMailTemplateCommand(
              gameMailTemplateRequest.name(),
              gameMailTemplateRequest.subject(),
              gameMailTemplateRequest.body());
      gameMailTemplate = gameMailTemplateCommandService.initializeDefaultGameMailTemplates(command);
    } else {
      int expirationDays = Objects.requireNonNull(gameMailTemplateRequest.expirationDays());
      InitializeGameMailTemplateCommand command =
          new InitializeGameMailTemplateCommand(
              gameMailTemplateRequest.name(),
              gameMailTemplateRequest.subject(),
              gameMailTemplateRequest.body(),
              expirationDays);
      gameMailTemplate = gameMailTemplateCommandService.initializeGameMailTemplate(command);
    }
    GameMailTemplateResponse response = gameMailTemplateResponseMapper.map(gameMailTemplate);
    return generateResponse(OK, response);
  }

  @Override
  public Logger getLogger() {
    return log;
  }
}
