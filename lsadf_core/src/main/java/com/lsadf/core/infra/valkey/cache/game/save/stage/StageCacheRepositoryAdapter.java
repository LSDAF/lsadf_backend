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

package com.lsadf.core.infra.valkey.cache.game.save.stage;

import com.lsadf.core.application.game.save.stage.StageCachePort;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.HashRepository;
import com.lsadf.core.infra.valkey.cache.config.properties.CacheExpirationProperties;
import com.lsadf.core.infra.valkey.cache.impl.ValkeyCacheRepositoryAdapter;
import java.util.Optional;
import java.util.UUID;

public class StageCacheRepositoryAdapter
    extends ValkeyCacheRepositoryAdapter<Stage, StageHash, UUID> implements StageCachePort {
  private static final HashModelMapper<StageHash, Stage> HASH_MAPPER = StageHashMapper.INSTANCE;

  public StageCacheRepositoryAdapter(
      HashRepository<StageHash, UUID> repository,
      CacheExpirationProperties cacheExpirationProperties) {
    super(repository);
    this.hashMapper = HASH_MAPPER;
    this.expirationSeconds = cacheExpirationProperties.getStageExpirationSeconds();
  }

  @Override
  public Optional<Stage> get(String key) {
    UUID uuid = UUID.fromString(key);
    Optional<StageHash> optional = repository.findById(uuid);
    return optional.map(stageHash -> hashMapper.map(stageHash));
  }

  @Override
  public void set(String key, Stage value) {
    UUID uuid = UUID.fromString(key);
    StageHash hash =
        StageHash.builder()
            .gameSaveId(uuid)
            .maxStage(value.maxStage())
            .expiration(this.expirationSeconds)
            .currentStage(value.currentStage())
            .build();
    repository.save(hash);
  }

  @Override
  public void set(String key, Stage value, int expirationSeconds) {
    UUID uuid = UUID.fromString(key);
    StageHash hash =
        StageHash.builder()
            .gameSaveId(uuid)
            .maxStage(value.maxStage())
            .currentStage(value.currentStage())
            .expiration((long) expirationSeconds)
            .build();
    repository.save(hash);
  }

  @Override
  public void unset(String key) {
    UUID uuid = UUID.fromString(key);
    repository.deleteById(uuid);
  }
}
