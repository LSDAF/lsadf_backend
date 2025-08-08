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
package com.lsadf.admin.application.cache;

import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.infra.valkey.cache.flush.CacheFlushService;
import com.lsadf.core.infra.valkey.cache.manager.CacheManager;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.response.ApiResponse;
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

  private final CacheManager redisCacheManager;
  private final CacheFlushService cacheFlushService;

  @Autowired
  public AdminCacheControllerImpl(
      CacheManager redisCacheManager, CacheFlushService cacheFlushService) {
    this.redisCacheManager = redisCacheManager;
    this.cacheFlushService = cacheFlushService;
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public ResponseEntity<ApiResponse<Boolean>> isCacheEnabled(Jwt jwt) {
    validateUser(jwt);
    boolean cacheEnabled = redisCacheManager.isEnabled();
    return generateResponse(HttpStatus.OK, cacheEnabled);
  }

  @Override
  public ResponseEntity<ApiResponse<Boolean>> toggleRedisCacheEnabling(Jwt jwt) {
    validateUser(jwt);
    redisCacheManager.toggleCacheEnabling();
    Boolean cacheEnabled = redisCacheManager.isEnabled();
    return generateResponse(HttpStatus.OK, cacheEnabled);
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> flushAndClearCache(Jwt jwt) {
    validateUser(jwt);

    log.info("Clearing all caches");
    cacheFlushService.flushCharacteristics();
    cacheFlushService.flushCurrencies();
    cacheFlushService.flushStages();
    redisCacheManager.clearCaches();

    return generateResponse(HttpStatus.OK);
  }
}
