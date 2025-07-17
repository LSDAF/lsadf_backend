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

package com.lsadf.core.application.info;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.infra.clock.ClockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for defining beans related to global system information and response
 * mapping functionality. This class configures and provides the necessary beans to handle the
 * retrieval and mapping of global information pertaining to the system's state, including user
 * metrics and game save statistics.
 *
 * <p>The primary beans defined in this configuration include: - GlobalInfoService: Provides methods
 * to retrieve global system information using user, game save, and clock services. -
 * GlobalInfoResponseMapper: Maps internal GlobalInfo model data into API response representations.
 *
 * <p>Dependencies for the beans (like UserService, GameSaveService, and ClockService) are
 * automatically injected by the Spring container.
 */
@Configuration
public class GlobalInfoConfiguration {
  @Bean
  public GlobalInfoService globalInfoService(
      UserService userService, GameSaveService gameSaveService, ClockService clockService) {
    return new GlobalInfoServiceImpl(userService, clockService, gameSaveService);
  }
}
