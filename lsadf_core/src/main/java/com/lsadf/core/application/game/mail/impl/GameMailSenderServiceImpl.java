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

package com.lsadf.core.application.game.mail.impl;

import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.GameMailSenderService;
import com.lsadf.core.application.game.mail.GameMailTemplateQueryService;
import com.lsadf.core.application.game.mail.command.SendEmailCommand;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameMailSenderServiceImpl implements GameMailSenderService {

  private final GameSaveService gameSaveService;
  private final GameMailTemplateQueryService gameMailTemplateQueryService;
  private final GameMailRepositoryPort gameMailRepositoryPort;

  @Override
  public void sendGameMailToAllGameSaves(UUID gameTemplateId) {
    List<UUID> gameSaveIds =
        gameSaveService.getGameSaves().stream().map(gs -> gs.getMetadata().id()).toList();
    for (UUID gameSaveId : gameSaveIds) {
      SendEmailCommand command = new SendEmailCommand(gameSaveId, gameTemplateId);
      sendGameMailToGameSaveById(command);
    }
  }

  @Override
  public void sendGameMailToGameSaveById(SendEmailCommand command) {
    if (!gameSaveService.existsById(command.gameSaveId())) {
      throw new NotFoundException("Game save with id " + command.gameSaveId() + " not found");
    }

    if (!gameMailTemplateQueryService.existsById(command.emailTemplateId())) {
      throw new NotFoundException(
          "Game mail template with id " + command.emailTemplateId() + " not found");
    }

    UUID mailId = UUID.randomUUID();
    gameMailRepositoryPort.createNewGameEmail(
        mailId, command.gameSaveId(), command.emailTemplateId());
  }
}
