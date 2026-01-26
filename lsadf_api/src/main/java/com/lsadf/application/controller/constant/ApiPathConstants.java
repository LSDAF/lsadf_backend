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
package com.lsadf.application.controller.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** API path constants for lsadf-api module */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPathConstants {

  public static final String AUTH = "/api/v1/auth";
  public static final String CHARACTERISTICS = "/api/v1/characteristics";
  public static final String CURRENCY = "/api/v1/currency";
  public static final String GAME_SAVE = "/api/v1/game_save";
  public static final String GAME_SESSION = "/api/v1/game_session";
  public static final String GAME_SESSION_V2 = "/api/v2/game_session";
  public static final String INVENTORY = "/api/v1/inventory";
  public static final String GAME_MAIL = "/api/v1/game_mail";
  public static final String OAUTH2 = "/api/oauth2";
  public static final String STAGE = "/api/v1/stage";
  public static final String USER = "/api/v1/user";
}
