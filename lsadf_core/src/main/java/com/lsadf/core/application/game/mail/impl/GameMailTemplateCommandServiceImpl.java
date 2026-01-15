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
package com.lsadf.core.application.game.mail.impl;

import com.lsadf.core.application.game.mail.GameMailTemplateCommandService;
import com.lsadf.core.application.game.mail.GameMailTemplateQueryService;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.mail.command.CreateNewTemplateAttachmentCommand;
import com.lsadf.core.application.game.mail.command.InitializeGameMailTemplateCommand;
import com.lsadf.core.domain.game.mail.GameMailTemplate;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;

@RequiredArgsConstructor
public class GameMailTemplateCommandServiceImpl implements GameMailTemplateCommandService {

  private final GameMailTemplateRepositoryPort gameMailTemplateRepositoryPort;
  private final GameMailTemplateQueryService gameMailTemplateQueryService;

  @Override
  public GameMailTemplate initializeGameMailTemplate(InitializeGameMailTemplateCommand command) {
    return gameMailTemplateRepositoryPort.createNewMailTemplate(
        command.name(), command.subject(), command.body(), command.expirationDays());
  }

  @Override
  public void deleteGameMailTemplateById(UUID id) {
    if (!gameMailTemplateQueryService.existsById(id)) {
      throw new NotFoundException("Game mail template with id " + id + " does not exist");
    }
    gameMailTemplateRepositoryPort.deleteMailTemplateById(id);
  }

  @Override
  public void attachToGameMailTemplate(CreateNewTemplateAttachmentCommand command)
      throws JacksonException {
    UUID gameMailTemplateId = command.mailTemplateId();
    if (!gameMailTemplateQueryService.existsById(gameMailTemplateId)) {
      throw new NotFoundException(
          "Game mail template with id " + gameMailTemplateId + " does not exist");
    }
    gameMailTemplateRepositoryPort.attachNewObjectToTemplate(
        gameMailTemplateId, command.type(), command.attachment());
  }
}
