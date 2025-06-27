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
package com.lsadf.core.application.game.stage;

import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.cache.Cache;
import com.lsadf.core.infra.cache.HistoCache;
import com.lsadf.core.infra.cache.config.ValkeyProperties;
import com.lsadf.core.infra.cache.properties.CacheExpirationProperties;
import com.lsadf.core.infra.persistence.game.stage.StageEntityMapper;
import com.lsadf.core.infra.persistence.game.stage.StageRepository;
import com.lsadf.core.infra.web.request.game.stage.StageRequestMapper;
import com.lsadf.core.infra.web.response.game.stage.StageResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Configuration class for the Stage service and its dependencies.
 *
 * <p>This class is marked with the @Configuration annotation, indicating it defines beans within
 * the Spring application context. It provides the necessary bean definitions required for managing
 * stages, including the implementation of the {@link StageService}.
 */
@Configuration
public class StageConfiguration {

  public static final String STAGE_CACHE = "stageCache";

  @Bean
  public StageService stageService(
      StageRepository stageRepository, Cache<Stage> stageCache, StageEntityMapper mapper) {
    return new StageServiceImpl(stageRepository, stageCache, mapper);
  }

  @Bean
  public StageEntityMapper stageEntityModelMapper() {
    return new StageEntityMapper();
  }

  @Bean
  public StageRequestMapper stageRequestModelMapper() {
    return new StageRequestMapper();
  }

  @Bean
  public StageResponseMapper stageResponseModelMapper() {
    return new StageResponseMapper();
  }

  @Bean(name = STAGE_CACHE)
  public HistoCache<Stage> redisStageCache(
      RedisTemplate<String, Stage> redisTemplate,
      CacheExpirationProperties cacheExpirationProperties,
      ValkeyProperties valkeyProperties) {
    return new ValkeyStageCache(
        redisTemplate, cacheExpirationProperties.getStageExpirationSeconds(), valkeyProperties);
  }
}
