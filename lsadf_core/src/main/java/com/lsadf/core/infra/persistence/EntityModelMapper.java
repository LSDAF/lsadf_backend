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
package com.lsadf.core.infra.persistence;

import com.lsadf.core.shared.model.Model;
import com.lsadf.core.shared.model.ModelMapper;

/**
 * A functional interface representing a mapper that transforms an entity of type {@code E} into a
 * model of type {@code M}.
 *
 * <p>This interface extends {@link ModelMapper} and specializes it for use with entity-to-model
 * mapping, where the input type is an implementation of {@link Entity} and the output type is an
 * implementation of {@link Model}. It is primarily intended for converting persistence-layer
 * entities into domain models used in the application layer, promoting modularity and separation of
 * concerns.
 *
 * <p>Implementations of this interface are responsible for defining the mapping logic specific to
 * their domain, allowing for consistent and reusable transformation between entities and models.
 *
 * <p>Design Considerations: - This interface is a {@link FunctionalInterface}, meaning it can be
 * implemented using a lambda expression or method reference. - The {@link #map(Entity)} method
 * should ensure that all relevant fields from the entity are correctly and completely transferred
 * to the model. - Implementations should handle any necessary validation of the input or
 * transformations required to align with business rules.
 *
 * @param <E> the type of the entity to be mapped (must extend {@link Entity})
 * @param <M> the type of the model to be produced (must extend {@link Model})
 */
@FunctionalInterface
public interface EntityModelMapper<E extends Entity, M extends Model> extends ModelMapper<E, M> {
  /**
   * Maps an entity object of type {@code E} to a model object of type {@code M}.
   *
   * @param entity the entity object of type {@code E} to be mapped, must not be null
   * @return the corresponding model object of type {@code M} resulting from the mapping
   */
  M map(E entity);
}
