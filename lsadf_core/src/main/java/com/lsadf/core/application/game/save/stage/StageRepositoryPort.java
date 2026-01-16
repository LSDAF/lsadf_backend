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
package com.lsadf.core.application.game.save.stage;

import com.lsadf.core.domain.game.save.stage.Stage;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface StageRepositoryPort {

  Optional<Stage> findById(UUID id);

  Stage create(UUID id, @Nullable Long currentStage, @Nullable Long maxStage, @Nullable Long wave);

  Stage create(UUID id);

  Stage update(UUID gameSaveId, Stage stage);

  boolean existsById(UUID id);

  Long count();
}
