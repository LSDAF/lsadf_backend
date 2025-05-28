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

package com.lsadf.core.infra.web.requests;

import com.lsadf.core.infra.mappers.ModelMapper;
import com.lsadf.core.shared.model.Model;

/**
 * Represents a functional interface responsible for converting a request object into its
 * corresponding model representation. Implementations of this interface define the concrete mapping
 * logic that transforms instances of {@code Request} into {@code Model}.
 *
 * @param <R> the type of the request object, which must extend the {@code Request} interface
 * @param <M> the type of the model object, which must extend the {@code Model} interface
 */
@FunctionalInterface
public interface RequestModelMapper<R extends Request, M extends Model> extends ModelMapper<R, M> {

  /**
   * Maps a given request object to a corresponding model representation.
   *
   * @param request the request object to be mapped; must not be null
   * @return the model representation corresponding to the provided request
   */
  M mapToModel(R request);
}
