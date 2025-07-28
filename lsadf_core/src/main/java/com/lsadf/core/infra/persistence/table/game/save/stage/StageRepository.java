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
package com.lsadf.core.infra.persistence.table.game.save.stage;

import static com.lsadf.core.infra.persistence.config.EntityAttributes.ID;
import static com.lsadf.core.infra.persistence.table.game.save.stage.StageEntity.StageEntityAttributes.STAGE_CURRENT_STAGE;
import static com.lsadf.core.infra.persistence.table.game.save.stage.StageEntity.StageEntityAttributes.STAGE_MAX_STAGE;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository
    extends org.springframework.data.repository.Repository<StageEntity, UUID> {
  @Query(
      "insert into t_stage_tgst (id, current_stage, max_stage) values (:id, :current_stage, :max_stage) returning *")
  StageEntity createNewStageEntity(
      @Param(ID) UUID id,
      @Param(STAGE_CURRENT_STAGE) Long currentStage,
      @Param(STAGE_MAX_STAGE) Long maxStage);

  @Query("insert into t_stage_tgst (id) values (:id) returning *")
  StageEntity createNewStageEntity(@Param(ID) UUID id);

  @Query(
      """
              update t_stage_tgst set
              current_stage=coalesce(:current_stage, current_stage),
              max_stage=coalesce(:max_stage, max_stage)
              where id=:id
              returning *
              """)
  StageEntity updateStage(
      @Param(ID) UUID id,
      @Param(STAGE_CURRENT_STAGE) Long currentStage,
      @Param(STAGE_MAX_STAGE) Long maxStage);

  @Query("select count(id) from t_stage_tgst")
  Long count();

  @Query("select * from t_stage_tgst where id=:id")
  Optional<StageEntity> findStageEntityById(@Param(ID) UUID id);
}
