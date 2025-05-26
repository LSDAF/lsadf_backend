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
package com.lsadf.core.infra.web.requests.game.game_save;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.lsadf.core.constants.JsonAttributes;
import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.web.requests.common.SortingParameter;
import java.util.Comparator;
import org.springframework.data.domain.Sort;

public enum GameSaveSortingParameter implements SortingParameter<GameSave> {

  // Game Save
  ID(JsonAttributes.ID, ASC, Comparator.comparing(GameSave::getId)),
  ID_DESC(JsonAttributes.ID, DESC, Comparator.comparing(GameSave::getId).reversed()),
  USER_EMAIL(JsonAttributes.GameSave.USER_EMAIL, ASC, Comparator.comparing(GameSave::getUserEmail)),
  USER_EMAIL_DESC(
      JsonAttributes.GameSave.USER_EMAIL,
      DESC,
      Comparator.comparing(GameSave::getUserEmail).reversed()),
  CREATED_AT(JsonAttributes.CREATED_AT, ASC, Comparator.comparing(GameSave::getCreatedAt)),
  CREATED_AT_DESC(
      JsonAttributes.CREATED_AT, DESC, Comparator.comparing(GameSave::getCreatedAt).reversed()),
  UPDATED_AT(JsonAttributes.UPDATED_AT, ASC, Comparator.comparing(GameSave::getUpdatedAt)),
  UPDATED_AT_DESC(
      JsonAttributes.UPDATED_AT, DESC, Comparator.comparing(GameSave::getUpdatedAt).reversed()),
  NICKNAME(JsonAttributes.GameSave.NICKNAME, ASC, Comparator.comparing(GameSave::getNickname)),
  NICKNAME_DESC(
      JsonAttributes.GameSave.NICKNAME,
      DESC,
      Comparator.comparing(GameSave::getNickname).reversed()),

  // Currency
  GOLD(
      JsonAttributes.Currency.GOLD,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCurrency().getGold())),
  GOLD_DESC(
      JsonAttributes.Currency.GOLD,
      DESC,
      (o1, o2) -> o2.getCurrency().getGold().compareTo(o1.getCurrency().getGold())),
  DIAMOND(
      JsonAttributes.Currency.DIAMOND,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCurrency().getDiamond())),
  DIAMOND_DESC(
      JsonAttributes.Currency.DIAMOND,
      DESC,
      (o1, o2) -> o2.getCurrency().getDiamond().compareTo(o1.getCurrency().getDiamond())),
  EMERALD(
      JsonAttributes.Currency.EMERALD,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCurrency().getEmerald())),
  EMERALD_DESC(
      JsonAttributes.Currency.EMERALD,
      DESC,
      (o1, o2) -> o2.getCurrency().getEmerald().compareTo(o1.getCurrency().getEmerald())),
  AMETHYST(
      JsonAttributes.Currency.AMETHYST,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCurrency().getAmethyst())),
  AMETHYST_DESC(
      JsonAttributes.Currency.AMETHYST,
      DESC,
      (o1, o2) -> o2.getCurrency().getAmethyst().compareTo(o1.getCurrency().getAmethyst())),

  // Stage
  CURRENT_STAGE(
      JsonAttributes.Stage.CURRENT_STAGE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getStage().getCurrentStage())),
  CURRENT_STAGE_DESC(
      JsonAttributes.Stage.CURRENT_STAGE,
      DESC,
      (o1, o2) -> o2.getStage().getCurrentStage().compareTo(o1.getStage().getCurrentStage())),
  MAX_STAGE(
      JsonAttributes.Stage.MAX_STAGE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getStage().getMaxStage())),
  MAX_STAGE_DESC(
      JsonAttributes.Stage.MAX_STAGE,
      DESC,
      (o1, o2) -> o2.getStage().getMaxStage().compareTo(o1.getStage().getMaxStage())),

  // Characteristics
  ATTACK(
      JsonAttributes.Characteristics.ATTACK,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().getAttack())),
  ATTACK_DESC(
      JsonAttributes.Characteristics.ATTACK,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics().getAttack().compareTo(o1.getCharacteristics().getAttack())),
  CRIT_CHANCE(
      JsonAttributes.Characteristics.CRIT_CHANCE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().getCritChance())),
  CRIT_CHANCE_DESC(
      JsonAttributes.Characteristics.CRIT_CHANCE,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics()
              .getCritChance()
              .compareTo(o1.getCharacteristics().getCritChance())),
  CRIT_DAMAGE(
      JsonAttributes.Characteristics.CRIT_DAMAGE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().getCritDamage())),
  CRIT_DAMAGE_DESC(
      JsonAttributes.Characteristics.CRIT_DAMAGE,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics()
              .getCritDamage()
              .compareTo(o1.getCharacteristics().getCritDamage())),
  HEALTH(
      JsonAttributes.Characteristics.HEALTH,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().getHealth())),
  HEALTH_DESC(
      JsonAttributes.Characteristics.HEALTH,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics().getHealth().compareTo(o1.getCharacteristics().getHealth())),
  RESISTANCE(
      JsonAttributes.Characteristics.RESISTANCE,
      ASC,
      Comparator.comparing(gameSave -> gameSave.getCharacteristics().getResistance())),
  RESISTANCE_DESC(
      JsonAttributes.Characteristics.RESISTANCE,
      DESC,
      (o1, o2) ->
          o2.getCharacteristics()
              .getResistance()
              .compareTo(o1.getCharacteristics().getResistance())),

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
