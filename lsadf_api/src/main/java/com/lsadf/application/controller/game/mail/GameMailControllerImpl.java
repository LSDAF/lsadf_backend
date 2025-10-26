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

package com.lsadf.application.controller.game.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailQueryService;
import com.lsadf.core.application.game.session.GameSessionQueryService;
import com.lsadf.core.domain.game.mail.GameMail;
import com.lsadf.core.exception.http.ForbiddenException;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.ResponseUtils;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailResponse;
import com.lsadf.core.infra.web.dto.response.game.mail.GameMailResponseMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(prefix = "api", name = "enabled", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class GameMailControllerImpl extends BaseController implements GameMailController {

  private final GameMailQueryService gameMailQueryService;
  private final GameMailCommandService gameMailCommandService;
  private final GameSessionQueryService gameSessionQueryService;
  private final ClockService clockService;

  private static final GameMailResponseMapper gameMailResponseMapper =
      GameMailResponseMapper.INSTANCE;

  @Override
  public ResponseEntity<ApiResponse<List<GameMailResponse>>> getAllGameMails(Jwt jwt, UUID gameId) {
    validateUser(jwt);
    List<GameMail> gameMailList = gameMailQueryService.getMailsByGameSaveId(gameId);
    List<GameMailResponse> responseList =
        gameMailList.stream().map(gameMailResponseMapper::map).toList();
    return ResponseUtils.generateResponse(HttpStatus.OK, responseList);
  }

  @Override
  public ResponseEntity<ApiResponse<GameMailResponse>> getGameMailById(Jwt jwt, UUID gameMailId)
      throws JsonProcessingException {
    validateUser(jwt);
    GameMail result = gameMailQueryService.getMailById(gameMailId);
    if (!result.isRead()) {
      gameMailCommandService.readGameMailById(gameMailId);
      result.setRead(true);
    }
    var response = gameMailResponseMapper.map(result);
    return ResponseUtils.generateResponse(HttpStatus.OK, response);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> deleteReadGameMails(
      Jwt jwt, UUID gameId, UUID sessionId) {
    validateUser(jwt);
    gameSessionQueryService.checkGameSessionValidity(sessionId, gameId, clockService.nowInstant());
    gameMailCommandService.deleteAllReadGameMailsByGameSaveId(gameId);
    return ResponseUtils.generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> claimGameMailAttachments(
      Jwt jwt, UUID gameMailId, UUID sessionId) throws JsonProcessingException {
    validateUser(jwt);

    var gameMail = gameMailQueryService.getMailById(gameMailId);

    gameSessionQueryService.checkGameSessionValidity(
        sessionId, gameMail.getGameSaveId(), clockService.nowInstant());

    if (gameMail.getAttachments() == null) {
      throw new ForbiddenException("Cannot claim an attachment without any attachments");
    }

    if (gameMail.isAttachmentsClaimed()) {
      throw new ForbiddenException("Cannot claim attachments, they are already claimed");
    }

    gameMailCommandService.claimGameMailAttachments(gameMailId);
    return ResponseUtils.generateResponse(HttpStatus.OK);
  }

  @Override
  public Logger getLogger() {
    return log;
  }
}
