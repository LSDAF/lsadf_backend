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

import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.NoOpHistoCache;
import com.lsadf.core.infra.cache.config.ValkeyProperties;
import com.lsadf.core.infra.cache.properties.CacheExpirationProperties;
import com.lsadf.core.infra.persistence.table.game.save.characteristics.CharacteristicsRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Configuration class for setting up the CharacteristicsService bean. This class defines and
 * provides a bean for the CharacteristicsService implementation. It integrates necessary
 * dependencies such as CharacteristicsRepository, Cache for Characteristics, and a ModelMapper for
 * handling object transformations.
 */
@Configuration
public class CharacteristicsConfiguration {

  public static final String CHARACTERISTICS_CACHE = "characteristicsCache";

  @Bean
  public CharacteristicsService characteristicsService(
      CharacteristicsRepository characteristicsRepository,
      Cache<Characteristics> characteristicsCache) {
    return new CharacteristicsServiceImpl(characteristicsRepository, characteristicsCache);
  }

  @Bean(name = CHARACTERISTICS_CACHE)
  @ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "true")
  public HistoCache<Characteristics> redisCharacteristicsCache(
      RedisTemplate<String, Characteristics> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      ValkeyProperties valkeyProperties) {
    return new ValkeyCharacteristicsCache(
        redisTemplate,
        cacheExpirationProperties.getCharacteristicsExpirationSeconds(),
        valkeyProperties);
  }

  @Bean(name = CHARACTERISTICS_CACHE)
  @ConditionalOnMissingBean
  public HistoCache<Characteristics> noOpCharacteristicsCache() {
    return new NoOpHistoCache<>();
  }
}
