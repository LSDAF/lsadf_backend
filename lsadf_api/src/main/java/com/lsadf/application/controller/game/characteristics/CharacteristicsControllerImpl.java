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
package com.lsadf.application.controller.game.characteristics;

import static com.lsadf.core.infra.web.config.auth.TokenUtils.getUsernameFromJwt;
import static com.lsadf.core.infra.web.responses.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.characteristics.CharacteristicsService;
import com.lsadf.core.application.game.game_save.GameSaveService;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.infra.cache.service.CacheService;
import com.lsadf.core.infra.web.controllers.BaseController;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.requests.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.web.responses.ApiResponse;
import com.lsadf.core.infra.web.responses.game.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.responses.game.characteristics.CharacteristicsResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CharacteristicsControllerImpl extends BaseController
    implements CharacteristicsController {
  private final GameSaveService gameSaveService;
  private final CharacteristicsService characteristicsService;
  private final CacheService cacheService;

  private final CharacteristicsRequestMapper requestMapper;
  private final CharacteristicsResponseMapper responseMapper;

  @Autowired
  public CharacteristicsControllerImpl(
      GameSaveService gameSaveService,
      CharacteristicsService characteristicsService,
      CacheService cacheService,
      CharacteristicsRequestMapper requestMapper,
      CharacteristicsResponseMapper responseMapper) {
    this.gameSaveService = gameSaveService;
    this.characteristicsService = characteristicsService;
    this.cacheService = cacheService;
    this.requestMapper = requestMapper;
    this.responseMapper = responseMapper;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Void>> saveCharacteristics(
      Jwt jwt, String gameSaveId, CharacteristicsRequest characteristicsRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

    Characteristics characteristics = requestMapper.mapToModel(characteristicsRequest);
    characteristicsService.saveCharacteristics(
        gameSaveId, characteristics, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<CharacteristicsResponse>> getCharacteristics(
      Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Characteristics characteristics = characteristicsService.getCharacteristics(gameSaveId);
    CharacteristicsResponse characteristicsResponse = responseMapper.mapToResponse(characteristics);
    return generateResponse(HttpStatus.OK, characteristicsResponse);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
