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
package com.lsadf.core.application.search.config;

import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.search.SearchService;
import com.lsadf.core.application.search.impl.SearchServiceImpl;
import com.lsadf.core.application.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for setting up beans related to search functionality in the
 * application.
 *
 * <p>This class defines the {@code SearchService} bean, which uses {@code UserService} and {@code
 * GameSaveService} to facilitate search-related operations. It provides an implementation of {@code
 * SearchService} in the form of {@code SearchServiceImpl}.
 */
@Configuration
public class SearchConfiguration {
  @Bean
  public SearchService searchService(UserService userService, GameSaveService gameSaveService) {
    return new SearchServiceImpl(userService, gameSaveService);
  }
}
