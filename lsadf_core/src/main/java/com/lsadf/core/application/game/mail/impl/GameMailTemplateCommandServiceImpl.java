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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.core.application.game.mail.GameMailTemplateCommandService;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.application.game.mail.command.CreateNewTemplateAttachmentCommand;
import com.lsadf.core.application.game.mail.command.InitializeDefaultGameMailTemplateCommand;
import com.lsadf.core.domain.game.mail.GameMailTemplate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameMailTemplateCommandServiceImpl implements GameMailTemplateCommandService {

  private final GameMailTemplateRepositoryPort gameMailTemplateRepositoryPort;

  @Override
  public GameMailTemplate initializeDefaultGameMailTemplates(
      InitializeDefaultGameMailTemplateCommand command) {
    return gameMailTemplateRepositoryPort.createNewMailTemplate(
        command.name(), command.subject(), command.body(), 30);
  }

  @Override
  public GameMailTemplate initializeGameMailTemplate(
      InitializeDefaultGameMailTemplateCommand command) {
    return gameMailTemplateRepositoryPort.createNewMailTemplate(
        command.name(), command.subject(), command.body(), 30);
  }

  @Override
  public void deleteGameMailTemplateById(UUID id) {
    gameMailTemplateRepositoryPort.deleteMailTemplateById(id);
  }

  @Override
  public void attachToGameMailTemplate(CreateNewTemplateAttachmentCommand command)
      throws JsonProcessingException {
    gameMailTemplateRepositoryPort.attachNewObjectToTemplate(
        command.mailTemplateId(), command.type(), command.attachment());
  }
}
