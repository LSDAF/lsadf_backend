package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.controllers.CurrencyController;
import com.lsadf.lsadf_backend.exceptions.http.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.http.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * Implementation of the Currency Controller
 */
@RestController
@Slf4j
public class CurrencyControllerImpl extends BaseController implements CurrencyController {

    private final GameSaveService gameSaveService;
    private final CurrencyService currencyService;
    private final CacheService cacheService;

    private final Mapper mapper;

    @Autowired
    public CurrencyControllerImpl(GameSaveService gameSaveService,
                                  CurrencyService currencyService,
                                  @Qualifier(REDIS_CACHE_SERVICE) CacheService cacheService,
                                  Mapper mapper) {
        this.gameSaveService = gameSaveService;
        this.currencyService = currencyService;
        this.cacheService = cacheService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveCurrency(Jwt jwt,
                                                              String gameSaveId,
                                                              CurrencyRequest currencyRequest) {
        validateUser(jwt);
        String userEmail = jwt.getSubject();
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

        Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);
        currencyService.saveCurrency(gameSaveId, currency, cacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> getCurrency(Jwt jwt,
                                                             String gameSaveId) {
        validateUser(jwt);
        String userEmail = jwt.getSubject();
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
        Currency currency = currencyService.getCurrency(gameSaveId);
        return generateResponse(HttpStatus.OK, currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }
}
