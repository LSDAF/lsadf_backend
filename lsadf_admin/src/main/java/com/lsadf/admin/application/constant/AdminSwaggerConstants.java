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
package com.lsadf.admin.application.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Swagger documentation constants for lsadf-admin module */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdminSwaggerConstants {

  public static final String AUTH_CONTROLLER = "Auth Controller";
  public static final String OAUTH_2_CONTROLLER = "OAuth2 Controller";
  public static final String ADMIN_CACHE_CONTROLLER = "Admin Cache Controller";
  public static final String ADMIN_USER_CONTROLLER = "Admin User Controller";
  public static final String ADMIN_GAME_SAVE_CONTROLLER = "Admin Game Save Controller";
  public static final String ADMIN_INVENTORY_CONTROLLER = "Admin Inventory Controller";
  public static final String ADMIN_SEARCH_CONTROLLER = "Admin Search Controller";
  public static final String ADMIN_GLOBAL_INFO_CONTROLLER = "Admin Global Info Controller";
  public static final String ADMIN_GAME_MAIL_TEMPLATE_CONTROLLER =
      "Admin Game Mail Template Controller";
  public static final String ADMIN_GAME_MAIL_CONTROLLER = "Admin Game Mail Controller";
}
