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
package com.lsadf.core.infra.persistence;

import java.io.Serializable;

/**
 * Represents an entity that can be uniquely identified within the persistence layer. This interface
 * provides a contract for retrieving the unique identifier of an entity. Entity classes
 * implementing this interface often serve as a foundation for domain-specific objects in the
 * application.
 *
 * <p>This interface extends {@link Serializable}, allowing entities to be serialized and
 * deserialized as needed, such as for caching or data transfer purposes.
 */
public interface Entity extends Serializable {
  /**
   * Retrieves the unique identifier of the entity.
   *
   * @return a string representing the unique identifier of the entity
   */
  String getId();
}
