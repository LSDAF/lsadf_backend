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

import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailTemplate;
import com.lsadf.core.shared.model.Model;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameMailTemplateRepositoryPort {

  void createNewMailTemplate(
      String name,
      String subject,
      String body,
      Integer expirationDays,
      List<GameMailAttachment<Model>> attachments);

  List<GameMailTemplate> getMailTemplates();

  Optional<GameMailTemplate> getMailTemplateById(UUID id);

  void deleteMailTemplateById(UUID id);
}
