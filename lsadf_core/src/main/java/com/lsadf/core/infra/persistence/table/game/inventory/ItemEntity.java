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
package com.lsadf.core.infra.persistence.table.game.inventory;

import static com.lsadf.core.infra.persistence.config.EntityAttributes.*;
import static com.lsadf.core.infra.persistence.table.game.inventory.ItemEntity.ItemAttributes.*;

import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemStatistic;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.infra.persistence.Dateable;
import com.lsadf.core.infra.persistence.Identifiable;
import java.io.Serial;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Table(ITEM_ENTITY)
public class ItemEntity implements Identifiable, Dateable {

  @Serial private static final long serialVersionUID = 7924047722096464427L;

  @Id
  @Column(ID)
  private UUID id;

  @Column(ITEM_GAME_SAVE_ID)
  private UUID gameSaveId;

  @Column(CREATED_AT)
  private Date createdAt;

  @Column(UPDATED_AT)
  private Date updatedAt;

  @Column(ITEM_CLIENT_ID)
  private String clientId;

  @Column(ITEM_BLUEPRINT_ID)
  private String blueprintId;

  @Column(ITEM_TYPE)
  private ItemType itemType;

  @Column(ITEM_RARITY)
  private ItemRarity itemRarity;

  @Column(ITEM_IS_EQUIPPED)
  private Boolean isEquipped;

  @Column(ITEM_LEVEL)
  private Integer level;

  @Column(ITEM_MAIN_STATISTIC)
  private ItemStatistic mainStatistic;

  @Column(ITEM_MAIN_BASE_VALUE)
  private Float mainBaseValue;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ItemAttributes {
    public static final String ITEM_ENTITY = "t_item_tgit";
    public static final String ITEM_CLIENT_ID = "client_id";
    public static final String ITEM_GAME_SAVE_ID = "tgme_id";
    public static final String ITEM_BLUEPRINT_ID = "blueprint_id";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_RARITY = "rarity";
    public static final String ITEM_MAIN_STATISTIC = "main_statistic";
    public static final String ITEM_MAIN_BASE_VALUE = "main_base_value";
    public static final String ITEM_IS_EQUIPPED = "is_equipped";
    public static final String ITEM_LEVEL = "level";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ItemEntity that = (ItemEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(clientId, that.clientId)
        && Objects.equals(createdAt, that.createdAt)
        && Objects.equals(updatedAt, that.updatedAt)
        && Objects.equals(blueprintId, that.blueprintId)
        && itemType == that.itemType
        && itemRarity == that.itemRarity
        && Objects.equals(isEquipped, that.isEquipped)
        && Objects.equals(level, that.level)
        && Objects.equals(mainStatistic, that.mainStatistic)
        && Objects.equals(mainBaseValue, that.mainBaseValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        clientId,
        blueprintId,
        itemType,
        itemRarity,
        isEquipped,
        level,
        mainStatistic,
        mainBaseValue,
        id,
        createdAt,
        updatedAt);
  }
}
