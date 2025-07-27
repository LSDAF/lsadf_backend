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

package com.lsadf.core.infra.web.response.game.game_save;

import com.lsadf.core.domain.game.game_save.GameSave;
import com.lsadf.core.infra.web.response.ModelResponseMapper;
import com.lsadf.core.infra.web.response.game.characteristics.CharacteristicsResponseMapper;
import com.lsadf.core.infra.web.response.game.currency.CurrencyResponseMapper;
import com.lsadf.core.infra.web.response.game.stage.StageResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting {@link GameSave} domain model objects into {@link
 * GameSaveResponse} objects.
 *
 * <p>This interface is used to encapsulate the mapping logic required for translating the internal
 * representations of game save data into external response formats that are exposed via APIs.
 *
 * <p>The implementation of this mapper is automatically generated at runtime by the MapStruct
 * library, ensuring accurate and efficient field mapping between the {@link GameSave} and {@link
 * GameSaveResponse} types. The mapper utilizes additional mappers for nested conversions, such as
 * {@link CharacteristicsResponseMapper}, {@link CurrencyResponseMapper}, and {@link
 * StageResponseMapper}.
 *
 * <p>The generated implementation of this mapper is accessible as a singleton instance, {@code
 * INSTANCE}.
 *
 * @see GameSave
 * @see GameSaveResponse
 * @see CharacteristicsResponseMapper
 * @see CurrencyResponseMapper
 * @see StageResponseMapper
 */
@Mapper(
    uses = {
      CharacteristicsResponseMapper.class,
      CurrencyResponseMapper.class,
      StageResponseMapper.class
    })
public interface GameSaveResponseMapper extends ModelResponseMapper<GameSave, GameSaveResponse> {

  GameSaveResponseMapper INSTANCE = Mappers.getMapper(GameSaveResponseMapper.class);

  /**
   * Maps a GameSave model object to a GameSaveResponse object.
   *
   * @param model the GameSave model containing game save data to be mapped
   * @return a GameSaveResponse object containing the mapped data
   */
  @Override
  GameSaveResponse map(GameSave model);
}
