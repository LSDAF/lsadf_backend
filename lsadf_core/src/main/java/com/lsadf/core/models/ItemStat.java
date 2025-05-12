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
package com.lsadf.core.models;

import static com.lsadf.core.constants.JsonAttributes.ItemStat.BASE_VALUE;
import static com.lsadf.core.constants.JsonAttributes.ItemStat.STATISTIC;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.core.constants.item.ItemStatistic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class ItemStat implements Model {
  @JsonView(JsonViews.External.class)
  @JsonProperty(value = STATISTIC)
  @Schema(description = "Item stat statistic", example = "ATTACK_ADD")
  @Enumerated(EnumType.STRING)
  private ItemStatistic statistic;

  @JsonView(JsonViews.External.class)
  @JsonProperty(value = BASE_VALUE)
  @Schema(description = "Item stat base value", example = "100.0")
  @Positive
  private Float baseValue;
}
