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
package com.lsadf.core.infra.web.dto.request.game.save;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.infra.web.JsonAttributes;
import com.lsadf.core.infra.web.dto.request.common.SortingParameter;
import java.util.Comparator;
import org.springframework.data.domain.Sort;

public enum GameSaveSortingParameter implements SortingParameter<GameSave> {

  // Game Save
  ID(JsonAttributes.ID, ASC, Comparator.comparing(gs -> gs.getMetadata().id())),
  ID_DESC(
      JsonAttributes.ID,
      DESC,
      ((gs1, gs2) -> gs2.getMetadata().id().compareTo(gs1.getMetadata().id()))),
  USER_EMAIL(
      JsonAttributes.USER_EMAIL, ASC, Comparator.comparing(gs -> gs.getMetadata().userEmail())),
  USER_EMAIL_DESC(
      JsonAttributes.USER_EMAIL,
      DESC,
      ((gs1, gs2) -> gs2.getMetadata().userEmail().compareTo(gs1.getMetadata().userEmail()))),
  CREATED_AT(
      JsonAttributes.CREATED_AT, ASC, Comparator.comparing(gs -> gs.getMetadata().createdAt())),
  CREATED_AT_DESC(
      JsonAttributes.CREATED_AT,
      DESC,
      (gs1, gs2) -> gs2.getMetadata().createdAt().compareTo(gs1.getMetadata().createdAt())),
  UPDATED_AT(
      JsonAttributes.UPDATED_AT, ASC, Comparator.comparing(gs -> gs.getMetadata().updatedAt())),
  UPDATED_AT_DESC(
      JsonAttributes.UPDATED_AT,
      DESC,
      (gs1, gs2) -> gs2.getMetadata().updatedAt().compareTo(gs1.getMetadata().updatedAt())),
  NICKNAME(JsonAttributes.NICKNAME, ASC, Comparator.comparing(gs -> gs.getMetadata().nickname())),
  NICKNAME_DESC(
      JsonAttributes.NICKNAME,
      DESC,
      (gs1, gs2) -> gs2.getMetadata().nickname().compareTo(gs1.getMetadata().nickname())),

  // Currency
  GOLD(JsonAttributes.GOLD, ASC, Comparator.comparing(gameSave -> gameSave.getCurrency().gold())),
  GOLD_DESC(
      JsonAttributes.GOLD,
      DESC,
      (o1, o2) -> o2.getCurrency().gold().compareTo(o1.getCurrency().gold())),
  DIAMOND(
      JsonAttributes.DIAMOND,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCurrency().diamond())),
  DIAMOND_DESC(
      JsonAttributes.DIAMOND,
      DESC,
      (o1, o2) -> o2.getCurrency().diamond().compareTo(o1.getCurrency().diamond())),
  EMERALD(
      JsonAttributes.EMERALD,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCurrency().emerald())),
  EMERALD_DESC(
      JsonAttributes.EMERALD,
      DESC,
      (o1, o2) -> o2.getCurrency().emerald().compareTo(o1.getCurrency().emerald())),
  AMETHYST(
      JsonAttributes.AMETHYST,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCurrency().amethyst())),
  AMETHYST_DESC(
      JsonAttributes.AMETHYST,
      DESC,
      (o1, o2) -> o2.getCurrency().amethyst().compareTo(o1.getCurrency().amethyst())),

  // Stage
  CURRENT_STAGE(
      JsonAttributes.CURRENT_STAGE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getStage().currentStage())),
  CURRENT_STAGE_DESC(
      JsonAttributes.CURRENT_STAGE,
      DESC,
      (o1, o2) -> o2.getStage().currentStage().compareTo(o1.getStage().currentStage())),
  MAX_STAGE(
      JsonAttributes.MAX_STAGE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getStage().maxStage())),
  MAX_STAGE_DESC(
      JsonAttributes.MAX_STAGE,
      DESC,
      (o1, o2) -> o2.getStage().maxStage().compareTo(o1.getStage().maxStage())),
  WAVE(JsonAttributes.WAVE, ASC, Comparator.comparing(gameSave -> gameSave.getStage().wave())),
  WAVE_DESC(
      JsonAttributes.WAVE, DESC, (o1, o2) -> o2.getStage().wave().compareTo(o1.getStage().wave())),
  // Characteristics
  ATTACK(
      JsonAttributes.ATTACK,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().attack())),
  ATTACK_DESC(
      JsonAttributes.ATTACK,
      DESC,
      (o1, o2) -> o2.getCharacteristics().attack().compareTo(o1.getCharacteristics().attack())),
  CRIT_CHANCE(
      JsonAttributes.CRIT_CHANCE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().critChance())),
  CRIT_CHANCE_DESC(
      JsonAttributes.CRIT_CHANCE,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics().critChance().compareTo(o1.getCharacteristics().critChance())),
  CRIT_DAMAGE(
      JsonAttributes.CRIT_DAMAGE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().critDamage())),
  CRIT_DAMAGE_DESC(
      JsonAttributes.CRIT_DAMAGE,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics().critDamage().compareTo(o1.getCharacteristics().critDamage())),
  HEALTH(
      JsonAttributes.HEALTH,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().health())),
  HEALTH_DESC(
      JsonAttributes.HEALTH,
      DESC,
      (o1, o2) -> o2.getCharacteristics().health().compareTo(o1.getCharacteristics().health())),
  RESISTANCE(
      JsonAttributes.RESISTANCE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().resistance())),
  RESISTANCE_DESC(
      JsonAttributes.RESISTANCE,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics().resistance().compareTo(o1.getCharacteristics().resistance())),

  // Misc
  NONE(null, null, null);

  GameSaveSortingParameter(
      String fieldName, Sort.Direction direction, Comparator<GameSave> comparator) {
    this.fieldName = fieldName;
    this.direction = direction;
    this.comparator = comparator;
  }

  private final String fieldName;
  private final Sort.Direction direction;
  private final Comparator<GameSave> comparator;

  @Override
  public String getFieldName() {
    return this.fieldName;
  }

  @Override
  public Sort.Direction getDirection() {
    return this.direction;
  }

  @Override
  public Comparator<GameSave> getComparator() {
    return this.comparator;
  }

  /**
   * Get the sorting parameter from a string
   *
   * @param parameter the parameter
   * @return the sorting parameter
   */
  public static GameSaveSortingParameter fromString(String parameter) {
    for (GameSaveSortingParameter gameSaveOrderBy : GameSaveSortingParameter.values()) {
      if (gameSaveOrderBy.name().equalsIgnoreCase(parameter)) {
        return gameSaveOrderBy;
      }
    }
    throw new IllegalArgumentException("Invalid sorting parameter");
  }

  /**
   * Get the sorting parameter from a Sort.Order object
   *
   * @param order the order
   * @return the sorting parameter
   */
  public static GameSaveSortingParameter fromOrder(Sort.Order order) {
    for (GameSaveSortingParameter gameSaveOrderBy : GameSaveSortingParameter.values()) {
      if (gameSaveOrderBy.getFieldName().equalsIgnoreCase(order.getProperty())
          && gameSaveOrderBy.getDirection().equals(order.getDirection())) {
        return gameSaveOrderBy;
      }
    }
    throw new IllegalArgumentException("Invalid order by parameter");
  }
}
