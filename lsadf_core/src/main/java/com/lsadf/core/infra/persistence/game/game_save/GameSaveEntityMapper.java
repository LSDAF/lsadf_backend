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
import com.lsadf.core.infra.persistence.EntityModelMapper;
import com.lsadf.core.infra.persistence.game.characteristics.CharacteristicsEntityMapper;
import com.lsadf.core.infra.persistence.game.currency.CurrencyEntityMapper;
import com.lsadf.core.infra.persistence.game.stage.StageEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * A MapStruct mapper interface for converting {@link GameSaveEntity} objects into {@link GameSave}
 * domain models.
 *
 * <p>This mapper performs the transformation of persistence-layer entities into domain models used
 * in the application layer. It extends the {@link EntityModelMapper} interface, inheriting the
 * mapping contract to handle entity-to-model conversions.
 *
 * <p>Design Considerations: - Utilizes MapStruct for automatic implementation generation. - Depends
 * on additional mappers {@link CharacteristicsEntityMapper}, {@link StageEntityMapper}, and {@link
 * CurrencyEntityMapper} to handle mappings of nested or composite objects within a {@link
 * GameSaveEntity} object.
 *
 * <p>Responsibilities: - Converts the attributes of a {@link GameSaveEntity} to its corresponding
 * {@link GameSave} representation. - Ensures seamless and accurate mapping for all relevant fields,
 * promoting clean separation between persistence and domain layers. - Handles nested or composite
 * objects by delegating to their respective mappers.
 */
@Mapper(
    uses = {CharacteristicsEntityMapper.class, StageEntityMapper.class, CurrencyEntityMapper.class})
public interface GameSaveEntityMapper extends EntityModelMapper<GameSaveEntity, GameSave> {
  GameSaveEntityMapper INSTANCE = Mappers.getMapper(GameSaveEntityMapper.class);

  /** {@inheritDoc} */
  @Override
  @Mapping(source = "characteristicsEntity", target = "characteristics")
  @Mapping(source = "stageEntity", target = "stage")
  @Mapping(source = "currencyEntity", target = "currency")
  GameSave map(GameSaveEntity gameSaveEntity);
}
