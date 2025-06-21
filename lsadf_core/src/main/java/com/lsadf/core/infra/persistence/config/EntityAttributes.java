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
package com.lsadf.core.infra.persistence.config;

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
