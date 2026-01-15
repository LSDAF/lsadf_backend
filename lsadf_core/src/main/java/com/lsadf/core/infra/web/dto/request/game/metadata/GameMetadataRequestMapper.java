/*
 * Copyright © 2024-2026 LSDAF
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
package com.lsadf.core.infra.web.dto.request.game.metadata;

import com.lsadf.core.domain.game.save.metadata.GameMetadata;
import com.lsadf.core.infra.web.dto.request.RequestModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * A mapper interface for converting {@link GameMetadataRequest} objects into {@link GameMetadata}
 * domain objects. This interface utilizes MapStruct to handle the mapping logic.
 *
 * <p>It extends the generic {@link RequestModelMapper} interface, which defines the contract for
 * mapping request-layer objects to model-layer objects within the application.
 *
 * <p>The mapper is implemented as a singleton instance provided by MapStruct's factory mechanism.
 */
@Mapper
public interface GameMetadataRequestMapper
    extends RequestModelMapper<GameMetadataRequest, GameMetadata> {
  GameMetadataRequestMapper INSTANCE =
      org.mapstruct.factory.Mappers.getMapper(GameMetadataRequestMapper.class);

  @Override
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  GameMetadata map(GameMetadataRequest gameMetadataRequest);
}
