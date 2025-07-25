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

package com.lsadf.core.infra.persistence.table.game.item;

import static com.lsadf.core.infra.persistence.config.EntityAttributes.ID;
import static com.lsadf.core.infra.persistence.table.game.item.AdditionalItemStatEntity.AdditionalStatsEntityAttributes.*;

import com.lsadf.core.domain.game.inventory.item.ItemStatistic;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

@org.springframework.stereotype.Repository
public interface AdditionalItemStatsRepository extends Repository<AdditionalItemStatEntity, UUID> {
  @Query("select count(id) from t_additional_stat_tias")
  Long count();

  @Modifying
  @Query("delete from t_additional_stat_tias")
  void deleteAll();

  @Modifying
  @Query("delete from t_additional_stat_tias where tgit_id=:tgit_id")
  void deleteAllAdditionalItemStatsByItemId(@Param(ADDITIONAL_STATS_ITEM_ID) UUID itemId);

  @Query(
      "insert into t_additional_stat_tias (id, tgit_id, statistic, base_value) values (:id, :tgit_id, :statistic, :base_value) returning *")
  AdditionalItemStatEntity createNewAdditionalItemStatEntity(
      @Param(ID) UUID id,
      @Param(ADDITIONAL_STATS_ITEM_ID) UUID itemId,
      @Param(ADDITIONAL_STATS_ITEM_STATISTIC) ItemStatistic statistic,
      @Param(ADDITIONAL_STATS_ITEM_BASE_VALUE) Float baseValue);

  @Query(
      "insert into t_additional_stat_tias (tgit_id, statistic, base_value) values (:tgit_id, :statistic, :base_value) returning *")
  AdditionalItemStatEntity createNewAdditionalItemStatEntity(
      @Param(ADDITIONAL_STATS_ITEM_ID) UUID itemId,
      @Param(ADDITIONAL_STATS_ITEM_STATISTIC) ItemStatistic statistic,
      @Param(ADDITIONAL_STATS_ITEM_BASE_VALUE) Float baseValue);

  @Query("select * from t_additional_stat_tias where tgit_id in (:tgit_id)")
  Stream<AdditionalItemStatEntity> findAllAdditionalItemStatsByGameSaveIds(
      @Param(ADDITIONAL_STATS_ITEM_ID) List<UUID> itemIdList);
}
