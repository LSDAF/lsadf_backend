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
package com.lsadf.core.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Constant class containing all property names for database entities */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityAttributes {

  // COMMON
  public static final String ID = "id";
  public static final String CREATED_AT = "created_at";
  public static final String UPDATED_AT = "updated_at";

  // COMMON TOKEN
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Token {
    public static final String TOKEN = "token";
    public static final String STATUS = "status";
    public static final String EXPIRATION_DATE = "expiration_date";
    public static final String INVALIDATION_DATE = "invalidation_date";
  }

  // JWT TOKEN
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class JwtToken {
    public static final String JWT_TOKEN_ENTITY = "t_jwt_token";
    public static final String JWT_TOKEN_TOKEN = "token";
    public static final String JWT_TOKEN_STATUS = "status";
    public static final String JWT_TOKEN_EXPIRATION_DATE = "expiration_date";
    public static final String JWT_TOKEN_INVALIDATION_DATE = "invalidation_date";
  }

  // REFRESH TOKEN
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RefreshToken {
    public static final String REFRESH_TOKEN_ENTITY = "t_refresh_token";
    public static final String REFRESH_TOKEN_TOKEN = "token";
    public static final String REFRESH_TOKEN_STATUS = "status";
    public static final String REFRESH_TOKEN_EXPIRATION_DATE = "expiration_date";
    public static final String REFRESH_TOKEN_INVALIDATION_DATE = "invalidation_date";
  }

  // GAME SAVE
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class GameSave {
    public static final String GAME_SAVE_ENTITY = "t_game_save";
    public static final String GAME_SAVE_USER_EMAIL = "user_email";
    public static final String GAME_SAVE_CHARACTERISTICS_ID = "characteristics_id";
    public static final String GAME_SAVE_CURRENCY_ID = "currency_id";
    public static final String GAME_SAVE_INVENTORY_ID = "inventory_id";
    public static final String GAME_SAVE_STAGE_ID = "stage_id";
    public static final String GAME_SAVE_NICKNAME = "nickname";
  }

  // INVENTORY
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Inventory {
    public static final String INVENTORY_ENTITY = "t_inventory";
  }

  // ITEMS
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Items {
    public static final String ITEM_ENTITY = "t_item";
    public static final String ITEM_ADDITIONAL_STATS = "t_additional_stats";
    public static final String ITEM_CLIENT_ID = "client_id";
    public static final String ITEM_BLUEPRINT_ID = "blueprint_id";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_RARITY = "rarity";
    public static final String ITEM_IS_EQUIPPED = "is_equipped";
    public static final String ITEM_LEVEL = "level";
    public static final String ITEM_MAIN_STAT = "main_stat";
  }

  // STAGES
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Stages {
    public static final String STAGE_ENTITY = "t_stage";
    public static final String STAGE_CURRENT = "current_stage";
    public static final String STAGE_MAX = "max_stage";
    public static final String STAGE_USER_EMAIL = "user_email";
  }

  // CHARACTERISTICS
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Characteristics {
    public static final String CHARACTERISTICS_ENTITY = "t_characteristics";
    public static final String CHARACTERISTICS_ATTACK = "attack";
    public static final String CHARACTERISTICS_CRIT_CHANCE = "crit_chance";
    public static final String CHARACTERISTICS_CRIT_DAMAGE = "crit_damage";
    public static final String CHARACTERISTICS_HEALTH = "health";
    public static final String CHARACTERISTICS_RESISTANCE = "resistance";
  }

  // CURRENCIES
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Currencies {
    public static final String CURRENCY_ENTITY = "t_currency";
    public static final String CURRENCY_GOLD_AMOUNT = "gold_amount";
    public static final String CURRENCY_DIAMOND_AMOUNT = "diamond_amount";
    public static final String CURRENCY_EMERALD_AMOUNT = "emerald_amount";
    public static final String CURRENCY_AMETHYST_AMOUNT = "amethyst_amount";
    public static final String CURRENCY_USER_EMAIL = "user_email";
  }

  // USER
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class User {
    public static final String USER_ENTITY = "t_user";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLES = "roles";
    public static final String USER_ENABLED = "enabled";
    public static final String USER_VERIFIED = "verified";
  }

  // USER VERIFICATION TOKEN
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class UserVerificationToken {
    public static final String USER_VERIFICATION_TOKEN_ENTITY = "t_user_verification_token";
    public static final String USER_VERIFICATION_TOKEN_VALIDATION_TOKEN = "verification_token";
    public static final String USER_VERIFICATION_TOKEN_USED = "used";
    public static final String USER_VERIFICATION_TOKEN_EXPIRATION_DATE = "expiration_date";
  }
}
