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
import static com.lsadf.core.infra.web.dto.response.ResponseUtils.generateResponse;

import com.lsadf.core.application.cache.CacheManager;
import com.lsadf.core.application.game.save.GameSaveService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsCommandService;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsEventPublisherPort;
import com.lsadf.core.application.game.save.characteristics.CharacteristicsQueryService;
import com.lsadf.core.application.game.save.characteristics.command.PersistCharacteristicsCommand;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.infra.web.controller.BaseController;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequest;
import com.lsadf.core.infra.web.dto.request.game.characteristics.CharacteristicsRequestMapper;
import com.lsadf.core.infra.web.dto.response.ApiResponse;
import com.lsadf.core.infra.web.dto.response.game.save.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.dto.response.game.save.characteristics.CharacteristicsResponseMapper;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(prefix = "api", name = "enabled", havingValue = "true")
@RestController
@Slf4j
public class CharacteristicsControllerImpl extends BaseController
    implements CharacteristicsController {
  private final CacheManager cacheManager;
  private final GameSaveService gameSaveService;
  private final CharacteristicsCommandService characteristicsCommandService;
  private final CharacteristicsQueryService characteristicsQueryService;
  private final CharacteristicsEventPublisherPort characteristicsEventPublisherPort;

  private static final CharacteristicsRequestMapper requestMapper =
      CharacteristicsRequestMapper.INSTANCE;
  private static final CharacteristicsResponseMapper responseMapper =
      CharacteristicsResponseMapper.INSTANCE;

  public CharacteristicsControllerImpl(
      CacheManager cacheManager,
      GameSaveService gameSaveService,
      CharacteristicsQueryService characteristicsQueryService,
      CharacteristicsCommandService characteristicsCommandService,
      CharacteristicsEventPublisherPort characteristicsEventPublisherPort) {
    this.cacheManager = cacheManager;
    this.gameSaveService = gameSaveService;
    this.characteristicsCommandService = characteristicsCommandService;
    this.characteristicsQueryService = characteristicsQueryService;
    this.characteristicsEventPublisherPort = characteristicsEventPublisherPort;
  }

  @Override
  public ResponseEntity<ApiResponse<Void>> saveCharacteristics(
      Jwt jwt, UUID gameSaveId, CharacteristicsRequest characteristicsRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

    Characteristics characteristics = requestMapper.map(characteristicsRequest);
    var enabled = cacheManager.isEnabled();
    if (Boolean.TRUE.equals(enabled)) {
      characteristicsEventPublisherPort.publishCharacteristicsUpdatedEvent(
          userEmail, gameSaveId, characteristics);
    } else {
      PersistCharacteristicsCommand command =
          PersistCharacteristicsCommand.fromCharacteristics(gameSaveId, characteristics);
      characteristicsCommandService.persistCharacteristics(command);
    }

    return generateResponse(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApiResponse<CharacteristicsResponse>> getCharacteristics(
      Jwt jwt, UUID gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    Characteristics characteristics =
        characteristicsQueryService.retrieveCharacteristics(gameSaveId);
    CharacteristicsResponse characteristicsResponse = responseMapper.map(characteristics);
    return generateResponse(HttpStatus.OK, characteristicsResponse);
  }

  @Override
  public Logger getLogger() {
    return log;
  }
}
