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
import com.lsadf.core.infra.valkey.cache.ValkeyCacheAdapter;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public class StageCacheAdapter extends ValkeyCacheAdapter<StageHash, Stage, UUID>
    implements StageCachePort {
  private final StageHashRepository stageHashRepository;
  private static final StageHashMapper stageHashMapper = StageHashMapper.INSTANCE;

  public StageCacheAdapter(StageHashRepository stageHashRepository) {
    this.stageHashRepository = stageHashRepository;
  }

  @Override
  protected CrudRepository<StageHash, UUID> getRepository() {
    return this.stageHashRepository;
  }

  @Override
  protected HashModelMapper<StageHash, Stage> getMapper() {
    return stageHashMapper;
  }

  @Override
  public void set(UUID key, Stage value) {
    StageHash hash =
        StageHash.builder().currentStage(value.currentStage()).maxStage(value.maxStage()).build();
    this.stageHashRepository.save(hash);
  }
}
