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

package com.lsadf.core.infra.web.response;

import com.lsadf.core.shared.mapper.Mapper;
import com.lsadf.core.shared.model.Model;

/**
 * A specialized functional interface for mapping objects of a domain model type {@code M} to
 * corresponding response objects of type {@code R}.
 *
 * <p>This interface extends the {@link Mapper} interface, providing additional semantic context
 * specific to mapping domain models to API response objects or other externally exposed
 * representations. The interface is particularly useful for defining a consistent mapping strategy
 * across various domain models and their respective response types.
 *
 * <p>It enforces a single abstract method {@code map}, making it compatible with lambda expressions
 * and method references for lightweight and reusable mapping definitions. Developers can implement
 * this interface to define the transformation logic between specific domain model classes and their
 * corresponding response representations.
 *
 * @param <M> the type of the domain model object to be mapped, which must extend the {@link Model}
 *     interface
 * @param <R> the type of the response object resulting from the mapping, which must extend the
 *     {@link Response} interface
 */
@FunctionalInterface
public interface ModelResponseMapper<M extends Model, R extends Response> extends Mapper<M, R> {
  /**
   * Maps a model object of type {@code M} to a response object of type {@code R}.
   *
   * @param model the model object to be mapped
   * @return the mapped response object
   */
  R map(M model);
}
