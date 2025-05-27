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
package com.lsadf.core.application.search;

import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.application.user.UserService;
import com.lsadf.core.infra.mappers.Mapper;
import com.lsadf.core.infra.persistence.mappers.game.GameSaveEntityModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up dependencies related to the search functionality within the
 * application context.
 *
 * <p>This configuration provides a {@link SearchService} bean, which is responsible for
 * facilitating search-related operations such as user searches and game save searches. The {@link
 * SearchService} implementation depends on the following services: - {@link UserService} for
 * user-related operations. - {@link GameSaveService} for operations related to game saves. - {@link
 * Mapper} for data mapping between entities and domain models.
 */
@Configuration
public class SearchConfiguration {
  @Bean
  public SearchService searchService(
      UserService userService, GameSaveService gameSaveService, GameSaveEntityModelMapper mapper) {
    return new SearchServiceImpl(userService, gameSaveService, mapper);
  }
}
