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
package com.lsadf.core.infra.web.request.game.inventory;

import static com.lsadf.core.infra.web.JsonAttributes.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.infra.web.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.util.List;
import lombok.Builder;

@Builder
public record ItemRequest(
    @Schema(
            description = "Client generated id, concatenation of inventory id and item id",
            example = "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
        @JsonProperty(value = CLIENT_ID)
        @NotNull
        String clientId,
    @Schema(description = "Item type", example = "boots") @JsonProperty(value = TYPE) @NotNull
        String itemType,
    @Schema(description = "Blueprint id", example = "blueprint_id")
        @JsonProperty(value = BLUEPRINT_ID)
        @NotNull
        String blueprintId,
    @Schema(description = "Item rarity", example = "LEGENDARY")
        @JsonProperty(value = RARITY)
        @NotNull
        String itemRarity,
    @Schema(description = "Is equipped", example = "true")
        @JsonProperty(value = IS_EQUIPPED)
        @NotNull
        Boolean isEquipped,
    @Schema(description = "Item level", example = "20")
        @JsonProperty(value = LEVEL)
        @NotNull
        @Positive
        Integer level,
    @Schema(description = "Main item stat") @JsonProperty(value = MAIN_STAT) @NotNull
        ItemStat mainStat,
    @Schema(description = "Additional item stat list")
        @JsonProperty(value = ADDITIONAL_STATS)
        @NotNull
        List<ItemStat> additionalStats)
    implements Request {

  @Serial private static final long serialVersionUID = -1116418739363127022L;
}
