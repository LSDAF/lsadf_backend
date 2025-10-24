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

import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.mail.command.SendEmailCommand;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.domain.game.mail.GameMail;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameMailCommandServiceImpl implements GameMailCommandService {
  private final GameMailRepositoryPort gameMailRepositoryPort;
  private final GameSaveService gameSaveService;

  @Override
  public void sendGameMailToAllGameSaves() {
    List<UUID> gameSaveIds =
        gameSaveService.getGameSaves().stream().map(gs -> gs.getMetadata().id()).toList();
    for (UUID gameSaveId : gameSaveIds) {
      SendEmailCommand command = new SendEmailCommand(gameSaveId, gameSaveId);
      sendGameMailToGameSaveById(command);
    }
  }

  @Override
  public void sendGameMailToGameSaveById(SendEmailCommand command) {
    UUID mailId = UUID.randomUUID();
    gameMailRepositoryPort.createNewGameEmail(
        mailId, command.gameSaveId(), command.emailTemplateId());
  }

  @Override
  public List<GameMail> getGameMailsByGameSaveId(UUID gameSaveId) {
    return gameMailRepositoryPort.findGameMailsByGameSaveId(gameSaveId);
  }

  @Override
  public void readGameMailById(UUID id) {
    gameMailRepositoryPort.readGameEmail(id);
  }

  @Override
  public void deleteAllReadGameMailsByGameSaveId(UUID gameSaveId) {
    gameMailRepositoryPort.deleteReadGameEmailsByGameSaveId(gameSaveId);
  }
}
