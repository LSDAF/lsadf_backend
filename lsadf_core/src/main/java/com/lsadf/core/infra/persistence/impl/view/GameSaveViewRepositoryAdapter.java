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
package com.lsadf.core.infra.persistence.impl.view;

import com.lsadf.core.application.game.save.GameSaveRepositoryPort;
import com.lsadf.core.domain.game.save.GameSave;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.transaction.annotation.Transactional;

public class GameSaveViewRepositoryAdapter implements GameSaveRepositoryPort {

  private final GameSaveViewRepository gameSaveViewRepository;
  private static final GameSaveViewMapper gameSaveViewMapper = GameSaveViewMapper.INSTANCE;

  public GameSaveViewRepositoryAdapter(GameSaveViewRepository gameSaveViewRepository) {
    this.gameSaveViewRepository = gameSaveViewRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<GameSave> findById(UUID gameSaveId) {
    return gameSaveViewRepository.findGameSaveEntityById(gameSaveId).map(gameSaveViewMapper::map);
  }

  @Override
  @Transactional(readOnly = true)
  public Stream<GameSave> findAll() {
    return gameSaveViewRepository.findAllGameSaves().map(gameSaveViewMapper::map);
  }

  @Override
  @Transactional(readOnly = true)
  public Stream<GameSave> findByUserEmail(String userEmail) {
    return gameSaveViewRepository
        .findGameSaveEntitiesByUserEmail(userEmail)
        .map(gameSaveViewMapper::map);
  }
}
