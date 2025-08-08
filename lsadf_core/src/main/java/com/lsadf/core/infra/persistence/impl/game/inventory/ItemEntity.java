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
package com.lsadf.core.infra.persistence.impl.game.inventory;

import static com.lsadf.core.infra.persistence.impl.game.inventory.ItemEntity.ItemAttributes.*;

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
  @Column(ITEM_ID)
  private UUID id;

  @Column(ITEM_GAME_SAVE_ID)
  private UUID gameSaveId;

  @Column(ITEM_CREATED_AT)
  private Date createdAt;

  @Column(ITEM_UPDATED_AT)
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
    public static final String ITEM_ID = "tgit_id";
    public static final String ITEM_CLIENT_ID = "tgit_client_id";
    public static final String ITEM_CREATED_AT = "tgit_created_at";
    public static final String ITEM_UPDATED_AT = "tgit_updated_at";
    public static final String ITEM_GAME_SAVE_ID = "tgme_id";
    public static final String ITEM_BLUEPRINT_ID = "tgit_blueprint_id";
    public static final String ITEM_TYPE = "tgit_type";
    public static final String ITEM_RARITY = "tgit_rarity";
    public static final String ITEM_MAIN_STATISTIC = "tgit_main_statistic";
    public static final String ITEM_MAIN_BASE_VALUE = "tgit_main_base_value";
    public static final String ITEM_IS_EQUIPPED = "tgit_is_equipped";
    public static final String ITEM_LEVEL = "tgit_level";
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
