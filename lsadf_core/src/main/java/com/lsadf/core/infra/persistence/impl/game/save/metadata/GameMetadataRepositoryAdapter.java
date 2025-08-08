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
package com.lsadf.core.infra.persistence.impl.game.save.metadata;

import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import java.util.Optional;
import java.util.UUID;

public class GameMetadataRepositoryAdapter implements GameMetadataRepositoryPort {

  private final GameMetadataRepository gameMetadataRepository;
  private static final GameMetadataEntityMapper gameMetadataEntityMapper =
      GameMetadataEntityMapper.INSTANCE;

  public GameMetadataRepositoryAdapter(GameMetadataRepository gameMetadataRepository) {
    this.gameMetadataRepository = gameMetadataRepository;
  }

  @Override
  public Optional<GameMetadata> findById(UUID id) {
    var entity = gameMetadataRepository.findGameSaveEntityById(id);
    return entity != null ? Optional.of(gameMetadataEntityMapper.map(entity)) : Optional.empty();
  }

  @Override
  public GameMetadata create(UUID id, String userEmail, String nickname) {
    GameMetadataEntity entity;
    if (id != null && nickname != null) {
      entity = gameMetadataRepository.createNewGameSaveEntity(id, userEmail, nickname);
    } else if (nickname != null) {
      entity = gameMetadataRepository.createNewGameSaveEntity(userEmail, nickname);
    } else if (id != null) {
      entity = gameMetadataRepository.createNewGameSaveEntity(id, userEmail, null);
    } else {
      entity = gameMetadataRepository.createNewGameSaveEntity(userEmail);
    }
    return gameMetadataEntityMapper.map(entity);
  }

  @Override
  public GameMetadata updateNickname(UUID id, String nickname) {
    var entity = gameMetadataRepository.updateGameSaveEntityNickname(id, nickname);
    return gameMetadataEntityMapper.map(entity);
  }

  @Override
  public void deleteById(UUID id) {
    gameMetadataRepository.deleteGameSaveEntityById(id);
  }

  @Override
  public boolean existsById(UUID id) {
    return gameMetadataRepository.existsById(id);
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return gameMetadataRepository.existsByNickname(nickname);
  }

  @Override
  public Long count() {
    return gameMetadataRepository.count();
  }

  @Override
  public Optional<String> findOwnerEmailById(UUID gameSaveId) {
    return gameMetadataRepository.findUserEmailById(gameSaveId);
  }
}
