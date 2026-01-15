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

import java.util.UUID;

/**
 * Represents an entity that can be identified by a unique identifier.
 *
 * <p>This interface extends the {@link Entity} interface, ensuring that all implementing entities
 * are serializable and can be uniquely identified by a {@link UUID}.
 *
 * <p>It provides a contract for retrieving the unique identifier associated with the entity.
 */
public interface Identifiable extends Entity {
  /**
   * Retrieves the unique identifier assigned to the entity.
   *
   * @return the unique identifier of the entity as a {@link UUID}
   */
  UUID getId();
}
