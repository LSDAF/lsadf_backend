/*
 * Copyright 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.lsadf.core.infra.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanConstants {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class TokenProvider {
    public static final String JWT_TOKEN_PROVIDER = "jwtTokenProvider";
    public static final String REFRESH_TOKEN_PROVIDER = "refreshTokenProvider";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Cache {
    public static final String CHARACTERISTICS_CACHE = "characteristicsCache";
    public static final String CURRENCY_CACHE = "currencyCache";
    public static final String STAGE_CACHE = "stageCache";
    public static final String LOCAL_INVALIDATED_JWT_TOKEN_CACHE = "localInvalidatedJwtTokenCache";
    public static final String INVALIDATED_JWT_TOKEN_CACHE = "invalidatedJwtTokenCache";
    public static final String GAME_SAVE_OWNERSHIP_CACHE = "gameSaveOwnershipCache";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class ClientRegistration {
    public static final String OAUTH2_GOOGLE_CLIENT_REGISTRATION = "oAuth2GoogleClientRegistration";
    public static final String OAUTH2_FACEBOOK_CLIENT_REGISTRATION =
        "oAuth2FacebookClientRegistration";
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class TokenParser {
    public static final String JWT_TOKEN_PARSER = "jwtParser";
    public static final String JWT_REFRESH_TOKEN_PARSER = "jwtRefreshTokenParser";
  }
}
