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

import java.util.Date;

/**
 * An interface representing an entity with timestamp attributes for creation and updates.
 *
 * <p>This interface extends {@link Entity} and provides methods for retrieving the creation and
 * last update timestamps of the entity. These timestamps allow tracking of when the entity was
 * initially created and when it was most recently modified.
 *
 * <p>Implementers of this interface should provide concrete definitions for handling the
 * acquisition of these timestamps, ensuring accurate reflection of entity lifecycle events.
 */
public interface Dateable extends Entity {
  /**
   * Retrieves the timestamp indicating when the entity was created.
   *
   * @return the {@link Date} representing the creation time of the entity
   */
  Date getCreatedAt();

  /**
   * Retrieves the timestamp indicating when the entity was last updated.
   *
   * @return the {@link Date} representing the last modification time of the entity
   */
  Date getUpdatedAt();
}
