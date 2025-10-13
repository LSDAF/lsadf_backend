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
package com.lsadf.core.infra.persistence.impl.game.save.stage;

import static com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_ID;
import static com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity.StageEntityAttributes.*;

import com.lsadf.core.infra.persistence.JdbcRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JdbcRepository<StageEntity> {
  @Query(
      "insert into t_stage_tgst (tgme_id, tgst_current_stage, tgst_max_stage, tgst_wave) values (:tgme_id, :tgst_current_stage, :tgst_max_stage, :tgst_wave) returning *")
  StageEntity createNewStageEntity(
      @Param(GAME_METADATA_ID) UUID id,
      @Param(STAGE_CURRENT_STAGE) Long currentStage,
      @Param(STAGE_MAX_STAGE) Long maxStage,
      @Param(STAGE_WAVE) Long wave);

  @Query("insert into t_stage_tgst (tgme_id) values (:tgme_id) returning *")
  StageEntity createNewStageEntity(@Param(GAME_METADATA_ID) UUID id);

  @Query(
      """
              update t_stage_tgst set
              tgst_current_stage=coalesce(:tgst_current_stage, tgst_current_stage),
              tgst_max_stage=coalesce(:tgst_max_stage, tgst_max_stage),
              tgst_wave=coalesce(:tgst_wave, tgst_wave)
              where tgme_id=:tgme_id
              returning *
              """)
  StageEntity updateStage(
      @Param(GAME_METADATA_ID) UUID id,
      @Param(STAGE_CURRENT_STAGE) Long currentStage,
      @Param(STAGE_MAX_STAGE) Long maxStage,
      @Param(STAGE_WAVE) Long wave);

  @Query("select count(tgme_id) from t_stage_tgst")
  Long count();

  @Query("select * from t_stage_tgst where tgme_id=:tgme_id")
  Optional<StageEntity> findStageEntityById(@Param(GAME_METADATA_ID) UUID id);
}
