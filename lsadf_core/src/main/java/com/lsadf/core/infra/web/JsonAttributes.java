/*
 * Copyright © 2024-2026 LSDAF
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
  public static final String ID = "id";

  public static final String CREATED_AT = "created_at";
  public static final String CREATED_AT_CAMEL_CASE = "createdAt";

  public static final String UPDATED_AT = "updated_at";
  public static final String UPDATED_AT_CAMEL_CASE = "updatedAt";

  public static final String TYPE = "type";
  public static final String VALUE = "value";

  public static final String USER_EMAIL = "user_email";
  public static final String USER_EMAIL_CAMEL_CASE = "userEmail";

  public static final String METADATA = "metadata";

  public static final String NICKNAME = "nickname";

  public static final String CHARACTERISTICS = "characteristics";

  public static final String CURRENCY = "currency";

  public static final String STAGE = "stage";

  public static final String NOW = "now";

  public static final String GAME_SAVE_COUNTER = "game_save_counter";

  public static final String USER_COUNTER = "user_counter";

  public static final String FIRST_NAME = "first_name";
  public static final String FIRST_NAME_CAMEL_CASE = "firstName";

  public static final String LAST_NAME = "last_name";
  public static final String LAST_NAME_CAMEL_CASE = "lastName";

  public static final String USERNAME = "username";

  public static final String CREATED_TIMESTAMP = "created_timestamp";
  public static final String CREATED_TIMESTAMP_CAMEL_CASE = "createdTimestamp";

  public static final String PASSWORD = "password";
  public static final String STATUS = "status";
  public static final String MESSAGE = "message";
  public static final String DATA = "data";
  public static final String ENABLED = "enabled";
  public static final String NAME = "name";
  public static final String ROLES = "roles";
  public static final String EMAIL_VERIFIED = "email_verified";
  public static final String USER_ROLES = "user_roles";
  public static final String USER_ROLES_CAMEL_CASE = "userRoles";
  public static final String VERIFIED = "verified";
  public static final String FILTERS = "filters";
  public static final String CURRENT_STAGE = "current_stage";
  public static final String CURRENT_STAGE_CAMEL_CASE = "currentStage";
  public static final String MAX_STAGE = "max_stage";
  public static final String MAX_STAGE_CAMEL_CASE = "maxStage";
  public static final String WAVE = "wave";
  public static final String ATTACK = "attack";
  public static final String CRIT_CHANCE = "crit_chance";
  public static final String CRIT_CHANCE_CAMEL_CASE = "critChance";
  public static final String CRIT_DAMAGE = "crit_damage";
  public static final String CRIT_DAMAGE_CAMEL_CASE = "critDamage";
  public static final String HEALTH = "health";
  public static final String RESISTANCE = "resistance";
  public static final String GOLD = "gold";
  public static final String DIAMOND = "diamond";
  public static final String EMERALD = "emerald";
  public static final String AMETHYST = "amethyst";
  public static final String ITEMS = "items";
  public static final String CLIENT_ID = "client_id";
  public static final String BLUEPRINT_ID = "blueprint_id";
  public static final String RARITY = "rarity";
  public static final String IS_EQUIPPED = "is_equipped";
  public static final String LEVEL = "level";
  public static final String MAIN_STAT = "main_stat";
  public static final String ADDITIONAL_STATS = "additional_stats";
  public static final String STATISTIC = "statistic";
  public static final String BASE_VALUE = "base_value";
  public static final String ACCESS_TOKEN = "access_token";
  public static final String REFRESH_TOKEN = "refresh_token";
  public static final String REFRESH_EXPIRES_IN = "refresh_expires_in";
  public static final String EXPIRES_IN = "expires_in";
  public static final String END_TIME = "end_time";
  public static final String VERSION = "version";
  public static final String GAME_SAVE_ID = "game_save_id";
  public static final String GAME_MAIL_TEMPLATE_ID = "game_mail_template_id";
  public static final String CANCELLED = "cancelled";
  public static final String EXPIRATION_DAYS = "expiration_days";
  public static final String BODY = "body";
  public static final String SUBJECT = "subject";
  public static final String MAIL = "mail";
  public static final String MAIL_IDS = "mail_ids";
  public static final String TEMPLATE = "template";
  public static final String OBJECT = "object";
}
