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
package com.lsadf.core.infra.persistence.table.game.characteristics;

import static com.lsadf.core.infra.persistence.config.EntityAttributes.ID;
import static com.lsadf.core.infra.persistence.table.game.characteristics.CharacteristicsEntity.CharacteristicsEntityAttributes.*;

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
@Table(CHARACTERISTICS_ENTITY)
public class CharacteristicsEntity implements Identifiable {

  @Serial private static final long serialVersionUID = 2100417080983225675L;

  protected CharacteristicsEntity() {
    super();
  }

  @Id
  @Column(ID)
  private UUID id;

  @Column(CHARACTERISTICS_ATTACK)
  private Long attack;

  @Column(CHARACTERISTICS_CRIT_CHANCE)
  private Long critChance;

  @Column(CHARACTERISTICS_CRIT_DAMAGE)
  private Long critDamage;

  @Column(CHARACTERISTICS_HEALTH)
  private Long health;

  @Column(CHARACTERISTICS_RESISTANCE)
  private Long resistance;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class CharacteristicsEntityAttributes {
    public static final String CHARACTERISTICS_ENTITY = "t_characteristics_tgch";
    public static final String CHARACTERISTICS_ATTACK = "attack";
    public static final String CHARACTERISTICS_CRIT_CHANCE = "crit_chance";
    public static final String CHARACTERISTICS_CRIT_DAMAGE = "crit_damage";
    public static final String CHARACTERISTICS_HEALTH = "health";
    public static final String CHARACTERISTICS_RESISTANCE = "resistance";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    CharacteristicsEntity that = (CharacteristicsEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(attack, that.attack)
        && Objects.equals(critChance, that.critChance)
        && Objects.equals(critDamage, that.critDamage)
        && Objects.equals(health, that.health)
        && Objects.equals(resistance, that.resistance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, attack, critChance, critDamage, health, resistance);
  }
}
