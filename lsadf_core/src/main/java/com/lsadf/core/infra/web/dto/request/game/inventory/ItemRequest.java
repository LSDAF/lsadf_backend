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
package com.lsadf.core.infra.web.dto.request.game.inventory;

import com.lsadf.core.application.game.inventory.ItemCommand;
import com.lsadf.core.application.game.inventory.ItemStatCommand;
import com.lsadf.core.infra.web.dto.common.game.inventory.ItemStatDto;
import com.lsadf.core.infra.web.dto.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Builder
public record ItemRequest(
    @Schema(
            description = "Client generated id, concatenation of inventory id and item id",
            example = "36f27c2a-06e8-4bdb-bf59-56999116f5ef__11111111-1111-1111-1111-111111111111")
        @NotNull
        String clientId,
    @Schema(description = "Item type", example = "boots") @NotNull String type,
    @Schema(description = "Blueprint id", example = "blueprint_id") @NotNull String blueprintId,
    @Schema(description = "Item rarity", example = "LEGENDARY") @NotNull String rarity,
    @Schema(description = "Is equipped", example = "true") @NotNull Boolean isEquipped,
    @Schema(description = "Item level", example = "20") @NotNull @Positive Integer level,
    @Schema(description = "Main item stat") @NotNull ItemStatDto mainStat,
    @Schema(description = "Additional item stat list") @Nullable List<ItemStatDto> additionalStats)
    implements Request, ItemCommand {

  @Serial private static final long serialVersionUID = -1116418739363127022L;

  // Interface implementation methods
  @Override
  public String getClientId() {
    return clientId;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getBlueprintId() {
    return blueprintId;
  }

  @Override
  public String getRarity() {
    return rarity;
  }

  @Override
  public Boolean getIsEquipped() {
    return isEquipped;
  }

  @Override
  public Integer getLevel() {
    return level;
  }

  @Override
  public ItemStatCommand getMainStat() {
    return mainStat; // No casting needed!
  }

  @Override
  public List<ItemStatCommand> getAdditionalStats() {
    return additionalStats != null ? List.copyOf(additionalStats) : Collections.emptyList();
  }
}
