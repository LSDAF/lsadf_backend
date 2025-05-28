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
package com.lsadf.core.infra.persistence.game.game_save;

import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.persistence.AEntity;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.game.inventory.InventoryEntity;
import com.lsadf.core.infra.persistence.game.stage.StageEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.io.Serial;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** Game Save Entity to persist data of a game save */
@Data
@Entity(name = GameSaveEntity.GameSaveAttributes.GAME_SAVE_ENTITY)
@Table(name = GameSaveEntity.GameSaveAttributes.GAME_SAVE_ENTITY)
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GameSaveEntity extends AEntity {

  @Serial private static final long serialVersionUID = 7786624859103259009L;

  protected GameSaveEntity() {
    super();
  }

  @Column(name = GameSaveAttributes.GAME_SAVE_USER_EMAIL)
  private String userEmail;

  @Column(name = GameSaveAttributes.GAME_SAVE_NICKNAME, unique = true)
  private String nickname;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = GameSaveAttributes.GAME_SAVE_CHARACTERISTICS_ID)
  private CharacteristicsEntity characteristicsEntity;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = GameSaveAttributes.GAME_SAVE_CURRENCY_ID)
  private CurrencyEntity currencyEntity;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = GameSaveAttributes.GAME_SAVE_INVENTORY_ID)
  private InventoryEntity inventoryEntity;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = GameSaveAttributes.GAME_SAVE_STAGE_ID)
  private StageEntity stageEntity;

  /**
   * Set the characteristics of the game save
   *
   * @param characteristicsEntity CharacteristicsEntity
   */
  public void setCharacteristicsEntity(CharacteristicsEntity characteristicsEntity) {
    this.characteristicsEntity = characteristicsEntity;
    characteristicsEntity.setGameSave(this);
  }

  /**
   * Set the currency of the game save
   *
   * @param currencyEntity CurrencyEntity
   */
  public void setCurrencyEntity(CurrencyEntity currencyEntity) {
    this.currencyEntity = currencyEntity;
    currencyEntity.setGameSave(this);
  }

  /**
   * Set the inventory of the game save
   *
   * @param inventoryEntity InventoryEntity
   */
  public void setInventoryEntity(InventoryEntity inventoryEntity) {
    this.inventoryEntity = inventoryEntity;
    inventoryEntity.setGameSave(this);
  }

  /**
   * Set the stage of the game save
   *
   * @param stageEntity StageEntity
   */
  public void setStageEntity(StageEntity stageEntity) {
    this.stageEntity = stageEntity;
    stageEntity.setGameSave(this);
  }

  /**
   * Set the characteristics of the game save with a characteristics POJO
   *
   * @param characteristics Characteristics
   */
  public void setCharacteristicsEntity(Characteristics characteristics) {
    if (characteristics.getAttack() != null) {
      this.characteristicsEntity.setAttack(characteristics.getAttack());
    }
    if (characteristics.getCritChance() != null) {
      this.characteristicsEntity.setCritChance(characteristics.getCritChance());
    }
    if (characteristics.getCritDamage() != null) {
      this.characteristicsEntity.setCritDamage(characteristics.getCritDamage());
    }
    if (characteristics.getHealth() != null) {
      this.characteristicsEntity.setHealth(characteristics.getHealth());
    }
    if (characteristics.getResistance() != null) {
      this.characteristicsEntity.setResistance(characteristics.getResistance());
    }
  }

  /**
   * Set the currency of the game save with a currency POJO
   *
   * @param currency Currency
   */
  public void setCurrencyEntity(Currency currency) {
    if (currency.getGold() != null) {
      this.currencyEntity.setGoldAmount(currency.getGold());
    }
    if (currency.getDiamond() != null) {
      this.currencyEntity.setDiamondAmount(currency.getDiamond());
    }
    if (currency.getEmerald() != null) {
      this.currencyEntity.setEmeraldAmount(currency.getEmerald());
    }
    if (currency.getAmethyst() != null) {
      this.currencyEntity.setAmethystAmount(currency.getAmethyst());
    }
  }

  /**
   * Set the stage of the game save with a stage POJO
   *
   * @param stage Stage
   */
  public void setStageEntity(Stage stage) {
    if (stage.getCurrentStage() != null) {
      this.stageEntity.setCurrentStage(stage.getCurrentStage());
    }
    if (stage.getMaxStage() != null) {
      this.stageEntity.setMaxStage(stage.getMaxStage());
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSaveAttributes {
    public static final String GAME_SAVE_ENTITY = "t_game_save";
    public static final String GAME_SAVE_USER_EMAIL = "user_email";
    public static final String GAME_SAVE_CHARACTERISTICS_ID = "characteristics_id";
    public static final String GAME_SAVE_CURRENCY_ID = "currency_id";
    public static final String GAME_SAVE_INVENTORY_ID = "inventory_id";
    public static final String GAME_SAVE_STAGE_ID = "stage_id";
    public static final String GAME_SAVE_NICKNAME = "nickname";
  }
}
