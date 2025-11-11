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

import com.lsadf.core.application.game.mail.GameMailSenderService;
import com.lsadf.core.application.game.mail.command.SendEmailCommand;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.mail.SendGameMailRequest;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
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

  public AdminGameMailControllerImpl(GameMailSenderService gameMailSenderService) {
    this.gameMailSenderService = gameMailSenderService;
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> sendGameMailToAllGameSaves(
      SendGameMailRequest request, Jwt jwt) {
    validateUser(jwt);
    log.info(
        "Sending game mail to all game saves with template ID: {}", request.gameMailTemplateId());
    gameMailSenderService.sendGameMailToAllGameSaves();
    return generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> sendGameMailToGameSaveById(
      UUID gameSaveId, SendGameMailRequest request, Jwt jwt) {
    validateUser(jwt);
    log.info(
        "Sending game mail to game save ID: {} with template ID: {}",
        gameSaveId,
        request.gameMailTemplateId());
    SendEmailCommand command = new SendEmailCommand(gameSaveId, request.gameMailTemplateId());
    gameMailSenderService.sendGameMailToGameSaveById(command);
    return generateResponse(HttpStatus.OK);
  }
}
