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
package com.lsadf.core.application.game.save.characteristics;

import com.lsadf.core.infra.valkey.cache.service.CacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the CharacteristicsService bean. This class defines and
 * provides a bean for the CharacteristicsService implementation. It integrates necessary
 * dependencies such as CharacteristicsRepositoryPort, CachePort for Characteristics, and a
 * ModelMapper for handling object transformations.
 */
@Configuration
public class CharacteristicsConfiguration {

  public static final String CHARACTERISTICS_CACHE = "characteristicsCache";

  @Bean
  public CharacteristicsService characteristicsService(
      CacheService cacheService,
      CharacteristicsRepositoryPort characteristicsRepositoryPort,
      CharacteristicsCachePort characteristicsCache) {
    return new CharacteristicsServiceImpl(
        cacheService, characteristicsRepositoryPort, characteristicsCache);
  }
}
