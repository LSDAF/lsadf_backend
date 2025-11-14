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

package com.lsadf.core.infra.web.dto.response.game.inventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lsadf.core.domain.game.inventory.ItemRarity;
import com.lsadf.core.domain.game.inventory.ItemType;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(name = "Item", description = "Item object")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemResponse(
    @Schema(description = "User Id", example = "7d9f92ce-3c8e-4695-9df7-ce10c0bbaaeb") String id,
    @Schema(
            description = "Client generated id, concatenation of inventory id and item id",
            example = "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
        String clientId,
    @Schema(description = "Blueprint id", example = "blueprint_id") String blueprintId,
    @Schema(description = "Item type", example = "BOOTS") ItemType type,
    @Schema(description = "Item rarity", example = "LEGENDARY") ItemRarity rarity,
    @Schema(description = "Is Equipped", example = "true") Boolean isEquipped,
    @Schema(description = "Item level", example = "20") Integer level,
    @Schema(description = "Main item stat") ItemStatDto mainStat,
    @Schema(description = "Additional item stat list") List<ItemStatDto> additionalStats)
    implements Response {}
