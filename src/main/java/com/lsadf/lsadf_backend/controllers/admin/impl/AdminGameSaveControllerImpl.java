package com.lsadf.lsadf_backend.controllers.admin.impl;

import com.lsadf.lsadf_backend.controllers.admin.AdminGameSaveController;
import com.lsadf.lsadf_backend.controllers.impl.BaseController;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.StageService;
import com.lsadf.lsadf_backend.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * The implementation of the AdminGameSaveController
 */
@RestController
@Slf4j
public class AdminGameSaveControllerImpl extends BaseController implements AdminGameSaveController {

    private final CurrencyService currencyService;
    private final StageService stageService;
    private final GameSaveService gameSaveService;
    private final CacheService redisCacheService;
    private final Mapper mapper;


    @Autowired
    public AdminGameSaveControllerImpl(CurrencyService currencyService,
                                       StageService stageService,
                                       GameSaveService gameSaveService,
                                       Mapper mapper,
                                       CacheService redisCacheService) {
        this.currencyService = currencyService;
        this.stageService = stageService;
        this.gameSaveService = gameSaveService;
        this.redisCacheService = redisCacheService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(Jwt jwt,
                                                                        String orderBy) {
        GameSaveOrderBy gameSaveOrderBy = orderBy != null ? GameSaveOrderBy.valueOf(orderBy) : GameSaveOrderBy.NONE;
        validateUser(jwt);
        try (Stream<GameSaveEntity> stream = gameSaveService.getGameSaves()) {
            Stream<GameSaveEntity> orderedStream = StreamUtils.sortGameSaves(stream, gameSaveOrderBy);
            List<GameSave> gameSaves = orderedStream.map(mapper::mapGameSaveEntityToGameSave).toList();
            return generateResponse(HttpStatus.OK, gameSaves);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(Jwt jwt,
                                                                 String gameSaveId) {
        validateUser(jwt);
        GameSaveEntity entity = gameSaveService.getGameSave(gameSaveId);
        GameSave gameSave = mapper.mapGameSaveEntityToGameSave(entity);
        return generateResponse(HttpStatus.OK, gameSave);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> updateGameSave(Jwt jwt,
                                                                    String gameSaveId,
                                                                    AdminGameSaveUpdateRequest adminGameSaveUpdateRequest) {

        validateUser(jwt);
        GameSaveEntity gameSaveEntity = gameSaveService.updateNickname(gameSaveId, adminGameSaveUpdateRequest);
        GameSave gameSave = mapper.mapGameSaveEntityToGameSave(gameSaveEntity);
        return generateResponse(HttpStatus.OK, gameSave);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(Jwt jwt,
                                                                         AdminGameSaveCreationRequest creationRequest) {

        validateUser(jwt);
        GameSaveEntity gameSaveEntity = gameSaveService.createGameSave(creationRequest);
        GameSave gameSave = mapper.mapGameSaveEntityToGameSave(gameSaveEntity);
        return generateResponse(HttpStatus.OK, gameSave);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteGameSave(Jwt jwt,
                                                                String gameSaveId) {

        validateUser(jwt);

        gameSaveService.deleteGameSave(gameSaveId);
        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> updateGameSaveCurrencies(Jwt jwt,
                                                                          String gameSaveId,
                                                                          CurrencyRequest currencyRequest) {

        validateUser(jwt);
        Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);
        currencyService.saveCurrency(gameSaveId, currency, redisCacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> updateGameSaveStages(Jwt jwt,
                                                                      String gameSaveId,
                                                                      StageRequest stageRequest) {

        validateUser(jwt);
        Stage stage = mapper.mapStageRequestToStage(stageRequest);
        stageService.saveStage(gameSaveId, stage, redisCacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }
}
