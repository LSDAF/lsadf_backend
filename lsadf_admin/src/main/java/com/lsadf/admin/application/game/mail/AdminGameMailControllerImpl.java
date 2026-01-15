/*
 * Copyright © 2024-2026 LSDAF
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

import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailSenderService;
import com.lsadf.core.application.game.mail.command.DeleteGameMailsCommand;
import com.lsadf.core.application.game.mail.command.SendEmailCommand;
import com.lsadf.core.exception.http.BadRequestException;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.mail.DeleteGameMailsRequest;
import com.lsadf.core.infra.web.dto.request.game.mail.SendGameMailRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of AdminGameMailController */
@RestController
@Slf4j
public class AdminGameMailControllerImpl extends BaseController implements AdminGameMailController {

  private final GameMailSenderService gameMailSenderService;
  private final GameMailCommandService gameMailCommandService;

  public AdminGameMailControllerImpl(
      GameMailSenderService gameMailSenderService, GameMailCommandService gameMailCommandService) {
    this.gameMailSenderService = gameMailSenderService;
    this.gameMailCommandService = gameMailCommandService;
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> sendGameMailToGameSaves(
      SendGameMailRequest request, Jwt jwt) {
    validateUser(jwt);
    if (request.gameSaveId() == null) {
      log.info(
          "Sending game mail template with ID {} to all game saves", request.gameMailTemplateId());
      gameMailSenderService.sendGameMailToAllGameSaves(request.gameMailTemplateId());
      return generateResponse(HttpStatus.OK);
    }
    log.info(
        "Sending game mail to all game saves with template ID: {}", request.gameMailTemplateId());
    gameMailSenderService.sendGameMailToGameSaveById(
        new SendEmailCommand(request.gameSaveId(), request.gameMailTemplateId()));
    return generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> deleteGameMails(
      @Nullable Instant expiration, @Nullable DeleteGameMailsRequest request, Jwt jwt) {
    validateUser(jwt);
    // Handle deletion by expiration timestamp
    if (expiration != null) {
      log.info("Deleting game mails expiration before timestamp: {}", expiration);
      var delete = gameMailCommandService.deleteExpiredGameMails(expiration);
      log.info("Deleted game mails expiration before timestamp: {}", delete);
      return generateResponse(HttpStatus.OK);
    }

    if (request == null) {
      log.error("DeleteGameMailsRequest cannot be null when no expiration parameter is provided");
      throw new BadRequestException(
          "DeleteGameMailsRequest cannot be null when no expiration parameter is provided");
    }

    // Handle deletion by specific IDs
    log.info("Deleting {} game mails by IDs", request.mailIds().size());
    DeleteGameMailsCommand command = new DeleteGameMailsCommand(request.mailIds());
    long deletedCount = gameMailCommandService.deleteGameMail(command);
    log.info("Deleted {} game mails", deletedCount);
    return generateResponse(HttpStatus.OK);
  }
}
