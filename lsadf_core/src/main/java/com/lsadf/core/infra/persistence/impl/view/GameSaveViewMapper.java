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

package com.lsadf.core.infra.persistence.impl.view;

import com.lsadf.core.domain.game.save.GameSave;
import com.lsadf.core.domain.game.save.characteristics.Characteristics;
import com.lsadf.core.domain.game.save.currency.Currency;
import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.domain.game.save.stage.Stage;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameSaveViewMapper extends EntityModelMapper<GameSaveViewEntity, GameSave> {
  GameSaveViewMapper INSTANCE = Mappers.getMapper(GameSaveViewMapper.class);

  @Mapping(target = "metadata", source = ".", qualifiedByName = "mapGameMetadata")
  @Mapping(target = "characteristics", source = ".", qualifiedByName = "mapCharacteristics")
  @Mapping(target = "currency", source = ".", qualifiedByName = "mapCurrency")
  @Mapping(target = "stage", source = ".", qualifiedByName = "mapStage")
  GameSave map(GameSaveViewEntity gameSaveViewEntity);

  @Named("mapGameMetadata")
  default GameMetadata mapGameMetadata(GameSaveViewEntity gameSaveViewEntity) {
    return new GameMetadata(
        gameSaveViewEntity.getId(),
        gameSaveViewEntity.getUserEmail(),
        gameSaveViewEntity.getCreatedAt(),
        gameSaveViewEntity.getUpdatedAt(),
        gameSaveViewEntity.getNickname());
  }

  @Named("mapCurrency")
  default Currency mapCurrency(GameSaveViewEntity gameSaveViewEntity) {
    return new Currency(
        gameSaveViewEntity.getGoldAmount(),
        gameSaveViewEntity.getDiamondAmount(),
        gameSaveViewEntity.getEmeraldAmount(),
        gameSaveViewEntity.getAmethystAmount());
  }

  @Named("mapCharacteristics")
  default Characteristics mapCharacteristics(GameSaveViewEntity gameSaveViewEntity) {
    return new Characteristics(
        gameSaveViewEntity.getAttack(),
        gameSaveViewEntity.getCritChance(),
        gameSaveViewEntity.getCritDamage(),
        gameSaveViewEntity.getHealth(),
        gameSaveViewEntity.getResistance());
  }

  @Named("mapStage")
  default Stage mapStage(GameSaveViewEntity gameSaveViewEntity) {
    return new Stage(gameSaveViewEntity.getCurrentStage(), gameSaveViewEntity.getMaxStage());
  }
}
