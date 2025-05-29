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

package com.lsadf.core.infra.web.responses.game.game_save;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.infra.web.responses.ModelResponseMapper;
import com.lsadf.core.infra.web.responses.game.characteristics.CharacteristicsResponse;
import com.lsadf.core.infra.web.responses.game.characteristics.CharacteristicsResponseMapper;
import com.lsadf.core.infra.web.responses.game.currency.CurrencyResponse;
import com.lsadf.core.infra.web.responses.game.currency.CurrencyResponseMapper;
import com.lsadf.core.infra.web.responses.game.stage.StageResponse;
import com.lsadf.core.infra.web.responses.game.stage.StageResponseMapper;
import lombok.AllArgsConstructor;

/**
 * Converts a GameSave model object into its corresponding GameSaveResponse representation. This
 * class implements the ModelResponseMapper interface for transforming GameSave to GameSaveResponse.
 * It ensures all relevant fields from the GameSave model, such as id, stage, characteristics,
 * currency, nickname, createdAt, updatedAt, and userEmail, are mapped into a GameSaveResponse
 * object.
 */
@AllArgsConstructor
public class GameSaveResponseMapper implements ModelResponseMapper<GameSave, GameSaveResponse> {

  private final CurrencyResponseMapper currencyResponseMapper;
  private final CharacteristicsResponseMapper characteristicsResponseMapper;
  private final StageResponseMapper stageResponseMapper;

  /**
   * Maps a GameSave model object to a GameSaveResponse object.
   *
   * @param model the GameSave model containing game save data to be mapped
   * @return a GameSaveResponse object containing the mapped data
   */
  @Override
  public GameSaveResponse mapToResponse(GameSave model) {
    CharacteristicsResponse characteristicsResponse =
        characteristicsResponseMapper.mapToResponse(model.getCharacteristics());
    StageResponse stageResponse = stageResponseMapper.mapToResponse(model.getStage());
    CurrencyResponse currencyResponse = currencyResponseMapper.mapToResponse(model.getCurrency());

    return GameSaveResponse.builder()
        .id(model.getId())
        .stage(stageResponse)
        .characteristics(characteristicsResponse)
        .currency(currencyResponse)
        .nickname(model.getNickname())
        .createdAt(model.getCreatedAt())
        .updatedAt(model.getUpdatedAt())
        .userEmail(model.getUserEmail())
        .build();
  }
}
