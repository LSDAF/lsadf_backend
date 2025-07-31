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

package com.lsadf.core.infra.persistence.table.game.inventory;

import static com.lsadf.core.infra.persistence.table.game.inventory.AdditionalItemStatEntity.AdditionalStatsEntityAttributes.*;

import com.lsadf.core.domain.game.inventory.item.ItemStatistic;
import com.lsadf.core.infra.persistence.Identifiable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Getter
@Table(ADDITIONAL_STATS_ENTITY)
public class AdditionalItemStatEntity implements Identifiable {
  @Column(ADDITIONAL_STATS_ID)
  private UUID id;

  @Column(ADDITIONAL_STATS_ITEM_ID)
  private UUID itemId;

  @Column(ADDITIONAL_STATS_ITEM_STATISTIC)
  private ItemStatistic statistic;

  @Column(ADDITIONAL_STATS_ITEM_BASE_VALUE)
  private Float baseValue;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class AdditionalStatsEntityAttributes {
    public static final String ADDITIONAL_STATS_ENTITY = "t_additional_stat_tias";
    public static final String ADDITIONAL_STATS_ID = "tias_id";
    public static final String ADDITIONAL_STATS_ITEM_ID = "tgit_id";
    public static final String ADDITIONAL_STATS_ITEM_STATISTIC = "tias_statistic";
    public static final String ADDITIONAL_STATS_ITEM_BASE_VALUE = "tias_base_value";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    AdditionalItemStatEntity that = (AdditionalItemStatEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
