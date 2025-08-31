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

package com.lsadf.core.application.game.save.metadata.impl;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.application.game.save.metadata.GameMetadataRepositoryPort;
import com.lsadf.core.application.game.save.metadata.GameMetadataService;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

public class GameMetadataServiceImpl implements GameMetadataService {

  private final CacheManager cacheManager;
  private final GameMetadataRepositoryPort gameMetadataRepositoryPort;
  private final GameMetadataCachePort gameMetadataCachePort;

  public GameMetadataServiceImpl(
      CacheManager cacheManager,
      GameMetadataRepositoryPort gameMetadataRepositoryPort,
      GameMetadataCachePort gameMetadataCachePort) {
    this.cacheManager = cacheManager;
    this.gameMetadataRepositoryPort = gameMetadataRepositoryPort;
    this.gameMetadataCachePort = gameMetadataCachePort;
  }

  @Override
  @Transactional
  public void deleteById(UUID gameSaveId) {
    gameMetadataRepositoryPort.deleteById(gameSaveId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(UUID gameSaveId) {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      var optionalGameMetadata = gameMetadataCachePort.get(gameSaveId.toString());
      if (optionalGameMetadata.isPresent()) {
        return true;
      }
    }
    return gameMetadataRepositoryPort.existsById(gameSaveId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByNickname(String nickname) {
    return gameMetadataRepositoryPort.existsByNickname(nickname);
  }

  @Override
  @Transactional(readOnly = true)
  public GameMetadata getGameMetadata(UUID gameSaveId) {
    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      var optionalGameMetadata = gameMetadataCachePort.get(gameSaveId.toString());
      if (optionalGameMetadata.isPresent()) {
        return optionalGameMetadata.get();
      }
      GameMetadata gameMetadata = getGameMetadataFromDatabase(gameSaveId);
      gameMetadataCachePort.set(gameSaveId.toString(), gameMetadata);
      return gameMetadata;
    }
    return getGameMetadataFromDatabase(gameSaveId);
  }

  @Override
  @Transactional(readOnly = true)
  public Long count() {
    return gameMetadataRepositoryPort.count();
  }

  @Override
  @Transactional
  public GameMetadata updateNickname(UUID gameSaveId, String nickname) {
    return gameMetadataRepositoryPort.updateNickname(gameSaveId, nickname);
  }

  @Override
  @Transactional
  public GameMetadata createNewGameMetadata(
      @Nullable UUID gameSaveId, String username, @Nullable String nickname) {
    if (gameSaveId != null && nickname != null) {
      return gameMetadataRepositoryPort.create(gameSaveId, username, nickname);
    } else if (nickname != null) {
      return gameMetadataRepositoryPort.create(null, username, nickname);
    } else {
      return gameMetadataRepositoryPort.create(null, username, null);
    }
  }

  private GameMetadata getGameMetadataFromDatabase(UUID gameSaveId) {
    return gameMetadataRepositoryPort
        .findById(gameSaveId)
        .orElseThrow(() -> new NotFoundException("Game metadata not found for id: " + gameSaveId));
  }
}
