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
package com.lsadf.core.domain.game.inventory.item;

import static com.lsadf.core.infra.web.JsonAttributes.BASE_VALUE;
import static com.lsadf.core.infra.web.JsonAttributes.STATISTIC;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsadf.core.shared.model.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class ItemStat implements Model {
  @JsonProperty(value = STATISTIC)
  @Schema(description = "Item stat statistic", example = "ATTACK_ADD")
  private ItemStatistic statistic;

  @JsonProperty(value = BASE_VALUE)
  @Schema(description = "Item stat base value", example = "100.0")
  @Positive
  private Float baseValue;
}
