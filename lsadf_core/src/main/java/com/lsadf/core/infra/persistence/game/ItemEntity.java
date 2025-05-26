/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.infra.persistence.game;

import com.lsadf.core.constants.EntityAttributes;
import com.lsadf.core.domain.game.inventory.item.ItemRarity;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.domain.game.inventory.item.ItemType;
import com.lsadf.core.infra.persistence.AEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Entity(name = EntityAttributes.Items.ITEM_ENTITY)
@Table(name = EntityAttributes.Items.ITEM_ENTITY)
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemEntity extends AEntity {

  @Serial private static final long serialVersionUID = 7924047722096464427L;

  protected ItemEntity() {
    super();
  }

  @ManyToOne @ToString.Exclude @EqualsAndHashCode.Exclude private InventoryEntity inventoryEntity;

  @Column(name = EntityAttributes.Items.ITEM_CLIENT_ID, unique = true)
  private String clientId;

  @Column(name = EntityAttributes.Items.ITEM_BLUEPRINT_ID)
  private String blueprintId;

  @Column(name = EntityAttributes.Items.ITEM_TYPE)
  @Enumerated(EnumType.STRING)
  private ItemType itemType;

  @Column(name = EntityAttributes.Items.ITEM_RARITY)
  @Enumerated(EnumType.STRING)
  private ItemRarity itemRarity;

  @Column(name = EntityAttributes.Items.ITEM_IS_EQUIPPED)
  private Boolean isEquipped;

  @Column(name = EntityAttributes.Items.ITEM_LEVEL)
  @Positive
  private Integer level;

  @Column(name = EntityAttributes.Items.ITEM_MAIN_STAT)
  private ItemStat mainStat;

  @ElementCollection
  @CollectionTable(
      name = EntityAttributes.Items.ITEM_ADDITIONAL_STATS,
      joinColumns = @JoinColumn(name = "item_entity_id"))
  private List<ItemStat> additionalStats;
}
