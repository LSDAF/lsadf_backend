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

import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.domain.game.mail.GameMail;
import com.lsadf.core.domain.game.mail.GameMailAttachment;
import com.lsadf.core.domain.game.mail.GameMailAttachmentType;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverter;
import com.lsadf.core.infra.persistence.adapter.game.mail.converter.GameMailAttachmentConverterRegistry;
import com.lsadf.core.infra.persistence.impl.game.mail.GameMailEntity;
import com.lsadf.core.infra.persistence.impl.game.mail.GameMailEntityMapper;
import com.lsadf.core.infra.persistence.impl.game.mail.GameMailRepository;
import com.lsadf.core.infra.persistence.impl.game.mail.attachment.GameMailAttachmentEntity;
import com.lsadf.core.infra.persistence.impl.game.mail.attachment.GameMailAttachmentRepository;
import com.lsadf.core.shared.model.Model;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class GameMailRepositoryAdapter implements GameMailRepositoryPort {

  private final GameMailRepository gameMailRepository;
  private final GameMailAttachmentRepository gameMailAttachmentRepository;
  private final GameMailAttachmentConverterRegistry converterRegistry;

  private static final GameMailEntityMapper gameMailEntityMapper = GameMailEntityMapper.INSTANCE;

  @Override
  @Transactional(readOnly = true)
  public Optional<GameMail> findGameMailEntityById(UUID mailId) {
    var mailIdStr = mailId.toString();
    var gameMailEntityOptional = gameMailRepository.findGameMailEntityById(mailIdStr);
    if (gameMailEntityOptional.isEmpty()) {
      return Optional.empty();
    }
    GameMailEntity gameMailEntity = gameMailEntityOptional.get();
    GameMail gameMail = gameMailEntityMapper.map(gameMailEntity);
    GameMail enrichedGameMail = enrichWithAttachments(gameMail);
    return Optional.of(enrichedGameMail);
  }

  @Override
  @Transactional(readOnly = true)
  public List<GameMail> findGameMailsByGameSaveId(UUID gameSaveId) {
    List<GameMailEntity> entities =
        gameMailRepository.findGameMailsByGameSaveId(gameSaveId.toString());
    return entities.stream().map(gameMailEntityMapper::map).toList();
  }

  @Override
  @Transactional
  public void createNewGameEmail(
      UUID id,
      UUID gameSaveId,
      String subject,
      String body,
      boolean isRead,
      Instant expiresAt,
      List<GameMailAttachment<Model>> attachments) {
    String idStr = id.toString();
    String gameSaveIdStr = gameSaveId.toString();
    gameMailRepository.createNewGameEmail(idStr, gameSaveIdStr, subject, body, isRead, expiresAt);
    for (GameMailAttachment<Model> gameMailAttachment : attachments) {
      GameMailAttachmentType type = gameMailAttachment.type();
      GameMailAttachmentConverter<?> converter = converterRegistry.getConverterByType(type);
      if (converter == null) {
        log.warn("No converter found for attachment type: {}. Skipping...", type);
        continue;
      }
      try {
        String json = converter.toJson(gameMailAttachment);
        gameMailAttachmentRepository.createNewGameMailAttachment(
            UUID.randomUUID().toString(), idStr, type.name(), json);
      } catch (Exception e) {
        log.error("Error while converting attachment model to entity", e);
      }
    }
  }

  @Override
  @Transactional
  public void readGameEmail(UUID mailId) {
    String mailIdStr = mailId.toString();
    gameMailRepository.readGameEmail(mailIdStr);
  }

  @Override
  @Transactional
  public void deleteGameEmails(List<UUID> mailIds) {
    List<String> mailIdStrs = mailIds.stream().map(UUID::toString).toList();
    gameMailRepository.deleteGameEmails(mailIdStrs);
  }

  @Override
  @Transactional
  public void claimGameMailAttachments(UUID mailId) {
    String mailIdStr = mailId.toString();
    gameMailRepository.claimGameMailAttachments(mailIdStr);
  }

  @Override
  @Transactional
  public void deleteExpiredGameMails(Instant currentTime) {
    gameMailRepository.deleteExpiredGameMails(currentTime);
  }

  private GameMail enrichWithAttachments(GameMail gameMail) {
    List<GameMailAttachmentEntity> attachmentEntities =
        gameMailAttachmentRepository.findByMailId(gameMail.getId().toString());
    for (GameMailAttachmentEntity attachmentEntity : attachmentEntities) {
      GameMailAttachmentType type = attachmentEntity.getAttachmentType();
      GameMailAttachmentConverter<?> converter = converterRegistry.getConverterByType(type);
      if (converter == null) {
        log.warn("No converter found for attachment type: {}. Skipping...", type);
        continue;
      }
      try {
        var attachmentModel = converter.toModel(attachmentEntity.getAttachmentObject());
        GameMailAttachment<?> attachment = new GameMailAttachment<>(type, attachmentModel);
        gameMail.addAttachment(attachment);
      } catch (Exception e) {
        log.error("Error while converting attachment object to GameMailAttachmentModel", e);
      }
    }
    return gameMail;
  }
}
