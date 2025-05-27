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

package com.lsadf.core.infra.persistence.mappers;

import com.lsadf.core.domain.Model;
import com.lsadf.core.infra.mappers.ModelMapper;
import com.lsadf.core.infra.persistence.Entity;

/**
 * A functional interface that provides a mapping capability to transform an entity of type {@code
 * E} into a model of type {@code M}. This is typically used to convert domain-specific entities
 * used in persistence layers into models used within the application's business logic or service
 * layers.
 *
 * @param <E> the type of the entity to be transformed, which extends {@link Entity}
 * @param <M> the type of the model that will result from the mapping, which extends {@link Model}
 */
@FunctionalInterface
public interface EntityModelMapper<E extends Entity, M extends Model> extends ModelMapper<E, M> {
  /**
   * Maps a provided entity to its corresponding model representation.
   *
   * @param entity the entity to be mapped
   * @return the mapped model representation of the entity
   */
  M mapToModel(E entity);
}
