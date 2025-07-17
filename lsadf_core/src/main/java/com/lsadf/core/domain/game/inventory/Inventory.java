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

import static com.lsadf.core.infra.web.JsonAttributes.Inventory.ITEMS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.domain.game.inventory.item.Item;
import com.lsadf.core.infra.web.controller.JsonViews;
import com.lsadf.core.shared.model.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Inventory", description = "Inventory object")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonView(JsonViews.External.class)
public class Inventory implements Model {

  @Serial private static final long serialVersionUID = 33494087785391763L;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = ITEMS)
  @Schema(description = "List of items in the inventory", example = "[\"item1\", \"item2\"]")
  private Set<Item> items;
}
