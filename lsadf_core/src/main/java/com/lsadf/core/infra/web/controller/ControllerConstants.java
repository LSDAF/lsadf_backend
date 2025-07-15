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
package com.lsadf.core.infra.web.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Controller constants class */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerConstants {

  public static final String AUTH = "/api/v1/auth";
  public static final String CHARACTERISTICS = "/api/v1/characteristics";
  public static final String CURRENCY = "/api/v1/currency";
  public static final String GAME_SAVE = "/api/v1/game_save";
  public static final String INVENTORY = "/api/v1/inventory";
  public static final String OAUTH2 = "/api/oauth2";
  public static final String STAGE = "/api/v1/stage";
  public static final String USER = "/api/v1/user";

  // ADMIN
  public static final String ADMIN = "/api/v1/admin";
  public static final String ADMIN_CACHE = "/api/v1/admin/cache";
  public static final String ADMIN_GAME_SAVES = "/api/v1/admin/game_saves";
  public static final String ADMIN_INVENTORIES = "/api/v1/admin/inventories";
  public static final String ADMIN_USERS = "/api/v1/admin/users";
  public static final String ADMIN_SEARCH = "/api/v1/admin/search";
  public static final String ADMIN_GLOBAL_INFO = "/api/v1/admin/global_info";

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Params {
    public static final String GAME_SAVE_ID = "game_save_id";
    public static final String ORDER_BY = "order_by";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Swagger {

    public static final String AUTH_CONTROLLER = "Auth Controller";
    public static final String CHARACTERISTICS_CONTROLLER = "Characteristics Controller";
    public static final String CURRENCY_CONTROLLER = "Currency Controller";
    public static final String GAME_SAVE_CONTROLLER = "Game Save Controller";
    public static final String INVENTORY_CONTROLLER = "Inventory Controller";
    public static final String OAUTH_2_CONTROLLER = "OAuth2 Controller";
    public static final String USER_CONTROLLER = "User Controller";

    // ADMIN
    public static final String ADMIN_CACHE_CONTROLLER = "Admin Cache Controller";
    public static final String ADMIN_USERS_CONTROLLER = "Admin Users Controller";
    public static final String ADMIN_GAME_SAVES_CONTROLLER = "Admin Game Saves Controller";
    public static final String ADMIN_INVENTORIES_CONTROLLER = "Admin Inventories Controller";
    public static final String ADMIN_SEARCH_CONTROLLER = "Admin Search Controller";
    public static final String ADMIN_GLOBAL_INFO_CONTROLLER = "Admin Global Info Controller";
  }
}
