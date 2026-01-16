/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.valkey.cache.adapter.game.session;

import com.lsadf.core.application.game.session.GameSessionCachePort;
import com.lsadf.core.domain.game.session.GameSession;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.HashRepository;
import com.lsadf.core.infra.valkey.cache.adapter.ValkeyCacheRepositoryAdapter;
import com.lsadf.core.infra.valkey.cache.game.session.GameSessionHash;
import com.lsadf.core.infra.valkey.cache.game.session.GameSessionHashMapper;
import com.lsadf.core.infra.valkey.config.properties.ValkeyCacheExpirationProperties;
import java.util.Optional;
import java.util.UUID;

public class GameSessionCacheRepositoryAdapter
    extends ValkeyCacheRepositoryAdapter<GameSession, GameSessionHash, UUID>
    implements GameSessionCachePort {

  private static final HashModelMapper<GameSessionHash, GameSession> HASH_MAPPER =
      GameSessionHashMapper.INSTANCE;

  public GameSessionCacheRepositoryAdapter(
      HashRepository<GameSessionHash, UUID> repository,
      ValkeyCacheExpirationProperties valkeyCacheExpirationProperties) {
    super(repository);
    this.hashMapper = HASH_MAPPER;
    this.expirationSeconds = valkeyCacheExpirationProperties.getGameSessionExpirationSeconds();
  }

  @Override
  public Optional<GameSession> get(String key) {
    UUID uuid = UUID.fromString(key);
    Optional<GameSessionHash> optional = repository.findById(uuid);
    return optional.map(hashMapper::map);
  }

  @Override
  public void set(String key, GameSession value) {
    UUID uuid = UUID.fromString(key);
    GameSessionHash hash =
        GameSessionHash.builder()
            .id(uuid)
            .expiration(this.expirationSeconds)
            .gameSaveId(value.getGameSaveId())
            .endTime(value.getEndTime())
            .version(value.getVersion())
            .userEmail(value.getUserEmail())
            .build();

    repository.save(hash);
  }

  @Override
  public void unset(String key) {
    UUID uuid = UUID.fromString(key);
    repository.deleteById(uuid);
  }
}
