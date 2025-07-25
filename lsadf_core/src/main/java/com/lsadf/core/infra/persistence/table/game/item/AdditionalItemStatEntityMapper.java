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

package com.lsadf.core.infra.persistence.table.game.item;

import com.lsadf.core.domain.game.inventory.item.ItemStat;
import com.lsadf.core.infra.persistence.EntityModelMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * This interface defines a mapper for converting {@link AdditionalItemStatEntity} entities into
 * {@link ItemStat} domain models. It provides a mechanism for mapping persistence-layer entities to
 * application-layer models, supporting modularity and separation of concerns.
 *
 * <p>It extends the {@link EntityModelMapper} interface, which is a functional interface designed
 * for general-purpose entity-to-model transformations, and specializes it for the mapping between
 * `AdditionalItemStatEntity` and `ItemStat`.
 *
 * <p>Key responsibilities of the mapper: - Transform an {@code AdditionalItemStatEntity} into an
 * {@code ItemStat} model while preserving all relevant data fields, ensuring consistency between
 * the database and application layers. - Promote code reusability by providing a static mapper
 * instance accessible via the {@code INSTANCE} field for straightforward integration with other
 * application components.
 */
@Mapper
public interface AdditionalItemStatEntityMapper
    extends EntityModelMapper<AdditionalItemStatEntity, ItemStat> {
  AdditionalItemStatEntityMapper INSTANCE = Mappers.getMapper(AdditionalItemStatEntityMapper.class);

  ItemStat map(AdditionalItemStatEntity additionalItemStatEntity);
}
