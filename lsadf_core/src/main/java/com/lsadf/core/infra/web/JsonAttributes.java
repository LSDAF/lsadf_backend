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
package com.lsadf.core.infra.web;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constant class containing all property names for JSON transfer objects.
 *
 * <p>This utility class defines string constants used as attribute names in JSON serialization and
 * deserialization. These constants help maintain consistency in API responses and requests
 * throughout the application.
 *
 * <p>The class is final and has a private constructor to prevent instantiation as it only contains
 * static constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonAttributes {
  /** Unique identifier attribute name used across different entities. */
  public static final String ID = "id";

  /** Timestamp attribute name indicating when an entity was created. */
  public static final String CREATED_AT = "created_at";

  /** Timestamp attribute name indicating when an entity was last updated. */
  public static final String UPDATED_AT = "updated_at";

  /**
   * Contains JSON attribute names for game save data.
   *
   * <p>These constants are used for serializing and deserializing game save information in API
   * communications, corresponding to properties in {@link
   * com.lsadf.core.infra.persistence.game.game_save.GameSaveEntity}.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameSave {
    /** User email associated with the game save. */
    public static final String USER_EMAIL = "user_email";

    /** Unique nickname for the game save. */
    public static final String NICKNAME = "nickname";

    /** Character statistics and attributes. */
    public static final String CHARACTERISTICS = "characteristics";

    /** In-game currency amounts. */
    public static final String CURRENCY = "currency";

    /** Game progression information. */
    public static final String STAGE = "stage";
  }

  /**
   * Contains JSON attribute names for global system information.
   *
   * <p>These constants are used for serializing system-wide statistics and information that provide
   * overview data about the application state.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GlobalInfo {
    /** Current server timestamp. */
    public static final String NOW = "now";

    /** Total number of game saves in the system. */
    public static final String GAME_SAVE_COUNTER = "game_save_counter";

    /** Total number of registered users. */
    public static final String USER_COUNTER = "user_counter";
  }

  /**
   * Contains JSON attribute names for user account data.
   *
   * <p>These constants are used for serializing and deserializing user information in
   * authentication and user management operations. They correspond to the user entities in the
   * authentication system (likely Keycloak based on imports).
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class User {
    /** User's first name. */
    public static final String FIRST_NAME = "first_name";

    /** User's last name. */
    public static final String LAST_NAME = "last_name";

    /** Unique username for authentication. */
    public static final String USERNAME = "username";

    /** Timestamp when the user account was created. */
    public static final String CREATED_TIMESTAMP = "created_timestamp";

    /** User's password (only used in request objects, never in responses). */
    public static final String PASSWORD = "password";

    /** Flag indicating if the user account is active. */
    public static final String ENABLED = "enabled";

    /** Flag indicating if the user's email has been verified. */
    public static final String EMAIL_VERIFIED = "email_verified";

    /** Collection of roles assigned to the user. */
    public static final String USER_ROLES = "user_roles";
  }

  /**
   * Contains JSON attribute names for search request parameters.
   *
   * <p>These constants define the structure of search request payloads used when filtering or
   * searching for entities in the system.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class SearchRequest {
    /** Collection of filter criteria to apply in the search. */
    public static final String FILTERS = "filters";
  }

  /**
   * Contains JSON attribute names for game stage progression data.
   *
   * <p>These constants are used for serializing and deserializing a player's progress through game
   * stages, corresponding to properties in {@link com.lsadf.core.domain.game.stage.Stage} and
   * {@link com.lsadf.core.infra.persistence.game.stage.StageEntity}.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Stage {
    /** Player's current stage in the game. */
    public static final String CURRENT_STAGE = "current_stage";

    /** Highest stage reached by the player. */
    public static final String MAX_STAGE = "max_stage";
  }

  /**
   * Contains JSON attribute names for character statistics.
   *
   * <p>These constants define the attributes that represent a character's combat abilities and
   * stats, corresponding to properties in {@link
   * com.lsadf.core.domain.game.characteristics.Characteristics} and {@link
   * com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity}.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Characteristics {
    /** Character's attack power. */
    public static final String ATTACK = "attack";

    /** Probability of landing a critical hit. */
    public static final String CRIT_CHANCE = "crit_chance";

    /** Damage multiplier applied on critical hits. */
    public static final String CRIT_DAMAGE = "crit_damage";

    /** Character's health points. */
    public static final String HEALTH = "health";

    /** Character's damage reduction capability. */
    public static final String RESISTANCE = "resistance";
  }

  /**
   * Contains JSON attribute names for in-game currency data.
   *
   * <p>These constants define the different types of currency that players can accumulate and spend
   * in the game, corresponding to properties in {@link
   * com.lsadf.core.domain.game.currency.Currency} and {@link
   * com.lsadf.core.infra.persistence.game.currency.CurrencyEntity}.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Currency {
    /** Common currency used for basic transactions. */
    public static final String GOLD = "gold";

    /** Premium currency typically used for special items or features. */
    public static final String DIAMOND = "diamond";

    /** Special currency with specific game purposes. */
    public static final String EMERALD = "emerald";

    /** Rare currency typically used for high-value items. */
    public static final String AMETHYST = "amethyst";
  }

  /**
   * Contains JSON attribute names for player inventory data.
   *
   * <p>These constants define the structure of a player's inventory in JSON format, corresponding
   * to properties in {@link com.lsadf.core.infra.persistence.game.inventory.InventoryEntity}.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Inventory {
    /** Collection of items in the player's inventory. */
    public static final String ITEMS = "items";
  }

  /**
   * Contains JSON attribute names for game item data.
   *
   * <p>These constants define the properties of items that players can collect, equip, and use in
   * the game. They correspond to item-related entities in the persistence layer.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Item {
    /** Client-side identifier for the item. */
    public static final String CLIENT_ID = "client_id";

    /** Category or classification of the item. */
    public static final String TYPE = "type";

    /** Reference to the item's template/definition. */
    public static final String BLUEPRINT_ID = "blueprint_id";

    /** Rarity level of the item (common, rare, epic, etc.). */
    public static final String RARITY = "rarity";

    /** Flag indicating if the item is currently equipped by the player. */
    public static final String IS_EQUIPPED = "is_equipped";

    /** Current upgrade level of the item. */
    public static final String LEVEL = "level";

    /** Primary statistic provided by the item. */
    public static final String MAIN_STAT = "main_stat";

    /** Secondary statistics provided by the item. */
    public static final String ADDITIONAL_STATS = "additional_stats";
  }

  /**
   * Contains JSON attribute names for item statistic data.
   *
   * <p>These constants define the properties of statistics that items can provide to the player
   * character. They represent the structure of statistical bonuses in the item system.
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class ItemStat {
    /** Type of statistic (attack, health, etc.). */
    public static final String STATISTIC = "statistic";

    /** Numerical value of the statistic bonus. */
    public static final String BASE_VALUE = "base_value";
  }
}
