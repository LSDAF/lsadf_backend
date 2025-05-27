/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.infra.persistence.game.currency;

import static com.lsadf.core.infra.persistence.game.currency.CurrencyEntity.CurrencyAttributes.*;
import static com.lsadf.core.infra.persistence.game.currency.CurrencyEntity.CurrencyAttributes.CURRENCY_ENTITY;

import com.lsadf.core.infra.persistence.config.EntityAttributes;
import com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity(name = CURRENCY_ENTITY)
@Table(name = CURRENCY_ENTITY)
@SuperBuilder
@AllArgsConstructor
@ToString(callSuper = true)
public class CurrencyEntity implements com.lsadf.core.infra.persistence.Entity {

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

  @Column(name = CURRENCY_USER_EMAIL)
  private String userEmail;

  @Column(name = CURRENCY_GOLD_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long goldAmount = 0L;

  @Column(name = CURRENCY_DIAMOND_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long diamondAmount = 0L;

  @Column(name = CURRENCY_EMERALD_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long emeraldAmount = 0L;

  @Column(name = CURRENCY_AMETHYST_AMOUNT)
  @PositiveOrZero
  @Builder.Default
  private Long amethystAmount = 0L;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class CurrencyAttributes {
    public static final String CURRENCY_ENTITY = "t_currency";
    public static final String CURRENCY_GOLD_AMOUNT = "gold_amount";
    public static final String CURRENCY_DIAMOND_AMOUNT = "diamond_amount";
    public static final String CURRENCY_EMERALD_AMOUNT = "emerald_amount";
    public static final String CURRENCY_AMETHYST_AMOUNT = "amethyst_amount";
    public static final String CURRENCY_USER_EMAIL = "user_email";
  }
}
