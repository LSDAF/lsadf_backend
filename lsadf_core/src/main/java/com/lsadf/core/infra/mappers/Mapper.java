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
package com.lsadf.core.infra.mappers;

/**
 * A generic interface for mapping an object of one type to another.
 *
 * <p>This interface establishes a contract for implementing mapping functionality between a source
 * type and a target type. It provides a single method, {@code mapToModel}, which should be
 * implemented by any class that performs the mapping logic.
 *
 * @param <I> the source type to be mapped
 * @param <O> the target type resulting from the mapping
 */
@FunctionalInterface
public interface Mapper<I, O> {
  /**
   * Maps an input object of type I to an output object of type O.
   *
   * @param input the input object of type I to be mapped
   * @return the mapped output object of type O
   */
  O mapToModel(I input);
}
