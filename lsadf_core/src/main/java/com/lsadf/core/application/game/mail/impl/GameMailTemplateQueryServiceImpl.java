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

import com.lsadf.core.application.game.mail.GameMailTemplateQueryService;
import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.domain.game.mail.GameMailTemplate;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;

@RequiredArgsConstructor
public class GameMailTemplateQueryServiceImpl implements GameMailTemplateQueryService {

  private final GameMailTemplateRepositoryPort gameMailTemplateRepositoryPort;

  @Override
  public List<GameMailTemplate> getMailTemplates() {
    return gameMailTemplateRepositoryPort.getMailTemplates();
  }

  @Override
  public GameMailTemplate getMailTemplateById(UUID id) throws JacksonException {
    Optional<GameMailTemplate> optional = gameMailTemplateRepositoryPort.getMailTemplateById(id);
    if (optional.isEmpty()) {
      throw new NotFoundException("Game mail template with id " + id + " not found");
    }
    return optional.get();
  }

  @Override
  public boolean existsById(UUID mailTemplateId) {
    return gameMailTemplateRepositoryPort.existsById(mailTemplateId);
  }
}
