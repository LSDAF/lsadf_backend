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
package com.lsadf.core.application.game.save.stage.config;

import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.application.game.save.stage.StageRepositoryPort;
import com.lsadf.core.application.game.save.stage.StageService;
import com.lsadf.core.application.game.save.stage.impl.StageServiceImpl;
import com.lsadf.core.infra.valkey.cache.service.CacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
      CacheService cacheService,
      StageRepositoryPort stageRepositoryPort,
      StageCachePort stageCache) {
    return new StageServiceImpl(cacheService, stageRepositoryPort, stageCache);
  }
}
