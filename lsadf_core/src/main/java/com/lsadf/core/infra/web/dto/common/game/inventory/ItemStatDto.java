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

package com.lsadf.core.infra.web.dto.common.game.inventory;

import com.lsadf.core.application.game.inventory.ItemStatCommand;
import com.lsadf.core.domain.game.inventory.ItemStatistic;
import com.lsadf.core.infra.web.dto.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemStatDto(
    @Schema(description = "Item stat statistic", example = "ATTACK_ADD") @NotNull
        ItemStatistic statistic,
    @Schema(description = "Item stat base value", example = "100.0") @Positive Float baseValue)
    implements Dto, ItemStatCommand {

  // Interface implementation methods
  @Override
  public ItemStatistic getStatistic() {
    return statistic;
  }

  @Override
  public Float getBaseValue() {
    return baseValue;
  }
}
