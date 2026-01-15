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

package com.lsadf.core.unit.application.info;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.lsadf.core.application.clock.ClockService;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.info.GlobalInfoService;
import com.lsadf.core.application.info.impl.GlobalInfoServiceImpl;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.domain.info.GlobalInfo;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class GlobalInfoServiceTests {
  @Mock private UserService userService;

  @Mock private GameSaveService gameSaveService;

  @Mock private ClockService clockService;

  private GlobalInfoService globalInfoService;

  @BeforeEach
  void init() {
    // Create all mocks and inject them into the service
    MockitoAnnotations.openMocks(this);
    when(userService.countUsers()).thenReturn(25L);
    when(clockService.nowDate()).thenReturn(new Date());
    when(gameSaveService.countGameSaves()).thenReturn(50L);
    this.globalInfoService = new GlobalInfoServiceImpl(userService, clockService, gameSaveService);
  }

  @Test
  void test_getGlobalInfo_returnsValidGlobalInfo_when_called() {
    GlobalInfo expected =
        GlobalInfo.builder()
            .now(new Date().toInstant())
            .gameSaveCounter(50L)
            .userCounter(25L)
            .build();
    GlobalInfo globalInfo = globalInfoService.getGlobalInfo();
    assertThat(globalInfo).usingRecursiveComparison().ignoringFields("now").isEqualTo(expected);
  }
}
