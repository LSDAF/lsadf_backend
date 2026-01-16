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
package com.lsadf.core.infra.persistence.adapter.game.mail;

import com.lsadf.core.application.game.mail.GameMailTemplateRepositoryPort;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.domain.game.mail.GameMailTemplate;
import com.lsadf.core.exception.http.NotFoundException;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverterRegistry;
import com.lsadf.core.infra.persistence.impl.game.mail.template.*;
import com.lsadf.core.infra.web.dto.request.Request;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class GameMailTemplateRepositoryAdapter implements GameMailTemplateRepositoryPort {
  private final GameMailTemplateRepository gameMailTemplateRepository;
  private final GameMailTemplateAttachmentRepository gameMailAttachmentRepository;
  private final GameMailAttachmentConverterRegistry converterRegistry;
  private final ObjectMapper objectMapper;

  private static final GameMailTemplateEntityMapper gameMailTemplateEntityMapper =
      GameMailTemplateEntityMapper.INSTANCE;

  @Override
  public GameMailTemplate createNewMailTemplate(
      String name, String subject, String body, Integer expirationDays) {
    UUID uuid = UUID.randomUUID();
    var entity =
        gameMailTemplateRepository.createNewMailTemplate(uuid, name, subject, body, expirationDays);
    return gameMailTemplateEntityMapper.map(entity);
  }

  @Override
  public List<GameMailTemplate> getMailTemplates() {
    var templates = gameMailTemplateRepository.findAllGameMailTemplates();
    return templates.stream().map(gameMailTemplateEntityMapper::map).toList();
  }

  @Override
  public Optional<GameMailTemplate> getMailTemplateById(UUID id) throws JacksonException {
    GameMailTemplateEntity templateEntity = getGameMailTemplateEntityFromDatabase(id);
    GameMailTemplate gameMailTemplate = gameMailTemplateEntityMapper.map(templateEntity);
    enrichGameMailTemplateWithAttachments(gameMailTemplate);
    return Optional.of(gameMailTemplate);
  }

  @Override
  public void deleteMailTemplateById(UUID id) {
    gameMailTemplateRepository.deleteGameMailTemplateById(id);
  }

  @Override
  public void deleteAllMailTemplates() {
    gameMailTemplateRepository.deleteAllMailTemplates();
  }

  @Override
  public boolean existsById(UUID mailTemplateId) {
    return gameMailTemplateRepository.existsById(mailTemplateId);
  }

  @Override
  @Transactional
  public void attachNewObjectToTemplate(
      UUID mailTemplateId, GameMailAttachmentType type, Object object) throws JacksonException {
    UUID newId = UUID.randomUUID();
    String json = object.toString();
    gameMailAttachmentRepository.createNewGameMailAttachment(newId, mailTemplateId, type, json);
  }

  private GameMailTemplateEntity getGameMailTemplateEntityFromDatabase(UUID gameMailTemplateId) {
    return gameMailTemplateRepository
        .findGameMailTemplateById(gameMailTemplateId)
        .orElseThrow(NotFoundException::new);
  }

  private void enrichGameMailTemplateWithAttachments(GameMailTemplate gameMailTemplate)
      throws JacksonException {
    for (GameMailTemplateAttachmentEntity entity :
        gameMailAttachmentRepository.findByMailTemplateId(gameMailTemplate.getId())) {
      GameMailAttachmentType type = entity.getType();
      String json = entity.getObject();

      var converter = converterRegistry.getConverterByType(type);
      if (converter == null) {
        log.error("Could not find converter for type {}", type);
        continue;
      }
      var result = converter.toRequest(json);
      GameMailAttachment<Request> attachment = new GameMailAttachment<>(type, result);
      gameMailTemplate.addAttachment(attachment);
    }
  }
}
