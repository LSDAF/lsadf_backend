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
  public static final class OAuth2 {
    public static final String CALLBACK = "/callback";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Swagger {
    public static final String AUTH_CONTROLLER = "Auth Controller";
    public static final String CHARACTERISTICS_CONTROLLER = "Characteristics Controller";
    public static final String CURRENCY_CONTROLLER = "Currency Controller";
    public static final String GAME_SAVE_CONTROLLER = "Game Save Controller";
    public static final String INVENTORY_CONTROLLER = "Inventory Controller";
    public static final String OAUTH_2_CONTROLLER = "OAuth2 Controller";
    public static final String STAGE_CONTROLLER = "Stage Controller";
    public static final String USER_CONTROLLER = "User Controller";

    // ADMIN
    public static final String ADMIN_CACHE_CONTROLLER = "Admin Cache Controller";
    public static final String ADMIN_USERS_CONTROLLER = "Admin Users Controller";
    public static final String ADMIN_GAME_SAVES_CONTROLLER = "Admin Game Saves Controller";
    public static final String ADMIN_INVENTORIES_CONTROLLER = "Admin Inventories Controller";
    public static final String ADMIN_SEARCH_CONTROLLER = "Admin Search Controller";
    public static final String ADMIN_GLOBAL_INFO_CONTROLLER = "Admin Global Info Controller";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Stage {
    public static final String GAME_SAVE_ID = "/{game_save_id}";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Currency {
    public static final String GAME_SAVE_ID = "/{game_save_id}";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Characteristics {
    public static final String GAME_SAVE_ID = "/{game_save_id}";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Inventory {
    public static final String GAME_SAVE_ID = "/{game_save_id}";
    public static final String ITEMS = "/{game_save_id}/items";
    public static final String CLIENT_ID = "/{game_save_id}/items/{client_id}";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class AdminGameSave {
    public static final String USER_GAME_SAVES = "/user/{username}";
    public static final String GAME_SAVE_ID = "/id/{game_save_id}";
    public static final String UPDATE_GAME_SAVE_CHARACTERISTICS =
        "/id/{game_save_id}/characteristics";
    public static final String UPDATE_GAME_SAVE_CURRENCIES = "/id/{game_save_id}/currencies";
    public static final String UPDATE_GAME_SAVE_INVENTORIES = "/id/{game_save_id}/inventories";
    public static final String UPDATE_GAME_SAVE_STAGES = "/id/{game_save_id}/stages";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class AdminUser {
    public static final String USER_ID = "/id/{user_id}";
    public static final String USERNAME = "/username/{username}";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class AdminSearch {
    public static final String SEARCH_GAME_SAVES = "/game_saves";
    public static final String SEARCH_USERS = "/users";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class AdminCache {
    public static final String FLUSH = "/flush";
    public static final String TOGGLE = "/toggle";
    public static final String ENABLED = "/enabled";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class GameSave {
    public static final String GENERATE = "/generate";
    public static final String ME = "/me";
    public static final String UPDATE_NICKNAME = "/{game_save_id}/nickname";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Auth {
    public static final String LOGIN = "/login";
    public static final String REFRESH = "/refresh";
    public static final String LOGOUT = "/logout";
    public static final String REGISTER = "/register";
    public static final String VALIDATE_TOKEN = "/validate";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class User {
    public static final String ME = "/me";
    public static final String USER_ME_GAME_SAVES = "/me/game_saves";
  }
}
