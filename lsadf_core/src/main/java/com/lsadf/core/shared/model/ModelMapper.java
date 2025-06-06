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

package com.lsadf.core.shared.model;

import com.lsadf.core.shared.mapper.Mapper;

/**
 * A functional interface designed to provide a mapping mechanism for converting objects of one type
 * into another. The mapping operation is defined by implementing the {@code mapToModel} method.
 *
 * @param <I> the source type to be mapped
 * @param <O> the target type resulting from the mapping
 */
@FunctionalInterface
public interface ModelMapper<I, O extends Model> extends Mapper<I, O> {
  /**
   * Converts an object of type M1 into an object of type M2.
   *
   * @param model the input object of type M1 to be converted
   * @return the transformed object of type M2
   */
  O mapToModel(I model);
}
