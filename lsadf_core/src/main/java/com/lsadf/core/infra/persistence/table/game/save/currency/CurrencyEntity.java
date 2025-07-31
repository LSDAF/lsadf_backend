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
package com.lsadf.core.infra.persistence.table.game.save.currency;

import static com.lsadf.core.infra.persistence.table.game.save.currency.CurrencyEntity.CurrencyEntityAttributes.*;
import static com.lsadf.core.infra.persistence.table.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.GAME_METADATA_ID;

import com.lsadf.core.infra.persistence.Identifiable;
import java.io.Serial;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Table(CURRENCY_ENTITY)
public class CurrencyEntity implements Identifiable {

  @Serial private static final long serialVersionUID = 1142981637978170899L;

  @Id
  @Column(GAME_METADATA_ID)
  private UUID id;

  @Column(CURRENCY_GOLD_AMOUNT)
  private Long goldAmount;

  @Column(CURRENCY_DIAMOND_AMOUNT)
  private Long diamondAmount;

  @Column(CURRENCY_EMERALD_AMOUNT)
  private Long emeraldAmount;

  @Column(CURRENCY_AMETHYST_AMOUNT)
  private Long amethystAmount;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class CurrencyEntityAttributes {
    public static final String CURRENCY_ENTITY = "t_currency_tgcu";
    public static final String CURRENCY_GOLD_AMOUNT = "tgcu_gold_amount";
    public static final String CURRENCY_DIAMOND_AMOUNT = "tgcu_diamond_amount";
    public static final String CURRENCY_EMERALD_AMOUNT = "tgcu_emerald_amount";
    public static final String CURRENCY_AMETHYST_AMOUNT = "tgcu_amethyst_amount";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    CurrencyEntity that = (CurrencyEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(goldAmount, that.goldAmount)
        && Objects.equals(diamondAmount, that.diamondAmount)
        && Objects.equals(emeraldAmount, that.emeraldAmount)
        && Objects.equals(amethystAmount, that.amethystAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, goldAmount, diamondAmount, emeraldAmount, amethystAmount);
  }
}
