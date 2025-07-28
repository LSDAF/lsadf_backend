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

package com.lsadf.core.application.game.save.metadata;

import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataEntity;
import com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataEntityMapper;
import com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataRepository;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class GameMetadataServiceImpl implements GameMetadataService {

  private final GameMetadataRepository gameMetadataRepository;

  private static final GameMetadataEntityMapper gameMetadataEntityMapper =
      GameMetadataEntityMapper.INSTANCE;

  public GameMetadataServiceImpl(GameMetadataRepository gameMetadataRepository) {
    this.gameMetadataRepository = gameMetadataRepository;
  }

  @Override
  @Transactional
  public void deleteById(UUID gameSaveId) {
    gameMetadataRepository.deleteGameSaveEntityById(gameSaveId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(UUID gameSaveId) {
    return gameMetadataRepository.existsById(gameSaveId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByNickname(String nickname) {
    return gameMetadataRepository.existsByNickname(nickname);
  }

  @Override
  @Transactional(readOnly = true)
  public GameMetadata getGameMetadata(UUID gameSaveId) {
    var gameMetadataEntity = gameMetadataRepository.findGameSaveEntityById(gameSaveId);
    return gameMetadataEntityMapper.map(gameMetadataEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Long count() {
    return gameMetadataRepository.count();
  }

  @Override
  @Transactional
  public GameMetadata updateNickname(UUID gameSaveId, String nickname) {
    var entity = gameMetadataRepository.updateGameSaveEntityNickname(gameSaveId, nickname);
    return gameMetadataEntityMapper.map(entity);
  }

  @Override
  @Transactional
  public GameMetadata createNewGameMetadata(UUID gameSaveId, String username, String nickname) {
    GameMetadataEntity newEntity;
    if (gameSaveId != null && nickname != null) {
      newEntity = gameMetadataRepository.createNewGameSaveEntity(gameSaveId, username, nickname);
    } else if (nickname != null) {
      newEntity = gameMetadataRepository.createNewGameSaveEntity(username, nickname);
    } else {
      newEntity = gameMetadataRepository.createNewGameSaveEntity(username);
    }
    return gameMetadataEntityMapper.map(newEntity);
  }
}
