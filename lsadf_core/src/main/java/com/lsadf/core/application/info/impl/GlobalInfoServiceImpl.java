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

package com.lsadf.core.application.info.impl;

import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.info.GlobalInfoService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.info.GlobalInfo;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

public class GlobalInfoServiceImpl implements GlobalInfoService {

  private final UserService userService;
  private final ClockService clockService;
  private final GameSaveService gameSaveService;

  public GlobalInfoServiceImpl(
      UserService userService, ClockService clockService, GameSaveService gameSaveService) {
    this.userService = userService;
    this.clockService = clockService;
    this.gameSaveService = gameSaveService;
  }

  @Override
  @Transactional(readOnly = true)
  public GlobalInfo getGlobalInfo() {
    Date now = clockService.nowDate();
    var userCount = userService.countUsers();
    var gameSaveCount = gameSaveService.countGameSaves();
    return GlobalInfo.builder()
        .now(now.toInstant())
        .gameSaveCounter(gameSaveCount)
        .userCounter(userCount)
        .build();
  }
}
