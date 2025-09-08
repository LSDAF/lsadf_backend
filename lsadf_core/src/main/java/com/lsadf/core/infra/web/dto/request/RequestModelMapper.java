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

package com.lsadf.core.infra.web.dto.request;

import com.lsadf.core.shared.model.Model;
import com.lsadf.core.shared.model.ModelMapper;

/**
 * A functional interface designed to map request-layer objects of type {@code R} that extend the
 * {@link Request} interface to model-layer objects of type {@code M} that extend the {@link Model}
 * interface.
 *
 * <p>Implementations of this interface provide the necessary logic to transform specific request
 * objects into matching domain or data-layer models. This is typically used for converting
 * externally received data, such as API request payloads, into the application's internal
 * representation.
 *
 * @param <R> the type parameter representing the request object that extends {@link Request}
 * @param <M> the type parameter representing the model object that extends {@link Model}
 */
@FunctionalInterface
public interface RequestModelMapper<R extends Request, M extends Model> extends ModelMapper<R, M> {
  /**
   * Maps the provided request object to a corresponding model object.
   *
   * @param request the request object to be mapped
   * @return the mapped model object
   */
  M map(R request);
}
