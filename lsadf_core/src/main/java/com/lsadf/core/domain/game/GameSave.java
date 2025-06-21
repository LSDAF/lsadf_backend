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
package com.lsadf.core.domain.game;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.shared.model.Model;
import java.io.Serial;
import java.util.Date;
import lombok.*;

/** Game Save DTO */
@Builder
@AllArgsConstructor
@Getter
public class GameSave implements Model {

  @Serial private static final long serialVersionUID = -2686370647354845265L;

  private final String id;

  private final String userEmail;

  private final Date createdAt;

  private final Date updatedAt;

  private final String nickname;

  private final Characteristics characteristics;

  private final Currency currency;

  private final Stage stage;

  public void setCharacteristics(Characteristics characteristics) {
    var attack = characteristics.getAttack();
    if (attack != null) {
      this.characteristics.setAttack(attack);
    }
    var critChance = characteristics.getCritChance();
    if (critChance != null) {
      this.characteristics.setCritChance(critChance);
    }
    var critDamage = characteristics.getCritDamage();
    if (critDamage != null) {
      this.characteristics.setCritDamage(critDamage);
    }
    var health = characteristics.getHealth();
    if (health != null) {
      this.characteristics.setHealth(health);
    }
    var resistance = characteristics.getResistance();
    if (resistance != null) {
      this.characteristics.setResistance(resistance);
    }
  }

  public void setCurrency(Currency currency) {
    var gold = currency.getGold();
    if (gold != null) {
      this.currency.setGold(gold);
    }
    var diamond = currency.getDiamond();
    if (diamond != null) {
      this.currency.setDiamond(diamond);
    }
    var emerald = currency.getEmerald();
    if (emerald != null) {
      this.currency.setEmerald(emerald);
    }
    var amethyst = currency.getAmethyst();
    if (amethyst != null) {
      this.currency.setAmethyst(amethyst);
    }
  }

  public void setStage(Stage stage) {
    var currentStage = stage.getCurrentStage();
    if (currentStage != null) {
      this.stage.setCurrentStage(currentStage);
    }
    var maxStage = stage.getMaxStage();
    if (maxStage != null) {
      this.stage.setMaxStage(maxStage);
    }
  }
}
