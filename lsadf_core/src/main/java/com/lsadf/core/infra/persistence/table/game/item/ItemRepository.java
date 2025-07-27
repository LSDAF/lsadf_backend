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
import static com.lsadf.core.infra.persistence.table.game.item.ItemEntity.ItemAttributes.*;

import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemStatistic;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, UUID> {
  @Query("select * from t_item_tgit where tgsa_id =:gameSaveId")
  Set<ItemEntity> findAllItemsByGameSaveId(UUID gameSaveId);

  @Query("select * from t_item_tgit where client_id =:clientId")
  Optional<ItemEntity> findItemByClientId(String clientId);

  @Modifying
  @Query("delete from t_item_tgit where tgsa_id=:gameSaveId")
  void deleteAllItemsByGameSaveId(UUID gameSaveId);

  @SuppressWarnings("java:S107")
  @Query(
      "insert into t_item_tgit (id, tgsa_id, client_id, blueprint_id, type, rarity, is_equipped, level, main_statistic, main_base_value) values (:id, :tgsa_id, :client_id, :blueprint_id, :type, :rarity, :is_equipped, :level, :main_statistic, :main_base_value) returning *")
  ItemEntity createNewItemEntity(
      @Param(ID) UUID id,
      @Param(ITEM_GAME_SAVE_ID) UUID gameSaveId,
      @Param(ITEM_CLIENT_ID) String clientId,
      @Param(ITEM_BLUEPRINT_ID) String blueprintId,
      @Param(ITEM_TYPE) ItemType type,
      @Param(ITEM_RARITY) ItemRarity rarity,
      @Param(ITEM_IS_EQUIPPED) Boolean isEquipped,
      @Param(ITEM_LEVEL) Integer level,
      @Param(ITEM_MAIN_STATISTIC) ItemStatistic mainStatistic,
      @Param(ITEM_MAIN_BASE_VALUE) Float mainBaseValue);

  @SuppressWarnings("java:S107")
  @Query(
      "insert into t_item_tgit (tgsa_id, client_id, blueprint_id, type, rarity, is_equipped, level, main_statistic, main_base_value) values (:tgsa_id, :client_id, :blueprint_id, :type, :rarity, :is_equipped, :level, :main_statistic, :main_base_value) returning *")
  ItemEntity createNewItemEntity(
      @Param(ITEM_GAME_SAVE_ID) UUID gameSaveId,
      @Param(ITEM_CLIENT_ID) String clientId,
      @Param(ITEM_BLUEPRINT_ID) String blueprintId,
      @Param(ITEM_TYPE) ItemType type,
      @Param(ITEM_RARITY) ItemRarity rarity,
      @Param(ITEM_IS_EQUIPPED) Boolean isEquipped,
      @Param(ITEM_LEVEL) Integer level,
      @Param(ITEM_MAIN_STATISTIC) ItemStatistic mainStatistic,
      @Param(ITEM_MAIN_BASE_VALUE) Float mainBaseValue);
}
