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
package com.lsadf.core.domain.game.inventory;

import com.lsadf.core.shared.model.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Item", description = "Item object")
@Data
@Builder
@Getter
@Setter
public class Item implements Model {

  @Serial private static final long serialVersionUID = 6615198748250122221L;

  private UUID id;

  private UUID gameSaveId;

  private String clientId;

  private String blueprintId;

  private ItemType itemType;

  private ItemRarity itemRarity;

  private Boolean isEquipped;

  private Integer level;

  private ItemStat mainStat;

  private List<ItemStat> additionalStats;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Item item = (Item) o;
    return Objects.equals(id, item.id)
        && Objects.equals(clientId, item.clientId)
        && Objects.equals(blueprintId, item.blueprintId)
        && itemType == item.itemType
        && itemRarity == item.itemRarity
        && Objects.equals(isEquipped, item.isEquipped)
        && Objects.equals(level, item.level)
        && Objects.equals(mainStat, item.mainStat)
        && Objects.equals(additionalStats, item.additionalStats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        clientId,
        blueprintId,
        itemType,
        itemRarity,
        isEquipped,
        level,
        mainStat,
        additionalStats);
  }
}
