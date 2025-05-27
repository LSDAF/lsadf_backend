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
package com.lsadf.core.application.game.characteristics;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsRepository;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequestMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the CharacteristicsService bean. This class defines and
 * provides a bean for the CharacteristicsService implementation. It integrates necessary
 * dependencies such as CharacteristicsRepository, Cache for Characteristics, and a ModelMapper for
 * handling object transformations.
 */
@Configuration
public class CharacteristicsConfiguration {
  @Bean
  public CharacteristicsService characteristicsService(
      CharacteristicsRepository characteristicsRepository,
      Cache<Characteristics> characteristicsCache,
      CharacteristicsEntityMapper mapper) {
    return new CharacteristicsServiceImpl(characteristicsRepository, characteristicsCache, mapper);
  }

  @Bean
  public CharacteristicsEntityMapper characteristicsEntityModelMapper() {
    return new CharacteristicsEntityMapper();
  }

  @Bean
  public CharacteristicsRequestMapper characteristicsRequestModelMapper() {
    return new CharacteristicsRequestMapper();
  }
}
