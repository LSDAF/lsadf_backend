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
package com.lsadf.core.infra.persistence.game.inventory.item;

import static com.lsadf.core.infra.persistence.game.inventory.item.ItemEntity.ItemAttributes.*;

import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.infra.persistence.AEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.util.List;
import java.util.Objects;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = ITEM_ENTITY)
@Table(name = ITEM_ENTITY)
@SuperBuilder
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ItemEntity extends AEntity {

  @Serial private static final long serialVersionUID = 7924047722096464427L;

  protected ItemEntity() {
    super();
  }

  @ManyToOne @ToString.Exclude @EqualsAndHashCode.Exclude private InventoryEntity inventoryEntity;

  @Column(name = ItemAttributes.ITEM_CLIENT_ID, unique = true)
  private String clientId;

  @Column(name = ItemAttributes.ITEM_BLUEPRINT_ID)
  private String blueprintId;

  @Column(name = ItemAttributes.ITEM_TYPE)
  @Enumerated(EnumType.STRING)
  private ItemType itemType;

  @Column(name = ItemAttributes.ITEM_RARITY)
  @Enumerated(EnumType.STRING)
  private ItemRarity itemRarity;

  @Column(name = ItemAttributes.ITEM_IS_EQUIPPED)
  private Boolean isEquipped;

  @Column(name = ItemAttributes.ITEM_LEVEL)
  @Positive
  private Integer level;

  @Column(name = ItemAttributes.ITEM_MAIN_STAT)
  private ItemStat mainStat;

  @ElementCollection
  @CollectionTable(
      name = ItemAttributes.ITEM_ADDITIONAL_STATS,
      joinColumns = @JoinColumn(name = "item_entity_id"))
  private List<ItemStat> additionalStats;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ItemAttributes {
    public static final String ITEM_ENTITY = "t_item";
    public static final String ITEM_ADDITIONAL_STATS = "t_additional_stats";
    public static final String ITEM_CLIENT_ID = "client_id";
    public static final String ITEM_BLUEPRINT_ID = "blueprint_id";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_RARITY = "rarity";
    public static final String ITEM_IS_EQUIPPED = "is_equipped";
    public static final String ITEM_LEVEL = "level";
    public static final String ITEM_MAIN_STAT = "main_stat";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ItemEntity that = (ItemEntity) o;
    return Objects.equals(inventoryEntity, that.inventoryEntity)
        && Objects.equals(clientId, that.clientId)
        && Objects.equals(blueprintId, that.blueprintId)
        && itemType == that.itemType
        && itemRarity == that.itemRarity
        && Objects.equals(isEquipped, that.isEquipped)
        && Objects.equals(level, that.level)
        && Objects.equals(mainStat, that.mainStat)
        && Objects.equals(additionalStats, that.additionalStats);
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
        mainStat,
        additionalStats,
        id,
        createdAt,
        updatedAt);
  }
}
