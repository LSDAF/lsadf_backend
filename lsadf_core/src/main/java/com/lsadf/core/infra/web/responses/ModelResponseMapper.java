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

package com.lsadf.core.infra.web.responses;

import com.lsadf.core.shared.model.Model;

/**
 * Interface for mapping model objects to their corresponding response representations.
 *
 * @param <M> the type of the model object
 * @param <R> the type of the response object
 */
@FunctionalInterface
public interface ModelResponseMapper<M extends Model, R extends Response> {
  /**
   * Maps a given model object to its corresponding response representation.
   *
   * @param model the model object to be mapped to a response
   * @return the response object that corresponds to the provided model
   */
  R mapToResponse(M model);
}
