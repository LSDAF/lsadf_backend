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
package com.lsadf.core.infra.web.responses;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Constant class containing all response messages for the application */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseMessages {
  public static final String OK = "OK";
  public static final String BAD_REQUEST = "Bad Request - The request was invalid";
  public static final String UNAUTHORIZED =
      "Unauthorized - Authentication credentials were missing or incorrect.";
  public static final String FORBIDDEN =
      "Forbidden - The user does not have permission to perform this operation";
  public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
  public static final String NOT_FOUND = "Not Found";
}
