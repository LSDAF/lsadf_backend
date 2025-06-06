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
package com.lsadf.application.controller.game.currency;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.currency.CurrencyService;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.request.game.currency.CurrencyRequestMapper;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.currency.CurrencyResponse;
import com.lsadf.core.infra.web.response.game.currency.CurrencyResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Currency Controller */
@RestController
@Slf4j
public class CurrencyControllerImpl extends BaseController implements CurrencyController {

  private final GameSaveService gameSaveService;
  private final CurrencyService currencyService;
  private final CacheService cacheService;

  private final CurrencyRequestMapper requestModelMapper;
  private final CurrencyResponseMapper currencyResponseMapper;

  @Autowired
  public CurrencyControllerImpl(
      GameSaveService gameSaveService,
      CurrencyService currencyService,
      CacheService cacheService,
      CurrencyRequestMapper requestModelMapper,
      CurrencyResponseMapper currencyResponseMapper) {
    this.gameSaveService = gameSaveService;
    this.currencyService = currencyService;
    this.cacheService = cacheService;
    this.requestModelMapper = requestModelMapper;
    this.currencyResponseMapper = currencyResponseMapper;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Void>> saveCurrency(
      Jwt jwt, String gameSaveId, CurrencyRequest currencyRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

    Currency currency = requestModelMapper.mapToModel(currencyRequest);
    currencyService.saveCurrency(gameSaveId, currency, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<CurrencyResponse>> getCurrency(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Currency currency = currencyService.getCurrency(gameSaveId);
    CurrencyResponse currencyResponse = currencyResponseMapper.mapToResponse(currency);
    return generateResponse(HttpStatus.OK, currencyResponse);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
