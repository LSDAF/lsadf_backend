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

package com.lsadf.core.infra.persistence.impl.game.mail;

import com.lsadf.core.domain.game.mail.GameMail;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for converting between GameMailEntity and GameMail domain model.
 *
 * <p>This mapper uses MapStruct to automatically generate the implementation. The attachments field
 * is ignored during mapping.
 *
 * @see GameMailEntity
 * @see GameMail
 * @see EntityModelMapper
 * @see <a href="https://mapstruct.org/">MapStruct Documentation</a>
 */
@Mapper
public interface GameMailEntityMapper extends EntityModelMapper<GameMailEntity, GameMail> {

  GameMailEntityMapper INSTANCE = Mappers.getMapper(GameMailEntityMapper.class);

  @Override
  @Mapping(target = "attachments", ignore = true)
  GameMail map(GameMailEntity input);
}
