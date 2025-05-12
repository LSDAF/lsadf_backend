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
package com.lsadf.core.entities;

import com.lsadf.core.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity(name = EntityAttributes.Currencies.CURRENCY_ENTITY)
@Table(name = EntityAttributes.Currencies.CURRENCY_ENTITY)
@SuperBuilder
@AllArgsConstructor
@ToString(callSuper = true)
public class CurrencyEntity implements com.lsadf.core.entities.Entity {

  @Serial private static final long serialVersionUID = 1142981637978170899L;

  protected CurrencyEntity() {
    super();
  }

  @Id
  @Column(name = EntityAttributes.ID)
  private String id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private GameSaveEntity gameSave;

  @Column(name = EntityAttributes.Currencies.CURRENCY_USER_EMAIL)
  private String userEmail;

  @Column(name = EntityAttributes.Currencies.CURRENCY_GOLD_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long goldAmount = 0L;

  @Column(name = EntityAttributes.Currencies.CURRENCY_DIAMOND_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long diamondAmount = 0L;

  @Column(name = EntityAttributes.Currencies.CURRENCY_EMERALD_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long emeraldAmount = 0L;

  @Column(name = EntityAttributes.Currencies.CURRENCY_AMETHYST_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long amethystAmount = 0L;
}
