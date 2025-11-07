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

package com.lsadf.core.infra.persistence.adapter.game.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.domain.game.mail.GameMail;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverterRegistry;
import com.lsadf.core.infra.persistence.impl.game.mail.GameMailRepository;
import com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateAttachmentEntity;
import com.lsadf.core.infra.persistence.impl.game.mail.template.GameMailTemplateAttachmentRepository;
import com.lsadf.core.infra.persistence.impl.view.GameMailViewEntity;
import com.lsadf.core.infra.persistence.impl.view.GameMailViewMapper;
import com.lsadf.core.infra.persistence.impl.view.GameMailViewRepository;
import com.lsadf.core.infra.web.dto.request.Request;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GameMailRepositoryAdapter implements GameMailRepositoryPort {

  private final GameMailRepository gameMailRepository;
  private final GameMailTemplateAttachmentRepository gameMailAttachmentRepository;
  private final GameMailViewRepository gameMailViewRepository;
  private final GameMailAttachmentConverterRegistry converterRegistry;

  private static final GameMailViewMapper gameMailViewMapper = GameMailViewMapper.INSTANCE;

  @Override
  public Optional<GameMail> findGameMailEntityById(UUID mailId) throws JsonProcessingException {
    var viewEntityOptional = gameMailViewRepository.findById(mailId);
    if (viewEntityOptional.isEmpty()) {
      return Optional.empty();
    }
    var viewEntity = viewEntityOptional.get();
    GameMail gameMail = gameMailViewMapper.map(viewEntity);
    enrichGameMailWithAttachments(gameMail, viewEntity.getTemplateId());
    return Optional.of(gameMail);
  }

  @Override
  public List<GameMail> findGameMailsByGameSaveId(UUID gameSaveId) {
    List<GameMailViewEntity> gameMailViewList = gameMailViewRepository.findByGameSaveId(gameSaveId);
    return gameMailViewList.stream().map(gameMailViewMapper::map).toList();
  }

  @Override
  public void createNewGameEmail(UUID id, UUID gameSaveId, UUID mailTemplateId) {
    boolean isRead = false;
    boolean isAttachmentClaimed = false;
    gameMailRepository.createNewGameEmail(
        id, gameSaveId, mailTemplateId, isRead, isAttachmentClaimed);
  }

  @Override
  public void readGameEmail(UUID mailId) {
    gameMailRepository.readGameEmail(mailId);
  }

  @Override
  public void deleteGameEmails(List<UUID> mailIds) {
    gameMailRepository.deleteGameEmails(mailIds);
  }

  @Override
  public void deleteReadGameEmailsByGameSaveId(UUID gameSaveId) {
    gameMailRepository.deleteReadGameEmailsByGameSaveId(gameSaveId);
  }

  @Override
  public void claimGameMailAttachments(UUID mailId) {
    gameMailRepository.claimGameMailAttachments(mailId);
  }

  @Override
  public void deleteExpiredGameMails(Instant currentTime) {
    gameMailRepository.deleteExpiredGameMails(currentTime);
  }

  @Override
  public boolean existsById(UUID mailId) {
    return gameMailRepository.existsById(mailId);
  }

  private void enrichGameMailWithAttachments(GameMail gameMail, UUID mailTemplateId)
      throws JsonProcessingException {
    for (GameMailTemplateAttachmentEntity entity :
        gameMailAttachmentRepository.findByMailTemplateId(mailTemplateId)) {
      GameMailAttachmentType type = entity.getType();
      String json = entity.getObject();

      var converter = converterRegistry.getConverterByType(type);
      if (converter == null) {
        log.error("Could not find converter for type {}", type);
        continue;
      }
      var result = converter.toRequest(json);
      GameMailAttachment<Request> attachment = new GameMailAttachment<>(type, result);
      gameMail.addAttachment(attachment);
    }
  }
}
