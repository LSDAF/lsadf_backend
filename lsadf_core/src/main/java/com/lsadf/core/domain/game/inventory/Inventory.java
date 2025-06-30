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
package com.lsadf.core.domain.game.inventory;

import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Inventory implements Model {

  @Serial private static final long serialVersionUID = 33494087785391763L;

  private Set<Item> items;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Inventory inventory = (Inventory) o;
    return Objects.equals(items, inventory.items);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(items);
  }
}
