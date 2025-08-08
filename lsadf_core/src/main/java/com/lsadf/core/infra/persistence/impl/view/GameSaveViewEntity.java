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

package com.lsadf.core.infra.persistence.impl.view;

import static com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity.CharacteristicsEntityAttributes.*;
import static com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity.CharacteristicsEntityAttributes.CHARACTERISTICS_HEALTH;
import static com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity.CharacteristicsEntityAttributes.CHARACTERISTICS_RESISTANCE;
import static com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntity.CurrencyEntityAttributes.*;
import static com.lsadf.core.infra.persistence.impl.game.save.metadata.GameMetadataEntity.GameSaveMetadataAttributes.*;
import static com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity.StageEntityAttributes.*;
import static com.lsadf.core.infra.persistence.impl.view.GameSaveViewEntity.GameSaveViewAttributes.GAME_SAVE_VIEW_ENTITY;

import com.lsadf.core.infra.persistence.Dateable;
import com.lsadf.core.infra.persistence.Identifiable;
import com.lsadf.core.infra.persistence.impl.game.save.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.impl.game.save.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.impl.game.save.stage.StageEntity;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(GAME_SAVE_VIEW_ENTITY)
public class GameSaveViewEntity implements Identifiable, Dateable {

  // Game Save
  @Id
  @Column(GAME_METADATA_ID)
  private UUID id;

  @Column(GAME_METADATA_CREATED_AT)
  private Date createdAt;

  @Column(GAME_METADATA_UPDATED_AT)
  private Date updatedAt;

  @Column(GAME_METADATA_USER_EMAIL)
  private String userEmail;

  @Column(GAME_METADATA_NICKNAME)
  private String nickname;

  // Stage
  @Column(STAGE_CURRENT_STAGE)
  private Long currentStage;

  @Column(STAGE_MAX_STAGE)
  private Long maxStage;

  // Characteristics
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

  // Currency
  @Column(CURRENCY_GOLD_AMOUNT)
  private Long goldAmount;

  @Column(CURRENCY_DIAMOND_AMOUNT)
  private Long diamondAmount;

  @Column(CURRENCY_EMERALD_AMOUNT)
  private Long emeraldAmount;

  @Column(CURRENCY_AMETHYST_AMOUNT)
  private Long amethystAmount;

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSaveViewAttributes {
    public static final String GAME_SAVE_VIEW_ENTITY = "v_game_save_vgsa";
  }

  /**
   * Constructs and retrieves an instance of {@link CharacteristicsEntity} containing the
   * characteristics data of the current entity.
   *
   * @return a {@link CharacteristicsEntity} object populated with attack, critical chance, critical
   *     damage, health, resistance, and the associated unique identifier.
   */
  public CharacteristicsEntity getCharacteristicsEntity() {
    return new CharacteristicsEntity(id, attack, critChance, critDamage, health, resistance);
  }

  /**
   * Constructs and retrieves an instance of {@link CurrencyEntity} containing the currency data of
   * the current entity.
   *
   * @return a {@link CurrencyEntity} object populated with the unique identifier, gold amount,
   *     diamond amount, emerald amount, and amethyst amount.
   */
  public CurrencyEntity getCurrencyEntity() {
    return new CurrencyEntity(id, goldAmount, diamondAmount, emeraldAmount, amethystAmount);
  }

  /**
   * Constructs and retrieves an instance of {@link StageEntity} containing the stage data of the
   * current entity.
   *
   * @return a {@link StageEntity} object populated with the unique identifier, current stage, and
   *     maximum stage values.
   */
  public StageEntity getStageEntity() {
    return new StageEntity(id, currentStage, maxStage);
  }
}
