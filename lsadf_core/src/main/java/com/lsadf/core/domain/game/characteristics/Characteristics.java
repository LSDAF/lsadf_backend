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
package com.lsadf.core.domain.game.characteristics;

import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import java.util.Objects;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Characteristics implements Model {
  @Serial private static final long serialVersionUID = 5623465292659597625L;

  private Long attack;

  private Long critChance;

  private Long critDamage;

  private Long health;

  private Long resistance;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Characteristics that = (Characteristics) o;
    return Objects.equals(attack, that.attack)
        && Objects.equals(critChance, that.critChance)
        && Objects.equals(critDamage, that.critDamage)
        && Objects.equals(health, that.health)
        && Objects.equals(resistance, that.resistance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attack, critChance, critDamage, health, resistance);
  }
}
