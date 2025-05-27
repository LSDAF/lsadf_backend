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
package com.lsadf.core.infra.persistence.game.inventory;

import com.lsadf.core.infra.persistence.AEntity;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import com.lsadf.core.infra.persistence.game.inventory.items.ItemEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.io.Serial;
import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity(name = InventoryEntity.InventoryAttributes.INVENTORY_ENTITY)
@Table(name = InventoryEntity.InventoryAttributes.INVENTORY_ENTITY)
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InventoryEntity extends AEntity {
  @Serial private static final long serialVersionUID = 8543208469573180701L;

  protected InventoryEntity() {
    super();
  }

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private GameSaveEntity gameSave;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<ItemEntity> items;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class InventoryAttributes {
    public static final String INVENTORY_ENTITY = "t_inventory";
  }
}
