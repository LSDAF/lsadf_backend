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
package com.lsadf.application.controller.game.save.currency;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.currency.CurrencyCommandService;
import com.lsadf.core.application.game.save.currency.CurrencyEventPublisherPort;
import com.lsadf.core.application.game.save.currency.CurrencyQueryService;
import com.lsadf.core.application.game.save.currency.command.PersistCurrencyCommand;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequest;
import com.lsadf.core.infra.web.dto.request.game.currency.CurrencyRequestMapper;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.currency.CurrencyResponse;
import com.lsadf.core.infra.web.dto.response.game.save.currency.CurrencyResponseMapper;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Currency Controller */
@ConditionalOnProperty(prefix = "api", name = "enabled", havingValue = "true")
@RestController
@Slf4j
public class CurrencyControllerImpl extends BaseController implements CurrencyController {

  private final GameSaveService gameSaveService;
  private final CurrencyEventPublisherPort currencyEventPublisherPort;
  private final CurrencyCommandService currencyCommandService;
  private final CurrencyQueryService currencyQueryService;
  private final CacheManager cacheManager;

  private static final CurrencyRequestMapper requestModelMapper = CurrencyRequestMapper.INSTANCE;
  private static final CurrencyResponseMapper currencyResponseMapper =
      CurrencyResponseMapper.INSTANCE;

  public CurrencyControllerImpl(
      GameSaveService gameSaveService,
      CurrencyCommandService currencyCommandService,
      CurrencyQueryService currencyQueryService,
      CurrencyEventPublisherPort currencyEventPublisherPort,
      CacheManager cacheManager) {
    this.gameSaveService = gameSaveService;
    this.currencyCommandService = currencyCommandService;
    this.currencyQueryService = currencyQueryService;
    this.currencyEventPublisherPort = currencyEventPublisherPort;
    this.cacheManager = cacheManager;
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> saveCurrency(
      Jwt jwt, UUID gameSaveId, CurrencyRequest currencyRequest, UUID sessionId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

    Currency currency = requestModelMapper.map(currencyRequest);

    if (Boolean.TRUE.equals(cacheManager.isEnabled())) {
      currencyEventPublisherPort.publishCurrencyUpdatedEvent(
          userEmail, gameSaveId, currency, sessionId);
    } else {
      var command = PersistCurrencyCommand.fromCurrency(gameSaveId, currency);
      currencyCommandService.persistCurrency(command);
    }

    return generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<CurrencyResponse>> getCurrency(Jwt jwt, UUID gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Currency currency = currencyQueryService.retrieveCurrency(gameSaveId);
    CurrencyResponse currencyResponse = currencyResponseMapper.map(currency);
    return generateResponse(HttpStatus.OK, currencyResponse);
  }

  @Override
  public Logger getLogger() {
    return log;
  }
}
