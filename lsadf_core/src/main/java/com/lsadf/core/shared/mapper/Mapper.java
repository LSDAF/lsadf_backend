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
package com.lsadf.core.shared.mapper;

/**
 * A functional interface for mapping an input object of type {@code I} to an output object of type
 * {@code O}.
 *
 * <p>The Mapper interface allows developers to define custom mapping logic to convert an input to a
 * desired output format. It serves as a foundational contract for defining mapping operations in
 * various contexts, such as transforming domain models, DTOs, or API response objects.
 *
 * <p>The use of the {@link FunctionalInterface} annotation ensures that this interface can be used
 * as the assignment target for a lambda expression or method reference, providing concise and
 * reusable mapping functionality across different layers of an application.
 *
 * @param <I> the type of the input object to be mapped
 * @param <O> the type of the output object resulting from the mapping
 */
@FunctionalInterface
public interface Mapper<I, O> {
  /**
   * Maps an input object of type {@code I} to an output object of type {@code O}.
   *
   * @param input the input object to be mapped
   * @return the output object resulting from the mapping
   */
  O map(I input);
}
