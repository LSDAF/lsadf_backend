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
import static com.lsadf.core.infra.web.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsService;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.valkey.cache.service.CacheService;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.request.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.web.response.ApiResponse;
import com.lsadf.core.infra.web.response.game.save.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.response.game.save.characteristics.CharacteristicsResponseMapper;
import java.util.UUID;
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

  private static final CharacteristicsRequestMapper requestMapper =
      CharacteristicsRequestMapper.INSTANCE;
  private static final CharacteristicsResponseMapper responseMapper =
      CharacteristicsResponseMapper.INSTANCE;

  @Autowired
  public CharacteristicsControllerImpl(
      GameSaveService gameSaveService,
      CharacteristicsService characteristicsService,
      CacheService cacheService) {
    this.gameSaveService = gameSaveService;
    this.characteristicsService = characteristicsService;
    this.cacheService = cacheService;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<Void>> saveCharacteristics(
      Jwt jwt, UUID gameSaveId, CharacteristicsRequest characteristicsRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

    Characteristics characteristics = requestMapper.map(characteristicsRequest);
    characteristicsService.saveCharacteristics(
        gameSaveId, characteristics, cacheService.isEnabled());

    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<ApiResponse<CharacteristicsResponse>> getCharacteristics(
      Jwt jwt, UUID gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Characteristics characteristics = characteristicsService.getCharacteristics(gameSaveId);
    CharacteristicsResponse characteristicsResponse = responseMapper.map(characteristics);
    return generateResponse(HttpStatus.OK, characteristicsResponse);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
