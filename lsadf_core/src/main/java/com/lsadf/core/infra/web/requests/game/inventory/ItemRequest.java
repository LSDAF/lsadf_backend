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

import static com.lsadf.core.infra.web.JsonAttributes.Item.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.infra.web.requests.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
  CLIENT_ID,
  TYPE,
  BLUEPRINT_ID,
  RARITY,
  IS_EQUIPPED,
  LEVEL,
  MAIN_STAT,
  ADDITIONAL_STATS
})
@Builder
public class ItemRequest implements Request {

  @Serial private static final long serialVersionUID = -1116418739363127022L;

  @Schema(
      description = "Client generated id, concatenation of inventory id and item id",
      example = "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
  @JsonProperty(value = CLIENT_ID)
  @NotNull
  private String clientId;

  @Schema(description = "Item type", example = "boots")
  @JsonProperty(value = TYPE)
  @NotNull
  private String itemType;

  @Schema(description = "Blueprint id", example = "blueprint_id")
  @JsonProperty(value = BLUEPRINT_ID)
  @NotNull
  private String blueprintId;

  @Schema(description = "Item rarity", example = "LEGENDARY")
  @JsonProperty(value = RARITY)
  @NotNull
  private String itemRarity;

  @Schema(description = "Is equipped", example = "true")
  @JsonProperty(value = IS_EQUIPPED)
  @NotNull
  private Boolean isEquipped;

  @Schema(description = "Item level", example = "20")
  @JsonProperty(value = LEVEL)
  @NotNull
  private Integer level;

  @Schema(description = "Main item stat")
  @JsonProperty(value = MAIN_STAT)
  @NotNull
  private ItemStat mainStat;

  @Schema(description = "Additional item stat list")
  @JsonProperty(value = ADDITIONAL_STATS)
  @NotNull
  private List<ItemStat> additionalStats;
}
