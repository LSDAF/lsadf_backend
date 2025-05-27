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
package com.lsadf.application.bdd;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Constants for the fields in the BDD scenarios */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BddFieldConstants {

  private static final String ID = "id";
  private static final String CREATED_AT = "createdAt";
  private static final String UPDATED_AT = "updatedAt";

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class StageCacheEntry {
    public static final String GAME_SAVE_ID = "gameSaveId";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class CharacteristicsCacheEntry {
    public static final String GAME_SAVE_ID = "gameSaveId";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class CurrencyCacheEntry {
    public static final String GAME_SAVE_ID = "gameSaveId";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Stage {
    public static final String ID = BddFieldConstants.ID;
    public static final String USER_EMAIL = "userEmail";
    public static final String CURRENT_STAGE = "currentStage";
    public static final String MAX_STAGE = "maxStage";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Characteristics {
    public static final String ATTACK = "attack";
    public static final String CRIT_CHANCE = "critChance";
    public static final String CRIT_DAMAGE = "critDamage";
    public static final String HEALTH = "health";
    public static final String RESISTANCE = "resistance";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Currency {
    public static final String GOLD = "gold";
    public static final String DIAMOND = "diamond";
    public static final String EMERALD = "emerald";
    public static final String AMETHYST = "amethyst";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameSaveOwnershipCacheEntry {
    public static final String GAME_SAVE_ID = "gameSaveId";
    public static final String USER_EMAIL = "userEmail";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameSave {
    public static final String ID = BddFieldConstants.ID;
    public static final String USER_EMAIL = "userEmail";
    public static final String NICKNAME = "nickname";
    public static final String CURRENT_STAGE = "currentStage";
    public static final String MAX_STAGE = "maxStage";
    public static final String CREATED_AT = BddFieldConstants.CREATED_AT;
    public static final String UPDATED_AT = BddFieldConstants.UPDATED_AT;
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Inventory {
    public static final String ITEMS = "items";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Item {
    public static final String ID = BddFieldConstants.ID;
    public static final String CLIENT_ID = "clientId";
    public static final String BLUEPRINT_ID = "blueprintId";
    public static final String ITEM_TYPE = "itemType";
    public static final String ITEM_RARITY = "itemRarity";
    public static final String IS_EQUIPPED = "isEquipped";
    public static final String LEVEL = "level";
    public static final String MAIN_STAT_BASE_VALUE = "mainStatBaseValue";
    public static final String MAIN_STAT_STATISTIC = "mainStatStatistic";
    public static final String ADDITIONAL_STAT_1_BASE_VALUE = "additionalStat1BaseValue";
    public static final String ADDITIONAL_STAT_1_STATISTIC = "additionalStat1Statistic";
    public static final String ADDITIONAL_STAT_2_BASE_VALUE = "additionalStat2BaseValue";
    public static final String ADDITIONAL_STAT_2_STATISTIC = "additionalStat2Statistic";
    public static final String ADDITIONAL_STAT_3_BASE_VALUE = "additionalStat3BaseValue";
    public static final String ADDITIONAL_STAT_3_STATISTIC = "additionalStat3Statistic";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class User {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USERNAME = "username";
    public static final String ENABLED = "enabled";
    public static final String EMAIL_VERIFIED = "emailVerified";
    public static final String ROLES = "roles";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class UserCreationRequest {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class UserInfo {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String ROLES = "roles";
    public static final String VERIFIED = "verified";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class UserLoginRequest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class UserUpdateRequest {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class JwtAuthentication {
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String EMAIL = "email";
    public static final String ROLES = "roles";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class UserAdminDetails {
    public static final String ID = BddFieldConstants.ID;
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String SOCIAL_PROVIDER = "socialProvider";
    public static final String ENABLED = "enabled";
    public static final String VERIFIED = "verified";
    public static final String ROLES = "roles";
    public static final String GAME_SAVES = "gameSaves";
    public static final String UPDATED_AT = BddFieldConstants.UPDATED_AT;
    public static final String CREATED_AT = BddFieldConstants.CREATED_AT;
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GlobalInfo {
    public static final String GAME_SAVE_COUNTER = "gameSaveCounter";
    public static final String USER_COUNTER = "userCounter";
    public static final String NOW = "now";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class AdminUserUpdateRequest {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ENABLED = "enabled";
    public static final String EMAIL_VERIFIED = "emailVerified";
    public static final String USER_ROLES = "userRoles";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class AdminUserCreationRequest {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String ENABLED = "enabled";
    public static final String EMAIL_VERIFIED = "emailVerified";
    public static final String SOCIAL_PROVIDER = "socialProvider";
    public static final String PROVIDER_USER_ID = "providerUserId";
    public static final String USER_ROLES = "userRoles";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class SearchRequest {
    public static final String KEY = "key";
    public static final String VALUE = "value";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class UserRefreshLoginRequest {
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String EMAIL = "email";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Keycloak {
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String REALM = "realm";
  }
}
