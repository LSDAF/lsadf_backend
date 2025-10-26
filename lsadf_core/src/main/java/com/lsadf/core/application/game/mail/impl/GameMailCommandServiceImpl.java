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

package com.lsadf.core.application.game.mail.impl;

import com.lsadf.core.application.game.mail.GameMailCommandService;
import com.lsadf.core.application.game.mail.GameMailQueryService;
import com.lsadf.core.application.game.mail.GameMailRepositoryPort;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.exception.http.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameMailCommandServiceImpl implements GameMailCommandService {
  private final GameMailRepositoryPort gameMailRepositoryPort;
  private final GameSaveService gameSaveService;
  private final GameMailQueryService gameMailQueryService;

  @Override
  public void readGameMailById(UUID id) {
    if (!gameMailQueryService.existsById(id)) {
      throw new NotFoundException("Game mail with id " + id + " not found");
    }
    gameMailRepositoryPort.readGameEmail(id);
  }

  @Override
  public void deleteAllReadGameMailsByGameSaveId(UUID gameSaveId) {
    if (!gameSaveService.existsById(gameSaveId)) {
      throw new NotFoundException("Game save with id " + gameSaveId + " not found");
    }
    gameMailRepositoryPort.deleteReadGameEmailsByGameSaveId(gameSaveId);
  }

  @Override
  public void claimGameMailAttachments(UUID id) {
    if (!gameMailQueryService.existsById(id)) {
      throw new NotFoundException("Game mail with id " + id + " not found");
    }
    gameMailRepositoryPort.claimGameMailAttachments(id);
  }
}
