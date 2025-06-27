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

import com.lsadf.core.infra.exception.http.UnauthorizedException;
import org.slf4j.Logger;
import org.springframework.security.oauth2.jwt.Jwt;

/** Base controller for all controllers */
public abstract class BaseController implements Controller {

  /**
   * Validates the user
   *
   * @param jwt the jwt
   * @throws UnauthorizedException if the user is not valid
   */
  public void validateUser(Jwt jwt) throws UnauthorizedException {
    if (jwt == null) {
      Logger logger = getLogger();
      logger.error("Unauthorized. Didn't manage to build UserInfo from token. Please login");
      throw new UnauthorizedException("Didn't manage to build UserInfo from token. Please login");
    }
  }
}
