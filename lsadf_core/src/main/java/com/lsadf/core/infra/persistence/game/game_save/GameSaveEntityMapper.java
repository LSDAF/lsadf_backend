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

package com.lsadf.core.infra.persistence.game.game_save;

import com.lsadf.core.domain.game.GameSave;
import com.lsadf.core.domain.game.characteristics.Characteristics;
import com.lsadf.core.domain.game.currency.Currency;
import com.lsadf.core.domain.game.stage.Stage;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntity;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntity;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.stage.StageEntity;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import com.lsadf.core.infra.persistence.game.stage.StageEntityMapper;

/**
 * Maps {@link GameSaveEntity} objects to their corresponding {@link GameSave} models. This mapper
 * facilitates the transformation of data from the persistence layer entity to the application layer
 * model, ensuring that associated entities such as characteristics, stage, and currency are
 * properly mapped as part of the conversion.
 *
 * <p>This class utilizes other mappers, including: - {@link CharacteristicsEntityMapper} for
 * mapping {@link CharacteristicsEntity}. - {@link StageEntityMapper} for mapping {@link
 * StageEntity}. - {@link CurrencyEntityMapper} for mapping {@link CurrencyEntity}.
 *
 * <p>The mapping allows developers to abstract entity-to-model translations within the business
 * logic, promoting separation of concerns and maintainable structure.
 */
public class GameSaveEntityMapper implements EntityModelMapper<GameSaveEntity, GameSave> {

  public GameSaveEntityMapper(
      CharacteristicsEntityMapper characteristicsEntityModelMapper,
      StageEntityMapper stageEntityMapper,
      CurrencyEntityMapper currencyEntityMapper) {
    this.characteristicsEntityModelMapper = characteristicsEntityModelMapper;
    this.stageEntityMapper = stageEntityMapper;
    this.currencyEntityMapper = currencyEntityMapper;
  }

  private final CharacteristicsEntityMapper characteristicsEntityModelMapper;
  private final StageEntityMapper stageEntityMapper;
  private final CurrencyEntityMapper currencyEntityMapper;

  /** {@inheritDoc} */
  @Override
  public GameSave map(GameSaveEntity gameSaveEntity) {
    Stage stage = stageEntityMapper.map(gameSaveEntity.getStageEntity());
    Characteristics characteristics =
        characteristicsEntityModelMapper.map(gameSaveEntity.getCharacteristicsEntity());
    Currency currency = currencyEntityMapper.map(gameSaveEntity.getCurrencyEntity());

    return GameSave.builder()
        .id(gameSaveEntity.getId())
        .userEmail(gameSaveEntity.getUserEmail())
        .nickname(gameSaveEntity.getNickname())
        .characteristics(characteristics)
        .currency(currency)
        .stage(stage)
        .createdAt(gameSaveEntity.getCreatedAt())
        .updatedAt(gameSaveEntity.getUpdatedAt())
        .build();
  }
}
