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

package com.lsadf.core.infra.valkey.cache.adapter.game.save.metadata;

import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.HashRepository;
import com.lsadf.core.infra.valkey.cache.adapter.ValkeyCacheRepositoryAdapter;
import com.lsadf.core.infra.valkey.cache.config.properties.CacheExpirationProperties;
import com.lsadf.core.infra.valkey.cache.impl.save.metadata.GameMetadataHash;
import com.lsadf.core.infra.valkey.cache.impl.save.metadata.GameMetadataHashMapper;
import java.util.Optional;
import java.util.UUID;

public class GameMetadataCacheRepositoryAdapter
    extends ValkeyCacheRepositoryAdapter<GameMetadata, GameMetadataHash, UUID>
    implements GameMetadataCachePort {

  private static final HashModelMapper<GameMetadataHash, GameMetadata> HASH_MAPPER =
      GameMetadataHashMapper.INSTANCE;

  public GameMetadataCacheRepositoryAdapter(
      HashRepository<GameMetadataHash, UUID> repository,
      CacheExpirationProperties cacheExpirationProperties) {
    super(repository);
    this.hashMapper = HASH_MAPPER;
    this.expirationSeconds = cacheExpirationProperties.getGameMetadataExpirationSeconds();
  }

  @Override
  public Optional<GameMetadata> get(String key) {
    UUID uuid = UUID.fromString(key);
    Optional<GameMetadataHash> optional = repository.findById(uuid);
    return optional.map(gameMetadataHash -> hashMapper.map(gameMetadataHash));
  }

  @Override
  public void set(String key, GameMetadata value) {
    UUID uuid = UUID.fromString(key);
    GameMetadataHash hash =
        GameMetadataHash.builder()
            .id(uuid)
            .nickname(value.nickname())
            .expiration(expirationSeconds)
            .userEmail(value.userEmail())
            .build();
    repository.save(hash);
  }

  @Override
  public void set(String key, GameMetadata value, int expirationSeconds) {
    UUID uuid = UUID.fromString(key);
    Long expiration = (long) expirationSeconds;
    GameMetadataHash hash =
        GameMetadataHash.builder()
            .id(uuid)
            .nickname(value.nickname())
            .userEmail(value.userEmail())
            .expiration(expiration)
            .build();
    repository.save(hash);
  }

  @Override
  public void unset(String key) {
    UUID uuid = UUID.fromString(key);
    repository.deleteById(uuid);
  }
}
