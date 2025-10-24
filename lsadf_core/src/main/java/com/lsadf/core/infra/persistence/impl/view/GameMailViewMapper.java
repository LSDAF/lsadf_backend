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

import com.lsadf.core.domain.game.mail.GameMail;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameMailViewMapper extends EntityModelMapper<GameMailViewEntity, GameMail> {
  GameMailViewMapper INSTANCE = Mappers.getMapper(GameMailViewMapper.class);

  @Mapping(target = "attachments", ignore = true)
  @Override
  GameMail map(GameMailViewEntity entity);
}
