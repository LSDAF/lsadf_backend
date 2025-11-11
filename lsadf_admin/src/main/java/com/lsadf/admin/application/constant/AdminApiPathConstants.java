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

/** Admin API path constants for lsadf-admin module */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdminApiPathConstants {

  public static final String AUTH = "/api/v1/auth";
  public static final String OAUTH2 = "/api/oauth2";
  public static final String ADMIN_CACHE = "/api/v1/admin/cache";
  public static final String ADMIN_GAME_SAVE = "/api/v1/admin/game_save";
  public static final String ADMIN_INVENTORY = "/api/v1/admin/inventory";
  public static final String ADMIN_USER = "/api/v1/admin/user";
  public static final String ADMIN_SEARCH = "/api/v1/admin/search";
  public static final String ADMIN_GLOBAL_INFO = "/api/v1/admin/global_info";
  public static final String ADMIN_GAME_MAIL_TEMPLATE = "/api/v1/admin/game_mail_template";
  public static final String ADMIN_GAME_MAIL = "/api/v1/admin/game_mail";
}
