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

package com.lsadf.core.infra.valkey.cache.game.save.metadata;

import com.lsadf.core.application.game.save.metadata.GameMetadataCachePort;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.infra.valkey.cache.HashModelMapper;
import com.lsadf.core.infra.valkey.cache.ValkeyCacheAdapter;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public class GameMetadataCacheAdapter
    extends ValkeyCacheAdapter<GameMetadataHash, GameMetadata, UUID>
    implements GameMetadataCachePort {

  private final GameMetadataHashRepository gameMetadataHashRepository;
  private static final GameMetadataHashMapper gameMetadataHashMapper =
      GameMetadataHashMapper.INSTANCE;

  public GameMetadataCacheAdapter(GameMetadataHashRepository gameMetadataHashRepository) {
    this.gameMetadataHashRepository = gameMetadataHashRepository;
  }

  @Override
  protected CrudRepository<GameMetadataHash, UUID> getRepository() {
    return this.gameMetadataHashRepository;
  }

  @Override
  protected HashModelMapper<GameMetadataHash, GameMetadata> getMapper() {
    return gameMetadataHashMapper;
  }

  @Override
  public void set(UUID key, GameMetadata value) {
    GameMetadataHash hash =
        GameMetadataHash.builder()
            .id(value.id())
            .nickname(value.nickname())
            .userEmail(value.userEmail())
            .build();

    this.gameMetadataHashRepository.save(hash);
  }
}
