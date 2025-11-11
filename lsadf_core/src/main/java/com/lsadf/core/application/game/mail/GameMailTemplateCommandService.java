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

package com.lsadf.core.application.game.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.core.application.game.mail.command.CreateNewTemplateAttachmentCommand;
import com.lsadf.core.application.game.mail.command.InitializeDefaultGameMailTemplateCommand;
import com.lsadf.core.application.game.mail.command.InitializeGameMailTemplateCommand;
import com.lsadf.core.domain.game.mail.GameMailTemplate;
import java.util.UUID;

public interface GameMailTemplateCommandService {
  default GameMailTemplate initializeDefaultGameMailTemplates(
      InitializeDefaultGameMailTemplateCommand command) {
    return initializeGameMailTemplate(
        new InitializeGameMailTemplateCommand(
            command.name(), command.subject(), command.body(), 30));
  }

  GameMailTemplate initializeGameMailTemplate(InitializeGameMailTemplateCommand command);

  void deleteGameMailTemplateById(UUID id);

  void attachToGameMailTemplate(CreateNewTemplateAttachmentCommand command)
      throws JsonProcessingException;
}
