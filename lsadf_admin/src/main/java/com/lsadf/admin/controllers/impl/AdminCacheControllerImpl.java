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
package com.lsadf.admin.controllers.impl;

import static com.lsadf.core.common.utils.ResponseUtils.generateResponse;

import com.lsadf.admin.controllers.AdminCacheController;
import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.CacheFlushService;
import com.lsadf.core.services.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** The implementation of the AdminCacheController */
@RestController
@Slf4j
public class AdminCacheControllerImpl extends BaseController implements AdminCacheController {

  private final CacheService redisCacheService;
  private final CacheFlushService cacheFlushService;

  @Autowired
  public AdminCacheControllerImpl(
      CacheService redisCacheService, CacheFlushService cacheFlushService) {
    this.redisCacheService = redisCacheService;
    this.cacheFlushService = cacheFlushService;
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(Jwt jwt) {
    validateUser(jwt);
    boolean cacheEnabled = redisCacheService.isEnabled();
    return generateResponse(HttpStatus.OK, cacheEnabled);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(Jwt jwt) {
    validateUser(jwt);
    redisCacheService.toggleCacheEnabling();
    Boolean cacheEnabled = redisCacheService.isEnabled();
    return generateResponse(HttpStatus.OK, cacheEnabled);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> flushAndClearCache(Jwt jwt) {
    validateUser(jwt);

    log.info("Clearing all caches");
    cacheFlushService.flushCharacteristics();
    cacheFlushService.flushCurrencies();
    cacheFlushService.flushStages();
    redisCacheService.clearCaches();

    return generateResponse(HttpStatus.OK);
  }
}
