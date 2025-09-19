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

/**
 * Utility class holding constant parameter keys used in request handling and processing within
 * controllers. This class centralizes the definition of frequently used parameter names, promoting
 * consistency and reducing the risk of typos in keys across the application.
 *
 * <p>All constants in this class are public, static, and final, making them immutable and
 * accessible wherever needed in the application.
 *
 * <p>This class is designed to be non-instantiable by declaring a private no-arguments constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParameterConstants {

  /**
   * Represents a constant parameter key used for identifying and accessing the "game_save_id"
   * parameter in request handling within controllers. Typically utilized to denote or reference a
   * specific game save identifier in the application.
   */
  public static final String GAME_SAVE_ID = "game_save_id";

  /**
   * Represents a constant parameter key used for identifying and accessing the "game_session_id"
   */
  public static final String GAME_SESSION_ID = "game_session_id";

  /**
   * Represents a constant parameter key for identifying and accessing the "client_id" parameter in
   * request handling within controllers. Typically used in scenarios involving authentication or
   * client-specific operations.
   */
  public static final String CLIENT_ID = "client_id";

  /**
   * Represents a constant parameter key used for identifying and accessing the "order_by" parameter
   * in request handling within controllers.
   */
  public static final String ORDER_BY = "order_by";

  /**
   * Represents a constant parameter key for identifying and accessing the "user_id" parameter in
   * request handling within controllers.
   */
  public static final String USER_ID = "user_id";

  /**
   * Represents a constant parameter key for identifying and accessing the "username" parameter in
   * request handling within controllers.
   */
  public static final String USERNAME = "username";

  /**
   * Represents a constant parameter key used for identifying and accessing the "code" parameter in
   * request handling within controllers.
   */
  public static final String CODE = "code";

  public static final String X_GAME_SESSION_ID = "X-GameSession-ID";
}
