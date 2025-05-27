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
package com.lsadf.core.infra.web.requests.game.inventory;

import static com.lsadf.core.infra.web.JsonAttributes.Inventory.ITEMS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.infra.web.requests.Request;
import com.lsadf.core.infra.web.requests.game.inventory.item.ItemRequest;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ITEMS})
public class InventoryRequest implements Request {

  @Serial private static final long serialVersionUID = 9140883232214789509L;

  @JsonProperty(value = ITEMS)
  @NotNull
  private List<ItemRequest> items;
}
